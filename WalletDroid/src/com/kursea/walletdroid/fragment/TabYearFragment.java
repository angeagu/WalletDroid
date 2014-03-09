package com.kursea.walletdroid.fragment;

import java.util.Calendar;

import com.kursea.walletdroid.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kursea.walletdroid.bbdd.ManagerBBDD;
import com.kursea.walletdroid.listener.TextViewMesOnClickListener;
import com.kursea.walletdroid.utils.CrearCelda;
import com.kursea.walletdroid.utils.UserInterfaceComponent;
import com.kursea.walletdroid.utils.VentanaAlerta;
import com.kursea.walletdroid.utils.WalletDroidDateUtils;

public class TabYearFragment extends Fragment {
    
	View v;
	LinearLayout linearLayout;
    Calendar cal = Calendar.getInstance(); 
	int anoActual = cal.get(cal.YEAR);
	ManagerBBDD managerbbdd;
	SQLiteDatabase bbdd;
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    	
    	try {
        
    		Context ctx = this.getActivity().getApplicationContext();
    		v = inflater.inflate(R.layout.tab_year, container, false);
    		linearLayout = (LinearLayout)v.findViewById(R.id.tab2);
    		linearLayout.setOrientation(LinearLayout.VERTICAL);
    		linearLayout.setGravity(Gravity.CENTER);
    		
        	managerbbdd = ManagerBBDD.getInstance(this.getActivity().getApplicationContext());
    		bbdd = managerbbdd.getReadableDatabase();
        	actualizarListaFacturasAno(ctx,anoActual);
    		
        }
        catch (Exception ex) {
        	Log.e("TabYearFragment", "Excepcion: " + ex.toString());
        	Log.e("TabYearFragment", "Mensaje: " + ex.getMessage());
        }
    	
    	
        return linearLayout;
        
    }
    
    private void actualizarListaFacturasAno(final Context ctx,int ano) {
    	
    	linearLayout = (LinearLayout)v.findViewById(R.id.tab2);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);
		//Borramos los datos buscados anteriormente, si hay.
		linearLayout.removeAllViews(); 	
		
		//Anadimos un separador
		View separador = UserInterfaceComponent.createSeparator(this.getActivity().getApplicationContext());
		linearLayout.addView(separador);
		
		//Creamos la cabecera de Ano Actual.
		TextView textViewAno = CrearCelda.getTextViewCabecera(this.getActivity().getApplicationContext());
		textViewAno.setText(ctx.getResources().getString(R.string.anoActual) + anoActual);
		linearLayout.addView(textViewAno);
		
		//Anadimos otro separador
		View separador2 = UserInterfaceComponent.createSeparator(this.getActivity().getApplicationContext());
		linearLayout.addView(separador2);
		
    	//Creamos la tabla de meses.
    	//TableLayout tablaMensual = (TableLayout) this.findViewById(R.id.tablaMensual);
    	TableLayout tablaMensual = new TableLayout(this.getActivity().getApplicationContext());
    	tablaMensual.setStretchAllColumns(true);
    	tablaMensual.setGravity(Gravity.CENTER);
    	
    	TableRow filaEncabezado = new TableRow(this.getActivity().getApplicationContext());
    	TextView mes = CrearCelda.getCeldaEncabezado(this.getActivity().getApplicationContext());
    	mes.setText("  "+ctx.getResources().getString(R.string.Mes)+"  ");
    	TextView total = CrearCelda.getCeldaEncabezado(this.getActivity().getApplicationContext());
    	total.setText("   "+ctx.getResources().getString(R.string.Total)+"  ");
    	filaEncabezado.addView(mes);
    	filaEncabezado.addView(total);
    	tablaMensual.addView(filaEncabezado);
    	
    	Calendar c = Calendar.getInstance();
    	int numeromeses = 12;
    	int numeroano = ano;
    	Log.d("InformeMesesActivity", "Mes Actual: " + numeromeses);
    	float importeTotal = 0.0f;
    	
    	for (int i=1;i<=numeromeses;i++) {
    	
    		TableRow filaMes = new TableRow(this.getActivity().getApplicationContext());
    		//Cursor cursor = (Cursor) bbdd.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    		Float importe = new Float (0);
    		Cursor cursor = (Cursor) bbdd.rawQuery("SELECT SUM(IMPORTE) FROM FACTURAS WHERE FECHA LIKE '%-%"+ i +"-"+numeroano+"'" , null);
    		while (cursor.moveToNext()) {
    			importe = cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(0)));
    			String stringMes = "";
    			stringMes = WalletDroidDateUtils.monthToString(ctx,i);
    			TextView celdaMes = CrearCelda.getCeldaTablaFacturas(this.getActivity().getApplicationContext());
    			celdaMes.setText(stringMes);
    			//Anadimos el listener para el TextView de Mes.
    			celdaMes.setOnClickListener(TextViewMesOnClickListener.getInstance());
    			celdaMes.setTag(stringMes+"-"+ano);
    			filaMes.addView(celdaMes);
    			
    		}
			
    		TextView celdaImporte = CrearCelda.getCeldaTablaFacturas(this.getActivity().getApplicationContext());
			celdaImporte.setText("   " + String.valueOf(importe));
			filaMes.addView(celdaImporte);
			tablaMensual.addView(filaMes);
			importeTotal = importeTotal + importe.floatValue();
    	}
    	
    	TableRow filaTotales = new TableRow(this.getActivity().getApplicationContext());
    	TextView celdaTotales1 = CrearCelda.getCeldaTotales(this.getActivity().getApplicationContext());
    	celdaTotales1.setText(ctx.getResources().getString(R.string.totalAnual)+"   ");
    	filaTotales.addView(celdaTotales1);
    	TextView celdaTotales2 = CrearCelda.getCeldaTotales(this.getActivity().getApplicationContext());
    	celdaTotales2.setText("   " + importeTotal);
    	filaTotales.addView(celdaTotales2);
    	tablaMensual.addView(filaTotales);
    	
    	linearLayout.addView(tablaMensual);
    	
    	LinearLayout filaBotones = new LinearLayout(this.getActivity().getApplicationContext());
    	filaBotones.setOrientation(0); //Horizontal.
    	filaBotones.setGravity(Gravity.CENTER);
    	
    	//Definimos el listener del Botón Anterior
    	Button botonAnterior = new Button(this.getActivity().getApplicationContext());
    	botonAnterior.setText(ctx.getResources().getString(R.string.anoAnterior));
    	botonAnterior.setTextColor(Color.BLACK);
    	botonAnterior.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			 anoActual = anoActual - 1;
    			 VentanaAlerta.mostrarAlerta(v.getContext(), ctx.getResources().getString(R.string.BalanceAno)+" " + anoActual);
    			 TabYearFragment.this.actualizarListaFacturasAno(ctx,anoActual);  
    			 
    		}
    	});
    	
    	//Definimos el listener del Botón Siguiente
    	Button botonSiguiente = new Button(this.getActivity().getApplicationContext());
    	botonSiguiente.setTextColor(Color.BLACK);
    	botonSiguiente.setText(ctx.getResources().getString(R.string.anoSiguiente));
    	botonSiguiente.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			 anoActual = anoActual + 1;
    			 VentanaAlerta.mostrarAlerta(v.getContext(), ctx.getResources().getString(R.string.BalanceAno)+" " + anoActual);
    			 TabYearFragment.this.actualizarListaFacturasAno(ctx,anoActual);   
    			 
    		}
    	});
    	
    	filaBotones.addView(botonAnterior);
    	filaBotones.addView(botonSiguiente);
    	
    	linearLayout.addView(filaBotones);
    	
    	
    }
    
    
}