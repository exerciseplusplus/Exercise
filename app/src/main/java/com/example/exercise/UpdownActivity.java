package com.example.exercise;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UpdownActivity extends Activity {


    Button startButton ;
    Button countButton ;
    TextView numberTextView;

    int number=0;
    boolean isStart=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updown);

        startButton=(Button)findViewById(R.id.button_updown_start);
        countButton=(Button)findViewById(R.id.button_updown_count);

        numberTextView=(TextView)findViewById(R.id.editText_updown_number);

        startButton.setOnClickListener(startListener);
        countButton.setOnClickListener(countListener);

    }
    OnClickListener startListener =new OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if (isStart)
            {
                isStart=false;
                Log.d("Updown", "end count");
                Log.d("Updown", Integer.toString(number));

            }else
            {
                Log.d("Updown", "start count");
                Log.d("Updown", Integer.toString(number));
                numberTextView.setText(R.string.updown_number);
                isStart=true;
                startButton.setText("停止");
            }
        }
    };

    OnClickListener countListener =new OnClickListener()
    {
        @Override
        public void onClick(View v) {
            String count= Integer.toString(number)+"个俯卧撑";
            numberTextView.setText(count);
            Log.d("Updown", Integer.toString(number));
            number++;
        }
    };
}
