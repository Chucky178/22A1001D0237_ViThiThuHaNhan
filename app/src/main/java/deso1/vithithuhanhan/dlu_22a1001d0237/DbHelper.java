package deso1.vithithuhanhan.dlu_22a1001d0237;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FoodDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FOOD = "Food";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TABLE = "CREATE TABLE " + TABLE_FOOD + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_PRICE + " REAL NOT NULL, " +
                    COLUMN_IMAGE + " TEXT)";
            db.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        onCreate(db);
    }

    public void addFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, food.getName());
            values.put(COLUMN_PRICE, food.getPrice());
            values.put(COLUMN_IMAGE, food.getImage());

            db.insertOrThrow(TABLE_FOOD, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public List<Food> getAllFoods() {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOOD, null)) {
            while (cursor.moveToNext()) {
                Food food = new Food(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                );
                foodList.add(food);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return foodList;
    }

    public int getFoodCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        try (Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_FOOD, null)) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return count;
    }

    public void updateFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, food.getName());
            values.put(COLUMN_PRICE, food.getPrice());
            values.put(COLUMN_IMAGE, food.getImage());

            db.update(TABLE_FOOD, values, COLUMN_ID + "=?", new String[]{String.valueOf(food.getId())});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void deleteFood(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_FOOD, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public boolean isFoodExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean exists = false;

        try (Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOOD + " WHERE " + COLUMN_NAME + "=?", new String[]{name})) {
            exists = cursor.getCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return exists;
    }
}