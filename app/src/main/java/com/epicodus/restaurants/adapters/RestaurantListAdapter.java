package com.epicodus.restaurants.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.restaurants.R;
import com.epicodus.restaurants.models.Restaurant;
import com.epicodus.restaurants.ui.RestaurantDetailActivity;
import com.epicodus.restaurants.util.OnRestaurantSelectedListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kassidy on 3/20/2016.
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private ArrayList<Restaurant> mRestaurants = new ArrayList<>();
    private OnRestaurantSelectedListener mOnRestaurantSelectedListener;
    public RestaurantViewHolder viewHolder;

    public RestaurantListAdapter(ArrayList<Restaurant> restaurants, OnRestaurantSelectedListener onRestaurantSelectedListener) {
        mOnRestaurantSelectedListener = onRestaurantSelectedListener;
        mRestaurants = restaurants;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item, parent, false);
        viewHolder = new RestaurantViewHolder(view, mRestaurants, mOnRestaurantSelectedListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        holder.bindRestaurant(mRestaurants.get(position));
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }


}