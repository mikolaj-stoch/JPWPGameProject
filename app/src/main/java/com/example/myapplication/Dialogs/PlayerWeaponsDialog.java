package com.example.myapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.Clickable;
import com.example.myapplication.GameObj.Player;
import com.example.myapplication.Items.Inventory;
import com.example.myapplication.Items.Item;
import com.example.myapplication.Items.Weapon;
import com.example.myapplication.R;

import java.util.ArrayList;

public class PlayerWeaponsDialog extends Dialog implements View.OnClickListener, Clickable {

    private TableLayout tableLayout;
    private ArrayList<TableRow> rows;
    private ArrayList<View> itemSpots;

    private ImageView helmet;
    private ImageView chestArmor;
    private ImageView primaryWeapon;
    private ImageView secondaryWeapon;

    private TextView description;
    private Button equipBtn;

    private Inventory inventory;
    private Player player;
    private ArrayList<Item> weapons;

    private Item selectedItem;
    private int selectedIdx;

    Context context;
    public PlayerWeaponsDialog(Context context, Player player, Inventory inventory) {
        super(context);
        this.context = context;
        this.player = player;
        this.inventory = inventory;

        rows = new ArrayList<>();
        itemSpots = new ArrayList<>();
        weapons = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.weapons_dialog);

        tableLayout = (TableLayout)findViewById(R.id.weaponsTable);

        helmet = (ImageView)findViewById(R.id.helmet);
        helmet.setOnClickListener(this);
        chestArmor = (ImageView)findViewById(R.id.chest);
        chestArmor.setOnClickListener(this);
        primaryWeapon = (ImageView)findViewById(R.id.primaryWeapon);
        primaryWeapon.setOnClickListener(this);
        secondaryWeapon = (ImageView)findViewById(R.id.secondaryWeapon);
        secondaryWeapon.setOnClickListener(this);

        description = (TextView)findViewById(R.id.itemDescription);
        equipBtn = (Button)findViewById(R.id.eqBtn);
        equipBtn.setOnClickListener(this);

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
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
        initWeaponList();
        fillWithItems();
    }

    private void fillWithItems() {
        int i = 0;
        for(View iv: itemSpots){        //reset view
            ((ImageButton) iv).setImageResource(0);
        }
        for(Item item: weapons){
            ((ImageButton) itemSpots.get(i)).setImageResource(item.getResourceID());
            i++;
        }
    }

    private void initWeaponList(){
        ArrayList<Item> itemList = inventory.getItemList();
        weapons.clear();
        for(Item item: itemList){
            if (item instanceof Weapon){
                weapons.add(item);
            }
        }
        weapons.remove(player.getHelmet());
        weapons.remove(player.getPrimaryWeapon());
        weapons.remove(player.getSecondaryWeapon());
        weapons.remove(player.getChestArmor());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.eqBtn:
                if(selectedItem != null) {
                    changeItem();
                    fillWithItems();
                    setDescription(selectedIdx);
                }
                else {
                    Log.d("change Item", "NULL ITEM");
                }
                break;
            case R.id.spot0:
                Log.d("spot 0", "click");
                setDescription(0);
                break;
            case R.id.spot1:
                setDescription(1);
                break;
            case R.id.spot2:
                setDescription(2);
                break;
            case R.id.spot3:
                setDescription(3);
                break;
            case R.id.spot4:
                setDescription(4);
                break;
            case R.id.spot5:
                setDescription(5);
                break;
            case R.id.spot6:
                setDescription(6);
                break;
            case R.id.spot7:
                setDescription(7);
                break;
            case R.id.spot8:
                setDescription(8);
                break;
            case R.id.spot9:
                setDescription(9);
                break;
            case R.id.spot10:
                setDescription(10);
                break;
            case R.id.spot11:
                setDescription(11);
                break;
            case R.id.spot12:
                setDescription(12);
                break;
            case R.id.spot13:
                setDescription(13);
                break;
            case R.id.spot14:
                setDescription(14);
                break;
            case R.id.spot15:
                setDescription(15);
                break;
            case R.id.primaryWeapon:
                setDescription(Weapon.WeaponType.PRIMARY_WEAPON);
                break;
            case R.id.secondaryWeapon:
                setDescription(Weapon.WeaponType.SECONDARY_WEAPON);
                break;
            case R.id.helmet:
                setDescription(Weapon.WeaponType.HELMET);
                break;
            case R.id.chest:
                setDescription(Weapon.WeaponType.CHEST);
                break;
            default:
                String defaultTxt = "Chose item to get more information";
                description.setText(defaultTxt);
                this.selectedItem = null;
                break;
        }
    }

    private void setDescription(int idx) {
        this.selectedIdx = idx;
        if(weapons.size() > idx){
            String str = weapons.get(idx).toString();
            this.selectedItem = weapons.get(idx);
            description.setText(str);
        }
        else {
            String defaultTxt = "Chose item to get more information";
            this.description.setText(defaultTxt);
            selectedItem = null;
        }
    }

    private void setDescription(Weapon.WeaponType weaponType){
        String text;
        switch (weaponType){
            case CHEST:
                if(player.getChestArmor() != null) {
                    text = player.getChestArmor().toString();
                }
                else {
                    text = context.getResources().getString(R.string.itemNotChosen);
                }
                description.setText(text);
                break;
            case HELMET:
                if(player.getHelmet() != null) {
                    text = player.getHelmet().toString();
                }
                else {
                    text = context.getResources().getString(R.string.itemNotChosen);
                }
                description.setText(text);
                break;
            case PRIMARY_WEAPON:
                if(player.getPrimaryWeapon() != null) {
                    text = player.getPrimaryWeapon().toString();
                }
                else {
                    text = context.getResources().getString(R.string.itemNotChosen);
                }
                description.setText(text);
                break;
            case SECONDARY_WEAPON:
                if(player.getSecondaryWeapon() != null) {
                    text = player.getSecondaryWeapon().toString();
                }
                else {
                    text = context.getResources().getString(R.string.itemNotChosen);
                }
                description.setText(text);
                break;
        }
    }

    private void changeItem(){

        Item itemIn = selectedItem;
        Weapon.WeaponType weaponType = ((Weapon)itemIn).getWeaponType();
        Item itemOut;
        switch (weaponType){
            case HELMET:
                itemOut = player.getHelmet();
                helmet.setImageResource(itemIn.getResourceID());
                break;
            case CHEST:
                itemOut = player.getChestArmor();
                chestArmor.setImageResource(itemIn.getResourceID());
                break;
            case PRIMARY_WEAPON:
                itemOut = player.getPrimaryWeapon();
                primaryWeapon.setImageResource(itemIn.getResourceID());
                break;
            case SECONDARY_WEAPON:
                itemOut = player.getSecondaryWeapon();
                secondaryWeapon.setImageResource(itemIn.getResourceID());
                break;
            default:
                itemOut = null;
                break;
        }
        weapons.remove(itemIn);
        if(itemOut != null) {
            weapons.add(itemOut);
        }


        player.unequip(weaponType);          // 3 - primaryWeapon
        player.equip(selectedItem);
    }

    @Override
    public void actionOnClick() {

        this.show();
        initWeaponList();
        fillWithItems();

    }
}
