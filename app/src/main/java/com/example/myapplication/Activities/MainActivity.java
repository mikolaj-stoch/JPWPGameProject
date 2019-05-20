package com.example.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.TitleView;

// onPause() and onResume() <- important when we get the call
// home button doesn't kill current activity, but back button does

//Display view (i.e. new window)
//Two approaches:
//-replace current content view with new view
//-launch new activity for the game(new activity on stack) <- when player press back button app won't shutdown, only current activity will be killed
public class MainActivity extends Activity {
    private Button playBtn;
    private Button loadBtn;
    private Button newGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //setFlags(flag, flagToChange)
//        TitleView tView = new TitleView(this);
//        tView.setKeepScreenOn(true);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        initView();
        initListeners();
    }


    private void initView(){
        playBtn = findViewById(R.id.playBtn);
        loadBtn = findViewById(R.id.loadBtn);
        newGameBtn = findViewById(R.id.newGameBtn);
    }

    private void initListeners() {
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(gameIntent);
            }
        });
    }

}
