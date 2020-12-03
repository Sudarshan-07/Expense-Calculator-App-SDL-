package com.example.beraccountmanager.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ExpensesContract {
    /**
     * The authority for the expenses provider
     */
    public static final String AUTHORITY = "com.example.beraccountmanager.provider";


    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static class Categories implements BaseColumns, CategoriesColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "categories");
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.com.beraccountmanager.provider.expense_category";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single category.
         */
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.com.beraccountmanager.provider.expense_category";

        /**
         * Sort by ascending order of _id column (the order as items were added).
         */
        public static final String DEFAULT_SORT_ORDER = _ID + " ASC";
    }

    public static class Expenses implements BaseColumns, ExpensesColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "expenses");
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd..com.beraccountmanager.provider.expense";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single expense.
         */
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd..com.beraccountmanager.provider.expense";

        /**
         * Sort by descending order of date (the most recent items are at the end).
         */
        public static final String DEFAULT_SORT_ORDER = DATE + " ASC";

        public static final String VALUES_SUM = "values_sum";
    }
    public static class ExpensesWithCategories implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "expensesWithCategories");

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.com.beraccountmanager.provider.expense_with_category";

        public static final Uri DATE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "date");

        public static final Uri DATE_RANGE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "dateRange");

        public static final Uri SUM_DATE_CONTENT_URI = Uri.withAppendedPath(DATE_CONTENT_URI, "sum");

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
