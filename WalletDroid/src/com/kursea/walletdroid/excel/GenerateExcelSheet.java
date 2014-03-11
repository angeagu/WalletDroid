package com.kursea.walletdroid.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.kursea.walletdroid.R;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.kursea.walletdroid.bbdd.ManagerBBDD;
import com.kursea.walletdroid.utils.WalletDroidDateUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class GenerateExcelSheet {
	
	static ManagerBBDD managerbbdd;	
	static SQLiteDatabase bbdd;
	//Libro Excel
    static HSSFWorkbook workbook;
    //Hoja de cálculo Excel
    static HSSFSheet sheet;
    static String errors = "";
    static int rownum = 0;


	public static String generateExcelSheet ( int mesInicio,int anoInicio,int mesFin, int anoFin,Context ctx) {

		try {
		 String nombreFichero = ctx.getResources().getString(R.string.facturas)+"_"+mesInicio+"-"+anoInicio+"_A_"+mesFin+"-"+anoFin+".xls";
		 workbook = new HSSFWorkbook();
		 sheet = workbook.createSheet(ctx.getResources().getString(R.string.facturas));
		 managerbbdd = ManagerBBDD.getInstance(ctx);
         bbdd = managerbbdd.getReadableDatabase();
         ArrayList<String[]> facturas;
         String concepto=ctx.getResources().getString(R.string.Concepto);
         String fecha=ctx.getResources().getString(R.string.Fecha);
         String importeString=ctx.getResources().getString(R.string.Importe);
         FileOutputStream out;
         
         //Escribimos la excel y cerramos.
         if (isExternalStorageWritable()) {
         	File file = new File(Environment.getExternalStoragePublicDirectory(
                     Environment.DIRECTORY_DOWNLOADS), nombreFichero);
         	out = new FileOutputStream(file);
         	//FileOutputStream out = ctx.getApplicationContext().openFileOutput(nombreFichero, Context.MODE_PRIVATE);
         }
         else {
         	//Avisar de que el almacenamiento externo no está disponible.
         	errors = ctx.getResources().getString(R.string.almacenamientoNoDisponible);
         	return errors;
         }
         
        
         do {
        	//String query = "SELECT ID_RECIBO,CONCEPTO,IMPORTE,FECHA FROM FACTURAS WHERE FECHA LIKE '%-" + mesInicio + "-"+anoInicio+"%' ORDER BY FECHA ASC";
        	String query = "SELECT ID_RECIBO,CONCEPTO,IMPORTE,FECHA FROM FACTURAS WHERE FECHA LIKE '%-" + mesInicio + "-"+anoInicio+"' ORDER BY FECHA ASC";
        	Cursor cursor = (Cursor) bbdd.rawQuery(query , null);
	
        	//Datos a escribir
			facturas = new ArrayList<String[]>();
			facturas.add(new String[]{WalletDroidDateUtils.monthToString(ctx, mesInicio) + "-" + anoInicio});
	        facturas.add(new String[] {concepto, fecha, importeString});
        	while (cursor.moveToNext()) {
        		//Procesamos las facturas.
        		String conceptoValue = cursor.getString(cursor.getColumnIndex("CONCEPTO"));
    			Integer id_recibo  = cursor.getInt(cursor.getColumnIndex("ID_RECIBO"));
    			String fechaValue = cursor.getString(cursor.getColumnIndex("FECHA"));
    			Float importe = cursor.getFloat(cursor.getColumnIndex("IMPORTE"));

    	        facturas.add(new String[]{conceptoValue,fechaValue,importe.toString()});
    	        
        	}
        	
        	addFacturasExcel(facturas);
        	
        	mesInicio = mesInicio + 1;
        	if (mesInicio>12) {
        		mesInicio = 1;
        		anoInicio = anoInicio + 1;
        	}
        	
        }
        //while (mesInicio<=mesFin || anoInicio<=anoFin);
        while ((anoInicio!=anoFin)|| ((anoInicio==anoFin)&&(mesInicio<=mesFin)));

        workbook.write(out);
      	out.close(); 
         
        
		}
        catch (Exception ex) {
        	ex.printStackTrace();
        	errors = ctx.getResources().getString(R.string.errorGenerarExcel)+": " + ex.getMessage();
        }
		
		return errors;
	}
	
	private static void addFacturasExcel(ArrayList<String[]> facturas) {
		//Iteramos sobre los datos y escribimos en la hoja de cálculo.
        if  (facturas.size()>2) {
        	Iterator<String[]> it  = facturas.iterator();
        
        	while (it.hasNext())
        	{
        		Row row = sheet.createRow(rownum++);
        		String [] stringArray = it.next();
        		int cellnum = 0;
        		for (String str : stringArray)
        		{
        			Cell cell = row.createCell(cellnum++);
        			cell.setCellValue(str);
        		}
        	}
        }
	}

	private static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
}
