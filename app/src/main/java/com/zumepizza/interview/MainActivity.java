package com.zumepizza.interview;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;


// * See "Instructions" text file

public class MainActivity extends AppCompatActivity implements PizzaAdapter.PizzaSelectionListener,
        PizzaDetailsFragment.AddPizzaListener {

    private TextView cartBadge;
    private int cartItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
        View actionView = menuItem.getActionView();
        cartBadge = actionView.findViewById(R.id.cart_badge);
        setupBadge();
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initializeUi();
    }

    private void initializeUi() {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, MainFragment.newInstance(), "main")
                    .commit();
    }

    @Override
    public void selectPizza(JSONObject pizza) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.main_container, PizzaDetailsFragment.newInstance(pizza), "details")
                .commit();
    }

    @Override
    public void addPizza() {
        cartItemCount++;
        setupBadge();
        getSupportFragmentManager().popBackStack();
    }

    private void setupBadge() {

        if (cartBadge != null) {
            if (cartItemCount == 0) {
                if (cartBadge.getVisibility() != View.GONE) cartBadge.setVisibility(View.GONE);
            } else {
                cartBadge.setText(String.valueOf(Math.min(cartItemCount, 99)));
                if (cartBadge.getVisibility() != View.VISIBLE)
                    cartBadge.setVisibility(View.VISIBLE);
            }
        }
    }
}
