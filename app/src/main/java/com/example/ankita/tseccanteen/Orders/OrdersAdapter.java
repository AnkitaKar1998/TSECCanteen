package com.example.ankita.tseccanteen.Orders;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ankita.tseccanteen.R;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    Context context;
    ArrayList<OrdersModalClass> orderList;
    OnOrderClickListener onOrderClickListener;

    public OrdersAdapter(Context context, ArrayList<OrdersModalClass> orderList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.onOrderClickListener = onOrderClickListener;
    }


    public interface OnOrderClickListener {
        public  void onOrderClick(String orderNo);
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.order_layout, viewGroup, false);
        OrdersViewHolder ordersViewHolder = new OrdersViewHolder(view);
        return ordersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder ordersViewHolder, int position) {
        ordersViewHolder.orderNo.setText("Order No: "+orderList.get(position).getOrder_id());
        ordersViewHolder.studentName.setText(orderList.get(position).getName());

        for (int i=0; i<orderList.get(position).getFoods().size(); i++) {
            LinearLayout itemDetails = new LinearLayout(context);
            TextView foodName = new TextView(context);
            TextView foodQuantity = new TextView(context);
            TextView foodPrice = new TextView(context);

            foodName.setText(orderList.get(position).getFoods().get(i).getName());
            foodQuantity.setText(orderList.get(position).getFoods().get(i).getQuantity());
            foodPrice.setText(orderList.get(position).getFoods().get(i).getPrice());

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f);
            foodName.setLayoutParams(layoutParams1);
            foodName.setTextColor(Color.parseColor("#000000"));
            foodName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            foodQuantity.setLayoutParams(layoutParams2);
            foodQuantity.setTextColor(Color.parseColor("#000000"));
            foodQuantity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            foodQuantity.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            foodPrice.setLayoutParams(layoutParams3);
            foodPrice.setTextColor(Color.parseColor("#000000"));
            foodPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            foodPrice.setGravity(Gravity.CENTER);

            itemDetails.addView(foodName);
            itemDetails.addView(foodQuantity);
            itemDetails.addView(foodPrice);

            ordersViewHolder.orderMenu.addView(itemDetails);
        }

        LinearLayout llAmount = new LinearLayout(context);
        TextView total = new TextView(context);
        total.setText("Total: ");
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        total.setLayoutParams(layoutParams1);
        total.setGravity(Gravity.LEFT);
        total.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        total.setTextColor(Color.parseColor("#000000"));
        llAmount.addView(total);

        TextView amount = new TextView(context);
        amount.setText(orderList.get(position).getPrice());
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        total.setLayoutParams(layoutParams2);
        amount.setGravity(Gravity.CENTER);
        amount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        amount.setTextColor(Color.parseColor("#000000"));
        llAmount.addView(amount);

        ordersViewHolder.orderMenu.addView(llAmount);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    class OrdersViewHolder extends RecyclerView.ViewHolder {

        LinearLayout orderMenu;
        TextView orderNo, studentName;
        CardView orderItem;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            orderMenu = itemView.findViewById(R.id.ll_order_menu);
            orderNo = itemView.findViewById(R.id.tv_order_no);
            studentName = itemView.findViewById(R.id.tv_student_name);
            orderItem = itemView.findViewById(R.id.card_view_order_item);

            orderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderClickListener.onOrderClick(orderList.get(getAdapterPosition()).getOrder_id());
                }
            });
        }
    }

}
