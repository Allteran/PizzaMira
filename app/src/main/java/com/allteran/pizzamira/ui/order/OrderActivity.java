package com.allteran.pizzamira.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.allteran.pizzamira.R;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Fragment deliveryDetailsFragment = DeliveryDetailsFragment.newInstance("param1");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.order_host_fragment, deliveryDetailsFragment);
        ft.commit();
    }
}