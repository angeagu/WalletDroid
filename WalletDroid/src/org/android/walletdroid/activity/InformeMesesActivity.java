package org.android.walletdroid.activity;

import java.util.Calendar;

import org.android.walletdroid.bbdd.ManagerBBDD;
import org.android.walletdroid.utils.CrearCelda;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class InformeMesesActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
        	
        	super.onCreate(savedInstanceState);
        	//setContentView(R.layout.main);
        	//this.setContentView(R.id.tablaMensual);
        	
        	ManagerBBDD managerbbdd = ManagerBBDD.getInstance(this);
    		SQLiteDatabase bbdd = managerbbdd.getReadableDatabase();
        	
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
        	int numeroaño = c.get(Calendar.YEAR);
        	Log.d("InformeMesesActivity", "Mes Actual: " + numeromes);
        	float importeTotal = 0.0f;
        	
        	for (int i=1;i<=numeromes;i++) {
        	
        		TableRow filaMes = new TableRow(this);
        		//Cursor cursor = (Cursor) bbdd.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        		Float importe = new Float (0);
        		Cursor cursor = (Cursor) bbdd.rawQuery("SELECT SUM(IMPORTE) FROM FACTURAS WHERE FECHA LIKE '%-%"+ i +"-"+numeroaño+"'" , null);
        		while (cursor.moveToNext()) {
        			importe = cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(0)));
        			String stringMes = "";
        			switch (i) {
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
        			TextView celdaMes = CrearCelda.getCeldaTablaFacturas(this);
        			celdaMes.setText(stringMes);
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
        	
        	setContentView(tablaMensual);
        }
        catch (Exception ex) {
        	Log.e("InformeMesesActivity", "Excepcion: " + ex.toString());
        	Log.e("InformeMesesActivity", "Mensaje: " + ex.getMessage());
        }
     }
    

}
