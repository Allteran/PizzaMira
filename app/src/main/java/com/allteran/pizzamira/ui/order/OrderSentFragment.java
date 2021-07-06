package com.allteran.pizzamira.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.ui.MainActivity;
import com.allteran.pizzamira.util.Const;

import org.jetbrains.annotations.NotNull;

public class OrderSentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public OrderSentFragment() {
        // Required empty public constructor
    }

    public static OrderSentFragment newInstance() {
        return new OrderSentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_sent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatButton gotoOrderStatusButton = view.findViewById(R.id.button_goto_order_status);
        gotoOrderStatusButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(Const.ARG_FROM_SENT_ORDER_TO_MENU, true);
            startActivity(intent);
        });
    }
}