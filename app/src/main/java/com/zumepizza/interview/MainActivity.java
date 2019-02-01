package com.zumepizza.interview;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;


// * See "Instructions" text file

public class MainActivity extends AppCompatActivity implements PizzaAdapter.PizzaSelectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initializeUi();
    }

    private void initializeUi() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, MainFragment.newInstance())
                .commit();
    }

    @Override
    public void selectPizza(JSONObject pizza) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, PizzaDetailsFragment.newInstance(pizza))
                .addToBackStack(null)
                .commit();
    }
}
