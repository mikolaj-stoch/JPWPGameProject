package com.example.myapplication.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.myapplication.Clickable;
import com.example.myapplication.R;

public class PauseMenu extends Dialog implements View.OnClickListener, Clickable {

    private Button resume;
    private Button exit;

    private Context context;

    public PauseMenu(Context context){
        super(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pause);
        resume = (Button)findViewById(R.id.resume);
        exit = (Button)findViewById(R.id.exit);

        resume.setOnClickListener(this);
        exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit:
                dismiss();
                ((Activity)context).finish();  //without pauseMenu.dismiss(): leaked window(we are trying to show a Dialog after we've exited an Activity)
                break;
            case R.id.resume:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void actionOnClick(){
        PauseMenu menu = new PauseMenu(context);
        menu.show();
    }
}

