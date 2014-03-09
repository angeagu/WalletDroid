package com.kursea.walletdroid.listener;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerLanguageListener implements OnItemSelectedListener {
	
	private Context ctx; 
	
	/*
	 Para evitar el problema de que el onItemSelectedListener se dispara al
	posicionar el combo en el layout, definimos un contador, que impide que
	se ejecute código la primera vez que se posiciona el listener. 
	*/
	public int count = 0; 
	
	public SpinnerLanguageListener (Context c) {
		ctx = c;
		count = 0;
	} 
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		  
		Locale locale = new Locale("es");
		String language = (String)parent.getItemAtPosition(pos);
		count++;//Incrementamos el contador para que los cambios en el combo se pasen al onItemSelected
		if (language.equals("Español")) {
			locale = new Locale("es");
		}
		else if (language.equals("English")) {
			locale = new Locale("en");
		}
		else if (language.equals("Deutsch")) {
			locale = new Locale("de");
		}
		else if (language.equals("Français")) {
			locale = new Locale("fr");
		}
		
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ctx.getApplicationContext().getResources().updateConfiguration(config, null);
        
	}
	
	public void onNothingSelected(AdapterView<?> parent) {
		// Do nothing.
	}

	
}

