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

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FeatureView extends FrameLayout {

	public FeatureView(Context context) {
		super(context);

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.feature, this);
	}

	public synchronized void setTitleId(int titleId) {
		((TextView) (findViewById(R.id.title))).setText(titleId);
	}

	public synchronized void setDescriptionId(int descriptionId) {
		((TextView) (findViewById(R.id.description))).setText(descriptionId);
	}

}
