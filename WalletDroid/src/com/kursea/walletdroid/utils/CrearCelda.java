package com.kursea.walletdroid.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

public class CrearCelda {

	public static TextView getCeldaTablaFacturas(Context c) {
		
		TextView celda = new TextView(c);
		celda.setTextSize(16);
		celda.setTextColor(Color.BLACK);
		celda.setBackgroundColor(Color.GRAY);
		return celda;
		
	}
	
	public static TextView getCeldaEncabezado(Context c) {
		
		TextView celda = new TextView(c);
		celda.setTextSize(18);
    	celda.setTextColor(Color.WHITE);
    	celda.setBackgroundColor(Color.BLUE);
		return celda;
		
	}
	

	public static TextView getCeldaTotales(Context c) {
		
		TextView celda = new TextView(c);
		celda.setTextSize(18);
    	celda.setTextColor(Color.WHITE);
    	celda.setBackgroundColor(Color.BLACK);
		return celda;
		
	}
	
	public static TextView getTextViewCabecera(Context c) {
		TextView celda = new TextView(c);
		celda.setTextSize(22);
    	celda.setTextColor(Color.RED);
    	celda.setBackgroundColor(Color.BLACK);
    	return celda;
	}
	
}
