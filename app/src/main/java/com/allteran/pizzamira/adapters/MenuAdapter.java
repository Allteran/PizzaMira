package com.allteran.pizzamira.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.services.FirebaseService;
import com.allteran.pizzamira.services.RealmService;
import com.allteran.pizzamira.ui.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "MENU_ADAPTER";
    private FragmentManager mFragmentManager;
    private RecyclerView mRecycler;
    private Point mScreenSize;
    private Context mContext;

    private int mPosition;

    private RealmService mDatabase;
    private Realm mRealm;

    private List<FoodItem> mFoodList;

    public MenuAdapter() {
    }

    public MenuAdapter(Context context, FragmentManager fragmentManager, RecyclerView recyclerView,
                       List<FoodItem> foodList, Point screenSize) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.mRecycler = recyclerView;
        this.mFoodList = foodList;
        this.mScreenSize = screenSize;
        this.mDatabase = new RealmService();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_menu_food_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = mFoodList.get(position);
        holder.name.setText(foodItem.getName());
        holder.foodWeight.setText(String.valueOf(foodItem.getWeight()));
        holder.price.setText("+" + String.valueOf(foodItem.getPrice()) + " р."); //TODO: in future replace "p." with string from res placeholder
        //TODO: REPLACE PIC DOWNLOAD WITH PICASSO
        holder.foodImg.setImageResource(R.drawable.ic_launcher_foreground);
        int imageHeight = mScreenSize.y / 5;
        int imageWidth = mScreenSize.x / 2;

        holder.foodImg.requestLayout();
        holder.foodImg.getLayoutParams().height = imageHeight;
        holder.foodImg.getLayoutParams().width = imageWidth;

        holder.textContainer.setOnClickListener(this);
        holder.foodImg.setOnClickListener(this);
        holder.price.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    public void refreshList(List<FoodItem> foodItems) {
        this.mFoodList = foodItems;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        //TODO: find way to get mRecycler child position
        if (v.getId() == R.id.food_item_thumb) {
            //Next part of code is totally cursed, but it works.
            //To get right child of RecyclerView we must go into full deep of levels to our clicked view
            View rootView = (View) v
                    .getParent() // ConstraintLayout
                    .getParent() // LinearLayout
                    .getParent(); // CardView
            int position = mRecycler.getChildLayoutPosition(rootView);
            Log.d(TAG, "mPosition = " + position);
            Log.d(TAG, "item.getName = " + mFoodList.get(position).getName());
            //TODO: open foodItem details
            return;
        }
        if (v.getId() == R.id.food_item_name) {
            //same sht
            View rootView = (View) v
                    .getParent() // ConstraintLayout
                    .getParent() // Linear
                    .getParent() // Linear
                    .getParent(); // CardView
            int position = mRecycler.getChildLayoutPosition(rootView);
            Log.d(TAG, "position = " + position);
            Log.d(TAG, "item.getName = " + mFoodList.get(position).getName());
            //TODO: open foodItem details
        }
        if (v.getId() == R.id.food_item_price) {
            mRealm = Realm.getDefaultInstance();
            //Deep dark fantasy (c)
            View rootView = (View) v
                    .getParent() // ConstraintLayout
                    .getParent() // RelativeLayout
                    .getParent() // Linear
                    .getParent() // Linear
                    .getParent(); //cardView
            int position = mRecycler.getChildLayoutPosition(rootView);

            FoodItem item = mFoodList.get(position);
            Order order = mDatabase.getCurrentOrder(mRealm);
            if (order == null) {
                mDatabase.createOrder(mRealm, item);
            }
            mDatabase.addItemToOrder(mRealm, item);
            Snackbar.make(rootView, "Блюдо " + item.getName() + " добавлено в корзину", Snackbar.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView foodImg;
        private TextView name;
        private TextView foodWeight;
        private TextView price;
        private LinearLayout textContainer;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.food_item_thumb);
            name = itemView.findViewById(R.id.food_item_name);
            foodWeight = itemView.findViewById(R.id.food_item_weight);
            price = itemView.findViewById(R.id.food_item_price);
            textContainer = itemView.findViewById(R.id.food_item_text_container);
        }


    }
}
