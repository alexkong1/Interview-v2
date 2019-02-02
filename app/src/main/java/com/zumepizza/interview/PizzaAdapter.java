package com.zumepizza.interview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zumepizza.interview.model.Pizza;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class PizzaAdapter extends StatelessSection {

    private Context context;
    private PizzaSelectionListener listener;
    private List<Pizza> pizzas = new ArrayList<>();
    private String category;

    public PizzaAdapter(Context context, PizzaSelectionListener listener, String category, List<Pizza> response) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_pizza)
                .headerResourceId(R.layout.item_pizza_category)
                .build());
        this.context = context;
        this.listener = listener;
        this.pizzas = response;
        this.category = category;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        ((CategoryViewHolder) holder).categoryName.setText(category);
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new PizzaViewHolder(view);
    }

    @Override
    public int getContentItemsTotal() {
        return pizzas.size();
    }

    /**
     * Note: There was no actual field to signify that a pizza is classified as spicy.  Instead I went for gluten-free.
     */
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        PizzaViewHolder viewHolder = (PizzaViewHolder) holder;

        Pizza pizza = pizzas.get(position);
        Glide.with(context)
                .load(pizza.getAssets().getMenu().get(0).getUrl())
                .into(viewHolder.pizzaImage);

        viewHolder.pizzaName.setText(pizza.getName());
        viewHolder.pizzaPrice.setText(
                String.format(context.getString(R.string.price), pizza.getPrice()));

        viewHolder.itemView.setOnClickListener(view -> listener.selectPizza(pizza));

        if (pizza.getClassifications() != null && pizza.getClassifications().isVegetarian())
            viewHolder.pizzaVeg.setVisibility(View.VISIBLE);
        else viewHolder.pizzaVeg.setVisibility(View.GONE);

        if (pizza.getClassifications() != null && pizza.getClassifications().isGlutenFree())
            viewHolder.pizzaGlutenFree.setVisibility(View.VISIBLE);
        else viewHolder.pizzaGlutenFree.setVisibility(View.GONE);

        String toppingsString = "";
        List<Pizza.Topping> toppings = pizza.getToppings();
        for (int p = 0; p < toppings.size(); p++) {
            toppingsString = toppingsString + toppings.get(p).getName();
            if (p != toppings.size() - 1) toppingsString = toppingsString + ", ";
        }

        viewHolder.pizzaToppings.setText(toppingsString);

        int numInCart = listener.getNumInCart(pizza.getId());
        if (numInCart > 0) {
            viewHolder.pizzaNum.setVisibility(View.VISIBLE);
            viewHolder.pizzaNum.setText(String.format(context.getString(R.string.num_in_cart), numInCart));
        } else viewHolder.pizzaNum.setVisibility(View.GONE);
    }

    class PizzaViewHolder extends RecyclerView.ViewHolder {

        ImageView pizzaImage;
        TextView pizzaToppings;
        TextView pizzaName;
        TextView pizzaPrice;
        ImageView pizzaVeg;
        ImageView pizzaGlutenFree;
        TextView pizzaNum;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaImage = itemView.findViewById(R.id.pizza_image);
            pizzaToppings = itemView.findViewById(R.id.pizza_toppings);
            pizzaName = itemView.findViewById(R.id.pizza_name);
            pizzaPrice = itemView.findViewById(R.id.pizza_price);
            pizzaGlutenFree = itemView.findViewById(R.id.pizza_gluten_free);
            pizzaVeg = itemView.findViewById(R.id.pizza_veg);
            pizzaNum = itemView.findViewById(R.id.pizza_num);
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }

    interface PizzaSelectionListener {
        void selectPizza(Pizza pizza);
        int getNumInCart(int id);
    }
}
