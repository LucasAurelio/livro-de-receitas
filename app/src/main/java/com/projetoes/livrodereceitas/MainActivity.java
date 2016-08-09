package com.projetoes.livrodereceitas;

import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.projetoes.livrodereceitas.fragments.InitialFragment;
import com.projetoes.livrodereceitas.fragments.SearchFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;
    private InitialFragment initialFragment;
    private SearchFragment searchFragment;
    public static final String TAG = "MAIN_ACTIVITY";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Bottom bar navigation menu
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.noTopOffset();
        mBottomBar.noNavBarGoodness();
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setDefaultTabPosition(0);
        initializeBottomNavigation();


        initialFragment = InitialFragment.getInstance();
        searchFragment = SearchFragment.getInstance();


        changeFragment(initialFragment, InitialFragment.TAG ,true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }


    public void onSearchButtonPressed(View view) {
        //changeFragment(searchFragment, InitialFragment.TAG ,true);


        changeBottomBarItem(SearchFragment.TAG);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,
                searchFragment, SearchFragment.TAG).addToBackStack(SearchFragment.TAG).commit();

        Toast.makeText(getBaseContext(), "I choose you!", Toast.LENGTH_SHORT).show();
    }


    /**
     * Navigate between fragments when clicking the bottom bar navigation
     */
    private void initializeBottomNavigation() {

        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            // The user selected any item.
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case (R.id.homeItem) :
                        //changeFragment(initialFragment,InitialFragment.TAG ,true);
                    case (R.id.searchItem):
                        //changeFragment(searchFragment, SearchFragment.TAG,true);
                        break;
                    case (R.id.favoriteItem):
                        Toast.makeText(getBaseContext(), "My precious", Toast.LENGTH_SHORT).show();
                        break;
                    case (R.id.helpItem):
                        Toast.makeText(getBaseContext(), "Help! I need somebody...", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }

            // The user reselected any item, maybe scroll your content to top.
            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case (R.id.homeItem):
                        //changeFragment(initialFragment, InitialFragment.TAG,false);
                        break;
                    case (R.id.searchItem):
                        //changeFragment(searchFragment, SearchFragment.TAG,false);
                        break;
                    case (R.id.favoriteItem):
                        Toast.makeText(getBaseContext(), "My precious", Toast.LENGTH_SHORT).show();
                        break;
                    case (R.id.helpItem):
                        Toast.makeText(getBaseContext(), "Help! I need somebody...", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

    }


    private void changeBottomBarItem(String currentFragment) {
        switch (currentFragment) {
            case InitialFragment.TAG:
                mBottomBar.selectTabAtPosition(0, true);
                break;
            case SearchFragment.TAG:
                mBottomBar.selectTabAtPosition(1, true);
                break;
        }
    }



    // TA DANDO ERRO java.lang.RuntimeException: Unable to start activity ComponentInfo
    /**
     * Change the current displayed fragment by a new one.
     * - if the fragment is in backstack, it will pop it
     * - if the fragment is already displayed (trying to change the fragment with the same), it will not do anything
     *
     * @param frag            the new fragment to display
     * @param saveInBackstack if we want the fragment to be in backstack
     */

    private void changeFragment(Fragment frag, String tag, boolean saveInBackstack) {


        try {
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate(tag, 0);

            if (!fragmentPopped && manager.findFragmentByTag(tag) == null) {
                //fragment not in back stack, create it.
                FragmentTransaction transaction = manager.beginTransaction();


                transaction.replace(R.id.content_layout, frag, tag);

                if (saveInBackstack) {
                    Log.d(TAG, "Change Fragment: addToBackTack " + tag);
                    transaction.addToBackStack(tag);
                } else {
                    Log.d(TAG, "Change Fragment: NO addToBackTack");
                }
                changeBottomBarItem(tag);
                transaction.commit();
            } else {
                // custom effect if fragment is already instanciated
            }
        } catch (IllegalStateException exception) {
            Log.w(TAG, "Unable to commit fragment, could be activity as been killed in background. " + exception.toString());
        }
    }


    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
            return;
        }

        super.onBackPressed();
    }


}