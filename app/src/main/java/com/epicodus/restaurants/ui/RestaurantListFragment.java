package com.epicodus.restaurants.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.epicodus.restaurants.MyRestaurantsApplication;
import com.epicodus.restaurants.R;
import com.epicodus.restaurants.adapters.RestaurantListAdapter;
import com.epicodus.restaurants.models.Restaurant;
import com.epicodus.restaurants.services.YelpService;
import com.epicodus.restaurants.util.OnRestaurantSelectedListener;
import com.firebase.client.Firebase;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Guest on 4/6/16.
 */
public class RestaurantListFragment extends Fragment {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;
    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentAddress;
    private Firebase mFirebaseRef;
    OnRestaurantSelectedListener mOnRestaurantSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnRestaurantSelectedListener = (OnRestaurantSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseRef = MyRestaurantsApplication.getAppInstance().getFirebaseRef();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        ButterKnife.bind(this, view);
        mSharedPreferences = view.getContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        mRecentAddress = mSharedPreferences.getString("location", null);
        mEditor = mSharedPreferences.edit();
        if (mRecentAddress != null) {
            getRestaurants(mRecentAddress);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(query);
                getRestaurants(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_search:
                return true;
            default:
                break;
        }

        return false;
    }

    private void getRestaurants(String location) {
        final YelpService yelpService = new YelpService(getContext());

        yelpService.findRestaurants(location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRestaurants = yelpService.processResults(response);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new RestaurantListAdapter(mRestaurants, mOnRestaurantSelectedListener);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }

    private void addToSharedPreferences(String location) {
        mEditor.putString("location", location).commit();
    }

    private void logout() {
        mFirebaseRef.unauth();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}