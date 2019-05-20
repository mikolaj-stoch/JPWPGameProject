package com.example.myapplication.GameObj;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.GameView;
import com.example.myapplication.Items.Inventory;
import com.example.myapplication.Items.Item;
import com.example.myapplication.Items.QuestItem;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Chest extends GameObject implements View.OnClickListener {

    private LinkedHashMap<Item, Integer> items;
    private ArrayList<Item> itemList;
    private QuestItem key;

    //View elements
    private Dialog dialog;
    private TableLayout tableLayout;
    private ArrayList<TableRow> rows;
    private ArrayList<View> itemSpots;

    private TextView itemName;

    private Button takeBtn;
    private Button takeAllBtn;
    private Button cancelBtn;

    private boolean opened;

    private int width;
    private int height;
    private Rect rect;

    private Item selectedItem;
    private Player player;
    private Inventory inventory;

    private int ID;

    public Chest(Bitmap res, int x, int y, String name, GameView gameView,
                 LinkedHashMap<Item, Integer> items, QuestItem key,Boolean drawOrNo, int ID){
        super(res, x, y, name, gameView, drawOrNo);
        this.items = items;
        this.key = key;

        this.width = res.getWidth();
        this.height = res.getHeight();

        rect = new Rect(x, y, x+width, y+height);
        this.player = gameView.getPlayer();
        this.inventory = gameView.getInventory();
        this.key = inventory.getKey1();
        this.ID = ID;
    }

    public void open(){      //eventually we should check whether player has key
        if(inventory.getItems().containsKey(this.key) || opened){
            Log.d("OPEN", "key work");
            initDialog();
            initItemList();
            fillWithItems();
            inventory.removeItem(key);
            opened = true;
        }
        else {
            new AlertDialog.Builder(gameView.getContext())
                    .setTitle("You don't have a key")
                    .setMessage("Firstly you need to find a key")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Continue with delete operation
//                        }
//                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void fillWithItems() {
        int i = 0;
        for(View iv: itemSpots){        //reset view
            ((ImageButton) iv).setImageResource(0);
        }
        if(items != null){
            for(Item item: itemList){
                ((ImageButton) itemSpots.get(i)).setImageResource(item.getResourceID());
                i++;
            }
        }
    }

    private void initItemList(){
        itemList = new ArrayList<Item>(items.keySet());
        Log.d("itemList: ", "size: " +  itemList.size());
    }

    private void initDialog() {
        dialog = new Dialog(super.gameView.getContext());
        dialog.setContentView(R.layout.chest);

        tableLayout = (TableLayout)dialog.findViewById(R.id.tableLayout);
        rows = new ArrayList<>();
        itemSpots = new ArrayList<>();

        itemName = (TextView)dialog.findViewById(R.id.itemName);
        itemName.setText(R.string.itemNotChosen);

        takeBtn = dialog.findViewById(R.id.takeBtn);
        takeBtn.setOnClickListener(this);
        takeAllBtn = dialog.findViewById(R.id.takeAllBtn);
        takeAllBtn.setOnClickListener(this);
        cancelBtn = dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);

        for (int i = 0; i < tableLayout.getChildCount(); i++){
            View child = tableLayout.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                rows.add(row);

                for (int x = 0; x < row.getChildCount(); x++) {
                    View spot = row.getChildAt(x);
                    spot.setOnClickListener(this);
                    itemSpots.add(spot);
                }
            }
        }

        dialog.show();
    }

    @Override
    public void onClick(View v) {       //!TODO
        switch (v.getId()){
            case R.id.takeBtn:
                takeItem(0);        //take just selected
                break;
            case R.id.takeAllBtn:
                takeItem(1);        //take all
                break;
            case R.id.cancelBtn:
                dialog.dismiss();
                break;
            case R.id.spot0:
                selectItem(0);
                break;
            case R.id.spot1:
                selectItem(1);
                break;
            case R.id.spot2:
                selectItem(2);
                break;
            case R.id.spot3:
                selectItem(3);
                break;
            case R.id.spot4:
                selectItem(4);
                break;
            case R.id.spot5:
                selectItem(5);
                break;
            case R.id.spot6:
                selectItem(6);
                break;
            case R.id.spot7:
                selectItem(7);
                break;
            case R.id.spot8:
                selectItem(8);
                break;
            case R.id.spot9:
                selectItem(9);
                break;
            case R.id.spot10:
                selectItem(10);
                break;
            case R.id.spot11:
                selectItem(11);
                break;
        }
    }

    private void takeItem(int all) {
        if(all == 0 && selectedItem != null){
            int quantity = items.get(selectedItem);
            for(int i = 0; i < quantity; i++){
                inventory.addItem(selectedItem);
            }
            items.remove(selectedItem);
            itemList.remove(selectedItem);
            selectedItem = null;
            fillWithItems();
            itemName.setText(R.string.itemNotChosen);
        }
        else if(all == 1){
            if (itemList != null){
                for (Item item : itemList) {
                    int quantity = items.get(item);
                    for (int i = 0; i < quantity; i++) {
                        inventory.addItem(item);
                    }
                    items.remove(item);
                    initItemList();
                    fillWithItems();
                }
                selectedItem = null;
                itemList = null;
                itemName.setText(R.string.emptyChest);
            }

        }
    }

    private void selectItem(int position){
        if(itemList != null) {
            if (position > -1 && position < itemList.size()) {
                selectedItem = itemList.get(position);
                String txt = selectedItem.getName() + ", quantity: " + items.get(selectedItem);
                itemName.setText(txt);
            }
            else {
                itemName.setText(R.string.itemNotChosen);
            }
        }
    }

    public QuestItem getKey() {
        return key;
    }

    public boolean isOpened() {
        return opened;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getID(){
        return ID;
    }
}

