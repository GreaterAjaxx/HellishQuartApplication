package com.example.hellishquartapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import java.util.Arrays;

@SuppressLint("ClickableViewAccessibility")
public class CharacterActivity extends AppCompatActivity
        implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private static final String[] CHAR_LIST = {"Isabella", "Gedeon", "Jacek", "Barabasz", "Marie"};
    private GestureDetector detector;
    private Intent intent;
    // pull from system preferences
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = prefs.getString("DEFAULT_CHAR", "null");
        //System.out.println(pref);
        if(!Arrays.asList(CHAR_LIST).contains(pref)) {
            index = 0;
        }
        else {
            index = Arrays.asList(CHAR_LIST).indexOf(pref);
            //System.out.println(index);
        }
        detector = new GestureDetector(this,this);
        ViewPager viewPager = findViewById(R.id.viewPager);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
        //System.out.println(index);
        viewPager.setCurrentItem(index);

        TextView nameText = findViewById(R.id.character_text);
        this.intent = new Intent(this, ListActivity.class);
        intent.putExtra("CHAR_INDEX", index);
        System.out.println(index);
        nameText.setText(CHAR_LIST[index]);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                index = viewPager.getCurrentItem();
                nameText.setText(CHAR_LIST[index]);
                intent.putExtra("CHAR_INDEX", index);
            }
        });
        viewPager.setOnTouchListener((v, event) -> detector.onTouchEvent(event));
        nameText.setOnClickListener(v -> {
            startActivity(this.intent);
        });
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        startActivity(this.intent);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffY = e1.getY() - e2.getY();
        if(Math.abs(diffY) > 300 && Math.abs(velocityY) > 300) {
            startActivity(this.intent);
            return true;
        }
        return false;
    }
}