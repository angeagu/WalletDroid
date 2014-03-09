package com.kursea.walletdroid.activity;

import java.util.Calendar;

import org.android.walletdroid.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

public class InformeMesesActivity extends Activity {
	/** Called when the activity is first created. */
	
	Calendar cal = Calendar.getInstance(); 
	int anoActual = cal.get(cal.YEAR);
	ManagerBBDD managerbbdd;
	SQLiteDatabase bbdd;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
        	
        	super.onCreate(savedInstanceState);
        	
        	managerbbdd = ManagerBBDD.getInstance(this);
    		bbdd = managerbbdd.getReadableDatabase();
        	actualizarListaFacturasAno(anoActual);
    		
    		
        }
        catch (Exception ex) {
        	Log.e("InformeMesesActivity", "Excepcion: " + ex.toString());
        	Log.e("InformeMesesActivity", "Mensaje: " + ex.getMessage());
        }
     }
    

    private void actualizarListaFacturasAno(int ano) {
    	
    	Context ctx = this.getApplicationContext();
    	
    	LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(1); //Vertical
		
		//Anadimos un separador
		View v = UserInterfaceComponent.createSeparator(this);
		linearLayout.addView(v);
		
		//Creamos la cabecera de Ano Actual.
		TextView textViewAno = CrearCelda.getTextViewCabecera(this);
		textViewAno.setText(R.string.anoActual + anoActual);
		linearLayout.addView(textViewAno);
		
		//Anadimos un separador.
		v = UserInterfaceComponent.createSeparator(this);
		linearLayout.addView(v);
		
    	//Creamos la tabla de meses.
    	//TableLayout tablaMensual = (TableLayout) this.findViewById(R.id.tablaMensual);
    	TableLayout tablaMensual = new TableLayout(this);
    	
    	TableRow filaEncabezado = new TableRow(this);
    	TextView mes = CrearCelda.getCeldaEncabezado(this);
    	mes.setText("  "+R.string.Mes+"  ");
    	TextView total = CrearCelda.getCeldaEncabezado(this);
    	total.setText("   "+R.string.Total+"  ");
    	filaEncabezado.addView(mes);
    	filaEncabezado.addView(total);
    	tablaMensual.addView(filaEncabezado);
    	
    	Calendar c = Calendar.getInstance();
    	int numeromes = c.get(Calendar.MONTH) + 1;
    	int numeroano = ano;
    	Log.d("InformeMesesActivity", "Mes Actual: " + numeromes);
    	float importeTotal = 0.0f;
    	
    	for (int i=1;i<=numeromes;i++) {
    	
    		TableRow filaMes = new TableRow(this);
    		//Cursor cursor = (Cursor) bbdd.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    		Float importe = new Float (0);
    		Cursor cursor = (Cursor) bbdd.rawQuery("SELECT SUM(IMPORTE) FROM FACTURAS WHERE FECHA LIKE '%-%"+ i +"-"+numeroano+"'" , null);
    		while (cursor.moveToNext()) {
    			importe = cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(0)));
    			String stringMes = "";
    			stringMes = WalletDroidDateUtils.monthToString(ctx,i);
    			TextView celdaMes = CrearCelda.getCeldaTablaFacturas(this);
    			celdaMes.setText(stringMes);
    			//Anadimos el listener para el TextView de Mes.
    			celdaMes.setOnClickListener(TextViewMesOnClickListener.getInstance());
    			celdaMes.setTag(stringMes+"-"+ano);
    			filaMes.addView(celdaMes);
    			
    		}
			
    		TextView celdaImporte = CrearCelda.getCeldaTablaFacturas(this);
			celdaImporte.setText("   " + String.valueOf(importe));
			filaMes.addView(celdaImporte);
			tablaMensual.addView(filaMes);
			importeTotal = importeTotal + importe.floatValue();
    	}
    	
    	TableRow filaTotales = new TableRow(this);
    	TextView celdaTotales1 = CrearCelda.getCeldaTotales(this);
    	celdaTotales1.setText(R.string.totalAnual+" :   ");
    	filaTotales.addView(celdaTotales1);
    	TextView celdaTotales2 = CrearCelda.getCeldaTotales(this);
    	celdaTotales2.setText("   " + importeTotal);
    	filaTotales.addView(celdaTotales2);
    	tablaMensual.addView(filaTotales);
    	
    	linearLayout.addView(tablaMensual);
    	
    	LinearLayout filaBotones = new LinearLayout(this);
    	linearLayout.setOrientation(1); //Horizontal.
    	
    	//Definimos el listener del Botón Anterior
    	Button botonAnterior = new Button(this);
    	botonAnterior.setText(R.string.anoAnterior);
    	botonAnterior.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			 anoActual = anoActual - 1;
    			 VentanaAlerta.mostrarAlerta(getApplicationContext(), R.string.BalanceAno+" " + anoActual);
    			 InformeMesesActivity.this.actualizarListaFacturasAno(anoActual);       			
    		}
    	});
    	
    	//Definimos el listener del Botón Siguiente
    	Button botonSiguiente = new Button(this);
    	botonSiguiente.setText(R.string.anoSiguiente);
    	botonSiguiente.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			 anoActual = anoActual + 1;
    			 VentanaAlerta.mostrarAlerta(getApplicationContext(), R.string.BalanceAno+" "  + anoActual);
    			 InformeMesesActivity.this.actualizarListaFacturasAno(anoActual);        			
    		}
    	});
    	
    	filaBotones.addView(botonAnterior);
    	filaBotones.addView(botonSiguiente);
    	
    	linearLayout.addView(filaBotones);
    	
    	setContentView(linearLayout);
    	
    }
    
}
