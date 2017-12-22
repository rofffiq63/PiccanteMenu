package com.piccante.anurr.piccantemenu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Obaro on 19/09/2016.
 */
public class SampleSQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "piccante_dbase";
    public static final String PIZZA_TABLE_NAME = "pasta";
    public static final String PIZZA_COLUMN_ID = "_id";
    public static final String PIZZA_COLUMN_OWNER = "owner";
    public static final String PIZZA_COLUMN_ORDER_ID = "order_id";
    public static final String PIZZA_COLUMN_DATE = "date";
    public static final String PIZZA_COLUMN_TYPE = "type";
    public static final String PIZZA_COLUMN_SAUCE = "sauce";
    public static final String PIZZA_COLUMN_LEVEL = "level";
    public static final String PIZZA_COLUMN_TOP = "toppings";
    public static final String PIZZA_COLUMN_AMOUNT = "amount";
    public static final String PIZZA_COLUMN_PIZZA_PRICE = "pizza_price";

    public SampleSQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + PIZZA_TABLE_NAME + " (" +
                PIZZA_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PIZZA_COLUMN_OWNER + " VARCHAR (20), " +
                PIZZA_COLUMN_ORDER_ID + " INT, " +
                PIZZA_COLUMN_DATE + " DATE, " +
                PIZZA_COLUMN_TYPE + " INT, " +
                PIZZA_COLUMN_SAUCE + " INT, " +
                PIZZA_COLUMN_LEVEL + " INT, " +
                PIZZA_COLUMN_TOP + " TEXT, " +
                PIZZA_COLUMN_AMOUNT + " INT, " +
                PIZZA_COLUMN_PIZZA_PRICE + " INT" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PIZZA_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
