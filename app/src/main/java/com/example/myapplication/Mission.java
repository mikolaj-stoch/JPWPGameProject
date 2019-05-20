package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.GameObj.Player;
import com.example.myapplication.Items.Inventory;
import com.example.myapplication.Items.Item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class Mission {  //Builder could be a good solution?

    public static final int MISSION1_ID = 1;

    private String missionName;
    private TreeMap<Integer, String> stages;
    private HashMap<Integer, Integer> killMission;
    private String description;

    private int currentStage = 1;
    private int numOfStages;

    private boolean finished;
    private int[] reward;

    private Player player;
    private Inventory inventory;
    private Context context;

    @SuppressLint("UseSparseArrays")
    public Mission(Context context, Player player, Inventory inventory, String missionName, int numOfStages, int[] reward,
                   TreeMap<Integer, String> stages, String description){
        this.context = context;
        this.missionName = missionName;
        this.player = player;
        this.inventory = inventory;
        this.stages = stages;
        this.description = description;
        this.numOfStages = numOfStages;
        this.reward = reward;

        killMission = new HashMap<>();
    }

    public void nextStage(){
        if(currentStage <= numOfStages){
            currentStage++;
            new AlertDialog.Builder(context)
                    .setTitle("Mission update")
                    .setMessage("Check your inventory to get more information")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            finished = true;
            new AlertDialog.Builder(this.context)
                    .setTitle("Mission finished")
                    .setMessage("Congratulations !")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            description = "Congratulations, now villagers are safe";
            stages.put(currentStage, "Maybe you should talk with Edrick...");
            inventory.addLoot(this);
            popUpReward(reward);

        }
    }
    public void updateKills(){
        Log.d("UpdateKills", "curr stage: " + currentStage);
        Log.d("UpdateKills", "killMission(curr): " + killMission.get(currentStage));
        if(killMission.containsKey(currentStage)){
            if(killMission.get(currentStage) == 1){
                killMission.remove(currentStage);
                nextStage();
            }
            else {
                killMission.put(currentStage, killMission.get(currentStage) - 1);
            }
        }
    }

    public void setKillMission(int stage, int numOfEnemies){
        killMission.put(stage, numOfEnemies);
    }

    public String getCurrentGoal(){
        return stages.get(currentStage);
    }

    public boolean isFinished(){
        return finished;
    }

    public TreeMap<Integer, String> getStages() {
        return stages;
    }

    public String getDescription() {
        return description;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public int getNumOfStages() {
        return numOfStages;
    }

    public int[] getReward() {
        return reward;
    }

    public String getMissionName() {
        return missionName;
    }

    private void popUpReward(int[] lootID){     //!TODO check whether it works for more items !
        Log.i("GameActivity", "POPUP");
        final Dialog rewardDialog = new Dialog(context);
        rewardDialog.setContentView(R.layout.loot_dialog);
        TableLayout tableLayout = rewardDialog.findViewById(R.id.rewardTable);
        TableRow row1 = rewardDialog.findViewById(R.id.row1);
        TableRow row2 = rewardDialog.findViewById(R.id.row2);
        Button btn = rewardDialog.findViewById(R.id.lootBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardDialog.dismiss();
            }
        });

        Log.i("GameActivity", "lootID len: " + lootID.length);
        HashMap<Integer, Integer> loot = new HashMap<>();
        for(int id: lootID){
            if(loot.containsKey(id)){
                loot.put(id, loot.get(id) + 1);
                Log.i("GameActivity", "add same item: " + loot.get(id));
            }
            else {
                loot.put(id, 1);
            }
        }
        Log.i("GameActivity", "loot len: " + loot.size());
        Iterator iter = loot.keySet().iterator();
        for(int i = 0; i < loot.size(); i++){
            int itemId = (int)iter.next();
            LayoutInflater inflater;
            inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.item_reward , null);

            ImageView imageView = parent.findViewById(R.id.rewardImg);
            Log.i("GameActivity", "imgview found, decoded id: " + itemId);
            if(inventory == null){
                Log.i("PROBLEM", "NULL inventory");
            }
            Item item = inventory.decodeItem(itemId);
            Log.i("GameActivity", "decoded, ID: " + item.getId());
            imageView.setImageResource(item.getResourceID());
            imageView.setAdjustViewBounds(true);
            imageView.setMaxWidth(100);
            imageView.setMaxHeight(100);

            TextView textView = parent.findViewById(R.id.rewardTxt);
            String txt = loot.get(itemId) + "";
            textView.setText(txt);
            Log.i("GameActivity", "txt OK");


            if(i < 8){
                row1.addView(parent);
            }
            else {
                row2.addView(parent);
            }
        }
        rewardDialog.show();
    }
}
