package com.example.beraccountmanager.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.beraccountmanager.R;
import com.example.beraccountmanager.providers.ExpensesContract.Categories;
import com.example.beraccountmanager.providers.ExpensesContract.Expenses;
import com.example.beraccountmanager.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String EXTRA_EDIT_EXPENSE = "com.example.beraccountmanager.edit_expense";
    private static final int EXPENSE_LOADER_ID = 1;
    private static final int CATEGORIES_LOADER_ID = 0;
    private EditText expense_add_edit_value, transaction_date;
    private AppCompatSpinner choose_category_spinner;
    private ProgressBar select_cat_progress_bar;
    //private RadioGroup radio_group;
    //private RadioButton rb1, rb2;
    private SimpleCursorAdapter mAdapter;
    private long mExtraValue;
    private long mExpenseCategoryId = -1;
    final DialogFragment dialogFragment = new DatePickerDialogTheme4();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.add_transaction_fragment, container, false);
        transaction_date = (EditText) rootView.findViewById(R.id.transaction_date);
        expense_add_edit_value = (EditText) rootView.findViewById(R.id.expense_add_edit_value);
        choose_category_spinner = (AppCompatSpinner) rootView.findViewById(R.id.choose_category_spinner);
        select_cat_progress_bar = rootView.findViewById(R.id.select_cat_progress_bar);
        //radio_group = rootView.findViewById(R.id.radio_group);
        //rb1 = rootView.findViewById(R.id.rb1);
        //rb2 = rootView.findViewById(R.id.rb2);
        setEditTextDefaultValue();
        transaction_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getFragmentManager(), "theme 4");
            }
        });
