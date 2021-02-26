package com.kt.mapdemo.adapter;


import com.kt.mapdemo.base.BaseModel;
import com.kt.mapdemo.base.BaseViewHolder;
import com.kt.mapdemo.databinding.ViewHeaderDemoListBinding;


public class HeaderDemoViewHolder extends BaseViewHolder {
    private ViewHeaderDemoListBinding binding;
    public HeaderDemoViewHolder(ViewHeaderDemoListBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void setModel(BaseModel model) {
        if (model instanceof HeaderModel) {
            binding.setModel((HeaderModel) model);
        }
    }
}
