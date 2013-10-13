package org.android.walletdroid.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class UserInterfaceComponent {

	public static View createSeparator(Context ctx) {
		View v = new View(ctx);
		v.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,15));
		return v;
	}
	
}
