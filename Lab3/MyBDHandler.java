package com.example.lab3databases;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRODUCT_PRICE = "price";
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_cmd = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " TEXT, "
                + COLUMN_PRODUCT_PRICE + " REAL "
                + ")";
        db.execSQL(create_table_cmd);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null); // returns "cursor" all products from the table
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(COLUMN_PRODUCT_PRICE, product.getProductPrice());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    // 查询：支持 name 前缀匹配、price 精确匹配，二者可单独或组合
    public Cursor queryProducts(String namePrefix, Double exactPrice) {
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder selection = new StringBuilder();
        ArrayList<String> args = new ArrayList<>();

        if (namePrefix != null && !namePrefix.trim().isEmpty()) {
            selection.append(COLUMN_PRODUCT_NAME).append(" LIKE ?");
            args.add(namePrefix.trim() + "%"); // 前缀匹配
        }
        if (exactPrice != null) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append(COLUMN_PRODUCT_PRICE).append(" = ?");
            args.add(String.valueOf(exactPrice));
        }

        String sel = selection.length() == 0 ? null : selection.toString();
        String[] selArgs = args.isEmpty() ? null : args.toArray(new String[0]);

        return db.query(
                TABLE_NAME,
                null,
                sel,
                selArgs,
                null,
                null,
                COLUMN_PRODUCT_NAME + " ASC"
        );
    }

    // 删除：按名称删除；caseSensitive=false 表示不区分大小写
    public int deleteByName(String name, boolean caseSensitive) {
        if (name == null || name.trim().isEmpty()) return 0;
        SQLiteDatabase db = this.getWritableDatabase();

        if (caseSensitive) {
            return db.delete(TABLE_NAME, COLUMN_PRODUCT_NAME + " = ?", new String[]{ name.trim() });
        } else {
            // SQLite 的 NOCASE 排序规则：不区分大小写匹配
            return db.delete(TABLE_NAME, COLUMN_PRODUCT_NAME + " = ? COLLATE NOCASE", new String[]{ name.trim() });
        }
    }


    public void seedIfEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        try {
            if (c.moveToFirst()) {
                int cnt = c.getInt(0);
                if (cnt == 0) {
                    ContentValues v1 = new ContentValues();
                    v1.put(COLUMN_PRODUCT_NAME, "Apple");
                    v1.put(COLUMN_PRODUCT_PRICE, 1.99);
                    db.insert(TABLE_NAME, null, v1);

                    ContentValues v2 = new ContentValues();
                    v2.put(COLUMN_PRODUCT_NAME, "Banana");
                    v2.put(COLUMN_PRODUCT_PRICE, 0.99);
                    db.insert(TABLE_NAME, null, v2);
                }
            }
        } finally {
            c.close();
        }
    }

}
