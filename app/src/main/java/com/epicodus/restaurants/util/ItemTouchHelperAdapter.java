package com.epicodus.restaurants.util;

/**
 * Created by Guest on 4/5/16.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}