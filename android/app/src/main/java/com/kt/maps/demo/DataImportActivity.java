/*
 * Copyright (c) 2014 kt corp. All rights reserved.
 *
 * This is a proprietary software of kt corp, and you may not use this file
 * except in compliance with license agreement with kt corp. Any redistribution
 * or use of this software, with or without modification shall be strictly
 * prohibited without prior written approval of kt corp, and the copyright
 * notice above does not evidence any actual or intended publication of such
 * software.
 */
package com.kt.maps.demo;

import java.io.File;
import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kt.maps.GMapShared;
import com.kt.maps.GMapShared.OnImportStateChangeListener;

public class DataImportActivity extends Activity implements OnImportStateChangeListener {

    private EditText rootPathEdit;
    private EditText replaceFilePathEdit;
    private GMapShared mapShared;
    private long timeStart;
    private Stack<Long> handles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tile_data_import);

        rootPathEdit = (EditText) findViewById(R.id.root_dir);
        
        rootPathEdit.setText(Environment.getExternalStorageDirectory().getAbsolutePath() + "/telos/tempDb");
        
        replaceFilePathEdit = (EditText) findViewById(R.id.file_path);;
        handles = new Stack<Long>();
        
        mapShared = GMapShared.getInstance(this);
        mapShared.addOnImportStateChangeListener(this);
        
        findViewById(R.id.start_import).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // invoke import data API
                if (DataImportActivity.this.mapShared == null) {
                    Log.d("DataImportActivity", "GMap is null, doing nothing.");
                } else {

                    File file = new File(rootPathEdit.getText().toString());
                    if (!file.exists() || !file.isDirectory()) {
                        Toast.makeText(DataImportActivity.this, "Directory is wrong!",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    // String[] paths = new String[1];
                    // paths[0] =
                    // "/sdcard/telos/regionData/seoul/regionData_1_2.dat";

                    
                    File importSrc = new File(file, "import_test.db");
                    if (importSrc == null || !importSrc.isFile()) {
                        Log.d("DataImportActivity", "Wrong path for target file :" + importSrc.getAbsolutePath());
                    } else {
                        timeStart = System.currentTimeMillis();
                        long handle = DataImportActivity.this.mapShared.importTileData(importSrc.getAbsolutePath());
                        handles.push(handle);
                    }
                }
            }

        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // invoke cancel import API
                if (DataImportActivity.this.mapShared == null) {
                    Log.d("DataImportActivity", "GMap is null, doing nothing.");
                } else if (!handles.empty()) {
                    long handle = handles.pop();
                    Log.d("DataImportActivity", "Canceling import with handle:" + handle);
                    DataImportActivity.this.mapShared.cancelImport(handle);
                }
            }
        });
        
        findViewById(R.id.start_replace).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                //invoke replace API
                if (DataImportActivity.this.mapShared == null) {
                    Log.d("DataImportActivity", "GMapShared object is null, doing nothing.");
                } else {
                    Log.d("DataImportActivity", "file to replace : " + replaceFilePathEdit.getText().toString());
                    File file = new File(Environment.getExternalStorageDirectory(), replaceFilePathEdit.getText().toString());
                    if (!file.exists() || !file.isFile()) {
                        Toast.makeText(DataImportActivity.this, "file path is wrong!",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    
                    
                    if (mapShared.replaceTileData(file.getAbsolutePath())) {
                        Toast.makeText(DataImportActivity.this, "Replacing DB file done successfully.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DataImportActivity.this, "Replacing DB file failed!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        
        findViewById(R.id.start_clear).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (DataImportActivity.this.mapShared == null) {
                    Log.d("DataImportActivity", "GMapShared object is null, doing nothing.");
                } else {
                    if (DataImportActivity.this.mapShared.clearTileData()) {
                        Toast.makeText(DataImportActivity.this, "Clearing tiles DB done successfully.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DataImportActivity.this, "Clearing tiles DB failed.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        
        // NativeHelper.setActivity(this);
    }
    
    @Override
    protected void onDestroy() {
        this.mapShared.removeOnImportStateChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onProgressUpdate(long handle, int progress, int total) {
        // TODO Auto-generated method stub
        Log.i("DataImportActivity", "--- onProgressUpdate called with handle:" + handle
                + ", progress:" + progress + ", total:" + total);
    }

    @Override
    public void onFinish(long handle, int total) {
        // TODO Auto-generated method stub
        Log.i("DataImportActivity", "--- onFinish called with handle: " + handle + ", total: "
                + total + " elapsed time: " + (System.currentTimeMillis() - timeStart));
        DataImportActivity.this.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                Toast.makeText(DataImportActivity.this, "Done data import successfully. db revision:" + DataImportActivity.this.mapShared.getTileDataRevision(),
                        Toast.LENGTH_LONG).show();
                }
        });
        
    }

    @Override
    public void onError(long handle, int errorCode) {

        switch (errorCode) {
        case GMapShared.OnImportStateChangeListener.ERRORCODE_DB_ERROR:
            Log.i("DataImportActivity", "Db insert fail Error called with handle:" + handle);
            break;
        case GMapShared.OnImportStateChangeListener.ERRORCODE_IO_ERROR:
            Log.i("DataImportActivity", "Disk io fail Error called with handle:" + handle);
            break;
        case GMapShared.OnImportStateChangeListener.ERRORCODE_PARSE_ERROR:
            Log.i("DataImportActivity", "Data file parsing fail Error called with handle:" + handle);
            break;
        default:
            Log.i("DataImportActivity", "Unkown Error called with handle:" + handle
                    + ", errorCode:" + errorCode);
            break;
        }
        DataImportActivity.this.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                Toast.makeText(DataImportActivity.this, "Failed data import.",
                        Toast.LENGTH_LONG).show();
                }
        });
    }
}
