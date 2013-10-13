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
    	//Obtenemos el a�o y el mes.
    	String mesA�oValue = (String)v.getTag();
    	String[] tokens = mesA�oValue.split("-");
    	String mes = tokens[0];
    	String a�o = tokens[1];
    	
    	
    	//Nuevo Intent para invocar WalletDroidActivity.
    	Intent i = new Intent(v.getContext(),WalletDroidActivity.class);
    	//Paso de par�metros e invocaci�n de la actividad.
    	i.putExtra("mes", WalletDroidDateUtils.montToInt(mes));
    	i.putExtra("a�o",Integer.parseInt(a�o));
    	v.getContext().startActivity(i);
    	
    }
	
}