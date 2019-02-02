package com.zumepizza.interview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PizzaDetailsFragment extends Fragment {

    public static PizzaDetailsFragment newInstance(String pizzaJson) {
        PizzaDetailsFragment fragment = new PizzaDetailsFragment();
        Bundle args = new Bundle();
        args.putString("pizza", pizzaJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pizza_details, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeUi(view);
    }

    private void initializeUi(View root) {

        if (getArguments() != null) {
            try {
                JSONObject pizza = new JSONObject(getArguments().getString("pizza"));

                RecyclerView recyclerView = root.findViewById(R.id.details_ingredients_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new ToppingsAdapter(getContext(), pizza.getJSONArray("toppings")));

                ImageView imageView = root.findViewById(R.id.details_image);
                if (getContext() != null)
                    Glide.with(getContext())
                            .load(pizza.getJSONObject("menuAsset").getString("url"))
                            .into(imageView);

                if (getActivity() != null)
                    root.findViewById(R.id.details_add).setOnClickListener(view ->
                            ((AddPizzaListener) getActivity()).addPizza());

                ((TextView) root.findViewById(R.id.details_name)).setText(pizza.getString("name"));

                if (pizza.optInt("vegetarian", 0) == 1) root.findViewById(R.id.details_veg).setVisibility(View.VISIBLE);
                else root.findViewById(R.id.details_veg).setVisibility(View.GONE);

                if (pizza.optInt("spicy", 0) == 1) root.findViewById(R.id.details_spicy).setVisibility(View.VISIBLE);
                else root.findViewById(R.id.details_spicy).setVisibility(View.GONE);

                ((TextView) root.findViewById(R.id.details_name)).setText(pizza.getString("menu_description"));

            } catch (JSONException e) {

            }
        }
    }

    class ToppingsAdapter extends RecyclerView.Adapter<ViewHolder> {

        private JSONArray toppings;
        private Context context;

        ToppingsAdapter(Context context, JSONArray toppings) {
            this.context = context;
            this.toppings = toppings;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_ingredient, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            try {
                JSONObject topping = toppings.getJSONObject(viewHolder.getAdapterPosition());

                Glide.with(context)
                        .load(topping.getJSONObject("asset").getString("url"))
                        .into(viewHolder.ingredientBackground);

                viewHolder.ingredientName.setText(topping.getString("name"));

            } catch (JSONException e) {

            }
        }

        @Override
        public int getItemCount() {
            return toppings.length();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ingredientBackground;
        TextView ingredientName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientBackground = itemView.findViewById(R.id.ingredient_background);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
        }
    }

    interface AddPizzaListener {
        void addPizza();
    }
}
