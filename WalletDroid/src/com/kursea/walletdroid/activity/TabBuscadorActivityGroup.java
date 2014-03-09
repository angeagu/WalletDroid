package com.kursea.walletdroid.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TabBuscadorActivityGroup extends ActivityGroup {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);

	      //you can get the local activitymanager to start the new activity

	      View view = getLocalActivityManager()
	                                .startActivity("BuscadorFacturasActivity", new
	      Intent(this,BuscadorFacturasActivity.class)
	                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
	                                .getDecorView();
	       this.setContentView(view);

	   }

	
}
