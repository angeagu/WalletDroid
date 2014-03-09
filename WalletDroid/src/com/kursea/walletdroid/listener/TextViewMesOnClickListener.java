package com.kursea.walletdroid.listener;


import com.kursea.walletdroid.activity.ActionBarWalletDroidActivity;
import com.kursea.walletdroid.bbdd.ManagerBBDD;
import com.kursea.walletdroid.utils.WalletDroidDateUtils;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class TextViewMesOnClickListener implements OnClickListener {

	private static TextViewMesOnClickListener listener;
	
	public static TextViewMesOnClickListener getInstance() {
		
		if (listener==null)
			listener = new TextViewMesOnClickListener();
		
		return listener;
	}
	
    public void onClick(View v) {
    	//Obtenemos el ano y el mes.
    	String mesAnoValue = (String)v.getTag();
    	String[] tokens = mesAnoValue.split("-");
    	String mes = tokens[0];
    	String ano = tokens[1];
    	
    	
    	//Nuevo Intent para invocar WalletDroidActivity.
    	Intent i = new Intent(v.getContext(),ActionBarWalletDroidActivity.class);
    	//Paso de parámetros e invocación de la actividad.
    	i.putExtra("mes", WalletDroidDateUtils.montToInt(mes));
    	i.putExtra("ano",Integer.parseInt(ano));
    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	//i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    	v.getContext().startActivity(i);
    	
    }
	
}