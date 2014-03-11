package com.kursea.walletdroid.activity;

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
import android.widget.LinearLayout;

import com.kursea.walletdroid.R;
import com.kursea.walletdroid.bbdd.ManagerBBDD;
import com.kursea.walletdroid.utils.VentanaAlerta;

public class NuevaFacturaActivity extends Activity {

	boolean updateFactura = false; //Indica si debemos hacer update de una factura o no.
	int id_recibo = 0; //Indica el id del Recibo que queremos actualizar.
	
	public void onCreate(Bundle savedInstanceState) {
		
		Bundle extras = getIntent().getExtras();
		
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.form);	
			Button botonEliminarFactura = (Button) this.findViewById(R.id.BotonEliminarFactura);
			
			if (extras!=null) {
				
				//Estamos tratando de actualizar una factura.
				updateFactura=true;
				
				LinearLayout formulario = (LinearLayout)this.findViewById(R.id.vista_principal);
				
				id_recibo = extras.getInt("id_recibo");
				String concepto = extras.getString("concepto"); 
				String fecha = extras.getString("fecha");
				Float importe = extras.getFloat("importe");

				//Establecemos el valor del concepto.

				EditText etConcepto = (EditText) this.findViewById(R.id.TextoConceptoFactura);
				etConcepto.setText(concepto);
				etConcepto.clearFocus();
				
				//Establecemos la fecha
				DatePicker datePicker = (DatePicker) this.findViewById(R.id.FechaFactura);
				String[] tokens = fecha.split("-");
    			int dia = Integer.parseInt(tokens[0]); 
    			int mes = Integer.parseInt(tokens[1]);
    			int ano = Integer.parseInt(tokens[2]);
				datePicker.updateDate(ano, mes-1, dia);
				etConcepto.setText(concepto);
				
				//Establecemos el valor del importe.
				EditText etImporte = (EditText) this.findViewById(R.id.TextoImporteFactura);
				etImporte.setText(importe.toString());
				
				
	        	botonEliminarFactura.setEnabled(true);
				
			}
			else {
				//Nueva Factura. 
				
				//Deshabilitamos el botón de borrar factura.
				botonEliminarFactura.setEnabled(false);
			}
        
			
			//Definimos el listener del Botón Guardar Factura
        	Button botonGuardarFactura = (Button) this.findViewById(R.id.BotonGuardarFactura);
        	botonGuardarFactura.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			        			    
        			if (guardarFactura(v.getContext())) {
        			//Si la factura se ha guardado correctamente, 
        			//invocamos a la actividad principal para que vuelva a mostrar
        			//la lista de facturas.
        			//Intent i = new Intent(NuevaFacturaActivity.this,WalletDroidActivity.class);
        				Intent intent = new Intent(NuevaFacturaActivity.this,ActionBarWalletDroidActivity.class);
        				//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        				//startActivityForResult(i, 1);
        				startActivity(intent);
        			
        			}
        			
        		}
        	});
        	
        	//Definimos el listener del Botón Guardar Factura
        	botonEliminarFactura.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			        			    
        			if (eliminarFactura(v.getContext())) {
        				//Si la factura se ha eliminado correctamente, 
        				//invocamos a la actividad principal para que vuelva a mostrar
        				//la lista de facturas.
        				//Intent i = new Intent(NuevaFacturaActivity.this,WalletDroidActivity.class);
        				Intent intent = new Intent(NuevaFacturaActivity.this,ActionBarWalletDroidActivity.class);
        				//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        				//startActivityForResult(i, 1);
        				startActivity(intent);
        			
        			}
        			
        		}
        	});
        	
		}
		catch (Exception e) {
        	Log.e("NuevaFacturaActivity", "Excepcion: " + e.toString());
        	Log.e("NuevaFacturaActivity", "Mensaje: " + e.getMessage());
        	e.printStackTrace();
      }
	}
	
	private boolean guardarFactura(Context context) {
		float importe=0;
		String errores = "";
		boolean guardadoCorrecto = false;
		
		try {
			
			EditText conceptoEditText = (EditText)this.findViewById(R.id.TextoConceptoFactura);
			DatePicker fechaPicker = (DatePicker)this.findViewById(R.id.FechaFactura);
			EditText importeEditText = (EditText)this.findViewById(R.id.TextoImporteFactura);
		
			String concepto = conceptoEditText.getText().toString();
			String fecha = fechaPicker.getDayOfMonth() + "-" + 
						(fechaPicker.getMonth()+1) + "-" +
						fechaPicker.getYear();
			try {
				importe = Float.parseFloat(importeEditText.getText().toString());
			}
			catch (NumberFormatException ex) {
				errores += context.getResources().getString(R.string.errorFormatoNumero) + System.getProperty("line.separator"); 
			}
			if (concepto.length()==0) {
				errores += context.getResources().getString(R.string.conceptoNoVacio) + System.getProperty("line.separator");
			}
			
			if (errores.length()==0) {
				//No hay errores. Actualizamos.
				ManagerBBDD manager = ManagerBBDD.getInstance(context);
				if (updateFactura==false) {
					//Anadimos nueva factura.
					manager.addFactura(concepto, fecha, importe);
				}
				else { 
					manager.updateFactura(id_recibo,concepto, fecha, importe);
				}
				guardadoCorrecto=true;
			}
			else {
				VentanaAlerta.mostrarAlerta(context, errores);
				guardadoCorrecto=false;
			}
			
			
		}
		catch (Exception e) {
			Log.e("NuevaFacturaActivity.guardarFactura()", "Excepcion: " + e.toString());
        	Log.e("NuevaFacturaActivity.guardarFactura()", "Mensaje: " + e.getMessage());
        	e.printStackTrace();
		}
		return guardadoCorrecto;
	}
	
	
	private boolean eliminarFactura(Context context) {
		float importe=0;
		boolean guardadoCorrecto = false;
		
		try {
			
			EditText conceptoEditText = (EditText)this.findViewById(R.id.TextoConceptoFactura);
			DatePicker fechaPicker = (DatePicker)this.findViewById(R.id.FechaFactura);
			EditText importeEditText = (EditText)this.findViewById(R.id.TextoImporteFactura);
		
			String concepto = conceptoEditText.getText().toString();
			String fecha = fechaPicker.getDayOfMonth() + "-" + 
						(fechaPicker.getMonth()+1) + "-" +
						fechaPicker.getYear();
						
			//No hay errores. Actualizamos.
			ManagerBBDD manager = ManagerBBDD.getInstance(context);
			manager.eliminarFactura(id_recibo);
			guardadoCorrecto=true;
					
		}
		catch (Exception e) {
			Log.e("NuevaFacturaActivity.guardarFactura()", "Excepcion: " + e.toString());
        	Log.e("NuevaFacturaActivity.guardarFactura()", "Mensaje: " + e.getMessage());
        	e.printStackTrace();
		}
		return guardadoCorrecto;
	}
}
