package com.example.beraccountmanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.beraccountmanager.R;
import com.example.beraccountmanager.providers.ExpensesContract.Categories;
import com.example.beraccountmanager.providers.ExpensesContract.Expenses;
public class ExpenseDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "beraccountmanager.db";
    public static final String CATEGORIES_TABLE_NAME = "categories";
    public static final String EXPENSES_TABLE_NAME = "expenses";
    private Context nContext;
    public ExpenseDbHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        nContext = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CategoriesTable.CREATE_TABLE_QUERY);
        CategoriesTable.fillTable(db, nContext);
        db.execSQL(ExpensesTable.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*Temporary upgrade policy*/
        db.execSQL(ExpensesTable.DELETE_TABLE_QUERY);
        db.execSQL(CategoriesTable.DELETE_TABLE_QUERY);
        onCreate(db);
    }
    private static final class CategoriesTable {
        public static final String CREATE_TABLE_QUERY =
                "CREATE TABLE " + CATEGORIES_TABLE_NAME + " (" +
                        Categories._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Categories.NAME + " TEXT NOT NULL);";

        public static final String DELETE_TABLE_QUERY =
                "DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME + ";";

        public static void fillTable(SQLiteDatabase db, Context ctx) {
            String[] predefinedNames = ctx.getResources().getStringArray(R.array.predefined_categories);
            ContentValues values = new ContentValues();
            for (String name : predefinedNames) {
                values.put(Categories.NAME, name);
                db.insert(CATEGORIES_TABLE_NAME, null, values);
            }
        }
    }

    private static final class ExpensesTable {
        public static final String CREATE_TABLE_QUERY =
                "CREATE TABLE " + EXPENSES_TABLE_NAME + " (" +
                        Expenses._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Expenses.VALUE + " FLOAT NOT NULL, " +
                        Expenses.DATE + " DATE NOT NULL, " +
                        Expenses.CATEGORY_ID + " INTEGER NOT NULL);";

        public static final String DELETE_TABLE_QUERY =
                "DROP TABLE IF EXISTS " + EXPENSES_TABLE_NAME + ";";
    }
}
