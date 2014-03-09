package com.kursea.walletdroid.fragment;

import java.lang.reflect.Field;

import com.kursea.walletdroid.R;

import com.kursea.walletdroid.activity.ActionBarWalletDroidActivity;
import com.kursea.walletdroid.bbdd.ManagerBBDD;
import com.kursea.walletdroid.listener.TextViewFacturaOnClickListener;
import com.kursea.walletdroid.utils.Constants;
import com.kursea.walletdroid.utils.CrearCelda;
import com.kursea.walletdroid.utils.UserInterfaceComponent;
import com.kursea.walletdroid.utils.VentanaAlerta;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TabSearchFragment extends DialogFragment {
    
    LinearLayout linearLayout;
    View v;
    
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    	
    	try {
    		
    		v = inflater.inflate(R.layout.tab_search, container, false);
    		Context ctx = v.getContext();
    		//linearLayout = new LinearLayout(this.getActivity().getApplicationContext());
    		linearLayout = (LinearLayout)v.findViewById(R.id.tab3);
    		linearLayout.setOrientation(LinearLayout.VERTICAL);
    		linearLayout.setGravity(Gravity.CENTER);
    		
    		//Boton buscar.
    		Button botonBuscar = new Button(this.getActivity().getApplicationContext());
    		botonBuscar.setText(ctx.getResources().getString(R.string.buscar));
    		botonBuscar.setTextColor(Color.BLACK);
    		botonBuscar.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			buscarFacturas(v.getContext());
        			
        		}
        	});
    		linearLayout.addView(botonBuscar);
    		
    		
    		View separador = UserInterfaceComponent.createSeparator(this.getActivity().getApplicationContext());
    		linearLayout.addView(separador);
    		
    		//Etiqueta
    		TextView tView = new TextView(this.getActivity().getApplicationContext());
    		tView.setText(ctx.getResources().getString(R.string.buscar_por_concepto));
    		tView.setTextColor(Color.BLACK);
    		linearLayout.addView(tView);
    		
    		separador = UserInterfaceComponent.createSeparator(this.getActivity().getApplicationContext());
    		linearLayout.addView(separador);
    		
    		//Campo editable para buscar la factura.
    		EditText editText = new EditText(this.getActivity().getApplicationContext());
    		editText.setId(Constants.idEditTextBusqueda);
    		editText.setTextColor(Color.BLACK);
    		linearLayout.addView(editText);
    		
    		separador = UserInterfaceComponent.createSeparator(this.getActivity().getApplicationContext());
    		linearLayout.addView(separador);
    		
    		//Filtros de fecha
    		TableLayout tablaFiltrosFechas = new TableLayout(this.getActivity().getApplicationContext());
        	tablaFiltrosFechas.setStretchAllColumns(true);
        	tablaFiltrosFechas.setGravity(Gravity.CENTER);
        	
        	View searchDateFiltersView = inflater.inflate(R.layout.searchdatefilters, container, false);
        	DatePicker pickerInicio = (DatePicker) searchDateFiltersView.findViewById(R.id.datePickerFechaInicio);
            try {
                Field f[] = pickerInicio.getClass().getDeclaredFields();
                for (Field field : f) {
                	if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
                        field.setAccessible(true);
                        Object datePicker = new Object();
                        datePicker = field.get(pickerInicio);
                        ((View) datePicker).setVisibility(View.GONE);
                    }
                }
            } 
            catch (SecurityException e) {
                Log.d("ERROR", e.getMessage());
            } 
            catch (IllegalArgumentException e) {
                Log.d("ERROR", e.getMessage());
            } 
            catch (IllegalAccessException e) {
                Log.d("ERROR", e.getMessage());
            }
            
            
            DatePicker pickerFin = (DatePicker) searchDateFiltersView.findViewById(R.id.datePickerFechaFin);
            try {
                Field f[] = pickerFin.getClass().getDeclaredFields();
                for (Field field : f) {               	
                    if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
                        field.setAccessible(true);
                        Object datePicker = new Object();
                        datePicker = field.get(pickerFin);
                        ((View) datePicker).setVisibility(View.GONE);
                    }
                }
            } 
            catch (SecurityException e) {
                Log.d("ERROR", e.getMessage());
            } 
            catch (IllegalArgumentException e) {
                Log.d("ERROR", e.getMessage());
            } 
            catch (IllegalAccessException e) {
                Log.d("ERROR", e.getMessage());
            }
            
        	
        	TableLayout tableSearchFilters = (TableLayout)searchDateFiltersView.findViewById(R.id.tableSearchFilters);
        	linearLayout.addView(tableSearchFilters);

    		
    		
    	}
    	catch (Exception e) {
    		Log.e("TabSearchFragment", "Excepcion: " + e.toString());
        	Log.e("TabSearchFragment", "Mensaje: " + e.getMessage());
        	e.printStackTrace();
    	}
    	return v;
    }

    private void buscarFacturas (Context ctx) {
		
    	boolean hayErrores = false;
    	
    	LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.tab3);
    	
    	/* Recogemos el concepto por el que se desea buscar. Lo ponemos al principio
    	 * porque luego vamos a hacer un removeAllViews para dejar en blanco la vista 
    	 */
    	EditText editText = (EditText)this.getActivity().findViewById(Constants.idEditTextBusqueda);
    	String textoBusqueda = editText.getText().toString();
    	
    	/* Comprobamos si los checkboxes de Fecha Inicio y Fecha Fin
    	 * estan seleccionados, para filtrar por fecha.
    	 */
    	
    	String fechaInicio=null;
    	String fechaFin=null;
    	int mesInicio=0;
    	int añoInicio=0;
    	int mesFin=0;
    	int añoFin=0;
    	CheckBox checkBoxFiltroFechas = (CheckBox)linearLayout.findViewById(R.id.checkBoxFiltroFechas);
    	
    	if (checkBoxFiltroFechas.isChecked()) {
    		//Hay Fecha Inicio
    		TableLayout tableSearchFilters = (TableLayout)linearLayout.findViewById(R.id.tableSearchFilters);
    		TableRow filaFechaInicio = (TableRow)tableSearchFilters.findViewById(R.id.tableRowFechaInicio);
    		DatePicker datePickerFechaInicio = (DatePicker)filaFechaInicio.findViewById(R.id.datePickerFechaInicio);
    		mesInicio=datePickerFechaInicio.getMonth()+1;
    		añoInicio=datePickerFechaInicio.getYear();
    		fechaInicio = datePickerFechaInicio.getDayOfMonth() + "-" + 
    					  mesInicio + "-" +
    					  añoInicio;
    		
    		//Hay Fecha Fin
    		TableRow filaFechaFin = (TableRow)tableSearchFilters.findViewById(R.id.tableRowFechaFin);
    		DatePicker datePickerFechaFin = (DatePicker)filaFechaFin.findViewById(R.id.datePickerFechaFin);
    		mesFin=datePickerFechaFin.getMonth()+1;
    		añoFin=datePickerFechaFin.getYear();
    		fechaFin = datePickerFechaFin.getDayOfMonth() + "-" + 
    					  mesFin + "-" + añoFin;
    		
    	}
    	
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);
		//Borramos los datos buscados anteriormente, si hay.
		linearLayout.removeAllViews();
		
		View separador = UserInterfaceComponent.createSeparator(this.getActivity().getApplicationContext());
		linearLayout.addView(separador);
		
		TableLayout tablaFacturas = new TableLayout(this.getActivity().getApplicationContext());

    	ManagerBBDD managerbbdd = ManagerBBDD.getInstance(this.getActivity().getApplicationContext());
    	SQLiteDatabase bbdd = managerbbdd.getReadableDatabase();
    	
    	//Lanzamos la consulta.
    	String query = "";
    	if (fechaInicio==null && fechaFin==null) {
    		//No se filtra por fecha
    		query = "SELECT ID_RECIBO,CONCEPTO,IMPORTE,FECHA FROM FACTURAS";
    		query+= " WHERE CONCEPTO LIKE '%" + textoBusqueda + "%'";
    		query+= " ORDER BY ID_RECIBO ASC";
    		
    	}
    	else {

    		query = "SELECT ID_RECIBO,CONCEPTO,IMPORTE,FECHA FROM FACTURAS ";
    		query+= " WHERE CONCEPTO LIKE '%" + textoBusqueda + "%'";
    		query+= " AND (";
    		if (mesInicio==mesFin && añoInicio==añoFin) {
    			query+= " FECHA LIKE '%-"+mesInicio+"-"+añoInicio+"%');";
    		}
    		else if (añoInicio>añoFin || ((añoInicio==añoFin)&&(mesInicio>mesFin))) {
    			VentanaAlerta.mostrarAlerta(ctx, ctx.getResources().getString(R.string.errorComprobacionFechas));
    			hayErrores=true;
    		}
    		else {
    			while (mesInicio!=mesFin || añoInicio!=añoFin) {
    				query+= " FECHA LIKE '%-"+mesInicio+"-"+añoInicio+"%' OR ";
    				mesInicio = mesInicio+1;
    				if (mesInicio>12){
    					mesInicio=mesInicio-12;
    					añoInicio=añoInicio+1;
    				}
    			}
    			query+= " FECHA LIKE '%-"+mesInicio+"-"+añoInicio+"%'";
    			query+= " ) ORDER BY ID_RECIBO ASC;";
    		}  		
        	
    	}
		
    	if (!hayErrores) {
    	
    		// Tabla de datos.
        	tablaFacturas.setStretchAllColumns(true);
        	tablaFacturas.setGravity(Gravity.CENTER);
        	
        	//Construimos la fila de encabezado.
        	TableRow filaEncabezado = new TableRow(this.getActivity().getApplicationContext());
        	TextView concepto = CrearCelda.getCeldaEncabezado(this.getActivity().getApplicationContext());
        	concepto.setText(ctx.getResources().getString(R.string.Concepto)+"  ");
        	TextView fecha = CrearCelda.getCeldaEncabezado(this.getActivity().getApplicationContext());
        	fecha.setText(ctx.getResources().getString(R.string.Fecha)+"  ");
        	TextView ingreso = CrearCelda.getCeldaEncabezado(this.getActivity().getApplicationContext());
        	ingreso.setText(ctx.getResources().getString(R.string.Ingreso)+"  ");
        	TextView gasto = CrearCelda.getCeldaEncabezado(this.getActivity().getApplicationContext());
        	gasto.setText(ctx.getResources().getString(R.string.Gasto)+"  ");
        	//Anado los TextViews a la fila de encabezado.
        	filaEncabezado.addView(concepto);
        	filaEncabezado.addView(fecha);
        	filaEncabezado.addView(ingreso);
        	filaEncabezado.addView(gasto);
        	//Anado la fila de encabezado a la tabla.
        	tablaFacturas.addView(filaEncabezado);	
    		
    	Cursor cursor = (Cursor) bbdd.rawQuery(query , null);
    	float totalImportes = 0;
    	TextView celda = CrearCelda.getCeldaTablaFacturas(this.getActivity().getApplicationContext());
    	
    	while (cursor.moveToNext()) {
    		TableRow filaFactura = new TableRow(this.getActivity().getApplicationContext());
    		//Anadimos el concepto
    		String conceptoValue = cursor.getString(cursor.getColumnIndex("CONCEPTO"));
    		boolean newline = false;
    		if (conceptoValue.indexOf(" ")>0) {
    			conceptoValue = conceptoValue.replaceAll(" ", System.getProperty("line.separator"));
    			newline = true;
    		}
    		celda.setText(conceptoValue + "  ");
    		//Anadimos el listener al concepto.
    		celda.setOnClickListener(TextViewFacturaOnClickListener.getInstance());
    		int id_recibo  = cursor.getInt(cursor.getColumnIndex("ID_RECIBO"));
   			celda.setTag(new Integer(id_recibo));
   			
    		filaFactura.addView(celda);
    		
    		//Anadimos la fecha.
    		celda = CrearCelda.getCeldaTablaFacturas(this.getActivity().getApplicationContext());
    		String fechaValue = cursor.getString(cursor.getColumnIndex("FECHA"));
    		if (newline)
    			fechaValue = fechaValue + "\n ";
    		celda.setText(fechaValue+ "  ");
    		filaFactura.addView(celda);
    		
    		//Anadimos el importe.
    		celda = CrearCelda.getCeldaTablaFacturas(this.getActivity().getApplicationContext());
    		Float importe = cursor.getFloat(cursor.getColumnIndex("IMPORTE"));
    		totalImportes = totalImportes + importe.floatValue();
    		if (importe.compareTo(new Float(0)) > 0) {
    			String importeValue = importe.toString();
    			if (newline)
    				importeValue = importeValue + "\n ";
    			celda.setText(importeValue + "  ");
    			filaFactura.addView(celda);
    			celda = CrearCelda.getCeldaTablaFacturas(this.getActivity().getApplicationContext());
    			if (newline)
    				celda.setText("\n ");
    			else
    				celda.setText("");
    			filaFactura.addView(celda);
    			celda = CrearCelda.getCeldaTablaFacturas(this.getActivity().getApplicationContext());
    		}
    		else {
    			if (newline)
    				celda.setText("\n ");
    			else
    				celda.setText("");
    			filaFactura.addView(celda);
    			celda = CrearCelda.getCeldaTablaFacturas(this.getActivity().getApplicationContext());
    			String importeValue = importe.toString();
    			if (newline)
    				importeValue = importeValue + "\n ";
    			celda.setText( importeValue + "  ");
    			filaFactura.addView(celda);
    			celda = CrearCelda.getCeldaTablaFacturas(this.getActivity().getApplicationContext());
    		}
    		
    		tablaFacturas.addView(filaFactura);
    	}
    	
    	//Anadimos la fila de totales
    	TableRow filaTotales = new TableRow(this.getActivity().getApplicationContext());
    	celda = CrearCelda.getCeldaTotales(this.getActivity().getApplicationContext());
    	celda.setText(ctx.getResources().getString(R.string.Total)+" ");
    	filaTotales.addView(celda);
    	celda = CrearCelda.getCeldaTotales(this.getActivity().getApplicationContext());
    	filaTotales.addView(celda);
    	celda = CrearCelda.getCeldaTotales(this.getActivity().getApplicationContext());
    	filaTotales.addView(celda);
    	celda = CrearCelda.getCeldaTotales(this.getActivity().getApplicationContext());
    	celda.setText(String.valueOf(totalImportes));
    	if (totalImportes > 0)
    		celda.setTextColor(Color.GREEN);
    	else 
    		celda.setTextColor(Color.RED);
    	filaTotales.addView(celda);
    	tablaFacturas.addView(filaTotales);
    	
    	}
    	
    	Button botonVolver = new Button(this.getActivity().getApplicationContext());
		botonVolver.setText(ctx.getResources().getString(R.string.volver));
		botonVolver.setTextColor(Color.BLACK);
		botonVolver.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			Intent i = new Intent(TabSearchFragment.this.getActivity(),ActionBarWalletDroidActivity.class);
    			i.putExtra("mostrarTabFacturas",true);
    			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(i);

    		}
    	});
		
		linearLayout.addView(tablaFacturas);
		linearLayout.addView(botonVolver);

		
	}
    
    private String quitarGuionesFecha (String fecha) {
    	String aux = fecha;
    	String[] tokens = aux.split("-");
    	aux = tokens[0] + tokens[1] + tokens[2];
    	return aux;
    	
    }
    
}
