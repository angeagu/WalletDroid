package com.kursea.walletdroid.utils;

import com.kursea.walletdroid.R;

import android.content.Context;

public class WalletDroidDateUtils {

	public static String monthToString(Context ctx, int mes) {
		String stringMes = "";
		
		switch (mes) {
		case 1 : stringMes += ctx.getResources().getString(R.string.enero); break;
		case 2 : stringMes += ctx.getResources().getString(R.string.febrero); break;
		case 3 : stringMes += ctx.getResources().getString(R.string.marzo); break;
		case 4 : stringMes += ctx.getResources().getString(R.string.abril); break;
		case 5 : stringMes += ctx.getResources().getString(R.string.mayo); break;
		case 6 : stringMes += ctx.getResources().getString(R.string.junio); break;
		case 7 : stringMes += ctx.getResources().getString(R.string.julio); break;
		case 8 : stringMes += ctx.getResources().getString(R.string.agosto); break;
		case 9 : stringMes += ctx.getResources().getString(R.string.septiembre); break;
		case 10 : stringMes += ctx.getResources().getString(R.string.octubre); break;
		case 11 : stringMes += ctx.getResources().getString(R.string.noviembre); break;
		case 12 : stringMes += ctx.getResources().getString(R.string.diciembre); break;
		
		}
		
		return stringMes;
		
	}
	
	public static int montToInt(String mesValue) {
		
		int mes = 0;
		
		if (mesValue.equalsIgnoreCase("Enero")) mes = 1;
		else if (mesValue.equalsIgnoreCase("Febrero")) mes = 2;
		else if (mesValue.equalsIgnoreCase("Marzo")) mes = 3;
		else if (mesValue.equalsIgnoreCase("Abril")) mes = 4;
		else if (mesValue.equalsIgnoreCase("Mayo")) mes = 5;
		else if (mesValue.equalsIgnoreCase("Junio")) mes = 6;
		else if (mesValue.equalsIgnoreCase("Julio")) mes = 7;
		else if (mesValue.equalsIgnoreCase("Agosto")) mes = 8;
		else if (mesValue.equalsIgnoreCase("Septiembre")) mes = 9;
		else if (mesValue.equalsIgnoreCase("Octubre")) mes = 10;
		else if (mesValue.equalsIgnoreCase("Noviembre")) mes = 11;
		else if (mesValue.equalsIgnoreCase("Diciembre")) mes = 12;
		else if (mesValue.equalsIgnoreCase("Januar")) mes = 1;
		else if (mesValue.equalsIgnoreCase("Februar")) mes = 2;
		else if (mesValue.equalsIgnoreCase("März")) mes = 3;
		else if (mesValue.equalsIgnoreCase("April")) mes = 4;
		else if (mesValue.equalsIgnoreCase("Mai")) mes = 5;
		else if (mesValue.equalsIgnoreCase("Juni")) mes = 6;
		else if (mesValue.equalsIgnoreCase("Juli")) mes = 7;
		else if (mesValue.equalsIgnoreCase("August")) mes = 8;
		else if (mesValue.equalsIgnoreCase("September")) mes = 9;
		else if (mesValue.equalsIgnoreCase("Oktober")) mes = 10;
		else if (mesValue.equalsIgnoreCase("November")) mes = 11;
		else if (mesValue.equalsIgnoreCase("Dezember")) mes = 12;
		else if (mesValue.equalsIgnoreCase("January")) mes = 1;
		else if (mesValue.equalsIgnoreCase("February")) mes = 2;
		else if (mesValue.equalsIgnoreCase("March")) mes = 3;
		else if (mesValue.equalsIgnoreCase("April")) mes = 4;
		else if (mesValue.equalsIgnoreCase("May")) mes = 5;
		else if (mesValue.equalsIgnoreCase("June")) mes = 6;
		else if (mesValue.equalsIgnoreCase("July")) mes = 7;
		else if (mesValue.equalsIgnoreCase("August")) mes = 8;
		else if (mesValue.equalsIgnoreCase("September")) mes = 9;
		else if (mesValue.equalsIgnoreCase("October")) mes = 10;
		else if (mesValue.equalsIgnoreCase("November")) mes = 11;
		else if (mesValue.equalsIgnoreCase("December")) mes = 12;
		else if (mesValue.equalsIgnoreCase("Janvier")) mes = 1;
		else if (mesValue.equalsIgnoreCase("Février")) mes = 2;
		else if (mesValue.equalsIgnoreCase("Mars")) mes = 3;
		else if (mesValue.equalsIgnoreCase("Avril")) mes = 4;
		else if (mesValue.equalsIgnoreCase("Mai")) mes = 5;
		else if (mesValue.equalsIgnoreCase("Juin")) mes = 6;
		else if (mesValue.equalsIgnoreCase("Juillet")) mes = 7;
		else if (mesValue.equalsIgnoreCase("Août")) mes = 8;
		else if (mesValue.equalsIgnoreCase("Septembre")) mes = 9;
		else if (mesValue.equalsIgnoreCase("Octobre")) mes = 10;
		else if (mesValue.equalsIgnoreCase("Novembre")) mes = 11;
		else if (mesValue.equalsIgnoreCase("Décembre")) mes = 12;

	    
		return mes;
		
	}
	
}
