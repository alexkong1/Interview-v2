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
import com.google.gson.Gson;
import com.zumepizza.interview.model.Pizza;

import java.util.List;

/**
 * Note: There was no actual field to signify that a pizza is classified as spicy.  Instead I went for gluten-free.
 */
public class PizzaDetailsFragment extends Fragment {

    public static final String ARGS_PIZZA = "pizza";

    public static PizzaDetailsFragment newInstance(String pizzaJson) {
        PizzaDetailsFragment fragment = new PizzaDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_PIZZA, pizzaJson);
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
            Pizza pizza = new Gson().fromJson(getArguments().getString(ARGS_PIZZA), Pizza.class);

            RecyclerView recyclerView = root.findViewById(R.id.details_toppings_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new ToppingsAdapter(getContext(), pizza.getToppings()));

            ImageView imageView = root.findViewById(R.id.details_image);
            if (getContext() != null)
                Glide.with(getContext())
                        .load(pizza.getAssets().getProductDetailsPage().get(0).getUrl())
                        .into(imageView);

            if (getActivity() != null)
                root.findViewById(R.id.details_add).setOnClickListener(view ->
                        ((AddPizzaListener) getActivity()).addPizza(pizza.getId()));

            ((TextView) root.findViewById(R.id.details_name)).setText(pizza.getName());

            if (pizza.getClassifications() != null && pizza.getClassifications().isGlutenFree())
                root.findViewById(R.id.details_gluten_free).setVisibility(View.VISIBLE);
            else root.findViewById(R.id.details_gluten_free).setVisibility(View.GONE);

            if (pizza.getClassifications() != null && pizza.getClassifications().isVegetarian())
                root.findViewById(R.id.details_veg).setVisibility(View.VISIBLE);
            else root.findViewById(R.id.details_veg).setVisibility(View.GONE);

            ((TextView) root.findViewById(R.id.details_description)).setText(pizza.getMenuDescription());

            ((TextView) root.findViewById(R.id.details_price)).setText(String.format(getString(R.string.price), pizza.getPrice()));
        }
    }

    class ToppingsAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<Pizza.Topping> toppings;
        private Context context;

        ToppingsAdapter(Context context, List<Pizza.Topping> toppings) {
            this.context = context;
            this.toppings = toppings;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_topping, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Pizza.Topping topping = toppings.get(viewHolder.getAdapterPosition());

            Glide.with(context)
                    .load(topping.getAsset().getUrl())
                    .into(viewHolder.toppingBackground);

            viewHolder.toppingName.setText(topping.getName());
            viewHolder.toppingDscription.setText(topping.getDescription());
        }

        @Override
        public int getItemCount() {
            return toppings.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView toppingBackground;
        TextView toppingName;
        TextView toppingDscription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            toppingBackground = itemView.findViewById(R.id.topping_image);
            toppingName = itemView.findViewById(R.id.topping_name);
            toppingDscription = itemView.findViewById(R.id.topping_description);
        }
    }

    interface AddPizzaListener {
        void addPizza(int id);
    }
}
