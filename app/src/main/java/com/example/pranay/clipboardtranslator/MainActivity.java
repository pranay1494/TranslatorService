package com.example.pranay.clipboardtranslator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout selectLanguage;
    private Switch startOrStop;
    SharedPreferences sharedPreferences;
    long downtime;
    private long upTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectLanguage = (RelativeLayout) findViewById(R.id.language);
        startOrStop = (Switch) findViewById(R.id.startOrStop);
        selectLanguage= (RelativeLayout) findViewById(R.id.language);
        sharedPreferences = getSharedPreferences("Translator",MODE_PRIVATE);

        boolean isChecked = sharedPreferences.getBoolean("START_SERVICE",true);
        startOrStop.setChecked(isChecked);
        if(isChecked){
            startService(new Intent(MainActivity.this,Clipboard.class));
        }
        else{
            stopService(new Intent(MainActivity.this,Clipboard.class));
        }
        selectLanguage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
                int action = motionEvent.getActionMasked();
                if(action==MotionEvent.ACTION_DOWN){
                    //downtime = motionEvent.getDownTime();
                    downtime = System.currentTimeMillis();
                }
                if(action==MotionEvent.ACTION_UP){
                    upTime = System.currentTimeMillis();
                    if(upTime-downtime>2000){
                        Toast.makeText(MainActivity.this,"LongClick",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"OnClick",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        startOrStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    startService(new Intent(MainActivity.this,Clipboard.class));
                }
                else{
                    stopService(new Intent(MainActivity.this,Clipboard.class));
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("START_SERVICE",checked);
                editor.commit();
            }
        });

        selectLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
