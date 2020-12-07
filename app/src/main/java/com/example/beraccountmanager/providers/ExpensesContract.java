package com.example.beraccountmanager.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ExpensesContract {
    /*The author of provider */
    public static final String AUTHORITY = "com.example.beraccountmanager.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    private ExpensesContract(){}

    public static class Categories implements BaseColumns, CategoriesColumns {
        /*This class cannot be instantiated*/
        private Categories() {}
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "categories");
        /* Sort by ascending order of _id column (the order as items were added).*/
        public static final String DEFAULT_SORT_ORDER = _ID + " ASC";
    }

    public static class Expenses implements BaseColumns, ExpensesColumns {
        /*This utility class cannot be instantiated*/
        private Expenses() {}
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "expenses");
        /*Sort by descending order of date (the most recent items are at the end).*/
        public static final String DEFAULT_SORT_ORDER = DATE + " ASC";
        /*Expense sum value column name to return for joined tables*/
        public static final String VALUES_SUM = "values_sum";
    }

    public static class ExpensesWithCategories implements BaseColumns {
        /*This utility class cannot be instantiated.*/
        private ExpensesWithCategories() {}
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "expensesWithCategories");
        /* The content:// style URI for this joined table to filter items by a specific date.*/
        public static final Uri DATE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "date");
        /*The content:// style URI for this joined table to filter items by a specific date range. */
        public static final Uri DATE_RANGE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "dateRange");

        /*The content:// style URI for getting sum of expense values for this joined table by "date" filter.*/
        public static final Uri SUM_DATE_CONTENT_URI = Uri.withAppendedPath(DATE_CONTENT_URI, "sum");
        /*The content:// style URI for getting sum of expense values for this joined table by "date range" filter.*/
        public static final Uri SUM_DATE_RANGE_CONTENT_URI =
                Uri.withAppendedPath(DATE_RANGE_CONTENT_URI, "sum");
    }

    protected interface CategoriesColumns {
        String NAME = "name";
    }
    protected interface ExpensesColumns {
        String VALUE = "value";
        String DATE = "date";
        String CATEGORY_ID = "category_id";
    }
}
