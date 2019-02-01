package com.zumepizza.interview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.ViewHolder> {

    private JSONArray response;
    private Context context;
    private PizzaSelectionListener listener;

    public PizzaAdapter(Context context, PizzaSelectionListener listener, JSONArray response) {
        this.context = context;
        this.listener = listener;
        this.response = response;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            JSONObject object = response.getJSONObject(viewHolder.getAdapterPosition());
            Glide.with(context)
                    .load(object.getJSONObject("menuAsset").getString("url"))
                    .into(viewHolder.pizzaImage);

            viewHolder.pizzaName.setText(object.getString("name"));
            viewHolder.pizzaPrice.setText(
                    String.format(context.getString(R.string.price), object.getString("price")));

            viewHolder.itemView.setOnClickListener(view -> listener.selectPizza(object));
            viewHolder.pizzaAddRemove.setOnClickListener(view -> {

            });

            if (object.getInt("vegetarian") == 1) viewHolder.pizzaVeg.setVisibility(View.VISIBLE);
            else viewHolder.pizzaVeg.setVisibility(View.GONE);

            if (object.getInt("spicy") == 1) viewHolder.pizzaSpicy.setVisibility(View.VISIBLE);
            else viewHolder.pizzaSpicy.setVisibility(View.GONE);

            String toppingsString = "";
            JSONArray toppings = object.getJSONArray("toppings");
            for (int p = 0; p < toppings.length(); p++) {
                toppingsString = toppingsString + toppings.getJSONObject(p).getString("name");
                if (p != toppings.length() - 1) toppingsString = toppingsString + ", ";
            }

            viewHolder.pizzaToppings.setText(toppingsString);

        } catch (JSONException e) {
            Log.e("PIZZA LOAD", e.toString());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_pizza, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return response.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView pizzaImage;
        TextView pizzaToppings;
        TextView pizzaName;
        TextView pizzaPrice;
        ImageView pizzaVeg;
        ImageView pizzaSpicy;
        ImageView pizzaAddRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaImage = itemView.findViewById(R.id.pizza_image);
            pizzaToppings = itemView.findViewById(R.id.pizza_toppings);
            pizzaName = itemView.findViewById(R.id.pizza_name);
            pizzaPrice = itemView.findViewById(R.id.pizza_price);
            pizzaAddRemove = itemView.findViewById(R.id.pizza_add_remove);
            pizzaSpicy = itemView.findViewById(R.id.pizza_spicy);
            pizzaVeg = itemView.findViewById(R.id.pizza_veg);
        }
    }

    interface PizzaSelectionListener {
        void selectPizza(JSONObject pizza);
    }
}
