package com.example.android.futurevision;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    public static final int HIDE_TRESHOLD = 5;
    private int scrolledDistance = 0;
    private boolean visible = true;
    @Override
    public void onScrolled(RecyclerView recyclerView,int dx,int dy){
        super.onScrolled(recyclerView,dx,dy);

        int firstVisibleItem =((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        //show views if first item is first visible position and views are hidden
        if(firstVisibleItem == 0) {
            if (!visible) {
                onShow();
                visible = true;
            }
        }else {
            if (scrolledDistance > HIDE_TRESHOLD && visible) {
                onHide();
                visible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_TRESHOLD && !visible) {
                onShow();
                visible = true;
                scrolledDistance =0;
            }
        }
        if((visible && dy>0)||(!visible && dy<0))
        {
            scrolledDistance += dy;
        }
    }
    public abstract void onShow();
    public abstract void onHide();
}
