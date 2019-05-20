package com.example.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplication.Battleground;
import com.example.myapplication.GameObj.Mob1;
import com.example.myapplication.GameObj.Player;
import com.example.myapplication.R;

public class CombatActivity extends Activity {
    Intent intent;
    Intent intentBack;

    Player player;
    Mob1 enemy;

    Battleground battleground;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.combat_layout);

        this.intent = getIntent();

        player = intent.getParcelableExtra("Player Item");
        enemy = intent.getParcelableExtra("Enemy Item");
        intentBack = new Intent();

        battleground = new Battleground(player, enemy, this, intentBack);
    }

//    public void setHpBack(){
//        Log.d("putExtra/enemy", "newHp");
//        intentBack.putExtra("newHp", player.getHp() + "");
//    }
    @Override
    public void onBackPressed() {

    }

}
