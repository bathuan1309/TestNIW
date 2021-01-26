package com.thuan.testniw.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.thuan.testniw.R;
import com.thuan.testniw.databinding.ItemDetailsBinding;

public class ExampleDetailsActivity extends AppCompatActivity {
    private ItemDetailsBinding itemDetailsBinding;
    private String name, desc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDetailsBinding = DataBindingUtil.setContentView(this, R.layout.item_details);
        doInitialization();
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void doInitialization() {
        name = getIntent().getStringExtra("name");
        desc = getIntent().getStringExtra("desc");
        loadBasicExampleDetails();
    }

    private void loadBasicExampleDetails() {
        itemDetailsBinding.setName(name);
        itemDetailsBinding.setDesc(desc);
    }
}
