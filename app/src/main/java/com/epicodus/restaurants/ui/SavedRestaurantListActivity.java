package com.epicodus.restaurants.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.epicodus.restaurants.MyRestaurantsApplication;
import com.epicodus.restaurants.R;
import com.epicodus.restaurants.adapters.FirebaseRestaurantListAdapter;
import com.epicodus.restaurants.models.Restaurant;
import com.epicodus.restaurants.util.OnRestaurantSelectedListener;
import com.epicodus.restaurants.util.OnStartDragListener;
import com.epicodus.restaurants.util.SimpleItemTouchHelperCallback;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedRestaurantListActivity extends AppCompatActivity implements OnRestaurantSelectedListener {
    private Firebase mFirebaseRef;
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
        setContentView(R.layout.activity_saved_restaurant_list);
        mFirebaseRef = MyRestaurantsApplication.getAppInstance().getFirebaseRef();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                this.logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestaurantSelected(Integer position, ArrayList<Restaurant> restaurants) {
        mPosition = position;
        mRestaurants = restaurants;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mPosition != null && mRestaurants != null) {
            outState.putInt("position", Integer.valueOf(mPosition));
            outState.putParcelable("restaurants", Parcels.wrap(mRestaurants));
            super.onSaveInstanceState(outState);
        }
    }

    private void logout() {
        mFirebaseRef.unauth();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
