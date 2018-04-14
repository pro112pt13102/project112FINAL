package com.example.peter.project1.CustomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.peter.project1.Enum.DirectionViewpager;

/**
 * Created by daovip on 4/4/2018.
 */

public class OneDirectionWipeViewpager extends ViewPager {
    private float initialXValue;
    private DirectionViewpager direction;

    public OneDirectionWipeViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.direction = DirectionViewpager.all;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if(this.direction == DirectionViewpager.all) return true;

        if(direction == DirectionViewpager.none )//disable any swipe
            return false;

        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0 && direction == DirectionViewpager.right ) {
                    // swipe from left to right detected
                    return false;
                }else if (diffX < 0 && direction == DirectionViewpager.left ) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(DirectionViewpager direction) {
        this.direction = direction;
    }
}
