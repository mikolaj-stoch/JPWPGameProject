package com.example.myapplication.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.GameObj.Combat;
import com.example.myapplication.GameView;
import com.example.myapplication.Dialogs.PauseMenu;
import com.example.myapplication.Items.Item;
import com.example.myapplication.R;

import java.util.HashMap;
import java.util.Iterator;

public class GameActivity extends AppCompatActivity {
    GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        gameView.setKeepScreenOn(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(gameView);
        getSupportActionBar().hide();
    }
//---------------------???--------------
    @Override
    public void onPause(){
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume();
    }

    @Override
    public void onBackPressed(){
        PauseMenu menu = new PauseMenu(this);
        menu.show();
    }

    //---------send data back from inventory activity----------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("onActivityResult\t", gameView.getPlayer().getStrength()+ "");
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String newStrength = data.getStringExtra("newStrength");
                String newAgility = data.getStringExtra("newAgility");
                String newVitality = data.getStringExtra("newVitality");
                String newWisdom = data.getStringExtra("newWisdom");
                String newPoints = data.getStringExtra("newPoints");
                String newMaxHp = data.getStringExtra("newMaxHp");
                String newMaxStamina = data.getStringExtra("newMaxStamina");
                String newHp = data.getStringExtra("newHp");
                String newStamina = data.getStringExtra("newStamina");

                gameView.getPlayer().setStrength(Integer.parseInt(newStrength));
                gameView.getPlayer().setAgility(Integer.parseInt(newAgility));
                gameView.getPlayer().setVitality(Integer.parseInt(newVitality));
                gameView.getPlayer().setWisdom(Integer.parseInt(newWisdom));
                gameView.getPlayer().setPointsToSpend(Integer.parseInt(newPoints));
                gameView.getPlayer().setMaxHp(Integer.parseInt(newMaxHp));
                gameView.getPlayer().setMaxStamina(Integer.parseInt(newMaxStamina));
                gameView.getPlayer().setHp(Integer.parseInt(newHp));
                gameView.getPlayer().setStamina(Integer.parseInt(newStamina));

            }
        }
        else if(requestCode == 2){
            String newHp = data.getStringExtra("newHp");
            String newStamina = data.getStringExtra("newStamina");
            String xp = data.getStringExtra("xpReward");
            gameView.getPlayer().setHp(Integer.parseInt(newHp));
            gameView.getPlayer().setStamina(Integer.parseInt(newStamina));
            gameView.getPlayer().addXp(Integer.parseInt(xp));
            if(gameView.getPlayer().checkLvlUp()){      //!TODO
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.level_up);
                Button okBtn = dialog.findViewById(R.id.okBtn);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

            Combat enemy = gameView.getAc().getEnemy(); //if we get back to GameActivity that means we've won and we can add loot to player's inventory
            enemy.die();
            gameView.getInventory().addLoot(enemy);
            popUpReward(enemy.getLoot());
            if(gameView.getPlayer().getMission().getCurrentStage() != 1){
                gameView.getPlayer().getMission().updateKills();
            }


        }
    }

    private void popUpReward(int[] lootID){     //!TODO check whether it works for more items !
        Log.i("GameActivity", "POPUP");
        final Dialog rewardDialog = new Dialog(this);
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
            inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);

            LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.item_reward , null);

            ImageView imageView = parent.findViewById(R.id.rewardImg);
            Log.i("GameActivity", "imgview found, decoded id: " + itemId);

            Item item = gameView.getInventory().decodeItem(itemId);
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

//    public ActivityCreator getActivityCreator(){
//        return new ActivityCreator(this);
//    }
}
