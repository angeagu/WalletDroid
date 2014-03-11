package com.kursea.walletdroid.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.kursea.walletdroid.R;

import com.kursea.walletdroid.activity.NuevaFacturaActivity;
import com.kursea.walletdroid.bbdd.ManagerBBDD;
import com.kursea.walletdroid.listener.TextViewFacturaOnClickListener;
import com.kursea.walletdroid.utils.CrearCelda;
import com.kursea.walletdroid.utils.VentanaAlerta;
import com.kursea.walletdroid.utils.WalletDroidDateUtils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TabMonthFragment extends Fragment {
    
	ManagerBBDD managerbbdd;	
	SQLiteDatabase bbdd;
	int mesActual = 0;
    int anoActual = 0;
    View v;
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    	
    	try {
    		final Context ctx = this.getActivity().getApplicationContext();
    		v = inflater.inflate(R.layout.tab_month, container, false);
    		//v = this.getActivity().findViewById(R.id.tab1);
    		
            Bundle extras = this.getActivity().getIntent().getExtras();
            
            
            String mesString = "";
            String etiquetaCombo = "";
            boolean mostrarTabFacturas = false;
            
    		if(extras!=null){
    			//Recogemos los valores seleccionados en el SELECT de meses.
    			//Comprobamos si volvemos desde Buscar Facturas.
            	//Si volvemos desde ahí, el tab a mostrar es el 2.
            	mostrarTabFacturas = extras.getBoolean("mostrarTabFacturas");
            	if (mostrarTabFacturas==false) {
            		//No volvemos desde el Tab de Buscar Facturas
            		mesActual = extras.getInt("mes");
            		anoActual = extras.getInt("ano");
            		mesString = WalletDroidDateUtils.monthToString(ctx,mesActual);
            		etiquetaCombo = mesString + " " + String.valueOf(anoActual);
            	}
            	else {
            		//Entramos desde el Tab de Buscar Facturas. Mostramos las facturas
            		//del mes actual.
            		Calendar cal = Calendar.getInstance();
                	mesActual = cal.get(cal.MONTH) + 1; 
                	anoActual = cal.get(cal.YEAR);
                	mesString = WalletDroidDateUtils.monthToString(ctx,mesActual);
                	etiquetaCombo = mesString + " " + String.valueOf(anoActual);
            	}
    		} else {
    			// No entramos desde el Select de Meses, mostramos las facturas del mes actual.
    			Calendar cal = Calendar.getInstance();
            	mesActual = cal.get(cal.MONTH) + 1; 
            	anoActual = cal.get(cal.YEAR);
            	mesString = WalletDroidDateUtils.monthToString(ctx,mesActual);
            	etiquetaCombo = mesString + " " + String.valueOf(anoActual);

    		}
            
            managerbbdd = ManagerBBDD.getInstance(this.getActivity().getApplicationContext());
            managerbbdd.poblarBaseDatos();
            bbdd = managerbbdd.getReadableDatabase();
            
            
            if (v.findViewById(R.id.vista_principal) == null) {
            	
            	//Definimos el listener del Botón Nuevo Registro.
            	Button botonNuevoRegistro = (Button) v.findViewById(R.id.BotonNuevoRegistro);
            	botonNuevoRegistro.setOnClickListener(new OnClickListener() {
            		public void onClick(View v) {
            			       			
            			Intent i = new Intent(TabMonthFragment.this.getActivity(),NuevaFacturaActivity.class);
            			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            			//startActivityForResult(i, 1);
            			startActivity(i);
            			
            		}
            	});
            	
            	//Definimos el listener del Botón Anterior
            	Button botonAnterior = (Button) v.findViewById(R.id.BotonAnterior);
            	botonAnterior.setOnClickListener(new OnClickListener() {
            		public void onClick(View v) {
            			 mesActual = mesActual -1;
            			 if (mesActual == 0) {
            				 mesActual = 12;
            				 anoActual = anoActual - 1;
            			 }
            			 VentanaAlerta.mostrarAlerta(TabMonthFragment.this.getActivity().getApplicationContext(),
            					 ctx.getResources().getString(R.string.BalanceMes) + WalletDroidDateUtils.monthToString(ctx, mesActual) + "-" + anoActual);
            			 TabMonthFragment.this.actualizarListaFacturas(ctx,mesActual,anoActual);       			
            		}
            	});
            	
            	//Definimos el listener del Botón Siguiente
            	Button botonSiguiente = (Button) v.findViewById(R.id.BotonSiguiente);
            	botonSiguiente.setOnClickListener(new OnClickListener() {
            		public void onClick(View v) {
            			 mesActual = mesActual +1;
            			 if (mesActual == 13) {
            				 mesActual = 1;
            				 anoActual = anoActual + 1;
            			 }
            			 VentanaAlerta.mostrarAlerta(TabMonthFragment.this.getActivity().getApplicationContext(), ctx.getResources().getString(R.string.BalanceMes) + WalletDroidDateUtils.monthToString(ctx,mesActual) + "-" + anoActual);
            			 TabMonthFragment.this.actualizarListaFacturas(ctx,mesActual,anoActual);        			
            		}
            	});
            	
            	//ACTUALIZAMOS LA LISTA DE FACTURAS
            	actualizarListaFacturas(ctx,mesActual,anoActual);
            	
            	//Poblamos el comboBox.
            	Spinner comboBox = (Spinner)v.findViewById(R.id.comboMeses);
            	HashMap meses = new HashMap();
            	Cursor cursor = (Cursor) bbdd.rawQuery("SELECT FECHA FROM FACTURAS ORDER BY FECHA DESC" , null);
        		while (cursor.moveToNext()) {
        			String fechaValue = cursor.getString(cursor.getColumnIndex("FECHA"));
        			String[] tokens = fechaValue.split("-");
        			int dia = Integer.parseInt(tokens[0]); 
        			int mes = Integer.parseInt(tokens[1]);
        			int ano = Integer.parseInt(tokens[2]);
        			String stringMes = "";
        			stringMes = WalletDroidDateUtils.monthToString(ctx,mes);
        			    			
        			meses.put(stringMes + ano, ano + " " + stringMes);
        			
        		}
        		
        		//Anadimos también el mes y el ano actual.
        		meses.put(mesString + anoActual, anoActual + " " + mesString);
        		
        		List<String> listaMeses = new ArrayList<String>();
        		Collection c = meses.values();
        		Iterator<String> it = c.iterator();
        		while (it.hasNext()) {
        			listaMeses.add(it.next());
        		}
        		
        		//Ordenamos los meses del combo
        		Collections.sort(listaMeses);
        		
        		//Creamos el adaptador
        		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(TabMonthFragment.this.getActivity(), android.R.layout.simple_spinner_item, listaMeses);
        		//Anadimos el layout para el menú y se lo damos al spinner
        		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        		comboBox.setAdapter(spinner_adapter);
        		comboBox.setOnItemSelectedListener(new ComboMesListener(TabMonthFragment.this.getActivity()));
        		comboBox.setSelection(spinner_adapter.getPosition(etiquetaCombo));
        		
        		
            }
          }
          catch (Exception e) {
            	Log.e("TabMonthFragment", "Excepcion: " + e.toString());
            	Log.e("TabMonthFragment", "Mensaje: " + e.getMessage());
            	e.printStackTrace();
          }

    	 return v;
        
    }


    private void actualizarListaFacturas(Context ctx, int mesActual,int anoActual) {

 	   	//Obtenemos el TextView de Mes Actual:
 	   	TextView tViewMesActual = (TextView)v.findViewById(R.id.mesActualValue);
 	   	tViewMesActual.setText(WalletDroidDateUtils.monthToString(ctx,mesActual).toUpperCase() + " " + anoActual);
 	   
    		//Obtenemos los registros para la tabla.
 	   //Primero obtenemos la tabla de datos
    		TableLayout tablaFacturas = (TableLayout)v.findViewById(R.id.tablaFacturas);
    		tablaFacturas.removeAllViews();
    		//TableLayout tablaFacturas = new TableLayout(this);
    		//Construimos la fila de encabezado.
    		TableRow filaEncabezado = new TableRow(TabMonthFragment.this.getActivity());
    		TextView concepto = CrearCelda.getCeldaEncabezado(TabMonthFragment.this.getActivity().getApplicationContext());
    		concepto.setText(ctx.getResources().getString(R.string.Concepto)+"  ");
    		TextView fecha = CrearCelda.getCeldaEncabezado(TabMonthFragment.this.getActivity());
    		fecha.setText(ctx.getResources().getString(R.string.Fecha)+"  ");
    		TextView ingreso = CrearCelda.getCeldaEncabezado(TabMonthFragment.this.getActivity());
    		ingreso.setText(ctx.getResources().getString(R.string.Ingreso)+"  ");
    		TextView gasto = CrearCelda.getCeldaEncabezado(TabMonthFragment.this.getActivity());
    		gasto.setText(ctx.getResources().getString(R.string.Gasto)+"  ");
    		//Anado los TextViews a la fila de encabezado.
    		filaEncabezado.addView(concepto);
    		filaEncabezado.addView(fecha);
    		filaEncabezado.addView(ingreso);
    		filaEncabezado.addView(gasto);
    		//Anado la fila de encabezado a la tabla.
    		tablaFacturas.addView(filaEncabezado);
    	
    		//Obtenemos las facturas desde la base de datos:
    		SQLiteDatabase bbdd = managerbbdd.getReadableDatabase();
    		//Cursor cursor = (Cursor) bbdd.query("FACTURAS", new String[]{"CONCEPTO","IMPORTE","FECHA"}, "", null, null, null, null);
    		//String query = "SELECT ID_RECIBO,CONCEPTO,IMPORTE,FECHA FROM FACTURAS WHERE FECHA LIKE '%-%" + mesActual + "-"+anoActual+"%' ORDER BY FECHA ASC";
    		String query = "SELECT ID_RECIBO,CONCEPTO,IMPORTE,FECHA FROM FACTURAS WHERE FECHA LIKE '%-" + mesActual + "-"+anoActual+"' ORDER BY FECHA ASC";
    		
    		Cursor cursor = (Cursor) bbdd.rawQuery(query , null);
    		float totalImportes = 0;
    		TextView celda = CrearCelda.getCeldaTablaFacturas(TabMonthFragment.this.getActivity().getApplicationContext());
    	
    		while (cursor.moveToNext()) {
    			//Anadimos el concepto.
    			TableRow filaFactura = new TableRow(TabMonthFragment.this.getActivity().getApplicationContext());
    			String conceptoValue = cursor.getString(cursor.getColumnIndex("CONCEPTO"));
    			boolean newline = false;
    			if (conceptoValue.indexOf(" ")>0) {
    				conceptoValue = conceptoValue.replaceAll(" ", System.getProperty("line.separator"));
    				newline = true;
    			}
    			celda.setText(conceptoValue + "  ");
    			//Anadimos el listener al textview de concepto
    			celda.setOnClickListener(TextViewFacturaOnClickListener.getInstance());
    			//Anadimos la primary key de ese recibo.
    			int id_recibo  = cursor.getInt(cursor.getColumnIndex("ID_RECIBO"));
    			celda.setTag(new Integer(id_recibo));
    			filaFactura.addView(celda);
    			
    			//Anadimos la Fecha
    			celda = CrearCelda.getCeldaTablaFacturas(TabMonthFragment.this.getActivity().getApplicationContext());
    			String fechaValue = cursor.getString(cursor.getColumnIndex("FECHA"));
    			if (newline)
    				fechaValue = fechaValue + "\n ";
    				celda.setText(fechaValue+ "  ");
    				filaFactura.addView(celda);
    				celda = CrearCelda.getCeldaTablaFacturas(TabMonthFragment.this.getActivity().getApplicationContext());
    				Float importe = cursor.getFloat(cursor.getColumnIndex("IMPORTE"));
    				totalImportes = totalImportes + importe.floatValue();
    				if (importe.compareTo(new Float(0)) > 0) {
    					String importeValue = importe.toString();
    					if (newline)
    						importeValue = importeValue + "\n ";
    					celda.setText(importeValue + "  ");
    					filaFactura.addView(celda);
    					celda = CrearCelda.getCeldaTablaFacturas(TabMonthFragment.this.getActivity().getApplicationContext());
    					if (newline)
    						celda.setText("\n ");
    					else
    						celda.setText("");
    					filaFactura.addView(celda);
    					celda = CrearCelda.getCeldaTablaFacturas(TabMonthFragment.this.getActivity().getApplicationContext());
    				}
    				else {
    					if (newline)
    						celda.setText("\n ");
    					else
    						celda.setText("");
    					filaFactura.addView(celda);
    					celda = CrearCelda.getCeldaTablaFacturas(TabMonthFragment.this.getActivity().getApplicationContext());
    					String importeValue = importe.toString();
    					if (newline)
    						importeValue = importeValue + "\n ";
    					celda.setText( importeValue + "  ");
    					filaFactura.addView(celda);
    					celda = CrearCelda.getCeldaTablaFacturas(TabMonthFragment.this.getActivity().getApplicationContext());
    				}
    		
    				tablaFacturas.addView(filaFactura);
    		}
    	
    		//Anadimos la fila de totales
    		TableRow filaTotales = new TableRow(TabMonthFragment.this.getActivity().getApplicationContext());
    		celda = CrearCelda.getCeldaTotales(TabMonthFragment.this.getActivity().getApplicationContext());
    		celda.setText(ctx.getResources().getString(R.string.Total)+" ");
    		filaTotales.addView(celda);
    		celda = CrearCelda.getCeldaTotales(TabMonthFragment.this.getActivity().getApplicationContext());
    		filaTotales.addView(celda);
    		celda = CrearCelda.getCeldaTotales(TabMonthFragment.this.getActivity().getApplicationContext());
    		filaTotales.addView(celda);
    		celda = CrearCelda.getCeldaTotales(TabMonthFragment.this.getActivity().getApplicationContext());
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
     	se ejecute código la primera vez que se posiciona el listener. 
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
     			String mesValue = tokens[1];
     			int ano = Integer.valueOf(tokens[0]);
     			int mes = 0;
     			mes = WalletDroidDateUtils.montToInt(mesValue);
     			mesActual = mes;
     			anoActual = ano;
     			actualizarListaFacturas(ctx,mes,ano);
     			
     		  }
     		  count++;//Incrementamos el contador para que los cambios en el combo se pasen al onItemSelected

     		
     	}
     	public void onNothingSelected(AdapterView<?> parent) {
     		// Do nothing.
     	}

     }
    
}
