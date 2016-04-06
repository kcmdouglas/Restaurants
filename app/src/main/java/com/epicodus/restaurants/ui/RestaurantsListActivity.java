package com.epicodus.restaurants.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.epicodus.restaurants.MyRestaurantsApplication;
import com.epicodus.restaurants.R;
import com.epicodus.restaurants.adapters.RestaurantListAdapter;
import com.epicodus.restaurants.models.Restaurant;
import com.epicodus.restaurants.services.YelpService;
import com.epicodus.restaurants.util.OnRestaurantSelectedListener;
import com.firebase.client.Firebase;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RestaurantsListActivity extends AppCompatActivity implements OnRestaurantSelectedListener {
    public static final String TAG = RestaurantsListActivity.class.getSimpleName();
    private Integer mPosition;
    ArrayList<Restaurant> mRestaurants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int size;

        try {
            ArrayList<Restaurant> restaurants = Parcels.unwrap(savedInstanceState.getParcelable("restaurants"));
            size = restaurants.size();
        } catch (NullPointerException npe) {
            size = -1;
        }

        if (savedInstanceState != null && size > -1) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mPosition = savedInstanceState.getInt("position");
                mRestaurants = Parcels.unwrap(savedInstanceState.getParcelable("restaurants"));
                Intent intent = new Intent(this, RestaurantDetailActivity.class);
                intent.putExtra("position", mPosition.toString());
                intent.putExtra("restaurants", Parcels.wrap(mRestaurants));
                startActivity(intent);
            }
        }
        setContentView(R.layout.activity_restaurants);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mPosition != null && mRestaurants != null) {
            outState.putInt("position", Integer.valueOf(mPosition));
            outState.putParcelable("restaurants", Parcels.wrap(mRestaurants));
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onRestaurantSelected(Integer position, ArrayList<Restaurant> restaurants) {
        mPosition = position;
        mRestaurants = restaurants;
    }

}