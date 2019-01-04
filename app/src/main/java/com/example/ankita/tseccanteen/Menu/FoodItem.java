package com.example.ankita.tseccanteen.Menu;

public class FoodItem {

    String name, price, availability, description;

    public FoodItem(String name, String price, String availability, String description) {
        this.name = name;
        this.price = price;
        this.availability = availability;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getAvailability() {
        return availability;
    }

    public String getDescription() {
        return description;
    }
}
