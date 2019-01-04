package com.example.ankita.tseccanteen.Menu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ankita.tseccanteen.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    Context context;
    ArrayList<MenuModalClass> menuList;
    OnMenuClickListener onMenuClickListener;

    public MenuAdapter(Context context, ArrayList<MenuModalClass> menuList, OnMenuClickListener onMenuClickListener) {
        this.context = context;
        this.menuList = menuList;
        this.onMenuClickListener = onMenuClickListener;
    }

    public interface OnMenuClickListener {
        public void onMenuItemClick(int position);
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
        CardView menuItem;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.tv_food_name);
            foodPrice = itemView.findViewById(R.id.tv_food_price);
            foodAvailability = itemView.findViewById(R.id.tv_food_availability);
            foodDescription = itemView.findViewById(R.id.tv_food_description);
            menuItem = itemView.findViewById(R.id.card_view_menu_item);

            menuItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMenuClickListener.onMenuItemClick(getAdapterPosition());
                }
            });
        }
    }


}
