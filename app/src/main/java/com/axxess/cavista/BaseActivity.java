package com.axxess.cavista;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Activity class for the Main screen
 */
public class BaseActivity extends AppCompatActivity {

    public static final String SEARCH_QUERY = "Search Query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    hideKeyboard();

                    toggleTextviewVisibility(View.GONE);

                    launchSearchResultsFragment(query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);


        return true;
    }

    /**
     * Toggle the visibility of message textview
     * @param visibility
     */
    private void toggleTextviewVisibility(int visibility) {
        TextView tvMsg = findViewById(R.id.tv_start_msg);
        if(tvMsg != null) {
            tvMsg.setVisibility(visibility);
        }
    }

    /**
     * Hides the soft keyboard
     */
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Launch Search Results Fragment
     * @param query Search query
     */
    private void launchSearchResultsFragment(String query) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SearchResultsFragment fragment = SearchResultsFragment.newInstance(query);
        fragmentTransaction.add(R.id.fl_parent, fragment);
        fragmentTransaction.commit();
    }
}