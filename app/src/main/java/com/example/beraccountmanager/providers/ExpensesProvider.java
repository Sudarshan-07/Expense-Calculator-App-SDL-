package com.example.beraccountmanager.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.beraccountmanager.db.ExpenseDbHelper;
import com.example.beraccountmanager.providers.ExpensesContract.Categories;
import com.example.beraccountmanager.providers.ExpensesContract.Expenses;



import static com.example.beraccountmanager.db.ExpenseDbHelper.CATEGORIES_TABLE_NAME;
import static com.example.beraccountmanager.db.ExpenseDbHelper.EXPENSES_TABLE_NAME;

public class ExpensesProvider extends ContentProvider {
    public static final int EXPENSES = 10;
    public static final int EXPENSES_ID = 11;
    public static final int CATEGORIES = 12;
    public static final int CATEGORIES_ID = 13;
    public static final int EXPENSES_WITH_CATEGORIES = 14;
    public static final int EXPENSES_WITH_CATEGORIES_DATE = 15;
    public static final int EXPENSES_WITH_CATEGORIES_DATE_RANGE = 16;
    public static final int EXPENSES_WITH_CATEGORIES_SUM_DATE = 17;
    public static final int EXPENSES_WITH_CATEGORIES_SUM_DATE_RANGE = 18;
    private SQLiteOpenHelper nDbHelper;
    private SQLiteDatabase nDatabase;
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
        nDbHelper = new ExpenseDbHelper(getContext());
        return true;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        String table;
        String rawQuery;
        nDatabase = nDbHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {

            case CATEGORIES:
                table = CATEGORIES_TABLE_NAME;
                sortOrder = (sortOrder == null || sortOrder.isEmpty())
                        ? Categories.DEFAULT_SORT_ORDER
                        : sortOrder;
                break;


            case CATEGORIES_ID:
                table = CATEGORIES_TABLE_NAME;

                selection = Categories._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;


            case EXPENSES:
                table = EXPENSES_TABLE_NAME;
                sortOrder = (sortOrder == null || sortOrder.isEmpty())
                        ? Expenses.DEFAULT_SORT_ORDER
                        : sortOrder;
                break;


            case EXPENSES_ID:
                table = EXPENSES_TABLE_NAME;

                selection = Expenses._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;


            case EXPENSES_WITH_CATEGORIES:

                return nDatabase.rawQuery(BASE_SELECT_JOIN_EXPENSES_CATEGORIES_QUERY, null);



            case EXPENSES_WITH_CATEGORIES_DATE:

                rawQuery =
                        BASE_SELECT_JOIN_EXPENSES_CATEGORIES_QUERY + " WHERE " +
                                EXPENSES_TABLE_NAME + "." + Expenses.DATE + " = ?";

                return nDatabase.rawQuery(rawQuery, selectionArgs);


            case EXPENSES_WITH_CATEGORIES_SUM_DATE:

                rawQuery =
                        "SELECT SUM(" + EXPENSES_TABLE_NAME + "." + Expenses.VALUE + ") as " +
                                Expenses.VALUES_SUM + " FROM " + EXPENSES_TABLE_NAME +
                                " WHERE " + EXPENSES_TABLE_NAME + "." + Expenses.DATE + " = ?";

                return nDatabase.rawQuery(rawQuery, selectionArgs);


            case EXPENSES_WITH_CATEGORIES_DATE_RANGE:

                rawQuery =
                        BASE_SELECT_JOIN_EXPENSES_CATEGORIES_QUERY + " WHERE " +
                                EXPENSES_TABLE_NAME + "." + Expenses.DATE + " BETWEEN ? AND ?";

                return nDatabase.rawQuery(rawQuery, selectionArgs);


            case EXPENSES_WITH_CATEGORIES_SUM_DATE_RANGE:

                rawQuery =
                        "SELECT SUM(" + EXPENSES_TABLE_NAME + "." + Expenses.VALUE + ") as " +
                                Expenses.VALUES_SUM + " FROM " + EXPENSES_TABLE_NAME +
                                " WHERE " + EXPENSES_TABLE_NAME + "." + Expenses.DATE + " BETWEEN ? AND ?";

                return nDatabase.rawQuery(rawQuery, selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown Uri provided.");
        }
        cursor = nDatabase.query(
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

            case CATEGORIES:
                table = CATEGORIES_TABLE_NAME;
                contentUri = Categories.CONTENT_URI;
                break;

            case EXPENSES:
                table = EXPENSES_TABLE_NAME;
                contentUri = Expenses.CONTENT_URI;
                break;

            case CATEGORIES_ID:

            case EXPENSES_ID:
                throw new UnsupportedOperationException("Inserting rows with specified IDs is forbidden.");
            case EXPENSES_WITH_CATEGORIES:
            case EXPENSES_WITH_CATEGORIES_DATE:
            case EXPENSES_WITH_CATEGORIES_DATE_RANGE:
            case EXPENSES_WITH_CATEGORIES_SUM_DATE:
            case EXPENSES_WITH_CATEGORIES_SUM_DATE_RANGE:
                throw new UnsupportedOperationException("Modifying joined results is forbidden.");
            default:
                throw new IllegalArgumentException("Unknown Uri provided.");
        }

        nDatabase = nDbHelper.getWritableDatabase();

        long newRowID = nDatabase.insert(
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

            case CATEGORIES_ID:
                table = CATEGORIES_TABLE_NAME;

                selection = Categories._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;

            case EXPENSES:
                table = EXPENSES_TABLE_NAME;
                break;

            case EXPENSES_ID:
                table = EXPENSES_TABLE_NAME;

                selection = Expenses._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;

            case CATEGORIES:
                throw new UnsupportedOperationException("Removing multiple rows from the table is forbidden.");
            case EXPENSES_WITH_CATEGORIES:
            case EXPENSES_WITH_CATEGORIES_DATE:
            case EXPENSES_WITH_CATEGORIES_DATE_RANGE:
            case EXPENSES_WITH_CATEGORIES_SUM_DATE:
            case EXPENSES_WITH_CATEGORIES_SUM_DATE_RANGE:
                throw new UnsupportedOperationException("Modifying joined results is forbidden.");
            default:
                throw new IllegalArgumentException("Unknown Uri provided.");
        }

        nDatabase = nDbHelper.getWritableDatabase();

        return nDatabase.delete(
                table,
                selection,
                selectionArgs
        );
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table;
        switch (sUriMatcher.match(uri)) {

            case CATEGORIES_ID:
                table = CATEGORIES_TABLE_NAME;

                selection = Categories._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;

            case EXPENSES_ID:
                table = EXPENSES_TABLE_NAME;

                selection = Expenses._ID + " = ?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                break;

            case CATEGORIES:
            case EXPENSES:
                throw new UnsupportedOperationException("Updating multiple table rows is forbidden.");
            case EXPENSES_WITH_CATEGORIES:
            case EXPENSES_WITH_CATEGORIES_DATE:
            case EXPENSES_WITH_CATEGORIES_DATE_RANGE:
            case EXPENSES_WITH_CATEGORIES_SUM_DATE:
            case EXPENSES_WITH_CATEGORIES_SUM_DATE_RANGE:
                throw new UnsupportedOperationException("Modifying joined results is forbidden.");
            default:
                throw new IllegalArgumentException("Unknown Uri provided.");
        }

        nDatabase = nDbHelper.getWritableDatabase();

        return nDatabase.update(
                table,
                values,
                selection,
                selectionArgs
        );
    }
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CATEGORIES:
                return Categories.CONTENT_TYPE;
            case CATEGORIES_ID:
                return Categories.CONTENT_ITEM_TYPE;
            case EXPENSES:
                return Expenses.CONTENT_TYPE;
            case EXPENSES_ID:
                return Expenses.CONTENT_ITEM_TYPE;
            case EXPENSES_WITH_CATEGORIES:
            case EXPENSES_WITH_CATEGORIES_DATE:
            case EXPENSES_WITH_CATEGORIES_DATE_RANGE:
            case EXPENSES_WITH_CATEGORIES_SUM_DATE:
            case EXPENSES_WITH_CATEGORIES_SUM_DATE_RANGE:
                return ExpensesContract.ExpensesWithCategories.CONTENT_TYPE;
            default:
                return null;
        }
    }
}
