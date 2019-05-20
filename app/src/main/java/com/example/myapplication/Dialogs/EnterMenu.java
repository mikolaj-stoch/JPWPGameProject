package com.example.myapplication.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Clickable;
import com.example.myapplication.GameView;
import com.example.myapplication.R;

//To do.

public class EnterMenu extends Dialog implements View.OnClickListener, Clickable {

    private Button enter;
    private Button exit;

    private Context context;

    private String name;

    private GameView gameView;

    public EnterMenu(Context context,String name,GameView gameView){
        super(context);
        this.context = context;
        this.name = name;
        this.gameView = gameView;
    }

    @Override
    public void onCreate (Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.enter_menu);

        enter = (Button) findViewById(R.id.enter);
        exit = (Button) findViewById(R.id.exit);

        enter.setOnClickListener(this);
        exit.setOnClickListener(this);

        TextView temp = (TextView) findViewById(R.id.objectName);
        temp.setText(name);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.enter:
                if (name.equals("Edrick's house"))
                    gameView.enterCastle();
                if(name.equals("House"))
                    gameView.enterHouse();
                dismiss();
                break;
            case R.id.exit:
                dismiss();
                break;

            default:
                break;
        }
    }

    @Override
    public void actionOnClick (){
        EnterMenu enterMenu = new EnterMenu(context,name,gameView);
        enterMenu.show();
    }
}
