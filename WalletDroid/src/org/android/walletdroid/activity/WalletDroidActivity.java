package org.android.walletdroid.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.android.walletdroid.R;
import org.android.walletdroid.bbdd.ManagerBBDD;
import org.android.walletdroid.utils.CrearCelda;
import org.android.walletdroid.utils.VentanaAlerta;
import org.android.walletdroid.utils.WalletDroidDateUtils;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class WalletDroidActivity extends TabActivity {
    /** Called when the activity is first created. */
	
	ManagerBBDD managerbbdd;	
	SQLiteDatabase bbdd;
	int mesActual = 0;
    int a�oActual = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
        
        Bundle extras = getIntent().getExtras();
        
        String mesString = "";
        String etiquetaCombo = "";
        boolean mostrarTabFacturas = false;
        
		if(extras!=null){
			//Recogemos los valores seleccionados en el SELECT de meses.
			//Comprobamos si volvemos desde Buscar Facturas.
        	//Si volvemos desde ah�, el tab a mostrar es el 2.
        	mostrarTabFacturas = extras.getBoolean("mostrarTabFacturas");
        	if (mostrarTabFacturas==false) {
        		//No volvemos desde el Tab de Buscar Facturas
        		mesActual = extras.getInt("mes");
        		a�oActual = extras.getInt("a�o");
        		mesString = WalletDroidDateUtils.monthToString(mesActual);
        		etiquetaCombo = mesString + " " + String.valueOf(a�oActual);
        	}
        	else {
        		//Entramos desde el Tab de Buscar Facturas. Mostramos las facturas
        		//del mes actual.
        		Calendar cal = Calendar.getInstance();
            	mesActual = cal.get(cal.MONTH) + 1; 
            	a�oActual = cal.get(cal.YEAR);
            	mesString = WalletDroidDateUtils.monthToString(mesActual);
            	etiquetaCombo = mesString + " " + String.valueOf(a�oActual);
        	}
		} else {
			// No entramos desde el Select de Meses, mostramos las facturas del mes actual.
			Calendar cal = Calendar.getInstance();
        	mesActual = cal.get(cal.MONTH) + 1; 
        	a�oActual = cal.get(cal.YEAR);
        	mesString = WalletDroidDateUtils.monthToString(mesActual);
        	etiquetaCombo = mesString + " " + String.valueOf(a�oActual);

		}
        	
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        managerbbdd = ManagerBBDD.getInstance(this);
        managerbbdd.poblarBaseDatos();
        bbdd = managerbbdd.getReadableDatabase();
        
        
        // Invocamos setup() sobre el TabHost.
    	TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
    	tabHost.setup(getLocalActivityManager());
    	
    	TabSpec primeraPestana = tabHost.newTabSpec("mes").setIndicator("Mes").setContent(R.id.tab1);
    	Intent tabMesActivityGroup = new Intent(WalletDroidActivity.this,TabMesActivityGroup.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	TabSpec segundaPestana = tabHost.newTabSpec("a�o").setIndicator("A�o").setContent(tabMesActivityGroup);
    	Intent tabBuscadorActivityGroup = new Intent(WalletDroidActivity.this,TabBuscadorActivityGroup.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	TabSpec terceraPestana = tabHost.newTabSpec("buscadorFacturas").setIndicator("Buscar Facturas").setContent(tabBuscadorActivityGroup);
    	
    	tabHost.addTab(primeraPestana);
    	tabHost.addTab(segundaPestana);
    	tabHost.addTab(terceraPestana);
    	
    	if (!mostrarTabFacturas) {
    		//Si no volvemos desde el Tab de BuscarFacturas, ponemos el tab de meses, el inicial
    		tabHost.setCurrentTab(0);
    	}
    	else {
    		//Sivolvemos desde el Tab de BuscarFacturas, ponemos el tab de Buscar Facturas
    		tabHost.setCurrentTab(2);
    	}
        
        if (findViewById(R.id.vista_principal) == null) {
        	if (savedInstanceState != null) {
                //Volvemos desde un estado anterior y no necesitamos hacer nada
        		return;
            }
        	
        	//Definimos el listener del Bot�n Nuevo Registro.
        	Button botonNuevoRegistro = (Button) this.findViewById(R.id.BotonNuevoRegistro);
        	botonNuevoRegistro.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			       			
        			Intent i = new Intent(WalletDroidActivity.this,NuevaFacturaActivity.class);
        			//startActivityForResult(i, 1);
        			startActivity(i);
        			
        		}
        	});
        	
        	//Definimos el listener del Bot�n Anterior
        	Button botonAnterior = (Button) this.findViewById(R.id.BotonAnterior);
        	botonAnterior.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			 mesActual = mesActual -1;
        			 if (mesActual == 0) {
        				 mesActual = 12;
        				 a�oActual = a�oActual - 1;
        			 }
        			 VentanaAlerta.mostrarAlerta(getApplicationContext(), "Balance Mes: " + WalletDroidDateUtils.monthToString(mesActual) + "-" + a�oActual);
        			 WalletDroidActivity.this.actualizarListaFacturas(mesActual,a�oActual);       			
        		}
        	});
        	
        	//Definimos el listener del Bot�n Siguiente
        	Button botonSiguiente = (Button) this.findViewById(R.id.BotonSiguiente);
        	botonSiguiente.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			 mesActual = mesActual +1;
        			 if (mesActual == 13) {
        				 mesActual = 1;
        				 a�oActual = a�oActual + 1;
        			 }
        			 VentanaAlerta.mostrarAlerta(getApplicationContext(), "Balance Mes: " + WalletDroidDateUtils.monthToString(mesActual) + "-" + a�oActual);
        			 WalletDroidActivity.this.actualizarListaFacturas(mesActual,a�oActual);        			
        		}
        	});
        	
        	//ACTUALIZAMOS LA LISTA DE FACTURAS
        	actualizarListaFacturas(mesActual,a�oActual);
        	
        	//Poblamos el comboBox.
        	Spinner comboBox = (Spinner)this.findViewById(R.id.comboMeses);
        	HashMap meses = new HashMap();
        	Cursor cursor = (Cursor) bbdd.rawQuery("SELECT FECHA FROM FACTURAS ORDER BY FECHA DESC" , null);
    		while (cursor.moveToNext()) {
    			String fechaValue = cursor.getString(cursor.getColumnIndex("FECHA"));
    			String[] tokens = fechaValue.split("-");
    			int dia = Integer.parseInt(tokens[0]); 
    			int mes = Integer.parseInt(tokens[1]);
    			int a�o = Integer.parseInt(tokens[2]);
    			String stringMes = "";
    			stringMes = WalletDroidDateUtils.monthToString(mes);
    			    			
    			meses.put(stringMes + a�o, stringMes + " " + a�o);
    			
    		}
    		
    		List<String> listaMeses = new ArrayList<String>();
    		Collection c = meses.values();
    		Iterator<String> it = c.iterator();
    		while (it.hasNext()) {
    			listaMeses.add(it.next());
    		}
    		//A�adimos tambi�n el mes y el a�o actual.
    		listaMeses.add(mesString + " " + a�oActual);
    		
    		//Creamos el adaptador
    		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaMeses);
    		//A�adimos el layout para el men� y se lo damos al spinner
    		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		comboBox.setAdapter(spinner_adapter);
    		comboBox.setOnItemSelectedListener(new ComboMesListener(WalletDroidActivity.this));
    		comboBox.setSelection(spinner_adapter.getPosition(etiquetaCombo));
        }
      }
      catch (Exception e) {
        	Log.e("PetProjectActivity", "Excepcion: " + e.toString());
        	Log.e("PetProjectActivity", "Mensaje: " + e.getMessage());
        	e.printStackTrace();
      }
    }
    


   private void actualizarListaFacturas(int mesActual,int a�oActual) {

	   	//Obtenemos el TextView de Mes Actual:
	   	TextView tViewMesActual = (TextView)this.findViewById(R.id.mesActualValue);
	   	tViewMesActual.setText(WalletDroidDateUtils.monthToString(mesActual).toUpperCase() + " " + a�oActual);
	   
   		//Obtenemos los registros para la tabla.
	   //Primero obtenemos la tabla de datos
   		TableLayout tablaFacturas = (TableLayout) this.findViewById(R.id.tablaFacturas);
   		tablaFacturas.removeAllViews();
   		//TableLayout tablaFacturas = new TableLayout(this);
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
   		//A�ado los TextViews a la fila de encabezado.
   		filaEncabezado.addView(concepto);
   		filaEncabezado.addView(fecha);
   		filaEncabezado.addView(ingreso);
   		filaEncabezado.addView(gasto);
   		//A�ado la fila de encabezado a la tabla.
   		tablaFacturas.addView(filaEncabezado);
   	
   		//Obtenemos las facturas desde la base de datos:
   		SQLiteDatabase bbdd = managerbbdd.getReadableDatabase();
   		//Cursor cursor = (Cursor) bbdd.query("FACTURAS", new String[]{"CONCEPTO","IMPORTE","FECHA"}, "", null, null, null, null);
   		String query = "SELECT CONCEPTO,IMPORTE,FECHA FROM FACTURAS WHERE FECHA LIKE '%-%" + mesActual + "-"+a�oActual+"%' ORDER BY FECHA ASC";
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
   	
   		//A�adimos la fila de totales
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
   	
   }
    
   private class ComboMesListener implements OnItemSelectedListener {
    	
    	private Context ctx; 
    	/*
    	 Para evitar el problema de que el onItemSelectedListener se dispara al
    	posicionar el combo en el layout, definimos un contador, que impide que
    	se ejecute c�digo la primera vez que se posiciona el listener. 
    	*/
    	public int count = 0; 
    	
    	public ComboMesListener (Context c) {
    		ctx = c;
    		count = 0;
    	} 
    	
    	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
    		  
    		  if (count > 0 && parent.getId() == R.id.comboMeses) {
    			String fechaSeleccionada = (String)parent.getItemAtPosition(pos);
    			String[] tokens = fechaSeleccionada.split(" ");
    			String mesValue = tokens[0];
    			int a�o = Integer.valueOf(tokens[1]);
    			int mes = 0;
    			mes = WalletDroidDateUtils.montToInt(mesValue);
    			mesActual = mes;
    			a�oActual = a�o;
    			actualizarListaFacturas(mes,a�o);
    			
    		  }
    		  count++;//Incrementamos el contador para que los cambios en el combo se pasen al onItemSelected
    		
    		
    		
    	}
    	public void onNothingSelected(AdapterView<?> parent) {
    		// Do nothing.
    	}

    }
    
}