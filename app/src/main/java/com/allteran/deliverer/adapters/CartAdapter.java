package com.allteran.deliverer.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allteran.deliverer.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    //TODO: end it with displaying order with no duplicate of added item

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;
        private TextView name;
        private TextView weight;
        private TextView price;
        private TextView itemsCounter;
        private Button addItem;
        private Button minusItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.food_item_thumb);
            name = itemView.findViewById(R.id.food_item_name);
            weight = itemView.findViewById(R.id.food_item_weight);
            price = itemView.findViewById(R.id.food_item_price);
            itemsCounter = itemView.findViewById(R.id.items_counter);
            addItem = itemView.findViewById(R.id.button_add_item);
            minusItem = itemView.findViewById(R.id.button_minus_item);
        }
    }
}
