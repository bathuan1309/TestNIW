package com.thuan.testniw.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.thuan.testniw.R;
import com.thuan.testniw.adapter.ExampleAdapter;
import com.thuan.testniw.databinding.ActivityMainBinding;
import com.thuan.testniw.listener.ItemClickListener;
import com.thuan.testniw.model.Example;
import com.thuan.testniw.viewmodel.ExampleViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ItemClickListener {
    private ActivityMainBinding activityMainBinding;
    private ExampleViewModel viewModel;
    private List<Example> examples = new ArrayList<>();
    private ExampleAdapter exampleAdapter;
    private int currentPage = 1;
    private int totalPage = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if(isConnectedOnline()) {
            doInitialization();
        } else {
            Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
            doInitializationOff();
        }

    }

    private void doInitializationOff() {
        activityMainBinding.mainRecycler.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(ExampleViewModel.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadExamples().subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(ex -> {
            if(examples.size() > 0) {
                examples.clear();
            }
            examples.addAll(ex);
            exampleAdapter = new ExampleAdapter(examples, this);
            activityMainBinding.mainRecycler.setAdapter(exampleAdapter);
            compositeDisposable.dispose();
        }));

        searchExamples();

    }


    private void doInitialization() {
        activityMainBinding.mainRecycler.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(ExampleViewModel.class);
        exampleAdapter = new ExampleAdapter(examples, this);
        activityMainBinding.mainRecycler.setAdapter(exampleAdapter);

        searchExamples();

        activityMainBinding.mainRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!activityMainBinding.mainRecycler.canScrollVertically(1)) {
                    if(currentPage <= totalPage) {
                        currentPage++;
                        getExamples();
                    }
                }
            }
        });
        getExamples();
    }

    private void getExamples() {
        toggleLoading();
        viewModel.getExamples(currentPage).observe(this, ex -> {
            toggleLoading();
            if(ex != null) {
                int oldCount = examples.size();
                examples.addAll(ex);

                // Add to Room
                CompositeDisposable compositeDisposable = new CompositeDisposable();
                compositeDisposable.add(viewModel.addToRoom(ex)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
//                            Toast.makeText(getApplicationContext(), "Add to Room", Toast.LENGTH_LONG).show();
                            compositeDisposable.dispose();
                        }));

                exampleAdapter.notifyItemRangeInserted(oldCount, examples.size());

            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()) {
                activityMainBinding.setIsLoading(false);
            } else {
                activityMainBinding.setIsLoading(true);
            }
        } else {
            if(activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()) {
                activityMainBinding.setIsLoadingMore(false);
            } else {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onItemClicked(Example example) {
        Intent intent = new Intent(getApplicationContext(), ExampleDetailsActivity.class);
        intent.putExtra("name", example.getName());
        intent.putExtra("desc", example.getDescription());
        startActivity(intent);
    }

    private boolean isConnectedOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConnect != null && wifiConnect.isConnected()) || (mobileConnect != null && mobileConnect.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void searchExamples() {
        activityMainBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                exampleAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(examples.size() != 0) {
                    exampleAdapter.searchExamples(editable.toString());
                }
            }
        });
    }

}