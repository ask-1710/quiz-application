package com.example.quizapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SeekBar timeBar;
    TextView selectedTime;
    TextView highScore;
    EditText numQn;

    int highScoreInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeBar = (SeekBar) findViewById(R.id.seekBar);
        selectedTime = (TextView) findViewById(R.id.timeInput);
        numQn = findViewById(R.id.questionCount);


        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                int start = 5;
                int increment = (int)(((float)progress / 100.0) * 15);
                start += increment;

                selectedTime.setText(start+"s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void startQuiz(View view)
    {
        Intent intent = new Intent(this, Playground.class);

        String num = numQn.getText().toString();
        String time = selectedTime.getText().toString();

        time = time.substring(0, time.length() - 1);

        intent.putExtra("questionCount", num);
        intent.putExtra("time", time);

        startActivity(intent);
    }
}