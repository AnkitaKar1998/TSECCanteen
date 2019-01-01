package com.example.ankita.tseccanteen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    Context context;
    ArrayList<MenuModalClass> menuList;

    public MenuAdapter(Context context, ArrayList<MenuModalClass> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.menu_layout, viewGroup, false);
        MenuViewHolder menuViewHolder = new MenuViewHolder(view);
        return menuViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder menuViewHolder, int position) {
        menuViewHolder.foodName.setText(menuList.get(position).getName());
        menuViewHolder.foodPrice.setText(menuList.get(position).getPrice());
        menuViewHolder.foodAvailability.setText(menuList.get(position).getAvailability());
        menuViewHolder.foodDescription.setText(menuList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }


    public  class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView foodName, foodPrice, foodAvailability, foodDescription;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.tv_food_name);
            foodPrice = itemView.findViewById(R.id.tv_food_price);
            foodAvailability = itemView.findViewById(R.id.tv_food_availability);
            foodDescription = itemView.findViewById(R.id.tv_food_description);
        }
    }


}
