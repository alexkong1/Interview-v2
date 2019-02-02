package com.zumepizza.interview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zumepizza.interview.model.Pizza;

import java.util.List;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MainFragment extends Fragment implements API.ResponseHandler {

    private RecyclerView recyclerView;
    private API api;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeUi(view);
    }

    private void initializeUi(View root) {
        recyclerView = root.findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        api = new API(getContext());
        api.fetchMenu(this);
    }

    @Override
    public void completion(Map<String, List<Pizza>> response) {
        Log.e("PIZZAS", response.toString());

        SectionedRecyclerViewAdapter adapter = new SectionedRecyclerViewAdapter();

        for(String key : response.keySet()) {
            adapter.addSection(new PizzaAdapter(getContext(),
                    (PizzaAdapter.PizzaSelectionListener) getActivity(), key, response.get(key)));
        }

        recyclerView.setAdapter(adapter);
    }

    public void updateNumInCart() {
        if (recyclerView.getAdapter() != null) recyclerView.getAdapter().notifyDataSetChanged();
    }
}
