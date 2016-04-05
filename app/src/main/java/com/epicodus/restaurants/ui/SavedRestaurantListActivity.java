package com.epicodus.restaurants.ui;

import android.content.Intent;
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
import com.epicodus.restaurants.util.OnStartDragListener;
import com.epicodus.restaurants.util.SimpleItemTouchHelperCallback;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedRestaurantListActivity extends AppCompatActivity implements OnStartDragListener {
    private ItemTouchHelper mItemTouchHelper;
    private Query mQuery;
    private Firebase mFirebaseRef;
    private String mCurrentUserId;
    private FirebaseRestaurantListAdapter mAdapter;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);
        mFirebaseRef = MyRestaurantsApplication.getAppInstance().getFirebaseRef();
        mCurrentUserId = mFirebaseRef.getAuth().getUid();
        Log.d("Saved REST UID", mFirebaseRef.getAuth().getUid().toString());

        setupFirebaseQuery();
        setupRecyclerView();
    }

    private void setupFirebaseQuery() {
//        String location = mFirebaseRef.child("restaurants/" + mFirebaseRef.getAuth().getUid()).toString();
//        Log.d("LOCATION", location);
        mQuery = mFirebaseRef.child("restaurants/" + mFirebaseRef.getAuth().getUid()).orderByChild("index");
}

    private void setupRecyclerView() {
        mAdapter = new FirebaseRestaurantListAdapter(mQuery, Restaurant.class, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
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

    private void logout() {
        mFirebaseRef.unauth();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        for (Restaurant restaurant : mAdapter.getItems()) {
            restaurant.setIndex(Integer.toString(mAdapter.getItems().indexOf(restaurant)));
            mFirebaseRef.child("restaurants/" + mFirebaseRef.getAuth().getUid() + "/"
                    + restaurant.getName())
                    .setValue(restaurant);
        }
    }
}
