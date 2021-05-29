package com.allteran.pizzamira.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.services.RealmService;

import java.util.List;

import io.realm.Realm;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "CART_ADAPTER";
    private List<FoodItem> mFoodList;
    private FragmentManager mFragmentManager;
    private RecyclerView mRecycler;

    private RealmService mDatabase;
    private Realm mRealm;

    public CartAdapter() {
    }

    public CartAdapter(List<FoodItem> foodList, FragmentManager fragmentManager, RecyclerView recyclerView) {
        this.mFoodList = foodList;
        this.mFragmentManager = fragmentManager;
        this.mRecycler = recyclerView;
        mDatabase = new RealmService();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart_food_item, parent,
                false);
        mRealm = Realm.getDefaultInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = mFoodList.get(position);
        holder.name.setText(foodItem.getName());
        holder.weight.setText(String.valueOf(foodItem.getWeight() * foodItem.getCountInCart()) + "г.");
        holder.price.setText(String.valueOf(foodItem.getPrice() * foodItem.getCountInCart()) + " р.");
        holder.itemsCounter.setText(String.valueOf(foodItem.getCountInCart()));
        //TODO: REPLACE PIC DOWNLOAD WITH PICASSO/OTHER LIB

        holder.thumb.setImageResource(R.drawable.ic_launcher_foreground);

        holder.addItem.setOnClickListener(this);
        holder.minusItem.setOnClickListener(this);
        holder.deleteItem.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_add_item) {
            Log.d(TAG, "button - plus item");
            //Next part of code is totally cursed, but it works.
            //To get right child of RecyclerView we must go into full deep of levels to our clicked view
            View rootView = (View) v
                    .getParent()
                    .getParent()
                    .getParent()
                    .getParent()
                    .getParent()
                    .getParent();
            int position = mRecycler.getChildLayoutPosition(rootView);
            int counter = mFoodList.get(position).getCountInCart() + 1;
            mDatabase.changeItemCount(mRealm, mFoodList.get(position), counter);
            notifyDataSetChanged();
            return;
        }
        if (v.getId() == R.id.button_minus_item) {
            Log.d(TAG, "button - minus item");
            //Same shit here
            View rootView = (View) v
                    .getParent()
                    .getParent()
                    .getParent()
                    .getParent()
                    .getParent()
                    .getParent();
            int position = mRecycler.getChildLayoutPosition(rootView);
            int counter = mFoodList.get(position).getCountInCart() - 1;
            if (counter <= 0) {
                mDatabase.deleteItemFromOrder(mRealm, mFoodList.get(position));
            } else {
                mDatabase.changeItemCount(mRealm, mFoodList.get(position), counter);
            }
            notifyDataSetChanged();
            return;
        }
        if (v.getId() == R.id.button_delete_item_from_order) {
            View rootView = (View) v
                    .getParent()
                    .getParent()
                    .getParent()
                    .getParent();
            int position = mRecycler.getChildLayoutPosition(rootView);
            mDatabase.deleteItemFromOrder(mRealm, mFoodList.get(position));
            notifyDataSetChanged();
            return;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;
        private ImageView deleteItem;
        private TextView name;
        private TextView weight;
        private TextView price;
        private TextView itemsCounter;
        private Button addItem;
        private Button minusItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.food_item_thumb);
            deleteItem = itemView.findViewById(R.id.button_delete_item_from_order);
            name = itemView.findViewById(R.id.food_item_name);
            weight = itemView.findViewById(R.id.food_item_weight);
            price = itemView.findViewById(R.id.food_item_price);
            itemsCounter = itemView.findViewById(R.id.items_counter);
            addItem = itemView.findViewById(R.id.button_add_item);
            minusItem = itemView.findViewById(R.id.button_minus_item);
        }
    }
}
