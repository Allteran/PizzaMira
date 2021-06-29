package com.allteran.pizzamira.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.services.RealmService;
import com.allteran.pizzamira.util.Const;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_menu, R.id.navigation_cart,
                R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        RealmService realmService = new RealmService();
        Order order = realmService.getCurrentOrder(Realm.getDefaultInstance());
        if (order == null || order.getFoodList().isEmpty()) {
            BadgeDrawable badge = navView.getOrCreateBadge(R.id.navigation_cart);
            badge.setVisible(false, true);
        }

        String fragmentArg = getIntent().getStringExtra(Const.KEY_FROM_LOGIN);
        if (fragmentArg != null && fragmentArg.equals(Const.ARG_FROM_LOGIN_TO_CART)) {
            navController.navigate(R.id.navigation_cart);
        }


    }

}