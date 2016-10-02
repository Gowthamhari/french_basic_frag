
package com.example.android.miwok;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String preference = "pref";

    public static final String saveIt ="savekey";

    public int fragmentPosition = 0;

    SharedPreferences sf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());


        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        sf = getSharedPreferences(preference,Context.MODE_PRIVATE);

        if (sf.contains(saveIt)){
            viewPager.getCurrentItem();

        }


    }

    public void setFragmentPosition(int position) {
        fragmentPosition = position;
    }


    @Override
    public void onDestroy() {
        try {
            save();
            super.onDestroy();
        } catch (Exception e){
            Log.e("message", String.valueOf(e));
        }
    }
    private void save() {
        int position = fragmentPosition;
        if(sf!=null) {
            SharedPreferences.Editor editor = sf.edit();
            editor.putInt(saveIt, position);
            editor.commit();
        }

    }

}
