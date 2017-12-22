package com.piccante.anurr.piccantemenu;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.piccante.anurr.piccantemenu.databinding.CreatePastaBinding;
import com.piccante.anurr.piccantemenu.databinding.MenuOrderBinding;
import com.suke.widget.SwitchButton;

import cn.pedant.SweetAlert.SweetAlertDialog;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

/**
 * Created by anurr on 10/13/2017.
 */

public class Order extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    CreatePastaBinding createPasta;

    MenuOrderBinding orderBinding;
    SweetAlertDialog sweetAlertDialog;
    long newRowId;
    int amount, totalHarga, hargamix, hargamushroom = 0, hargacheddar = 0, hargamozarella = 0, hargawurst = 0, hargaegg = 0;
    String toppings = "", mushtext = "", chedtext = "", mozatext = "", wurstext = "", eggtext = "";

    private PastaAdapter pastaAdapter;
    Cursor rdcursor;
    Snackbar mySnackbar;
    Animation modalIn, modalOut;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        orderBinding = DataBindingUtil.inflate(
                inflater, R.layout.menu_order, container, false);
        View view = orderBinding.getRoot();

        orderBinding.menuGroup.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                switch (currentPosition){
                    case 0:
                        orderBinding.menuPastaGroup.pastamenu.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        orderBinding.menuPastaGroup.pastamenu.setVisibility(View.GONE);
                        break;
                    case 2:
                        orderBinding.menuPastaGroup.pastamenu.setVisibility(View.GONE);
                        break;
                    default:
                        orderBinding.menuPastaGroup.pastamenu.setVisibility(View.VISIBLE);
                }
            }
        });

        modalIn = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_in);
        modalOut = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_out);

        String guest = getArguments().getString("guest");
        if (guest.isEmpty()){
            orderBinding.guestname.setText(Html.fromHtml("Hello, <b><i>Guest!</i></b>"));
            orderBinding.menuPastaGroup.addpasta.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                }
            });
        } else {
            orderBinding.guestname.setText(Html.fromHtml("Hello, " + "<b>"+guest+"!</b>"));
        }

        orderBinding.menuPastaGroup.addpasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                createPastaDialog();
                sweetAlertDialog.show();
            }
        });

        //readDB();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.supportsPredictiveItemAnimations();
        orderBinding.menuPastaGroup.pastalist.setLayoutManager(layoutManager);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(orderBinding.menuPastaGroup.pastalist);

        pastaAdapter = new PastaAdapter(getContext());
        orderBinding.menuPastaGroup.pastalist.setAdapter(pastaAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mySnackbar = Snackbar.make(orderBinding.ordercoor, "Total Price: ", Snackbar.LENGTH_INDEFINITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mySnackbar.getView().setBackgroundColor(getActivity().getColor(R.color.piccante));
            mySnackbar.setActionTextColor(getActivity().getColor(R.color.colorWhite));
        }
        mySnackbar.setAction("Checkout", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySnackbar.dismiss();
            }
        });

    }

    public void createPastaDialog(){
        View vf = getActivity().getLayoutInflater().inflate(R.layout.create_pasta, null);
        createPasta = DataBindingUtil.bind(vf);
        createPastaView();

        sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Piccante's pasta maker")
                .setCustomView(vf)
                .setConfirmButton("Add Pasta", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sweetAlertDialog) {
                        //insert into table
                        //saveToDB();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addToList();
                            }
                        }, 500);
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });

        sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setEnabled(false);
            }
        });

        sweetAlertDialog.showCancelButton(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);

        createPasta.levelGroup.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setEnabled(true);
            }
        });

    }

    private void deleteFromDB(long values){
        SQLiteDatabase database =  new SampleSQLiteDBHelper(getContext()).getWritableDatabase();
        database.execSQL("DELETE FROM pasta WHERE _id = '" + values + "'");
        pastaAdapter.notifyDataSetChanged();
    }

    private void saveToDB() {

        SQLiteDatabase database = new SampleSQLiteDBHelper(getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleSQLiteDBHelper.PIZZA_COLUMN_TYPE, createPasta.pastaGroup.getPosition());
        values.put(SampleSQLiteDBHelper.PIZZA_COLUMN_SAUCE, createPasta.sauceGroup.getPosition());
        values.put(SampleSQLiteDBHelper.PIZZA_COLUMN_LEVEL, createPasta.levelGroup.getPosition());
        values.put(SampleSQLiteDBHelper.PIZZA_COLUMN_TOP, toppings);
        values.put(SampleSQLiteDBHelper.PIZZA_COLUMN_AMOUNT, amount);
        values.put(SampleSQLiteDBHelper.PIZZA_COLUMN_PIZZA_PRICE, totalHarga);
        newRowId = database.insert(SampleSQLiteDBHelper.PIZZA_TABLE_NAME, null, values);
        pastaAdapter.notifyDataSetChanged();

    }

    private void readDB(){

        SQLiteDatabase database = new SampleSQLiteDBHelper(getContext()).getReadableDatabase();

        String[] projection = {
                SampleSQLiteDBHelper.PIZZA_COLUMN_ID,
                SampleSQLiteDBHelper.PIZZA_COLUMN_TYPE,
                SampleSQLiteDBHelper.PIZZA_COLUMN_SAUCE,
                SampleSQLiteDBHelper.PIZZA_COLUMN_LEVEL,
                SampleSQLiteDBHelper.PIZZA_COLUMN_TOP,
                SampleSQLiteDBHelper.PIZZA_COLUMN_AMOUNT,
                SampleSQLiteDBHelper.PIZZA_COLUMN_PIZZA_PRICE
        };

        rdcursor = database.query(
                SampleSQLiteDBHelper.PIZZA_TABLE_NAME,   // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );

    }

    public void addToList(){
        toppings = mushtext+chedtext+mozatext+wurstext+eggtext;
        pastaAdapter.addItemAtPosition(0, createPasta.pastaGroup.getPosition(),
                createPasta.sauceGroup.getPosition(), createPasta.levelGroup.getPosition(),
                toppings, amount, totalHarga);
        sweetAlertDialog.cancel();
        mySnackbar.show();
    }

    private void createPastaView(){

        amount = 1;
        hargamix = 22000;
        totalHarga = hargamix;

        createPasta.harga.setText(String.valueOf(totalHarga));

        createPasta.amounttext.setText(String.valueOf(amount));

        createPasta.stepGroup.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                switch (currentPosition){
                    case 0:
                        createPasta.step1Sec.setVisibility(View.VISIBLE);
                        createPasta.step1Sec.startAnimation(modalIn);
                        createPasta.step2Sec.setAnimation(modalOut);
                        createPasta.step2Sec.setVisibility(View.GONE);
                        createPasta.step2Sec.setAnimation(modalOut);
                        createPasta.step3Sec.setVisibility(View.GONE);
                        break;
                    case 1:
                        createPasta.step1Sec.setAnimation(modalOut);
                        createPasta.step1Sec.setVisibility(View.GONE);
                        createPasta.step2Sec.setVisibility(View.VISIBLE);
                        createPasta.step2Sec.startAnimation(modalIn);
                        createPasta.step3Sec.setAnimation(modalOut);
                        createPasta.step3Sec.setVisibility(View.GONE);
                        break;
                    case 2:
                        createPasta.step1Sec.setAnimation(modalOut);
                        createPasta.step1Sec.setVisibility(View.GONE);
                        createPasta.step2Sec.setAnimation(modalOut);
                        createPasta.step2Sec.setVisibility(View.GONE);
                        createPasta.step3Sec.setVisibility(View.VISIBLE);
                        createPasta.step3Sec.startAnimation(modalIn);
                        break;
                    default:
                        createPasta.step1Sec.setVisibility(View.VISIBLE);
                        createPasta.step2Sec.setVisibility(View.GONE);
                        createPasta.step3Sec.setVisibility(View.GONE);
                }
            }
        });

        createPasta.mushroomSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                hargamushroom = 4000;
                if (createPasta.mushroomSwitch.isChecked()){
                    hargamix += hargamushroom;
                    totalHarga = hargamix * amount;
                    mushtext = " MUSHROOM";
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                } else {
                    hargamix -= hargamushroom;
                    totalHarga = hargamix * amount;
                    mushtext = "";
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                }
            }
        });

        createPasta.cheddarSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                hargacheddar = 2000;
                if (createPasta.cheddarSwitch.isChecked()){
                    hargamix += hargacheddar;
                    totalHarga = hargamix * amount;
                    chedtext = " CHEDDAR";
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                } else {
                    hargamix -= hargacheddar;
                    totalHarga = hargamix * amount;
                    chedtext = "";
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                }
            }
        });

        createPasta.mozarellaSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                hargamozarella =4000;
                if (createPasta.mozarellaSwitch.isChecked()){
                    hargamix += hargamozarella;
                    totalHarga = hargamix * amount;
                    mozatext = " MOZARELLA";
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                } else {
                    hargamix -= hargamozarella;
                    totalHarga = hargamix * amount;
                    mozatext = "";
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                }
            }
        });

        createPasta.wurstSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                hargawurst =4000;
                if (createPasta.wurstSwitch.isChecked()){
                    hargamix += hargawurst;
                    totalHarga = hargamix * amount;
                    wurstext = " WURST";
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                } else {;
                    hargamix -= hargawurst;
                    totalHarga = hargamix * amount;
                    wurstext = "";
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                }
            }
        });

        createPasta.eggSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                hargaegg  =3000;
                if (createPasta.eggSwitch.isChecked()){
                    hargamix += hargaegg;
                    totalHarga = hargamix * amount;
                    eggtext = " EGG";
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                } else {
                    hargamix -= hargaegg;
                    totalHarga = hargamix * amount;
                    eggtext = "";
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                }
            }
        });

        createPasta.addtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount++;
                totalHarga = hargamix * amount;
                createPasta.amounttext.setText(String.valueOf(amount));
                createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
            }
        });

        createPasta.mintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount == 1){
                    Toast.makeText(getActivity(), "Amount menu_order cannot less than 1",
                            Toast.LENGTH_SHORT).show();
                } else {
                    amount--;
                    totalHarga = hargamix * amount;
                    createPasta.amounttext.setText(String.valueOf(amount));
                    createPasta.harga.setText(String.valueOf(String.valueOf(totalHarga)));
                }
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        long id = viewHolder.getAdapterPosition();
        pastaAdapter.removeItemAtPosition(viewHolder.getAdapterPosition());
        //deleteFromDB(id);
        int datacount = pastaAdapter.getItemCount();
        if (datacount == 0){
            mySnackbar.dismiss();
        }
            Toast.makeText(getContext(), "Item Position: " + id + " Removed. Data count: " + datacount, Toast.LENGTH_SHORT).show();
    }
}
