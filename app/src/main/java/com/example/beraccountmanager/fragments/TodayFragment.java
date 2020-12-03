package com.example.beraccountmanager.fragments;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beraccountmanager.R;
import com.example.beraccountmanager.activities.TransactionEditActivity;
import com.example.beraccountmanager.adapters.SimpleExpenseAdapter;
import com.example.beraccountmanager.utils.Utils;
import com.example.beraccountmanager.providers.ExpensesContract.ExpensesWithCategories;
import com.example.beraccountmanager.providers.ExpensesContract.Expenses;
import java.util.Date;

public class TodayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int sum_loader_id = 0;
    private static final int list_loader_id = 1;

    private ListView expenses_view;
    private View progressbar;
    private SimpleExpenseAdapter mAdapter;
    private TextView total_exp_sum_text_view;
    private TextView mTotalExpCurrencyTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        expenses_view = (ListView) rootView.findViewById(R.id.expenses_income_list_view);
        progressbar = rootView.findViewById(R.id.expenses_progress_bar);
        total_exp_sum_text_view = (TextView) rootView.findViewById(R.id.total_expense_sum_text_view);
        mTotalExpCurrencyTextView = (TextView) rootView.findViewById(R.id.total_expense_currency_text_view_1);
        expenses_view.setEmptyView(rootView.findViewById(R.id.expenses_income_empty_list_view));
        expenses_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prepareExpenseToEdit(id);
            }
        });
        rootView.findViewById(R.id.add_expense_button_if_empty_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareExpenseToCreate();
            }
        });
        total_exp_sum_text_view.setText(Utils.formatToCurrency(0.0f));

        registerForContextMenu(expenses_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.currency_preference, false);
        mAdapter = new SimpleExpenseAdapter(getActivity());
        expenses_view.setAdapter(mAdapter);
        getLoaderManager().initLoader(sum_loader_id, null, this);
        getLoaderManager().initLoader(list_loader_id, null, this);

    }
    @Override
    public void onResume() {
        super.onResume();
        reloadExpenseData();
        //reloadSharedPreferences();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_transaction, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_transaction:
                prepareExpenseToCreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.delete_transaction, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_transaction:
                deleteExpense(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = null;
        switch (id) {
            case sum_loader_id:
                uri = ExpensesWithCategories.SUM_DATE_CONTENT_URI;
                break;
            case list_loader_id:
                uri = ExpensesWithCategories.DATE_CONTENT_URI;
                break;
        }
        // Retrieve today's date string
        String today = Utils.getDateString(new Date());
        String[] selectionArgs = { today };
        return new CursorLoader(getActivity(),
                uri,
                null,
                null,
                selectionArgs,
                null
        );
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case sum_loader_id:
                int valueSumIndex = data.getColumnIndex(Expenses.VALUES_SUM);
                data.moveToFirst();
                float valueSum = data.getFloat(valueSumIndex);
                total_exp_sum_text_view.setText(Utils.formatToCurrency(valueSum));
                break;

            case list_loader_id:
                // Hide the progress bar
                progressbar.setVisibility(View.GONE);

                mAdapter.swapCursor(data);
                break;
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case sum_loader_id:
                total_exp_sum_text_view.setText(Utils.formatToCurrency(0.0f));
                break;
            case list_loader_id:
                mAdapter.swapCursor(null);
                break;
        }
    }

//    private void reloadSharedPreferences() {
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String prefCurrency = sharedPref.getString(SettingsFragment.KEY_PREF_CURRENCY, "");
//
//        mTotalExpCurrencyTextView.setText(prefCurrency);
//        mAdapter.setCurrency(prefCurrency);
//    }

    private void reloadExpenseData() {
        // Show the progress bar
        progressbar.setVisibility(View.VISIBLE);
        // Reload data by restarting the cursor loaders
        getLoaderManager().restartLoader(list_loader_id, null, this);
        getLoaderManager().restartLoader(sum_loader_id, null, this);
    }
    private int deleteSingleExpense(long expenseId) {
        Uri uri = ContentUris.withAppendedId(Expenses.CONTENT_URI, expenseId);

        // Defines a variable to contain the number of rows deleted
        int rowsDeleted;

        // Deletes the expense that matches the selection criteria
        rowsDeleted = getActivity().getContentResolver().delete(
                uri,        // the URI of the row to delete
                null,       // where clause
                null        // where args
        );

        showStatusMessage(getResources().getString(R.string.transaction_deleted));
        reloadExpenseData();

        return rowsDeleted;
    }
    private void deleteExpense(final long expenseId) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_trans)
                .setMessage(R.string.delete_transaction_dialog_msg)
                .setNeutralButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.delete_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSingleExpense(expenseId);
                    }
                })
                .show();
    }

    private void showStatusMessage(CharSequence text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    private void prepareExpenseToCreate() {
        startActivity(new Intent(getActivity(), TransactionEditActivity.class));
    }

    private void prepareExpenseToEdit(long id) {
        Intent intent = new Intent(getActivity(), TransactionEditActivity.class);
        intent.putExtra(AddTransactionFragment.EXTRA_EDIT_EXPENSE, id);
        startActivity(intent);
    }

}