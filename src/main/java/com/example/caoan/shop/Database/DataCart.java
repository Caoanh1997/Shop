package com.example.caoan.shop.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.caoan.shop.Model.Cart;
import com.example.caoan.shop.Model.Store;

import java.util.ArrayList;
import java.util.List;

public class DataCart extends SQLiteOpenHelper {

    private List<Cart> cartList;
    private List<Store> storeList;
    private SQLiteDatabase sqLiteDatabase;

    public static String DATABASE_NAME = "Shop.db";
    public static String TABLE_CART = "CART";
    public static String TABLE_HISTORY = "HISTORY";
    public static String TABLE_STORE = "STORE";
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
    public static String KEY_STORE = "KEY_STORE";
    public static String NAME_STORE = "NAME_STORE";
    public static String DUONG = "DUONG";
    public static String XA = "XA";
    public static String HUYEN = "HUYEN";
    public static String TINH = "TINH";
    public static String USER_KEY = "USER_KEY";
    public static String URL_STORE = "URL_STORE";
    public static String PHONE = "PHONE";

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
                + URL_IMAGE + " nvarchar(200), "
                + KEY_STORE + " nvarchar(100), "
                + USER_KEY + " nvarchar(100))";

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
        String sql3 = "create table " + TABLE_STORE + " ( "
                + KEY_STORE + " nvarchar(100), "
                + NAME_STORE + " nvarchar(100), "
                + DUONG + " nvarchar(200), "
                + XA + " nvarchar(100), "
                + HUYEN + " nvarchar(100), "
                + TINH + " nvarchar(100), "
                + USER_KEY + " nvarchar(100), "
                + URL_STORE + " nvarchar(200), "
                + PHONE + " nvarchar(100))";
        sqLiteDatabase.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "Drop table if exists " + TABLE_CART;
        String sql2 = "Drop table if exists " + TABLE_STORE;
        String sql3 = "Drop table if exists " + TABLE_HISTORY;

        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
    }

    public List<Store> getStoreList() {
        storeList = new ArrayList<Store>();
        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        String sql = "select * from "+TABLE_STORE;
        cursor = sqLiteDatabase.rawQuery(sql,null);

        while (cursor.moveToNext()){
            Store store = new Store();
            store.setKey(cursor.getString(0));
            store.setName(cursor.getString(1));
            store.setDuong(cursor.getString(2));
            store.setXa(cursor.getString(3));
            store.setHuyen(cursor.getString(4));
            store.setTinh(cursor.getString(5));
            store.setUserkey(cursor.getString(6));
            store.setUrlImage(cursor.getString(7));
            store.setPhone(cursor.getString(8));

            storeList.add(store);
        }

        return storeList;
    }

    public void DeleteStore(String key){
        sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_STORE,KEY_STORE +"=?",new String[]{String.valueOf(key)});
        sqLiteDatabase.close();
    }

    public void InsertStore(Store store){

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STORE,store.getKey());
        values.put(NAME_STORE,store.getName());
        values.put(DUONG,store.getDuong());
        values.put(XA,store.getXa());
        values.put(HUYEN,store.getHuyen());
        values.put(TINH, store.getTinh());
        values.put(USER_KEY,store.getUserkey());
        values.put(URL_STORE,store.getUrlImage());
        values.put(PHONE,store.getPhone());
        sqLiteDatabase.insert(TABLE_STORE,null,values);

        sqLiteDatabase.close();
    }

    public void DeleteAllStore(){

        sqLiteDatabase = this.getWritableDatabase();
        String sql = "delete from " + TABLE_STORE;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }

    public List<Cart> getCartList(String key_store) {
        cartList = new ArrayList<Cart>();
        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =null;

        String sql = "select * from "+TABLE_CART + " where " + KEY_STORE + " == '" + key_store + "'";

        cursor  = sqLiteDatabase.rawQuery(sql,null);
        while (cursor.moveToNext()){
            Cart cart = new Cart();
            cart.setId(cursor.getInt(0));
            cart.setName(cursor.getString(1));
            cart.setPrice(cursor.getFloat(2));
            cart.setNumber(cursor.getInt(3));
            cart.setUrlImage(cursor.getString(4));
            cart.setKeyStore(cursor.getString(5));
            cart.setUserKey(cursor.getString(6));

            cartList.add(cart);
        }

        return cartList;
    }
    public void Delete(int id_cart){
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CART,ID_CART+"=?",new String[]{String.valueOf(id_cart)});
        sqLiteDatabase.close();
    }
    public void DeleteCart(String key_store){
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CART,KEY_STORE+"=?",new String[]{key_store});
        sqLiteDatabase.close();
    }

    public void InsertCart(Cart cart){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME,cart.getName());
        values.put(PRICE,cart.getPrice());
        values.put(NUMBER,cart.getNumber());
        values.put(URL_IMAGE,cart.getUrlImage());
        values.put(KEY_STORE, cart.getKeyStore());
        values.put(USER_KEY, cart.getUserKey());
        sqLiteDatabase.insert(TABLE_CART,null,values);
        sqLiteDatabase.close();
    }
    public void UpdateNumber(int number, Cart cart){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NUMBER,number);
        sqLiteDatabase.update(TABLE_CART,values,ID_CART+"=?",new String[]{String.valueOf(cart.getId())});
        sqLiteDatabase.close();
    }
    public int GetNumber(Cart cart){
        sqLiteDatabase = this.getWritableDatabase();
        String sql = "select " + NUMBER + " from " + TABLE_CART + " where " + ID_CART + " == " + cart.getId();
        Cursor cursor;
        cursor = sqLiteDatabase.rawQuery(sql,null);
        int number=1;
        while (cursor.moveToNext()){
            number = cursor.getInt(0);
        }
        return number;
    }
    public String Total(String key_store){
        String str="";
        sqLiteDatabase = this.getReadableDatabase();
        String sql = "select sum("+PRICE+"*"+NUMBER+") as Total from "+TABLE_CART + " where " + KEY_STORE + " == '" + key_store + "'";

        Cursor cursor = null;
        cursor = sqLiteDatabase.rawQuery(sql,null);
        while (cursor.moveToNext()){
            str = cursor.getString(0);
        }
        if(str == null){
            str="0";
        }
        return str;
    }
}
