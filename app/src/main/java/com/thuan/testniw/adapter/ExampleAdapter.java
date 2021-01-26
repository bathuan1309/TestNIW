package com.thuan.testniw.adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.thuan.testniw.R;
import com.thuan.testniw.databinding.ItemListBinding;
import com.thuan.testniw.listener.ItemClickListener;
import com.thuan.testniw.model.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>{

    private List<Example> exampleList;
    private LayoutInflater layoutInflater;
    private ItemClickListener listener;
    private List<Example> exampleSearch;
    private Timer timer;

    public ExampleAdapter(List<Example> examples, ItemClickListener listener) {
        this.listener = listener;
        this.exampleList = examples;
        exampleSearch = examples;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemListBinding itemListBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_list, parent, false);
        return new ExampleViewHolder(itemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        holder.bindExample(exampleList.get(position));
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

     class ExampleViewHolder extends RecyclerView.ViewHolder {
        private ItemListBinding itemListBinding;

        public ExampleViewHolder(ItemListBinding itemListBinding) {
            super(itemListBinding.getRoot());
            this.itemListBinding = itemListBinding;
        }

        public void bindExample(Example example) {
            itemListBinding.setExample(example);
            itemListBinding.executePendingBindings();
            itemListBinding.getRoot().setOnClickListener(view -> listener.onItemClicked(example));
        }
    }

    public void searchExamples(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    exampleList = exampleSearch;
                } else {
                    List<Example> temp = new ArrayList<>();
                    for(Example example: exampleSearch) {
                        if(example.getName().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            temp.add(example);
                        }
                    }
                    exampleList = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer() {
        if(timer != null) {
            timer.cancel();
        }
    }
}
