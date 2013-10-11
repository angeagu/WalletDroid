package org.android.walletdroid.activity;

import org.android.walletdroid.R;
import org.android.walletdroid.bbdd.ManagerBBDD;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class NuevaFacturaActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.form);
        
			/*
			//Ponemos el menú de nuevo registro, el Fragment.
			MenuNuevoRegistroFragment menuNuevoRegistro = new MenuNuevoRegistroFragment();
			menuNuevoRegistro.setArguments(getIntent().getExtras());
    	
			// Añadimos el fragmento al 'fragment_container' Linear Layout
			getSupportFragmentManager().beginTransaction()
               .add(R.id.vista_principal, menuNuevoRegistro).commit();
			*/
			
			//Definimos el listener del Botón Guardar Factura
        	Button botonGuardarFactura = (Button) this.findViewById(R.id.BotonGuardarFactura);
        	botonGuardarFactura.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			
        			//Guardamos la factura en la BBDD
        			guardarFactura(v.getContext());
        			
        			//Invocamos a la actividad principal para que vuelva a mostrar
        			//la lista de facturas.
        			Intent i = new Intent(NuevaFacturaActivity.this,WalletDroidActivity.class);
        			//startActivityForResult(i, 1);
        			startActivity(i);
        			
        			
        		}
        	});
		}
		catch (Exception e) {
        	Log.e("NuevaFacturaActivity", "Excepcion: " + e.toString());
        	Log.e("NuevaFacturaActivity", "Mensaje: " + e.getMessage());
        	e.printStackTrace();
      }
	}
	
	private void guardarFactura(Context context) {
		try {
			EditText conceptoEditText = (EditText)this.findViewById(R.id.TextoConceptoFactura);
			DatePicker fechaPicker = (DatePicker)this.findViewById(R.id.FechaFactura);
			EditText importeEditText = (EditText)this.findViewById(R.id.TextoImporteFactura);
		
			String concepto = conceptoEditText.getText().toString();
			String fecha = fechaPicker.getDayOfMonth() + "-" + 
						(fechaPicker.getMonth()+1) + "-" +
						fechaPicker.getYear();
			float importe = Float.parseFloat(importeEditText.getText().toString());
		
			ManagerBBDD manager = ManagerBBDD.getInstance(context);
			manager.addFactura(concepto, fecha, importe);
		}
		catch (Exception e) {
			Log.e("NuevaFacturaActivity.guardarFactura()", "Excepcion: " + e.toString());
        	Log.e("NuevaFacturaActivity.guardarFactura()", "Mensaje: " + e.getMessage());
        	e.printStackTrace();
		}
	}
}
