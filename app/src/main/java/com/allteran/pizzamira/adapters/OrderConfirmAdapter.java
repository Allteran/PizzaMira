package com.allteran.pizzamira.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.util.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrderConfirmAdapter extends RecyclerView.Adapter<OrderConfirmAdapter.ViewHolder> {
    private List<FoodItem> mFoodList;
    private RecyclerView mRecycler;

    public OrderConfirmAdapter() {
    }

    public OrderConfirmAdapter(List<FoodItem> mFoodList, RecyclerView mRecycler) {
        this.mFoodList = mFoodList;
        this.mRecycler = mRecycler;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirm_order_food, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position != mFoodList.size()) {
            FoodItem item = mFoodList.get(position);

            holder.name.setText(item.getName());

            String quantity = item.getCountInCart() + " шт.";
            holder.quantity.setText(quantity);

            String price = StringUtils.priceFormatter(item.getCountInCart() * item.getPrice());
            holder.price.setText(price);

            String weight = item.getWeight() * item.getCountInCart() + " гр.";
            holder.weight.setText(weight);
        } else {
            holder.name.setText("Итого");

            int price = 0;
            for (FoodItem foodItem : mFoodList) {
                price = price + foodItem.getCountInCart() * foodItem.getPrice();
            }
            holder.weight.setVisibility(View.GONE);
            holder.quantity.setVisibility(View.GONE);
            holder.price.setText(StringUtils.priceFormatter(price));
        }
    }

    @Override
    public int getItemCount() {
        return mFoodList.size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView quantity;
        private TextView weight;
        private TextView price;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            weight = itemView.findViewById(R.id.weight);
            price = itemView.findViewById(R.id.price);
        }
    }
}
