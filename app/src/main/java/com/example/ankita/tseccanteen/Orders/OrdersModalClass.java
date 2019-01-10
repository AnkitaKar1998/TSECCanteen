package com.example.ankita.tseccanteen.Orders;

import java.util.ArrayList;

public class OrdersModalClass {

    String order_id, name, price;
    ArrayList<FoodQuantityClass> foods;


    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setFoodQuantityList(ArrayList<FoodQuantityClass> foods) {
        this.foods = foods;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public ArrayList<FoodQuantityClass> getFoods() {
        return foods;
    }
}
