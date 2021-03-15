package com.allteran.deliverer.adapters;

import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allteran.deliverer.R;
import com.allteran.deliverer.domain.FoodItem;

import java.util.List;

public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.ViewHolder> implements View.OnClickListener {
    private FragmentManager mFragmentManager;
    private RecyclerView mRecycler;
    private List<FoodItem> mFoodList;
    private Point mScreenSize;

    public FoodItemsAdapter() {
    }

    public FoodItemsAdapter(FragmentManager fragmentManager, RecyclerView recyclerView,
                            List<FoodItem> foodList, Point screenSize) {
        this.mFragmentManager = fragmentManager;
        this.mRecycler = recyclerView;
        this.mFoodList = foodList;
        this.mScreenSize = screenSize;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_food_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO: here should be all logic
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
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.food_item_thumb || v.getId() == R.id.food_item_name ||
                v.getId() == R.id.food_item_weight) {
            //TODO: open foodItem details
            return;
        }
        if (v.getId() == R.id.food_item_price) {
            //TODO: ADD FOOD ITEM TO CART
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
            foodWeight = (TextView) itemView.findViewById(R.id.food_item_weight);
            price = itemView.findViewById(R.id.food_item_price);
            textContainer = itemView.findViewById(R.id.food_item_text_container);
        }
    }
}
