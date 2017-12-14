package com.example.baotu.textswag.utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.baotu.textswag.implement.OnItemRecyclerViewClickListener;


public class OnItemRecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

    private OnItemRecyclerViewClickListener mOnItemRecyclerViewClickListener;
    private GestureDetector mGestureDetector;

    public OnItemRecyclerViewTouchListener(Context context, OnItemRecyclerViewClickListener onItemRecyclerViewClickListener) {
        this.mOnItemRecyclerViewClickListener = onItemRecyclerViewClickListener;
        this.mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mOnItemRecyclerViewClickListener != null && mGestureDetector.onTouchEvent(e)) {
            mOnItemRecyclerViewClickListener.onItemClick(child, rv.getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
