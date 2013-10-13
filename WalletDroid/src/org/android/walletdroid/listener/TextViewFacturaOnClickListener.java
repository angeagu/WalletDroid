package org.android.walletdroid.listener;

import org.android.walletdroid.activity.NuevaFacturaActivity;
import org.android.walletdroid.activity.WalletDroidActivity;
import org.android.walletdroid.bbdd.ManagerBBDD;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;

public class TextViewFacturaOnClickListener implements OnClickListener {

	private static TextViewFacturaOnClickListener listener;
	private ManagerBBDD managerbbdd;
	
	public static TextViewFacturaOnClickListener getInstance() {
		
		if (listener==null)
			listener = new TextViewFacturaOnClickListener();
		
		return listener;
	}
	
    public void onClick(View v) {
        // TODO Auto-generated method stub
    	//Recogemos el id del Recibo.
    	int id_recibo = ((Integer)v.getTag()).intValue();
    	//Obtenemos conexión con la base de datos.
    	managerbbdd = ManagerBBDD.getInstance(v.getContext());
    	SQLiteDatabase bbdd = managerbbdd.getReadableDatabase();
   		String query = "SELECT ID_RECIBO,CONCEPTO,IMPORTE,FECHA FROM FACTURAS " +
   				"WHERE ID_RECIBO='"+id_recibo+"'";
   		Cursor cursor = (Cursor) bbdd.rawQuery(query , null);
    	
   		//Creamos un Intent para invocar a NuevaFacturaActivity para mostrar
   		//el formulario de la factura con los datos obtenidos.
   		Intent i = new Intent(v.getContext(),NuevaFacturaActivity.class);
   		if (cursor.moveToNext()) {
   			String concepto = cursor.getString(cursor.getColumnIndex("CONCEPTO"));
   			String fecha = cursor.getString(cursor.getColumnIndex("FECHA"));
   			Float importe = cursor.getFloat(cursor.getColumnIndex("IMPORTE"));
   			i.putExtra("id_recibo",id_recibo);
   			i.putExtra("concepto", concepto);
   	   		i.putExtra("fecha", fecha);
   	   		i.putExtra("importe",importe);
   		}
   		
		v.getContext().startActivity(i);
   		
    }
	
}
