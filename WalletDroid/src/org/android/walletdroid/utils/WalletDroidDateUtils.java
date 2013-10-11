package org.android.walletdroid.utils;

public class WalletDroidDateUtils {

	public static String monthToString(int mes) {
		String stringMes = "";
		
		switch (mes) {
		case 1 : stringMes += "Enero"; break;
		case 2 : stringMes += "Febrero"; break;
		case 3 : stringMes += "Marzo"; break;
		case 4 : stringMes += "Abril"; break;
		case 5 : stringMes += "Mayo"; break;
		case 6 : stringMes += "Junio"; break;
		case 7 : stringMes += "Julio"; break;
		case 8 : stringMes += "Agosto"; break;
		case 9 : stringMes += "Septiembre"; break;
		case 10 : stringMes += "Octubre"; break;
		case 11 : stringMes += "Noviembre"; break;
		case 12 : stringMes += "Diciembre"; break;
		
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
		
		return mes;
		
	}
	
}
