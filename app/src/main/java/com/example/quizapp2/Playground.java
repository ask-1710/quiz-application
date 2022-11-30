package com.example.quizapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

class EvaluateString
{
    public static int evaluate(String expression)
    {
        char[] tokens = expression.toCharArray();

        // Stack for numbers: 'values'
        Stack<Integer> values = new
                Stack<Integer>();

        // Stack for Operators: 'ops'
        Stack<Character> ops = new
                Stack<Character>();

        for (int i = 0; i < tokens.length; i++)
        {
            // Current token is a
            // whitespace, skip it
            if (tokens[i] == ' ')
                continue;

            // Current token is a number,
            // push it to stack for numbers
            if (tokens[i] >= '0' && tokens[i] <= '9')
            {
                StringBuffer sbuf = new StringBuffer();

                // There may be more than one
                // digits in number

                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
                    sbuf.append(tokens[i++]);
                values.push(Integer.parseInt(sbuf.toString()));

                // right now the i points to
                // the character next to the digit,
                // since the for loop also increases
                // the i, we would skip one
                //  token position; we need to
                // decrease the value of i by 1 to
                // correct the offset.
                i--;
            }

            // Current token is an opening brace,
            // push it to 'ops'
            else if (tokens[i] == '(')
                ops.push(tokens[i]);

                // Closing brace encountered,
                // solve entire brace
            else if (tokens[i] == ')')
            {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));
                ops.pop();
            }

            // Current token is an operator.
            else if (tokens[i] == '+' ||
                    tokens[i] == '-' ||
                    tokens[i] == '*' ||
                    tokens[i] == '/')
            {
                // While top of 'ops' has same
                // or greater precedence to current
                // token, which is an operator.
                // Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() &&
                        hasPrecedence(tokens[i],
                                ops.peek()))
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));

                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }
        }

        // Entire expression has been
        // parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty())
            values.push(applyOp(ops.pop(),
                    values.pop(),
                    values.pop()));

        // Top of 'values' contains
        // result, return it
        return values.pop();
    }

    // Returns true if 'op2' has higher
    // or same precedence as 'op1',
    // otherwise returns false.
    public static boolean hasPrecedence(
            char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') &&
                (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    // A utility method to apply an
    // operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public static int applyOp(char op, int b, int a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException(
                            "Cannot divide by zero");
                return a / b;
        }
        return 0;
    }
}




public class Playground extends AppCompatActivity {
    int num1 , num2 ;
    int op ;
    int score;
    int qnNoInt, qnCountInt;
    int remTime, timePerQuestion;
    TextView qnCount;
    TextView timeLeft;
    TextView question;
    TextView qnNo;
    String questionString;
    TextView userAnswer;
    TextView userScore;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 0ms the TimerTask will run every 1000ms
        timer.schedule(timerTask, 0, 1000); //

    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run()
                    {
                        if(remTime == 0)
                        {
                            stoptimertask();
                            generateQuestion();
                        }
                        timeLeft.setText(remTime+"");
                        remTime--;

                    }
                });
            }
        };
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);

        question = findViewById(R.id.questionDisplay);
        qnCount = findViewById(R.id.qnCount);
        qnNo = findViewById(R.id.qnNo);
        userAnswer = findViewById(R.id.userAnswer);
        userScore = findViewById(R.id.userScore);

        score = 0;
        Intent intent = getIntent();
        String str = intent.getStringExtra("questionCount");
        String time = intent.getStringExtra("time");

        remTime = Integer.parseInt(time);
        timePerQuestion = remTime;
        qnCount.setText(str);
        qnCountInt = Integer.parseInt(qnCount.getText().toString());

        // initialize question number and count
        qnNoInt = 0;
        generateQuestion();

        timeLeft = findViewById(R.id.timeLeft);
        timeLeft.setText(remTime + "s");
    }

    private int calcScore() {

        float score = (float)(remTime) / (float)(timePerQuestion);
        score = score * 10;

        return (int)score;

    }

    public void submitQuestion(View view)
    {
        int correctAnswer = evaluateExpression(questionString);
        int enteredAnswer = Integer.parseInt(userAnswer.getText().toString());

        if(correctAnswer == enteredAnswer)
        {
            score += calcScore();
            userScore.setText(score + "");
        }

        if(qnNoInt == qnCountInt) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }

        else {
            generateQuestion();
        }
    }

    protected String generateExpression()
    {
        int operand1, operand2;
        String operator = "";
        operand1 = (int) new Random().nextInt(50) ;
        operand2 = (int) (new Random().nextInt(50) + 1);

        int op = (int) (Math.random() % 4);

        switch (op) {
            case 0 :{
                operator = "+";
                break;
            }

            case 1: {
                operator = "-";
                break;
            }

            case 2: {
                operator = "*";
                break;
            }

            case 3: {
                operator = "/";
                break;
            }
        }

        return operand1 + " " + operator + " " + operand2;
    }

    private int evaluateExpression(String expression)
    {
        EvaluateString obj = new EvaluateString();
        return obj.evaluate(expression);
    }


    protected void generateQuestion()
    {
        if(qnNoInt == qnCountInt) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        remTime = timePerQuestion;
        questionString = generateExpression();
        qnNoInt++;

        question.setText(questionString);
        qnNo.setText(qnNoInt+"");
        startTimer();
    }

}