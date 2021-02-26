package com.kt.mapdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kt.mapdemo.adapter.DemoListAdapter
import com.kt.mapdemo.databinding.ActivityMainBinding

public class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var adapter: DemoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindView()
    }


    // dataBinding
    private fun bindView() {
        adapter = DemoListAdapter(DemoList.makeTestModel())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.apply {
            demoList.adapter = adapter
            demoList.apply {
                addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
            }
        }
    }


}