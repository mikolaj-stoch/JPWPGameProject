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
import com.example.myapplication.R;

//To do.

public class ObjectMenu extends Dialog implements View.OnClickListener, Clickable {

    private Button takeItems;
    private Button exit;

    private Context context;

    private String name;

    public ObjectMenu(Context context,String name){
        super(context);
        this.context = context;
        this.name = name;
    }

    @Override
    public void onCreate (Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.object_menu);

        takeItems = (Button) findViewById(R.id.takeItems);
        exit = (Button) findViewById(R.id.exit);

        takeItems.setOnClickListener(this);
        exit.setOnClickListener(this);

        TextView temp = (TextView) findViewById(R.id.objectName);
        temp.setText(name);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.takeItems:
                //should do something
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
        ObjectMenu objectMenu = new ObjectMenu(context,name);
        objectMenu.show();
    }



}
