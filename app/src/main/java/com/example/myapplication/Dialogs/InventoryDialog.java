package com.example.myapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.Clickable;
import com.example.myapplication.GameObj.Player;
import com.example.myapplication.Items.Inventory;
import com.example.myapplication.Items.Item;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Iterator;

public class InventoryDialog extends Dialog implements View.OnClickListener, Clickable {

    private TableLayout tableLayout;
    private ArrayList<TableRow> rows;
    private ArrayList<View> itemSpots;

    private TextView description;
    private Button useBtn;

    private Inventory inventory;
    private Player player;
    private Context context;

    private Item selectedItem;
    private int selecetedIdx;

    public InventoryDialog(Context context, Inventory inventory, Player player) {
        super(context);
        this.context = context;
        this.inventory = inventory;
        this.player = player;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.inventory_dialog);

        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        description = (TextView)findViewById(R.id.itemDescription);
        useBtn = (Button)findViewById(R.id.useBtn);

        rows = new ArrayList<>();
        itemSpots = new ArrayList<>();

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

        fillWithItems();

        useBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {           //must be a better way to do this, need to add spots
        switch (v.getId()){
            case R.id.useBtn:
                if(selectedItem != null) {
                    if (selectedItem.isUsable()) {
                        selectedItem.use(player, inventory);
                        fillWithItems();
                        setDescription(selecetedIdx);
                    }
                }
                break;
            case R.id.spot0:
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
            case R.id.spot16:
                setDescription(16);
                break;
            case R.id.spot17:
                setDescription(17);
                break;
            case R.id.spot18:
                setDescription(18);
                break;
            case R.id.spot19:
                setDescription(19);
                break;
            default:
                String defaultTxt = "Chose item to get more information";
                description.setText(defaultTxt);
                this.selectedItem = null;
                break;

        }


    }

    private void setDescription(int idx){
        ArrayList<Item> items = inventory.getItemList();
        this.selecetedIdx = idx;
        if(items.size() > idx){
            String str = items.get(idx).toString() + "\nQuantity: " +
                    inventory.getItems().get(items.get(idx));
            this.selectedItem = items.get(idx);
            description.setText(str);
        }
        else {
            String defaultTxt = "Chose item to get more information";
            this.description.setText(defaultTxt);
            selectedItem = null;
        }

    }

    @Override
    public void actionOnClick() {
        this.show();
        fillWithItems();
    }

    public void fillWithItems(){
        int itemNum = inventory.getItemList().size();
        Iterator iter = inventory.getItemList().iterator();

        for(int i = 0; i < itemSpots.size(); i++){
            if(i < itemNum) {
                Item item = (Item) iter.next();
                int resourceID = item.getResourceID();
                ((ImageButton) itemSpots.get(i)).setImageResource(resourceID);
            }
            else {
                ((ImageButton) itemSpots.get(i)).setImageResource(0);       //we have to clear empty spots
            }
        }
    }
}
