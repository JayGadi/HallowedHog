package com.hallowedhog;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.hallowedhog.PagerAdapter;


public class MainActivity extends AppCompatActivity {

    private TabLayout categories;
    private ViewPager viewPager;
    private com.hallowedhog.PagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categories = (TabLayout)findViewById(R.id.categories_tab);
        viewPager = (ViewPager) findViewById(R.id.pager);

        categories.addTab(categories.newTab().setText("Top Stories"));
        categories.addTab(categories.newTab().setText("Carleton"));
        categories.addTab(categories.newTab().setText("Sports"));
        categories.addTab(categories.newTab().setText("Worldwide Affairs"));
        categories.addTab(categories.newTab().setText("Comics"));
        categories.addTab(categories.newTab().setText("Pop Culture"));
        categories.addTab(categories.newTab().setText("Politics"));
        categories.setTabGravity(TabLayout.GRAVITY_FILL);


        adapter = new PagerAdapter(getSupportFragmentManager(), categories.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(categories));
        categories.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





    }

}
