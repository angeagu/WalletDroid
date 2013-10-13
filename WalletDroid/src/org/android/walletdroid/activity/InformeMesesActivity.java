package org.android.walletdroid.activity;

import java.util.Calendar;

import org.android.walletdroid.R;
import org.android.walletdroid.bbdd.ManagerBBDD;
import org.android.walletdroid.listener.TextViewMesOnClickListener;
import org.android.walletdroid.utils.CrearCelda;
import org.android.walletdroid.utils.UserInterfaceComponent;
import org.android.walletdroid.utils.VentanaAlerta;
import org.android.walletdroid.utils.WalletDroidDateUtils;

import android.app.Activity;
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

public class InformeMesesActivity extends Activity {
	/** Called when the activity is first created. */
	
	Calendar cal = Calendar.getInstance(); 
	int a�oActual = cal.get(cal.YEAR);
	ManagerBBDD managerbbdd;
	SQLiteDatabase bbdd;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
        	
        	super.onCreate(savedInstanceState);
        	
        	managerbbdd = ManagerBBDD.getInstance(this);
    		bbdd = managerbbdd.getReadableDatabase();
        	actualizarListaFacturasA�o(a�oActual);
    		
    		
        }
        catch (Exception ex) {
        	Log.e("InformeMesesActivity", "Excepcion: " + ex.toString());
        	Log.e("InformeMesesActivity", "Mensaje: " + ex.getMessage());
        }
     }
    

    private void actualizarListaFacturasA�o(int a�o) {
    	LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(1); //Vertical
		
		//A�adimos un separador
		View v = UserInterfaceComponent.createSeparator(this);
		linearLayout.addView(v);
		
		//Creamos la cabecera de A�o Actual.
		TextView textViewA�o = CrearCelda.getTextViewCabecera(this);
		textViewA�o.setText("A�o Actual: " + a�oActual);
		linearLayout.addView(textViewA�o);
		
		//A�adimos un separador.
		v = UserInterfaceComponent.createSeparator(this);
		linearLayout.addView(v);
		
    	//Creamos la tabla de meses.
    	//TableLayout tablaMensual = (TableLayout) this.findViewById(R.id.tablaMensual);
    	TableLayout tablaMensual = new TableLayout(this);
    	
    	TableRow filaEncabezado = new TableRow(this);
    	TextView mes = CrearCelda.getCeldaEncabezado(this);
    	mes.setText("  MES  ");
    	TextView total = CrearCelda.getCeldaEncabezado(this);
    	total.setText("   TOTAL  ");
    	filaEncabezado.addView(mes);
    	filaEncabezado.addView(total);
    	tablaMensual.addView(filaEncabezado);
    	
    	Calendar c = Calendar.getInstance();
    	int numeromes = c.get(Calendar.MONTH) + 1;
    	int numeroa�o = a�o;
    	Log.d("InformeMesesActivity", "Mes Actual: " + numeromes);
    	float importeTotal = 0.0f;
    	
    	for (int i=1;i<=numeromes;i++) {
    	
    		TableRow filaMes = new TableRow(this);
    		//Cursor cursor = (Cursor) bbdd.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    		Float importe = new Float (0);
    		Cursor cursor = (Cursor) bbdd.rawQuery("SELECT SUM(IMPORTE) FROM FACTURAS WHERE FECHA LIKE '%-%"+ i +"-"+numeroa�o+"'" , null);
    		while (cursor.moveToNext()) {
    			importe = cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(0)));
    			String stringMes = "";
    			stringMes = WalletDroidDateUtils.monthToString(i);
    			TextView celdaMes = CrearCelda.getCeldaTablaFacturas(this);
    			celdaMes.setText(stringMes);
    			//A�adimos el listener para el TextView de Mes.
    			celdaMes.setOnClickListener(TextViewMesOnClickListener.getInstance());
    			celdaMes.setTag(stringMes+"-"+a�o);
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
    	celdaTotales1.setText("TOTAL ANUAL:   ");
    	filaTotales.addView(celdaTotales1);
    	TextView celdaTotales2 = CrearCelda.getCeldaTotales(this);
    	celdaTotales2.setText("   " + importeTotal);
    	filaTotales.addView(celdaTotales2);
    	tablaMensual.addView(filaTotales);
    	
    	linearLayout.addView(tablaMensual);
    	
    	LinearLayout filaBotones = new LinearLayout(this);
    	linearLayout.setOrientation(1); //Horizontal.
    	
    	//Definimos el listener del Bot�n Anterior
    	Button botonAnterior = new Button(this);
    	botonAnterior.setText("A�o Anterior");
    	botonAnterior.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			 a�oActual = a�oActual - 1;
    			 VentanaAlerta.mostrarAlerta(getApplicationContext(), "Balance A�o: " + a�oActual);
    			 InformeMesesActivity.this.actualizarListaFacturasA�o(a�oActual);       			
    		}
    	});
    	
    	//Definimos el listener del Bot�n Siguiente
    	Button botonSiguiente = new Button(this);
    	botonSiguiente.setText("A�o Siguiente");
    	botonSiguiente.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			 a�oActual = a�oActual + 1;
    			 VentanaAlerta.mostrarAlerta(getApplicationContext(), "Balance A�o: " + a�oActual);
    			 InformeMesesActivity.this.actualizarListaFacturasA�o(a�oActual);        			
    		}
    	});
    	
    	filaBotones.addView(botonAnterior);
    	filaBotones.addView(botonSiguiente);
    	
    	linearLayout.addView(filaBotones);
    	
    	setContentView(linearLayout);
    	
    }
    
}
