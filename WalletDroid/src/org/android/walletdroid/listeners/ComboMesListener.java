package org.android.walletdroid.listeners;


import org.android.walletdroid.R;
import org.android.walletdroid.activity.WalletDroidActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ComboMesListener implements OnItemSelectedListener {
	
	private Context ctx; 
	/*
	 Para evitar el problema de que el onItemSelectedListener se dispara al
	posicionar el combo en el layout, definimos un contador, que impide que
	se ejecute código la primera vez que se posiciona el listener. 
	*/
	public int count = 0; 
	
	public ComboMesListener (Context c) {
		ctx = c;
		count = 0;
	} 
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		  
		  if (count > 0 && parent.getId() == R.id.comboMeses) {
			//alimentoId = ((ObjetosClase) parent.getItemAtPosition(pos)).getId();
			String fechaSeleccionada = (String)parent.getItemAtPosition(pos);
			String[] tokens = fechaSeleccionada.split(" ");
			String mesValue = tokens[0];
			int año = Integer.valueOf(tokens[2]);
			int mes = 0;
			if (mesValue.equalsIgnoreCase("Enero")) mes = 1;
			else if (mesValue.equalsIgnoreCase("Febrero")) mes = 2;
			else if (mesValue.equalsIgnoreCase("Marzo")) mes = 3;
			else if (mesValue.equalsIgnoreCase("Abril")) mes = 4;
			else if (mesValue.equalsIgnoreCase("Mayo")) mes = 5;
			else if (mesValue.equalsIgnoreCase("Junio")) mes = 6;
			else if (mesValue.equalsIgnoreCase("Julio")) mes = 7;
			else if (mesValue.equalsIgnoreCase("Agosto")) mes = 8;
			else if (mesValue.equalsIgnoreCase("Septiembre")) mes = 9;
			else if (mesValue.equalsIgnoreCase("Octubre")) mes = 10;
			else if (mesValue.equalsIgnoreCase("Noviembre")) mes = 11;
			else if (mesValue.equalsIgnoreCase("Diciembre")) mes = 12;
			
			
			Intent i = new Intent(ctx,WalletDroidActivity.class);
			i.putExtra("mes", mes);
			i.putExtra("año", año);
			ctx.startActivity(i);
			
		  }
		  count++;//Incrementamos el contador para que los cambios en el combo se pasen al onItemSelected
		
		
		
	}
	public void onNothingSelected(AdapterView<?> parent) {
		// Do nothing.
	}
	
	
	

}
