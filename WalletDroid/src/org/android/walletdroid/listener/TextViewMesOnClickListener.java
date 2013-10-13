package org.android.walletdroid.listener;

import org.android.walletdroid.activity.WalletDroidActivity;
import org.android.walletdroid.bbdd.ManagerBBDD;
import org.android.walletdroid.utils.WalletDroidDateUtils;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class TextViewMesOnClickListener implements OnClickListener {

	private static TextViewMesOnClickListener listener;
	private ManagerBBDD managerbbdd;
	
	public static TextViewMesOnClickListener getInstance() {
		
		if (listener==null)
			listener = new TextViewMesOnClickListener();
		
		return listener;
	}
	
    public void onClick(View v) {
    	//Obtenemos el año y el mes.
    	String mesAñoValue = (String)v.getTag();
    	String[] tokens = mesAñoValue.split("-");
    	String mes = tokens[0];
    	String año = tokens[1];
    	
    	
    	//Nuevo Intent para invocar WalletDroidActivity.
    	Intent i = new Intent(v.getContext(),WalletDroidActivity.class);
    	//Paso de parámetros e invocación de la actividad.
    	i.putExtra("mes", WalletDroidDateUtils.montToInt(mes));
    	i.putExtra("año",Integer.parseInt(año));
    	v.getContext().startActivity(i);
    	
    }
	
}