package com.kt.mapdemo.base

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity: AppCompatActivity() {
    //////////////////
    //  menu bar
    //////////////////
    override fun  onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (getMenuLayout() == 0) {
            false
        } else {
            val inflater = menuInflater
            inflater.inflate(getMenuLayout(), menu)
            true
        }
    }

    open fun getMenuLayout(): Int {
        return 0
    }

}