package org.android.walletdroid.utils;

import android.content.Context;
import android.widget.Toast;

public class VentanaAlerta {

	public static void mostrarAlerta(Context ctx, String mensaje) {
		Toast toast = Toast.makeText(ctx,mensaje, Toast.LENGTH_SHORT);
		toast.show();
	}
}
