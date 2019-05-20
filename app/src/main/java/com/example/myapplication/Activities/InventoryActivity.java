package com.example.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.GameObj.Player;
import com.example.myapplication.R;

// testing ListLayout

public class InventoryActivity extends Activity {

    Player player;

    //XML elements
    ProgressBar hpBar;
    ProgressBar staminaBar;
    ProgressBar xpBar;

    TextView level;
    TextView nickname;
    TextView xpTxt;
    TextView hpTxt;
    TextView staminaTxt;

    TextView availablePoints;

    TextView strengthTxt;
    TextView agilityTxt;
    TextView vitalityTxt;
    TextView wisdomTxt;

    Button addStrengthBtn;
    Button addAgilityBtn;
    Button addVitalityBtn;
    Button addWisdomBtn;

    Intent intent;
    Intent intentBack;

    private TextView missionNameTxt;
    private TextView missionDescrTxt;
    private TextView missionGoalTxt;

    //mission
    private String name;
    private String description;
    private String goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //setFlags(flag, flagToChange)
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.inventory);

        this.intent = getIntent();

        player = intent.getParcelableExtra("Player Item");
        this.name = intent.getStringExtra("missionName");
        this.description = intent.getStringExtra("missionDescription");
        this.goal = intent.getStringExtra("missionGoal");

        intentBack = new Intent();
        updateBars();
        initListeners();
    }

    //Unable to start activity ComponentInfo{com.example.myapplication/com.example.myapplication.Activities.InventoryActivity}:
    // android.content.res.Resources$NotFoundException: String resource ID #0x5 -> setText() needs string
    private void updateBars(){
        int hpProgress = (int)(100*(player.getHp())/player.getMaxHp());
        int staminaProgress = (int)(100*player.getStamina()/player.getMaxStamina());


        Log.d("hp fraction", hpProgress + "");
        hpBar = (ProgressBar) findViewById(R.id.hpBar);
        hpBar.setProgress(hpProgress);

        hpTxt = (TextView) findViewById(R.id.hpOnBar);
        hpTxt.setText(player.getHp() + "/" + player.getMaxHp());

        staminaBar = (ProgressBar)findViewById(R.id.staminaBar);
        staminaBar.setProgress(staminaProgress);

        staminaTxt = (TextView) findViewById(R.id.staminaOnBar );
        staminaTxt.setText(player.getStamina() + "/" + player.getMaxStamina());

        availablePoints = (TextView) findViewById(R.id.availablePoints);
        availablePoints.setText(player.getPointsToSpend() + "");

        strengthTxt = findViewById(R.id.strengthPoints);
        strengthTxt.setText(player.getStrength() + "");

        agilityTxt = findViewById(R.id.agilityPoints);
        agilityTxt.setText(player.getAgility() + "");

        vitalityTxt = findViewById(R.id.vitalityPoints);
        vitalityTxt.setText(player.getVitality() + "");

        wisdomTxt = findViewById(R.id.wisdomPoints);
        wisdomTxt.setText(player.getWisdom() + "");

        addStrengthBtn = findViewById(R.id.strengthBtn);
        addAgilityBtn = findViewById(R.id.agilityBtn);
        addVitalityBtn = findViewById(R.id.vitalityBtn);
        addWisdomBtn = findViewById(R.id.wisdomBtn);

        //left side
        int xpProgress = (int)(100*(player.getXp())/player.getXpToNextLvl());

        nickname = (TextView)findViewById(R.id.nicknameText);
        nickname.setText(player.getNickname());

        level = (TextView)findViewById(R.id.levelText);
        level.setText(player.getLevel() + "");

        xpTxt = (TextView)findViewById(R.id.xpOnBar);
        xpTxt.setText(player.getXp() + "/" + player.getXpToNextLvl());

        xpBar = (ProgressBar)findViewById(R.id.xpPB);
        xpBar.setProgress(xpProgress);

        missionNameTxt = findViewById(R.id.missionName);
        missionNameTxt.setText(name);
        missionDescrTxt = findViewById(R.id.missionDescr);
        missionDescrTxt.setText(description);
        this.missionDescrTxt.setMovementMethod(new ScrollingMovementMethod());  //scroll text
        missionGoalTxt = findViewById(R.id.missionGoal);
        missionGoalTxt.setText(goal);
    }

    private void initListeners() {

        addStrengthBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                player.increaseStrength(1, false);
                updateBars();
                intentBack.putExtra("newStrength", player.getStrength() + "");
                intentBack.putExtra("newPoints", player.getPointsToSpend() + "");
                setResult(RESULT_OK, intentBack);
            }
        });

        addAgilityBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                player.increaseAgility(1, false);
                updateBars();
                intentBack.putExtra("newAgility", player.getAgility() + "");
                intentBack.putExtra("newPoints", player.getPointsToSpend() + "");
                intentBack.putExtra("newMaxStamina", player.getMaxStamina() + "");
                intentBack.putExtra("newStamina", player.getStamina() +"");
                setResult(RESULT_OK, intentBack);
            }
        });

        addVitalityBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                player.increaseVitality(1, false);
                updateBars();
                intentBack.putExtra("newVitality", player.getVitality() + "");
                intentBack.putExtra("newPoints", player.getPointsToSpend() + "");
                intentBack.putExtra("newMaxHp", player.getMaxHp() + "");
                intentBack.putExtra("newHp", player.getHp() + "");
                setResult(RESULT_OK, intentBack);
            }
        });

        addWisdomBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                player.increaseWisdom(1, false);
                updateBars();
                intentBack.putExtra("newWisdom", player.getWisdom() + "");
                intentBack.putExtra("newPoints", player.getPointsToSpend() + "");
                setResult(RESULT_OK, intentBack);
            }
        });

        //if we try to extract extras that are not put inside the intent we get exception
        //probably temporary solution

        intentBack.putExtra("newStrength", player.getStrength() + "");
        intentBack.putExtra("newAgility", player.getAgility() + "");
        intentBack.putExtra("newVitality", player.getVitality() + "");
        intentBack.putExtra("newWisdom", player.getWisdom() + "");
        intentBack.putExtra("newPoints", player.getPointsToSpend() + "");
        intentBack.putExtra("newMaxHp", player.getMaxHp() + "");
        intentBack.putExtra("newHp", player.getHp() + "");
        intentBack.putExtra("newMaxStamina", player.getMaxStamina() + "");
        intentBack.putExtra("newStamina", player.getStamina() +"");
    }
}