//        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (rb1.isChecked()) { }
//                if (rb2.isChecked()) { }
//            }
//        });
        expense_add_edit_value.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    checkValueFieldForIncorrectInput();
                    return true;
                }
                return false;
            }
        });
        choose_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mExpenseCategoryId = id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        return rootView;
    }

    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        String date;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //these three lines are used to for cancel set previous dates
            calendar.add(Calendar.DATE, 0);
            Date newDate = calendar.getTime();
            /*datePickerDialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime() % (24 * 60 * 60 * 1000)));
            Use above method to disable past dates*/
            //here it ends
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int month2 = month + 1;
            String formattedMonth = "" + month2;
            String formattedDayOfMonth = "" + day;
            if (month2 < 10) {
                formattedMonth = "0" + month2;
            }
            if (day < 10) {
                formattedDayOfMonth = "0" + day;
            }
            EditText editText = getActivity().findViewById(R.id.transaction_date);
            editText.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + year);
            date = editText.getText().toString().trim();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                null,
                new String[]{Categories.NAME},
                new int[]{android.R.id.text1},
                0);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choose_category_spinner.setAdapter(mAdapter);
        mExtraValue = getActivity().getIntent().getLongExtra(EXTRA_EDIT_EXPENSE, -1);
        if (mExtraValue < 1) {
            getActivity().setTitle(R.string.add_transaction);
            loadCategories();
        } else {
            getActivity().setTitle(R.string.edit_trans);
            loadExpenseData();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.done_adding_transaction, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_add_transaction:
                if (checkForIncorrectInput()) {
                    // Create a new expense
                    if (mExtraValue < 1) {
                        insertNewExpense();
                        // Edit existing expense
                    } else {
                        updateExpense(mExtraValue);
                    }
                    getActivity().finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkForIncorrectInput() {
        if (!checkValueFieldForIncorrectInput()) {
            expense_add_edit_value.selectAll();
            return false;
        }
        // Future check of other fields
        return true;
    }

    private boolean checkValueFieldForIncorrectInput() {
        String etValue = expense_add_edit_value.getText().toString();
        try {
            if (etValue.length() == 0) {
                expense_add_edit_value.setError(getResources().getString(R.string.error_empty_field));
                return false;
            } else if (Float.parseFloat(etValue) == 0.00f) {
                expense_add_edit_value.setError(getResources().getString(R.string.error_zero_value));
                return false;
            }
        } catch (Exception e) {
            expense_add_edit_value.setError(getResources().getString(R.string.error_incorrect_input));
            return false;
        }
        return true;
    }

    private void loadCategories() {
        // Show the progress bar next to category spinner
        select_cat_progress_bar.setVisibility(View.VISIBLE);

        getLoaderManager().initLoader(CATEGORIES_LOADER_ID, null, this);
    }

    private void setEditTextDefaultValue() {
        expense_add_edit_value.setText(String.valueOf(0));
        expense_add_edit_value.selectAll();
    }

    private void loadExpenseData() {
        getLoaderManager().initLoader(EXPENSE_LOADER_ID, null, this);
        loadCategories();
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        String[] projectionFields = null;
        Uri uri = null;
        switch (id) {
            case EXPENSE_LOADER_ID:
                projectionFields = new String[]{
                        Expenses._ID,
                        Expenses.VALUE,
                        Expenses.CATEGORY_ID
                };

                uri = ContentUris.withAppendedId(Expenses.CONTENT_URI, mExtraValue);
                break;
            case CATEGORIES_LOADER_ID:
                projectionFields = new String[]{
                        Categories._ID,
                        Categories.NAME
                };

                uri = Categories.CONTENT_URI;
                break;
        }

        return new CursorLoader(getActivity(),
                uri,
                projectionFields,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case EXPENSE_LOADER_ID:
                int expenseValueIndex = data.getColumnIndex(Expenses.VALUE);
                int expenseCategoryIdIndex = data.getColumnIndex(Expenses.CATEGORY_ID);

                data.moveToFirst();
                mExpenseCategoryId = data.getLong(expenseCategoryIdIndex);
                updateSpinnerSelection();

                expense_add_edit_value.setText(String.valueOf(data.getFloat(expenseValueIndex)));
                expense_add_edit_value.selectAll();
                break;
            case CATEGORIES_LOADER_ID:
                // Hide the progress bar next to category spinner
                select_cat_progress_bar.setVisibility(View.GONE);

                if (null == data || data.getCount() < 1) {
                    mExpenseCategoryId = -1;
                    // Fill the spinner with default values
                    ArrayList<String> defaultItems = new ArrayList<>();
                    defaultItems.add(getResources().getString(R.string.no_categories_string));

                    ArrayAdapter<String> tempAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item,
                            defaultItems);
                    choose_category_spinner.setAdapter(tempAdapter);
                    // Disable the spinner
                    choose_category_spinner.setEnabled(false);
                } else {
                    // Set the original adapter
                    choose_category_spinner.setAdapter(mAdapter);
                    // Update spinner data
                    mAdapter.swapCursor(data);
                    // Enable the spinner
                    choose_category_spinner.setEnabled(true);
                    updateSpinnerSelection();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case EXPENSE_LOADER_ID:
                mExpenseCategoryId = -1;
                setEditTextDefaultValue();
                break;
            case CATEGORIES_LOADER_ID:
                mAdapter.swapCursor(null);
                break;
        }
    }

    private void updateSpinnerSelection() {
        choose_category_spinner.setSelection(0);
        for (int pos = 0; pos < mAdapter.getCount(); ++pos) {
            if (mAdapter.getItemId(pos) == mExpenseCategoryId) {
                // Set spinner item selected according to the value from db
                choose_category_spinner.setSelection(pos);
                break;
            }
        }
    }

    private void insertNewExpense() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date t_date = new Date();
        try {
            String t_date2 = (transaction_date.getText().toString());
            t_date = format.parse(t_date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ContentValues insertValues = new ContentValues();
        insertValues.put(Expenses.VALUE, Float.parseFloat(expense_add_edit_value.getText().toString()));
        insertValues.put(Expenses.DATE, Utils.getDateString(t_date)); // Put current date (today)
        insertValues.put(Expenses.CATEGORY_ID, mExpenseCategoryId);

        getActivity().getContentResolver().insert(
                Expenses.CONTENT_URI,
                insertValues
        );

        Toast.makeText(getActivity(),
                getResources().getString(R.string.done),
                Toast.LENGTH_SHORT).show();
    }

    private void updateExpense(long id) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(Expenses.VALUE, Float.parseFloat(expense_add_edit_value.getText().toString()));
        updateValues.put(Expenses.CATEGORY_ID, mExpenseCategoryId);

        Uri expenseUri = ContentUris.withAppendedId(Expenses.CONTENT_URI, id);

        getActivity().getContentResolver().update(
                expenseUri,
                updateValues,
                null,
                null
        );

        Toast.makeText(getActivity(),
                getResources().getString(R.string.transaction_updated),
                Toast.LENGTH_SHORT).show();
    }


}