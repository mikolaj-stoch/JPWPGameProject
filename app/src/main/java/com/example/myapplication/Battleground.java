package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.myapplication.Activities.CombatActivity;
import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.GameObj.Combat;
import com.example.myapplication.GameObj.Player;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Random;

public class Battleground {

    private Context context;
    private Intent intentBack;

    private Combat player;
    private Combat enemy;

    private boolean fighting;
    private boolean playerWins;
    private boolean enemyWins;

    private boolean playerTurn = true;

    private ProgressBar playerHpBar;
    private ProgressBar playerStaminaBar;

    private ProgressBar enemyHpBar;
    private ProgressBar enemyStaminaBar;

    private ToggleButton fastAttackBtn;
    private ToggleButton mediumAttackBtn;
    private ToggleButton heavyAttackBtn;
    private ToggleButton blockBtn;
    private Button actionBtn;

    private TextView playerHpText;
    private TextView playerStaminaText;
    private TextView enemyHpText;
    private TextView enemyStaminaText;
    private TextView detailsText;

    private ActionType actionType = ActionType.zero;
    enum ActionType{
        zero, fa, ma, ha, block;
    }

    public Battleground(Combat player, Combat enemy, Context context, Intent intentBack){
        this.player = player;
        this.enemy = enemy;
        this.context = context;
        this.intentBack = intentBack;

        initElements();
        updateBars();
        initListeners();
        intentBack.putExtra("newHp", player.getHp() + "");
        intentBack.putExtra("newStamina", player.getStamina() + "");
        intentBack.putExtra("xpReward", 0 + "");
        ((CombatActivity)context).setResult(Activity.RESULT_OK, intentBack);
    }


    public boolean checkEndFight(int dmgDealt, int dmgTaken, boolean phaseEnd){
        if(!enemy.isAlive()){
            popUpStats("End fight\nYou won and gained " + enemy.getXpToGet() +" xp",
                    true, dmgDealt, dmgTaken);
            intentBack.putExtra("xpReward", enemy.getXpToGet()+"");
            ((CombatActivity)context).setResult(Activity.RESULT_OK, intentBack);
            this.playerWins = true;
            this.fighting = false;
            return true;
        }
        else if(!player.isAlive()){
            popUpStats("End fight\nYou have lost", true, dmgDealt, dmgTaken);
            this.enemyWins = true;
            this.fighting = false;
            return true;
        }
        else if (phaseEnd){
            popUpStats("Continue battle", false, dmgDealt, dmgTaken);
            return false;
        }
        return false;
    }


