package org.android.walletdroid.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.android.walletdroid.R;
import org.android.walletdroid.bbdd.ManagerBBDD;
import org.android.walletdroid.listeners.ComboMesListener;
import org.android.walletdroid.utils.CrearCelda;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class WalletDroidActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
        	
        Bundle extras = getIntent().getExtras();
        int mesActual = 0;
        int añoActual = 0;
		if(extras!=null){
			//Recogemos los valores seleccionados en el SELECT de meses.
			mesActual = extras.getInt("mes");
			añoActual = extras.getInt("año");
		} else {
			// No entramos desde el Select de Meses, mostramos las facturas del mes actual.
			Calendar cal = Calendar.getInstance();
        	mesActual = cal.get(cal.MONTH) + 1; 
        	añoActual = cal.get(cal.YEAR);
		}
        	
        ManagerBBDD managerbbdd = ManagerBBDD.getInstance(this);	
        managerbbdd.poblarBaseDatos();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Invocamos setup() sobre el TabHost.
    	TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
    	tabHost.setup(getLocalActivityManager());
    	
    	TabSpec primeraPestana = tabHost.newTabSpec("mes").setIndicator("Mes").setContent(R.id.tab1);
    	Intent tabMesActivityGroup = new Intent(WalletDroidActivity.this,TabMesActivityGroup.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	TabSpec segundaPestana = tabHost.newTabSpec("año").setIndicator("Año").setContent(tabMesActivityGroup);
    	Intent tabBuscadorActivityGroup = new Intent(WalletDroidActivity.this,TabBuscadorActivityGroup.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	TabSpec terceraPestana = tabHost.newTabSpec("buscadorFacturas").setIndicator("Buscar Facturas").setContent(tabBuscadorActivityGroup);
    	
    	tabHost.addTab(primeraPestana);
    	tabHost.addTab(segundaPestana);
    	tabHost.addTab(terceraPestana);
    	
    	tabHost.setCurrentTab(0);
        
        if (findViewById(R.id.vista_principal) == null) {
        	if (savedInstanceState != null) {
                //Volvemos desde un estado anterior y no necesitamos hacer nada
        		return;
            }
        	
        	//Definimos el listener del Botón Nuevo Registro.
        	Button botonNuevoRegistro = (Button) this.findViewById(R.id.BotonNuevoRegistro);
        	botonNuevoRegistro.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			       			
        			Intent i = new Intent(WalletDroidActivity.this,NuevaFacturaActivity.class);
        			//startActivityForResult(i, 1);
        			startActivity(i);
        			
        		}
        	});
        	
        	//Obtenemos los registros para la tabla.
        	//Primero obtenemos la tabla de datos
        	TableLayout tablaFacturas = (TableLayout) this.findViewById(R.id.tablaFacturas);
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
        	
        	//Obtenemos las facturas desde la base de datos:
        	SQLiteDatabase bbdd = managerbbdd.getReadableDatabase();
        	//Cursor cursor = (Cursor) bbdd.query("FACTURAS", new String[]{"CONCEPTO","IMPORTE","FECHA"}, "", null, null, null, null);
        	String query = "SELECT CONCEPTO,IMPORTE,FECHA FROM FACTURAS WHERE FECHA LIKE '%-%" + mesActual + "-"+añoActual+"%' ORDER BY FECHA ASC";
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
        	
        	//Poblamos el comboBox.
        	Spinner comboBox = (Spinner)this.findViewById(R.id.comboMeses);
        	HashMap meses = new HashMap();
        	cursor = (Cursor) bbdd.rawQuery("SELECT FECHA FROM FACTURAS ORDER BY FECHA DESC" , null);
    		while (cursor.moveToNext()) {
    			String fechaValue = cursor.getString(cursor.getColumnIndex("FECHA"));
    			String[] tokens = fechaValue.split("-");
    			int dia = Integer.parseInt(tokens[0]); 
    			int mes = Integer.parseInt(tokens[1]);
    			int año = Integer.parseInt(tokens[2]);
    			String stringMes = "";
    			switch (mes) {
				case 1 : stringMes += "Enero  "; break;
				case 2 : stringMes += "Febrero  "; break;
				case 3 : stringMes += "Marzo  "; break;
				case 4 : stringMes += "Abril  "; break;
				case 5 : stringMes += "Mayo  "; break;
				case 6 : stringMes += "Junio  "; break;
				case 7 : stringMes += "Julio  "; break;
				case 8 : stringMes += "Agosto  "; break;
				case 9 : stringMes += "Septiembre  "; break;
				case 10 : stringMes += "Octubre  "; break;
				case 11 : stringMes += "Noviembre  "; break;
				case 12 : stringMes += "Diciembre  "; break;
				
    			}
    			
    			meses.put(stringMes + año, stringMes + año);
    			
    		}
    		
    		List<String> listaMeses = new ArrayList<String>();
    		Collection c = meses.values();
    		Iterator<String> it = c.iterator();
    		while (it.hasNext()) {
    			listaMeses.add(it.next());
    		}
    		
    		//Creamos el adaptador
    		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaMeses);
    		//Añadimos el layout para el menú y se lo damos al spinner
    		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		comboBox.setAdapter(spinner_adapter);
    		comboBox.setOnItemSelectedListener(new ComboMesListener(WalletDroidActivity.this));
    		
        }
      }
      catch (Exception e) {
        	Log.e("PetProjectActivity", "Excepcion: " + e.toString());
        	Log.e("PetProjectActivity", "Mensaje: " + e.getMessage());
        	e.printStackTrace();
      }
    }
}