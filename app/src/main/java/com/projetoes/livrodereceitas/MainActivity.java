package com.projetoes.livrodereceitas;

import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            // The user selected any item.
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId){
                    case (R.id.searchItem):
                        break;
                    case (R.id.favoriteItem):
                        Toast.makeText(getBaseContext(),"My precious", Toast.LENGTH_SHORT).show();
                        break;
                    case (R.id.helpItem):
                        Toast.makeText(getBaseContext(),"Help! I need somebody...", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }

            // The user reselected any item, maybe scroll your content to top.
            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId){
                    case (R.id.searchItem):
                        break;
                    case (R.id.favoriteItem):
                        Toast.makeText(getBaseContext(),"My precious", Toast.LENGTH_SHORT).show();
                        break;
                    case (R.id.helpItem):
                        Toast.makeText(getBaseContext(),"Help! I need somebody...", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

    }
}
