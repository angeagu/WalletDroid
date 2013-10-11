package org.android.walletdroid.activity;

import org.android.walletdroid.bbdd.ManagerBBDD;
import org.android.walletdroid.utils.Constants;
import org.android.walletdroid.utils.CrearCelda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BuscadorFacturasActivity extends Activity {
	 
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        try {
	        	
	        	super.onCreate(savedInstanceState);
	        	
	    		LinearLayout linearLayout = new LinearLayout(this);
	    		linearLayout.setOrientation(LinearLayout.VERTICAL);
	    		TextView tView = new TextView(this);
	    		tView.setText("Buscar Factura: ");
	    		EditText editText = new EditText(this);
	    		editText.setId(Constants.idEditTextBusqueda);
	    		Button botonBuscar = new Button(this);
	    		botonBuscar.setText("Buscar...");
	    		botonBuscar.setOnClickListener(new OnClickListener() {
	        		public void onClick(View v) {
	        			buscarFacturas(v.getContext());
	        			//Intent i = new Intent(BuscadorFacturasActivity.this,ResultadosBusquedaActivity.class);
	        			//i.putExtra("concepto", concepto);
	        			//startActivityForResult(i, 1);
	        			//startActivity(i);
	        			
	        		}
	        	});
	    		
	    		linearLayout.addView(tView);
	    		linearLayout.addView(editText);
	    		linearLayout.addView(botonBuscar);
	    		
	    		setContentView(linearLayout);
	    		
	        }
	        catch (Exception ex) {
	        	Log.e("BuscadorFacturasActivity", "Excepcion: " + ex.toString());
	        	Log.e("BuscadorFacturasActivity", "Mensaje: " + ex.getMessage());
	        }
	     }
		
		private void buscarFacturas (Context ctx) {
			
			LinearLayout linearLayout = new LinearLayout(this);
    		linearLayout.setOrientation(LinearLayout.VERTICAL);

        	ManagerBBDD managerbbdd = ManagerBBDD.getInstance(this);
        	SQLiteDatabase bbdd = managerbbdd.getReadableDatabase();
        	
        	// Recogemos el concepto por el que se desea buscar.
        	EditText editText = (EditText)this.findViewById(Constants.idEditTextBusqueda);
        	 String textoBusqueda = editText.getText().toString();
        	// Tabla de datos.
        	TableLayout tablaFacturas = new TableLayout(this);
		
        	//Construimos la fila de encabezado.
        	TableRow filaEncabezado = new TableRow(this);
        	TextView concepto = CrearCelda.getCeldaEncabezado(this);
        	concepto.setText("Concepto  ");
        	TextView fecha = CrearCelda.getCeldaEncabezado(this);
        	fecha.setText("Fecha  ");
        	TextView ingreso = CrearCelda.getCeldaEncabezado(this);
        	ingreso.setText("Ingreso  ");
        	TextView gasto = CrearCelda.getCeldaEncabezado(this);
        	gasto.setText("Gasto  ");
        	//Añado los TextViews a la fila de encabezado.
        	filaEncabezado.addView(concepto);
        	filaEncabezado.addView(fecha);
        	filaEncabezado.addView(ingreso);
        	filaEncabezado.addView(gasto);
        	//Añado la fila de encabezado a la tabla.
        	tablaFacturas.addView(filaEncabezado);
        	
        	//Lanzamos la consulta.
        	String query = "SELECT CONCEPTO,IMPORTE,FECHA FROM FACTURAS WHERE CONCEPTO LIKE '%" + textoBusqueda + "%'";
        	Cursor cursor = (Cursor) bbdd.rawQuery(query , null);
        	float totalImportes = 0;
        	TextView celda = CrearCelda.getCeldaTablaFacturas(this);
        	
        	while (cursor.moveToNext()) {
        		TableRow filaFactura = new TableRow(this);
        		String conceptoValue = cursor.getString(cursor.getColumnIndex("CONCEPTO"));
        		boolean newline = false;
        		if (conceptoValue.indexOf(" ")>0) {
        			conceptoValue = conceptoValue.replaceAll(" ", System.getProperty("line.separator"));
        			newline = true;
        		}
        		celda.setText(conceptoValue + "  ");
        		filaFactura.addView(celda);
        		celda = CrearCelda.getCeldaTablaFacturas(this);
        		String fechaValue = cursor.getString(cursor.getColumnIndex("FECHA"));
        		if (newline)
        			fechaValue = fechaValue + "\n ";
        		celda.setText(fechaValue+ "  ");
        		filaFactura.addView(celda);
        		celda = CrearCelda.getCeldaTablaFacturas(this);
        		Float importe = cursor.getFloat(cursor.getColumnIndex("IMPORTE"));
        		totalImportes = totalImportes + importe.floatValue();
        		if (importe.compareTo(new Float(0)) > 0) {
        			String importeValue = importe.toString();
        			if (newline)
        				importeValue = importeValue + "\n ";
        			celda.setText(importeValue + "  ");
        			filaFactura.addView(celda);
        			celda = CrearCelda.getCeldaTablaFacturas(this);
        			if (newline)
        				celda.setText("\n ");
        			else
        				celda.setText("");
        			filaFactura.addView(celda);
        			celda = CrearCelda.getCeldaTablaFacturas(this);
        		}
        		else {
        			if (newline)
        				celda.setText("\n ");
        			else
        				celda.setText("");
        			filaFactura.addView(celda);
        			celda = CrearCelda.getCeldaTablaFacturas(this);
        			String importeValue = importe.toString();
        			if (newline)
        				importeValue = importeValue + "\n ";
        			celda.setText( importeValue + "  ");
        			filaFactura.addView(celda);
        			celda = CrearCelda.getCeldaTablaFacturas(this);
        		}
        		
        		tablaFacturas.addView(filaFactura);
        	}
        	
        	//Añadimos la fila de totales
        	TableRow filaTotales = new TableRow(this);
        	celda = CrearCelda.getCeldaTotales(this);
        	celda.setText("TOTAL: ");
        	filaTotales.addView(celda);
        	celda = CrearCelda.getCeldaTotales(this);
        	filaTotales.addView(celda);
        	celda = CrearCelda.getCeldaTotales(this);
        	filaTotales.addView(celda);
        	celda = CrearCelda.getCeldaTotales(this);
        	celda.setText(String.valueOf(totalImportes));
        	if (totalImportes > 0)
        		celda.setTextColor(Color.GREEN);
        	else 
        		celda.setTextColor(Color.RED);
        	filaTotales.addView(celda);
        	tablaFacturas.addView(filaTotales);
    		
    		
        	Button botonVolver = new Button(this);
    		botonVolver.setText("Volver");
    		botonVolver.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			//buscarFacturas(v.getContext());
        			Intent i = new Intent(BuscadorFacturasActivity.this,BuscadorFacturasActivity.class);
        			//i.putExtra("concepto", concepto);
        			//startActivity(i);
        			//startActivityForResult(i, 1);
        			startActivity(i);
        			
        			
        		}
        	});
    		linearLayout.addView(tablaFacturas);
    		linearLayout.addView(botonVolver);
    		setContentView(linearLayout);
			
		}

}
