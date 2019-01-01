package com.example.ankita.tseccanteen;

import java.util.ArrayList;

public class OrdersModalClass {

    String orderNo, studentName, amount;
    ArrayList<FoodQuantityClass> foods;


    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setFoodQuantityList(ArrayList<FoodQuantityClass> foods) {
        this.foods = foods;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getAmount() {
        return amount;
    }

    public ArrayList<FoodQuantityClass> getFoods() {
        return foods;
    }
}
