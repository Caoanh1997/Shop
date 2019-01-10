package com.example.caoan.shop.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.caoan.shop.Model.Cart;

import java.util.ArrayList;
import java.util.List;

public class DataCart extends SQLiteOpenHelper {

    private List<Cart> cartList;
    private SQLiteDatabase sqLiteDatabase;

    public static String DATABASE_NAME = "Shop.db";
    public static String TABLE_CART = "CART";
    public static String TABLE_HISTORY = "HISTORY";
    public static String ID_CART = "ID_CART";
    public static String NAME = "NAME";
    public static String PRICE = "PRICE";
    public static String NUMBER = "NUMBER";
    public static String URL_IMAGE = "URL_IMAGE";
    public static String ID_HISTORY = "ID_HISTORY";
    public static String DATE_PUT = "DATE_PUT";
    public static String USER_NAME = "USER_NAME";
    public static String USER_ID = "USER_ID";
    public static String PRODUCT = "PRODUCT";
    public static String TOTAL = "TOTAL";

    public DataCart(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String table1 = "Create table "+ TABLE_CART + "( "
                + ID_CART + " integer Primary Key AUTOINCREMENT, "
                + NAME + " nvarchar(100), "
                + PRICE + " float, "
                + NUMBER + " integer, "
                + URL_IMAGE + " nvarchar(200))";

        sqLiteDatabase.execSQL(table1);
        String table2 = "create table "+ TABLE_HISTORY + " ( "
                + ID_HISTORY + " integer Primary key AUTOINCREMENT, "
                + DATE_PUT + " nvarchar(100), "
                + USER_ID + " nvarchar(100), "
                + USER_NAME + " nvarchar(100), "
                + PRODUCT + " nvarchar(100), "
                + PRICE + " float, "
                + NUMBER + "integer)";
        sqLiteDatabase.execSQL(table2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<Cart> getCartList() {
        cartList = new ArrayList<Cart>();
        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =null;

        String sql = "select * from "+TABLE_CART;

        cursor  = sqLiteDatabase.rawQuery(sql,null);
        while (cursor.moveToNext()){
            Cart cart = new Cart();
            cart.setId(cursor.getInt(0));
            cart.setName(cursor.getString(1));
            cart.setPrice(cursor.getFloat(2));
            cart.setNumber(cursor.getInt(3));
            cart.setUrlImage(cursor.getString(4));

            cartList.add(cart);
        }

        return cartList;
    }
    public void Delete(int id_cart){
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CART,ID_CART+"=?",new String[]{String.valueOf(id_cart)});
        sqLiteDatabase.close();
    }

    public void InsertCart(Cart cart){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME,cart.getName());
        values.put(PRICE,cart.getPrice());
        values.put(NUMBER,cart.getNumber());
        values.put(URL_IMAGE,cart.getUrlImage());
        sqLiteDatabase.insert(TABLE_CART,null,values);
        sqLiteDatabase.close();
    }
    public String Total(){
        String str="";
        sqLiteDatabase = this.getReadableDatabase();
        String sql = "select sum("+PRICE+"*"+NUMBER+") as Total from "+TABLE_CART;

        Cursor cursor = null;
        cursor = sqLiteDatabase.rawQuery(sql,null);
        while (cursor.moveToNext()){
            str = cursor.getString(0);
        }
        return str;
    }
}