    public void initListeners(){
        fastAttackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerTurn){
                    mediumAttackBtn.setChecked(false);
                    heavyAttackBtn.setChecked(false);
                    blockBtn.setChecked(false);
                    actionType = ActionType.fa;
                    String displayedText = "Fast Attack\nDeals: " +
                            player.getDmgRange(1) + "\nCost: " +
                            player.getStaminaCost(1) + " stamina.";
                    detailsText.setText(displayedText);
                }
            }
        });

        mediumAttackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerTurn){
                    fastAttackBtn.setChecked(false);
                    heavyAttackBtn.setChecked(false);
                    blockBtn.setChecked(false);
                    actionType = ActionType.ma;
                    String displayedText = "Medium Attack\nDeals: " +
                            player.getDmgRange(2) + "\nCost: " +
                            player.getStaminaCost(2) + " stamina.";
                    detailsText.setText(displayedText);
                }
            }
        });

        heavyAttackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerTurn){
                    fastAttackBtn.setChecked(false);
                    mediumAttackBtn.setChecked(false);
                    blockBtn.setChecked(false);
                    actionType = ActionType.ha;
                    String displayedText = "Heavy Attack\nDeals: " +
                            player.getDmgRange(3) + "\nCost: " +
                            player.getStaminaCost(3) + " stamina.";
                    detailsText.setText(displayedText);
                }

            }
        });

        blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerTurn){
                    fastAttackBtn.setChecked(false);
                    heavyAttackBtn.setChecked(false);
                    mediumAttackBtn.setChecked(false);
                    actionType = ActionType.block;
                    String displayedText = "Block\nRestores: " + player.getBlockReg() + " stamina";
                    detailsText.setText(displayedText);
                }

            }
        });

        actionBtn.setOnClickListener(new View.OnClickListener() {
            int dmg;
            @Override
            public void onClick(View v) {
                dmg = 0;

                switch (actionType) {
                    case fa:
                        dmg = player.fastAttack();
                        if (checkAttack(dmg)) {
                            enemy.takeDmg(dmg);
                        } else {
                            return;
                        }
                        break;
                    case ma:
                        dmg = player.mediumAttack();
                        if (checkAttack(dmg)) {
                            enemy.takeDmg(dmg);
                        } else {
                            return;
                        }
                        break;
                    case ha:
                        dmg = player.heavyAttack();
                        if (checkAttack(dmg)) {
                            enemy.takeDmg(dmg);
                        } else {
                            return;
                        }
                        break;
                    case block:
                        player.block();
                        break;
                    case zero:
                        return;
                }


                if(checkEndFight(dmg, 0, false)){
                    updateBars();
                    intentBack.putExtra("newHp", player.getHp() + "");
                    intentBack.putExtra("newStamina", player.getStamina() + "");
                }
                else {
                    int dmgTaken = enemyAttack();
                    intentBack.putExtra("newHp", player.getHp() + "");
                    intentBack.putExtra("newStamina", player.getStamina() + "");
                    ((CombatActivity) context).setResult(Activity.RESULT_OK, intentBack);

                    updateBars();
                    checkEndFight(dmg, dmgTaken, true);
                }

            }
        });
    }

    public void popUpStats(String resultMsg, boolean end, int dmgDealt, int dmgTaken){
        final Dialog resultDialog = new Dialog(context);
        resultDialog.setContentView(R.layout.fight_result);
        TextView resultInfo = resultDialog.findViewById(R.id.result);
        TextView statText = resultDialog.findViewById(R.id.stat);
        Button btn = resultDialog.findViewById(R.id.backToMap);

        statText.setText("You have dealt " + dmgDealt + " damage\nYou have taken " + dmgTaken + " damage");
        resultInfo.setText(resultMsg);
        resultDialog.show();
        if(end){
            btn.setText("Back to map");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resultDialog.dismiss();
                    if(player.isAlive()){
                        ((Activity)context).finish();
                    }
                    else {
                        ((Activity) context).startActivity(new Intent(context, MainActivity.class));
                        ((Activity)context).finish();
                    }
                }
            });

        }
        else {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resultDialog.dismiss();
                }
            });
        }

    }

    private void initElements() {
        playerHpBar = (ProgressBar) ((Activity)context).findViewById(R.id.playerPB_HP);
        playerStaminaBar = (ProgressBar) ((Activity)context).findViewById(R.id.playerPB_Stamina);

        enemyHpBar = (ProgressBar) ((Activity)context).findViewById(R.id.enemyPB_Hp);
        enemyStaminaBar = (ProgressBar) ((Activity)context).findViewById(R.id.enemyPB_Stamina);

        fastAttackBtn = (ToggleButton) ((Activity)context).findViewById(R.id.fastAttackBtn);
        mediumAttackBtn = (ToggleButton) ((Activity)context).findViewById(R.id.mediumAttackBtn);
        heavyAttackBtn = (ToggleButton) ((Activity)context).findViewById(R.id.heavyAttackBtn);
        blockBtn = (ToggleButton) ((Activity)context).findViewById(R.id.blockBtn);
        actionBtn = (Button) ((Activity)context).findViewById(R.id.actionBtn);

        detailsText = (TextView) ((Activity)context).findViewById(R.id.detailsText);

        playerHpText = (TextView) ((Activity)context).findViewById(R.id.playerCurrHp);
        playerStaminaText = (TextView) ((Activity)context).findViewById(R.id.playerCurrStamina);
        enemyHpText = (TextView) ((Activity)context).findViewById(R.id.enemyCurrHp);
        enemyStaminaText = (TextView) ((Activity)context).findViewById(R.id.enemyCurrStamina);
    }

    public void updateBars(){
        int hpProgressPlayer = (int)(100*(player.getHp())/player.getMaxHp());
        int staminaProgressPlayer = (int)(100*player.getStamina()/player.getMaxStamina());

        int hpProgressEnemy = (int)(100*(enemy.getHp())/enemy.getMaxHp());
        int staminaProgressEnemy = (int)(100*enemy.getStamina()/enemy.getMaxStamina());

        playerHpBar.setProgress(hpProgressPlayer);
        playerStaminaBar.setProgress(staminaProgressPlayer);
        playerHpText.setText(player.getHp() + "/" + player.getMaxHp());
        playerStaminaText.setText(player.getStamina() + "/" + player.getMaxStamina());

        enemyHpBar.setProgress(hpProgressEnemy);
        enemyStaminaBar.setProgress(staminaProgressEnemy);
        enemyHpText.setText(enemy.getHp() + "/" + enemy.getMaxHp());
        enemyStaminaText.setText(enemy.getStamina() + "/" + enemy.getMaxStamina());
    }

    private boolean checkAttack(int dmg){
        if(dmg < 0){
            // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.staminaError)
                    .setTitle(R.string.dialogTitle);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        else {
            return true;
        }

    }
    private int enemyAttack() {
        Random random = new Random();
        int dmg;
        while (true) {
            int attack = random.nextInt(4);
            switch (attack) {
                case 0:
                    dmg = enemy.fastAttack();
                    if (dmg > 0) {
                        player.takeDmg(dmg);
                        return dmg;
                    }
                    break;
                case 1:
                    dmg = enemy.mediumAttack();
                    if (dmg > 0) {
                        player.takeDmg(dmg);
                        return dmg;
                    }
                    break;
                case 2:
                    dmg = enemy.heavyAttack();
                    if (dmg > 0) {
                        player.takeDmg(dmg);
                        return dmg;
                    }
                    break;
                case 3:
                    enemy.block();
                    dmg = 0;
                    return dmg;

            }
        }
    }

}
