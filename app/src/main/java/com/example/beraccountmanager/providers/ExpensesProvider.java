package com.example.beraccountmanager.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.beraccountmanager.db.ExpenseDbHelper;
import com.example.beraccountmanager.providers.ExpensesContract.Categories;
import com.example.beraccountmanager.providers.ExpensesContract.Expenses;

import static com.example.beraccountmanager.db.ExpenseDbHelper.CATEGORIES_TABLE_NAME;
import static com.example.beraccountmanager.db.ExpenseDbHelper.EXPENSES_TABLE_NAME;

public class ExpensesProvider extends ContentProvider {
    public static final int EXPENSES = 10;
    public static final int EXPENSES_ID = 11;

    public static final int CATEGORIES = 20;
    public static final int CATEGORIES_ID = 21;

    public static final int EXPENSES_WITH_CATEGORIES = 30;
    public static final int EXPENSES_WITH_CATEGORIES_DATE = 31;
    public static final int EXPENSES_WITH_CATEGORIES_DATE_RANGE = 32;
    public static final int EXPENSES_WITH_CATEGORIES_SUM_DATE = 33;
    public static final int EXPENSES_WITH_CATEGORIES_SUM_DATE_RANGE = 34;

    private SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ExpensesContract.AUTHORITY, "expenses", EXPENSES);
        sUriMatcher.addURI(ExpensesContract.AUTHORITY, "expenses/#", EXPENSES_ID);
        sUriMatcher.addURI(ExpensesContract.AUTHORITY, "categories", CATEGORIES);
        sUriMatcher.addURI(ExpensesContract.AUTHORITY, "categories/#", CATEGORIES_ID);
        sUriMatcher.addURI(ExpensesContract.AUTHORITY, "expensesWithCategories",
                EXPENSES_WITH_CATEGORIES);
        sUriMatcher.addURI(ExpensesContract.AUTHORITY, "expensesWithCategories/date",
                EXPENSES_WITH_CATEGORIES_DATE);
        sUriMatcher.addURI(ExpensesContract.AUTHORITY, "expensesWithCategories/dateRange",
                EXPENSES_WITH_CATEGORIES_DATE_RANGE);
        sUriMatcher.addURI(ExpensesContract.AUTHORITY, "expensesWithCategories/date/sum",
                EXPENSES_WITH_CATEGORIES_SUM_DATE);
        sUriMatcher.addURI(ExpensesContract.AUTHORITY, "expensesWithCategories/dateRange/sum",
                EXPENSES_WITH_CATEGORIES_SUM_DATE_RANGE);
    }

    private static final String BASE_SELECT_JOIN_EXPENSES_CATEGORIES_QUERY =
            "SELECT " + EXPENSES_TABLE_NAME + "." + Expenses._ID + ", " +
                    EXPENSES_TABLE_NAME + "." + Expenses.VALUE + ", " +
                    CATEGORIES_TABLE_NAME + "." + Categories.NAME + ", " +
                    EXPENSES_TABLE_NAME + "." + Expenses.DATE + " FROM " +
                    EXPENSES_TABLE_NAME + " JOIN " + CATEGORIES_TABLE_NAME + " ON " +
                    EXPENSES_TABLE_NAME + "." + Expenses.CATEGORY_ID + " = " +
                    CATEGORIES_TABLE_NAME + "." + Categories._ID;

    @Override
    public boolean onCreate() {
        mDbHelper = new ExpenseDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        String table;
        String rawQuery;
        mDatabase = mDbHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            // The incoming URI is for all of categories
            case CATEGORIES:
                table = CATEGORIES_TABLE_NAME;
                sortOrder = (sortOrder == null || sortOrder.isEmpty())
                        ? Categories.DEFAULT_SORT_ORDER
                        : sortOrder;
                break;

            // The incoming URI is for a single row from categories
            case CATEGORIES_ID:
                table = CATEGORIES_TABLE_NAME;
                // Defines selection criteria for the row to query
                selection = Categories._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;

            // The incoming URI is for all of expenses
            case EXPENSES:
                table = EXPENSES_TABLE_NAME;
                sortOrder = (sortOrder == null || sortOrder.isEmpty())
                        ? Expenses.DEFAULT_SORT_ORDER
                        : sortOrder;
                break;

            // The incoming URI is for a single row from expenses
            case EXPENSES_ID:
                table = EXPENSES_TABLE_NAME;
                // Defines selection criteria for the row to query
                selection = Expenses._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;

            // The incoming URI is for all expenses with categories
            case EXPENSES_WITH_CATEGORIES:
                return mDatabase.rawQuery(BASE_SELECT_JOIN_EXPENSES_CATEGORIES_QUERY, null);

            // The incoming URI is for the expenses with categories for a specific date
            case EXPENSES_WITH_CATEGORIES_DATE:
                rawQuery =
                        BASE_SELECT_JOIN_EXPENSES_CATEGORIES_QUERY + " WHERE " +
                                EXPENSES_TABLE_NAME + "." + Expenses.DATE + " = ?";

                return mDatabase.rawQuery(rawQuery, selectionArgs);

            // The incoming URI is for the expense values sum for a specific date range
            case EXPENSES_WITH_CATEGORIES_SUM_DATE:
                rawQuery =
                        "SELECT SUM(" + EXPENSES_TABLE_NAME + "." + Expenses.VALUE + ") as " +
                                Expenses.VALUES_SUM + " FROM " + EXPENSES_TABLE_NAME +
                                " WHERE " + EXPENSES_TABLE_NAME + "." + Expenses.DATE + " = ?";

                return mDatabase.rawQuery(rawQuery, selectionArgs);

            // The incoming URI is for the expenses with categories for a specific date range
            case EXPENSES_WITH_CATEGORIES_DATE_RANGE:
                rawQuery =
                        BASE_SELECT_JOIN_EXPENSES_CATEGORIES_QUERY + " WHERE " +
                                EXPENSES_TABLE_NAME + "." + Expenses.DATE + " BETWEEN ? AND ?";

                return mDatabase.rawQuery(rawQuery, selectionArgs);

            // The incoming URI is for the expense values sum for a specific date range
            case EXPENSES_WITH_CATEGORIES_SUM_DATE_RANGE:
                rawQuery =
                        "SELECT SUM(" + EXPENSES_TABLE_NAME + "." + Expenses.VALUE + ") as " +
                                Expenses.VALUES_SUM + " FROM " + EXPENSES_TABLE_NAME +
                                " WHERE " + EXPENSES_TABLE_NAME + "." + Expenses.DATE + " BETWEEN ? AND ?";

                return mDatabase.rawQuery(rawQuery, selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown Uri provided.");
        }

        cursor = mDatabase.query(
                table,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table;
        Uri contentUri;
        switch (sUriMatcher.match(uri)) {
            // The incoming URI is for all of categories
            case CATEGORIES:
                table = CATEGORIES_TABLE_NAME;
                contentUri = Categories.CONTENT_URI;
                break;
            // The incoming URI is for all of expenses
            case EXPENSES:
                table = EXPENSES_TABLE_NAME;
                contentUri = Expenses.CONTENT_URI;
                break;
            // The incoming URI is for a single row from categories
            case CATEGORIES_ID:
                // The incoming URI is for a single row from expenses
            case EXPENSES_ID:
                throw new UnsupportedOperationException("Inserting rows with specified IDs is forbidden.");

            default:
                throw new IllegalArgumentException("Unknown Uri provided.");
        }

        mDatabase = mDbHelper.getWritableDatabase();

        long newRowID = mDatabase.insert(
                table,
                null,
                values
        );

        Uri newItemUri = ContentUris.withAppendedId(contentUri, newRowID);

        return (newRowID < 1) ? null : newItemUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table;
        switch (sUriMatcher.match(uri)) {
            // The incoming URI is for a single row from categories
            case CATEGORIES_ID:
                table = CATEGORIES_TABLE_NAME;
                // Defines selection criteria for the row to delete
                selection = Categories._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;
            // The incoming URI is for all of expenses
            case EXPENSES:
                table = EXPENSES_TABLE_NAME;
                break;
            // The incoming URI is for a single row from expenses
            case EXPENSES_ID:
                table = EXPENSES_TABLE_NAME;
                // Defines selection criteria for the row to delete
                selection = Expenses._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;
            // The incoming URI is for all of categories
            case CATEGORIES:
                throw new UnsupportedOperationException("Removing multiple rows from the table is forbidden.");

            default:
                throw new IllegalArgumentException("Unknown Uri provided.");
        }

        mDatabase = mDbHelper.getWritableDatabase();

        return mDatabase.delete(
                table,
                selection,
                selectionArgs
        );
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table;
        switch (sUriMatcher.match(uri)) {
            // The incoming URI is for a single row from categories
            case CATEGORIES_ID:
                table = CATEGORIES_TABLE_NAME;
                // Defines selection criteria for the row to delete
                selection = Categories._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;
            // The incoming URI is for a single row from expenses
            case EXPENSES_ID:
                table = EXPENSES_TABLE_NAME;
                // Defines selection criteria for the row to delete
                selection = Expenses._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;
            // The incoming URI is for all of categories
            case CATEGORIES:
                // The incoming URI is for all of expenses
            case EXPENSES:
                throw new UnsupportedOperationException("Updating multiple table rows is forbidden.");
       case EXPENSES_WITH_CATEGORIES:

            default:
                throw new IllegalArgumentException("Unknown Uri provided.");
        }

        mDatabase = mDbHelper.getWritableDatabase();

        return mDatabase.update(
                table,
                values,
                selection,
                selectionArgs
        );
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
