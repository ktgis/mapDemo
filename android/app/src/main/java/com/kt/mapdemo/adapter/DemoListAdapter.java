package com.kt.mapdemo.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kt.mapdemo.base.BaseModel;
import com.kt.mapdemo.base.BaseViewHolder;
import com.kt.mapdemo.databinding.ViewDemoCellBinding;
import com.kt.mapdemo.databinding.ViewHeaderDemoListBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DemoListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final int DemoCellType = 0;
    private final int HeaderCellType = -1;

    private List<BaseModel> demoList;

    public DemoListAdapter(){
        demoList = new ArrayList<BaseModel>();
    }

    public DemoListAdapter(List<BaseModel> demoList) {
        this.demoList = demoList;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            ViewDemoCellBinding binding = ViewDemoCellBinding.inflate(inflater, parent, false);
            return new DemoViewHolder(binding);
        } else {
            ViewHeaderDemoListBinding binding = ViewHeaderDemoListBinding.inflate(inflater, parent, false);
            return new HeaderDemoViewHolder(binding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(demoList.get(position) instanceof DemoModel) {
            return DemoCellType;
        } else {
            return HeaderCellType;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setModel(demoList.get(position));
    }

    @Override
    public int getItemCount() {
        return demoList.size();
    }
}
