/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mess.food;

import com.sun.lwuit.Image;

/**
 *
 * @author kirui
 */
public class FoodList {

    private String name;
    private String price;
    private String details;
    private Image icon;

    public FoodList() {
    }

    public FoodList(String name, String price, String details, Image icon) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.icon = icon;
    }
    

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public Image getIcon() {
        return icon;
    }
}
