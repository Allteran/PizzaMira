package com.allteran.pizzamira.ui.order;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.model.User;
import com.allteran.pizzamira.services.FirebaseService;
import com.allteran.pizzamira.services.RealmService;
import com.allteran.pizzamira.util.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveryDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryDetailsFragment extends Fragment {
    private static final String NO_DATA_MESSAGE = "Внесите данные";

    private EditText mInputCity;
    private EditText mInputStreet;
    private EditText mInputBuilding;
    private EditText mInputEntrance;
    private EditText mInputIntercom;
    private EditText mInputFloor;
    private EditText mInputApp;

    private EditText mInputNumberOfPeople;
    private EditText mInputComment;

    private EditText mInputFirstName;
    private EditText mInputSecondName;
    private EditText mInputPhone;

    private EditText mInputChange;

    private RadioButton mCardRadio;
    private RadioButton mCashRadio;

    private AppCompatButton mConfirmButton;

    private ProgressBar mProgress;
    private ConstraintLayout mMainContainer;

    private static final String ARG_ORDER_ID = "arg_order_id";

    private String mOrderId;

    private FirebaseService mFirebaseService;
    private RealmService mRealmService;
    private Realm mRealm;

    public DeliveryDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param orderId Parameter 1.
     * @return A new instance of fragment AddAddressFragment.
     */
    public static DeliveryDetailsFragment newInstance(String orderId) {
        DeliveryDetailsFragment fragment = new DeliveryDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOrderId = getArguments().getString(ARG_ORDER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFirebaseService = new FirebaseService(FirebaseDatabase.getInstance());
        mRealm = Realm.getDefaultInstance();
        mRealmService = new RealmService();
        return inflater.inflate(R.layout.fragment_delivery_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInputCity = view.findViewById(R.id.input_city_name);
        mInputStreet = view.findViewById(R.id.input_street);
        mInputBuilding = view.findViewById(R.id.input_building);
        mInputEntrance = view.findViewById(R.id.input_entrance);
        mInputIntercom = view.findViewById(R.id.input_intercom);
        mInputFloor = view.findViewById(R.id.input_floor);
        mInputApp = view.findViewById(R.id.input_app);

        mInputNumberOfPeople = view.findViewById(R.id.input_number_of_people);
        mInputComment = view.findViewById(R.id.input_customer_comment);

        mInputFirstName = view.findViewById(R.id.input_first_name);
        mInputSecondName = view.findViewById(R.id.input_second_name);
        mInputPhone = view.findViewById(R.id.input_customer_phone);

        mInputChange = view.findViewById(R.id.input_change);

        mCashRadio = view.findViewById(R.id.radio_paytype_cash);
        mCardRadio = view.findViewById(R.id.radio_paytype_card);

        mConfirmButton = view.findViewById(R.id.button_confirm_delivery);

        mMainContainer = view.findViewById(R.id.main_container);

        mProgress = view.findViewById(R.id.progress_bar_delivery);

        final int[] fakeDataCounter = {0};
        TextView labelAddAddress = view.findViewById(R.id.label_add_address);

        labelAddAddress.setOnClickListener(l -> {
            fakeDataCounter[0] = fakeDataCounter[0] + 1;
            if (fakeDataCounter[0] == 3) {
                fillFieldsWithFakeData();
                Toast.makeText(getActivity(), "Secret code activated: fake data fill", Toast.LENGTH_SHORT).show();
            }
        });

        //now we are going to find if user is logged and if it - we are gonna push data from user to exact fields
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            mProgress.setVisibility(View.VISIBLE);
            mMainContainer.setVisibility(View.GONE);
            mFirebaseService.findUserById(fUser.getPhoneNumber(), new FirebaseService.UserDataStatus() {
                @Override
                public void dataIsLoaded(User user) {
                    fillFieldsFromUser(user);
                    mProgress.setVisibility(View.GONE);
                    mMainContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadError(@NonNull @NotNull DatabaseError error) {
                    mProgress.setVisibility(View.GONE);
                    mMainContainer.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mMainContainer.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
        }


        mInputStreet.requestFocus();

        mInputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //autogenerated
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //autogenerated
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+7")) {
                    mInputPhone.setText("+7");
                    Selection.setSelection(mInputPhone.getText(), mInputPhone.getText().length());
                }
                //validate entered phone number
                if (s.toString().length() != 12) {
                    mInputPhone.setError("+79XXXXXXXXX");
                }
            }
        });

        mCashRadio.setOnCheckedChangeListener((buttonView, isChecked) -> mInputChange.setEnabled(isChecked));

        mConfirmButton.setOnClickListener(v -> {
            if (validateFields()) {
                Order order = new Order();
                order.setCity(mInputCity.getText().toString().trim());
                order.setStreetName(mInputStreet.getText().toString().trim());
                order.setBuildingNo(mInputBuilding.getText().toString().trim());
                order.setEntrance(Integer.parseInt(mInputEntrance.getText().toString().trim()));
                order.setIntercom(mInputIntercom.getText().toString().trim());
                order.setFloorNo(Integer.parseInt(mInputFloor.getText().toString().trim()));
                order.setAppNo(Integer.parseInt(mInputApp.getText().toString().trim()));

                order.setUserPhone(mInputPhone.getText().toString().trim());
                order.setCustomerFirstName(mInputFirstName.getText().toString().trim());
                order.setCustomerSecondName(mInputSecondName.getText().toString().trim());

                order.setNumberOfPersons(Integer.parseInt(mInputNumberOfPeople.getText().toString().trim()));
                order.setUserComment(mInputComment.getText().toString().trim());

                if (mCashRadio.isChecked()) {
                    order.setPayType(Const.PAY_CASH);
                    order.setChange(Integer.parseInt(mInputChange.getText().toString().trim()));
                }
                if (mCardRadio.isChecked()) {
                    order.setChange(0);
                    order.setPayType(Const.PAY_CARD);
                }

                mRealmService.updateOrderDetails(mRealm, order);

                Fragment confirmOrderFragment = OrderConfirmFragment.newInstance();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.order_host_fragment, confirmOrderFragment);
                ft.commit();
            }
        });

    }

    private void fillFieldsWithFakeData() {
        mInputStreet.setText("пр-кт Ленина");
        mInputBuilding.setText("50А");
        mInputEntrance.setText("1");
        mInputIntercom.setText("Нет");
        mInputFloor.setText("1");
        mInputApp.setText("25");
        mInputFirstName.setText("Елизавета");
        mInputSecondName.setText("Белоцерковская");
        mInputPhone.setText("+79021335276");
    }

    /**
     * Current method will return true every time when all fields are filled
     * This means that all field validated
     */
    private boolean validateFields() {
        if (mInputStreet.getText().toString().length() <= 3) {
            mInputStreet.setError(NO_DATA_MESSAGE);
            mInputStreet.requestFocus();
            return false;
        }
        if (mInputBuilding.getText().toString().trim().equals("")) {
            mInputBuilding.setError(NO_DATA_MESSAGE);
            mInputBuilding.requestFocus();
            return false;
        }
        if (mInputEntrance.getText().toString().trim().equals("")) {
            mInputEntrance.setError(NO_DATA_MESSAGE);
            mInputEntrance.requestFocus();
            return false;
        }
        if (mInputIntercom.getText().toString().trim().equals("")) {
            mInputIntercom.setError(NO_DATA_MESSAGE);
            mInputIntercom.requestFocus();
            return false;
        }
        if (mInputApp.getText().toString().trim().equals("")) {
            mInputApp.setError(NO_DATA_MESSAGE);
            mInputApp.requestFocus();
            return false;
        }
        if (mInputFirstName.getText().toString().length() <= 2) {
            mInputFirstName.setError(NO_DATA_MESSAGE);
            mInputFirstName.requestFocus();
            return false;
        }
        if (mInputSecondName.getText().toString().length() <= 2) {
            mInputSecondName.setError(NO_DATA_MESSAGE);
            return false;
        }
        if (mInputPhone.getText().toString().length() != 12) {
            mInputPhone.setError("+79XXXXXXXXX");
            mInputPhone.requestFocus();
            return false;
        }
        if (mCashRadio.isChecked()) {
            if (mInputChange.getText().toString().equals("")) {
                mInputChange.setError(NO_DATA_MESSAGE);
                mInputChange.requestFocus();
                return false;
            }
        }
        if (mInputNumberOfPeople.getText().toString().equals("") || mInputNumberOfPeople.getText().toString().equals("")) {
            mInputNumberOfPeople.setError(NO_DATA_MESSAGE);
            mInputNumberOfPeople.requestFocus();
            return false;
        }
        return true;
    }

    private void fillFieldsFromUser(User user) {
        if (user.getStreet() != null) {
            mInputStreet.setText(user.getStreet());
        }
        if (user.getBuildNo() != null) {
            mInputBuilding.setText(user.getBuildNo());
        }
        if (user.getAppNo() != 0) {
            mInputApp.setText(user.getAppNo());
        }
        if (user.getFirstName() != null) {
            mInputFirstName.setText(user.getFirstName());
        }
        if (user.getSecondName() != null) {
            mInputSecondName.setText(user.getSecondName());
        }
        if (user.getPhone() != null) {
            mInputPhone.setText(user.getPhone());
        }
    }
}