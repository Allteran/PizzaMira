package com.allteran.pizzamira.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.adapters.OrderConfirmAdapter;
import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.services.FirebaseService;
import com.allteran.pizzamira.services.RealmService;
import com.allteran.pizzamira.util.Const;
import com.allteran.pizzamira.util.StringUtils;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.realm.Realm;

public class OrderConfirmFragment extends Fragment {
    private TextView mCity;
    private TextView mStreet;
    private TextView mBuilding;
    private TextView mEntrance;
    private TextView mIntercom;
    private TextView mFloor;
    private TextView mApp;
    private TextView mCustomerName;
    private TextView mPhone;
    private TextView mPayment;
    private TextView mNumberOfPeople;
    private TextView mCustomerComment;

    private RecyclerView mRecycler;
    private AppCompatButton mConfirmOrderButton;

    private RealmService mRealmService;
    private FirebaseService mFirebaseService;

    public OrderConfirmFragment() {
        // Required empty public constructor
    }


    public static OrderConfirmFragment newInstance(String param1, String param2) {
        OrderConfirmFragment fragment = new OrderConfirmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //TODO: pull bundle from getArguments()
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRealmService = new RealmService();

        mFirebaseService = new FirebaseService(FirebaseDatabase.getInstance());
        return inflater.inflate(R.layout.fragment_order_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCity = view.findViewById(R.id.city_name);
        mStreet = view.findViewById(R.id.street_name);
        mBuilding = view.findViewById(R.id.building);
        mEntrance = view.findViewById(R.id.entrance);
        mIntercom = view.findViewById(R.id.intercom);
        mFloor = view.findViewById(R.id.floor);
        mApp = view.findViewById(R.id.app_number);
        mCustomerName = view.findViewById(R.id.customer_name);
        mPhone = view.findViewById(R.id.customer_phone);
        mPayment = view.findViewById(R.id.payment);
        mNumberOfPeople = view.findViewById(R.id.number_of_people);
        mCustomerComment = view.findViewById(R.id.customer_comment);

        mRecycler = view.findViewById(R.id.recycler_confirm_order);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);

        Realm realm = Realm.getDefaultInstance();
        Order order = mRealmService.getCurrentOrder(realm);
        //TODO: need to make an error message when for some reason order gonna be null
        if (order != null) {
            mCity.setText(order.getCity());
            mStreet.setText(order.getStreetName());
            mBuilding.setText(order.getBuildingNo());
            mEntrance.setText(String.valueOf(order.getEntrance()));
            mIntercom.setText(order.getIntercom());
            mFloor.setText(String.valueOf(order.getFloorNo()));
            mApp.setText(String.valueOf(order.getAppNo()));
            mCustomerName.setText(String.format("%s %s", order.getCustomerFirstName(), order.getCustomerSecondName()));
            mPhone.setText(order.getUserPhone());

            String paymentInfo = order.getPayType();
            if (order.getPayType().equals(Const.PAY_CASH)) {
                paymentInfo = paymentInfo + ", необходима сдача с " + StringUtils.priceFormatter(order.getChange());
            }

            mPayment.setText(paymentInfo);
            mNumberOfPeople.setText(String.valueOf(order.getNumberOfPersons()));
            mCustomerComment.setText(order.getUserComment());

            List<FoodItem> foodList = order.getFoodList();
            OrderConfirmAdapter adapter = new OrderConfirmAdapter(foodList, mRecycler);
            mRecycler.setAdapter(adapter);

        }
    }
}