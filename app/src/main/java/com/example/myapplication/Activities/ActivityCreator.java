package com.example.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.Clickable;
import com.example.myapplication.GameObj.Combat;
import com.example.myapplication.GameObj.Player;
import com.example.myapplication.GameView;

public class ActivityCreator implements Clickable {

    private Context context;
    private Player player;
    private Combat enemy;

    private Activity activity;

    private int selectActivity;

    public ActivityCreator(Context context, Player player, int selectActivity){
        this.selectActivity = selectActivity;
        this.context = context;
        this.player = player;
    }

//    public ActivityCreator(Activity activity){
//        this.activity = activity;
//    }

    public void createInventory(Context context, Player player){
        Intent newIntent = new Intent(context, InventoryActivity.class);
        newIntent.putExtra("Player Item", player);
        newIntent.putExtra("missionName", player.getMission().getMissionName());
        newIntent.putExtra("missionDescription", player.getMission().getDescription());
        newIntent.putExtra("missionGoal", player.getMission().getCurrentGoal());
//        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity)context).startActivityForResult(newIntent, 1);
    }

    public void createCombat(Context context, Combat player, Combat enemy){
        if(player != null && enemy != null) {
            Intent newIntent = new Intent(context, CombatActivity.class);
            newIntent.putExtra("Player Item", player);
            newIntent.putExtra("Enemy Item", enemy);
            ((Activity)context).startActivityForResult(newIntent, 2);
        }
        else {
//            throw new NullPointerException();
            Log.e("NullPointerException", "player or enemy is a null reference");
        }

    }

    @Override
    public void actionOnClick() {
        if(enemy == null){
            Log.e("NullPointerExClick", "enemy is a null reference");
        }
        if(player == null){
            Log.e("NullPointerExClick", "player is a null reference");
        }
        Log.d("selected: ", selectActivity + "");
        switch (selectActivity){
            case 0:
                if(player != null) {
                    createInventory(this.context, this.player);
                }
                else {
                    Log.e("NullPointerException", "player is a null reference");
                }
                break;
            case 1:
                createCombat(this.context, this.player, this.enemy);
                break;
        }
    }

    public void setEnemy(Combat enemy){
        if(enemy == null){
            Log.e("AC/NullPointerException", "enemy is a null reference");
        }
        else {
            this.enemy = enemy;
        }
        if(player == null){
            Log.e("AC/NullPointerException", "player is a null reference");
        }


    }

    public Combat getEnemy() {
        return enemy;
    }
}
