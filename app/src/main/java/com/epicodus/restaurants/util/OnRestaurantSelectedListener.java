package com.epicodus.restaurants.util;

import com.epicodus.restaurants.models.Restaurant;

import java.util.ArrayList;

/**
 * Created by Guest on 4/6/16.
 */
public interface OnRestaurantSelectedListener {
    public void onRestaurantSelected(Integer position, ArrayList<Restaurant> restaurants);
}
