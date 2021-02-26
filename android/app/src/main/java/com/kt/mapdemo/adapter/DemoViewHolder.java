package com.kt.mapdemo.adapter;


import android.content.Intent;
import android.view.View;

import com.kt.mapdemo.base.BaseModel;
import com.kt.mapdemo.base.BaseViewHolder;
import com.kt.mapdemo.databinding.ViewDemoCellBinding;

public class DemoViewHolder extends BaseViewHolder {
    private DemoModel model;
    private ViewDemoCellBinding binding;

    public DemoViewHolder(ViewDemoCellBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.demoCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(itemView.getContext(), model.getActivityClass());
                itemView.getContext().startActivity(it);
            }
        });
    }


    @Override
    public void setModel(BaseModel model) {
        if (model instanceof DemoModel) {
            this.model = (DemoModel) model;
            binding.setData(this.model);
            binding.executePendingBindings();
        }
    }
}
