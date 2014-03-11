package com.kursea.walletdroid.bbdd;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ManagerBBDD extends SQLiteOpenHelper {

	private static ManagerBBDD instancia;
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "FACTURAS";
    private static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                "ID_RECIBO" + " INTEGER PRIMARY KEY, " +
                "CONCEPTO" + " TEXT, " +
                "IMPORTE" + " REAL, " +
                "FECHA" + " TEXT);";

    private ManagerBBDD(Context context) {
        super(context, "BBDDFACTURAS", null, DATABASE_VERSION);
    }
    
    public static ManagerBBDD getInstance(Context context) {
    	if (instancia == null) {
    		return new ManagerBBDD(context);
    	}
    	else return instancia;
    	
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void poblarBaseDatos() {
		
		Calendar c = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
    	int mesActual = cal.get(cal.MONTH) + 1; 
    	int anoActual = cal.get(cal.YEAR);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = (Cursor) db.rawQuery( "SELECT * FROM FACTURAS" , null);
		int numFilas = cursor.getCount();
		
		if (numFilas == 0) {
			SQLiteDatabase db2 = this.getWritableDatabase();
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Gas','123.45','1-7-2012')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Alquiler','98.87','2-5-2012')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('IBI','123.45','3-6-2012')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Agua','98.87','4-7-2012')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Dinero Cajero','123.45','5-5-2012')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Comisiones','98.87','6-6-2012')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Telefono','123.45','2-7-2012')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Iberdrola','98.87','8-5-2012')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Compra Supermercado','123.45','9-6-2012')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Taller Coche','98.87','10-6-2012')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Compra Supermercado','123.45','9-10-2012')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Taller Coche','98.87','10-11-2012')");
		
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Gas','123.45','31-7-2013')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Alquiler','98.87','22-5-2013')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('IBI','123.45','13-6-2013')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Agua','98.87','24-7-2013')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Dinero Cajero','123.45','15-5-2013')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Comisiones','98.87','6-6-2013')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Telefono','123.45','21-7-2013')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Iberdrola','98.87','8-5-2013')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Compra Supermercado','123.45','9-6-2013')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Taller Coche','198.87','10-6-2013')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Compra Supermercado','123.45','9-11-2013')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Taller Coche','198.87','10-12-2013')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Taller Coche','198.87','10-11-2013')");
			
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Gas','123.45','31-"+mesActual+"-"+anoActual+"')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Alquiler','98.87','22-"+mesActual+"-"+anoActual+"')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('IBI','123.45','13-"+mesActual+"-"+anoActual+"')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Agua','98.87','24-"+mesActual+"-"+anoActual+"')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Dinero Cajero','123.45','15-"+mesActual+"-"+anoActual+"')"); 
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Comisiones','98.87','6-"+mesActual+"-"+anoActual+"')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Telefono','123.45','21-"+mesActual+"-"+anoActual+"')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Iberdrola','98.87','8-"+mesActual+"-"+anoActual+"')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Compra Supermercado','123.45','9-"+mesActual+"-"+anoActual+"')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Taller Coche','198.87','10-"+mesActual+"-"+anoActual+"')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Test1','98.87','8-"+mesActual+"-"+anoActual+"')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Test2','123.45','9-"+mesActual+"-"+anoActual+"')");
			db2.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('Test3','198.87','10-"+mesActual+"-"+anoActual+"')");
			
		}
	}
	
	public void addFactura(String concepto, String fecha, float importe) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO FACTURAS (CONCEPTO,IMPORTE,FECHA) VALUES ('"+concepto+"','"+importe+"','"+fecha+"')"); 
			
	}
	
	public void eliminarFactura(int id_recibo) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM FACTURAS WHERE ID_RECIBO = '"+id_recibo+"'"); 
			
	}
	
	public void updateFactura(int id_recibo, String concepto, String fecha, float importe) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE FACTURAS SET CONCEPTO='"+concepto+"', IMPORTE='"+importe+"', FECHA='"+fecha+"' WHERE ID_RECIBO='"+id_recibo+"'";
		db.execSQL(sql);
		
	}
	
}