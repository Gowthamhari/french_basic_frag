
package com.example.android.miwok;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    public static final String preference = "pref";

    public static final String saveIt ="savekey";

    public int fragmentPosition = 0;

    SharedPreferences sf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());


        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        sf = getSharedPreferences(preference,Context.MODE_PRIVATE);

        int pos = sf.getInt(saveIt, 1);
        viewPager.setCurrentItem(pos-1);


        /*if (sf.contains(saveIt)){
            viewPager.getCurrentItem();

        }*/


    }

    public void setFragmentPosition(int position) {
        fragmentPosition = position;
        //Toast.makeText(this, "position : "+fragmentPosition, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        try {
            save();
            Toast.makeText(this, "Destroying...", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(this, "sf is null", Toast.LENGTH_SHORT).show();
        }

    }

}
