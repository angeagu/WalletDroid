package com.kursea.walletdroid.activity;

import java.util.Locale;

import com.kursea.walletdroid.R;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.kursea.walletdroid.fragment.ConfigurationDialogFragment;
import com.kursea.walletdroid.fragment.ExcelDialogFragment;
import com.kursea.walletdroid.fragment.TabMonthFragment;
import com.kursea.walletdroid.fragment.TabSearchFragment;
import com.kursea.walletdroid.fragment.TabYearFragment;
import com.kursea.walletdroid.listener.WalletDroidTabListener;

public class ActionBarWalletDroidActivity extends ActionBarActivity {
    /** Called when the activity is first created. */

	Context ctx;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
	
        ctx = this.getApplicationContext();
        Bundle extras = getIntent().getExtras();
        boolean mostrarTabFacturas = false;
        
        /*
        Locale locale = new Locale("fr");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ctx.getApplicationContext().getResources().updateConfiguration(config, null);
        */
            
    	if(extras!=null){
    		//Recogemos los valores seleccionados en el SELECT de meses.
    		//Comprobamos si volvemos desde Buscar Facturas.
           	//Si volvemos desde ahí, el tab a mostrar es el 2.
           	mostrarTabFacturas = extras.getBoolean("mostrarTabFacturas");
    	}	
        	
        	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        //Obtenemos una referencia a la Action Bar
        ActionBar actionBar = this.getSupportActionBar();
        
        //Ponemos el modo de navegación por pestanas.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        //Podrimos ocultar el título de la actividad.
        //actionBar.setDisplayShowTitleEnabled(false);
        
        // Creamos las pestanas.
        ActionBar.Tab tabMonth = actionBar.newTab().setText(R.string.Mes);
        ActionBar.Tab tabYear = actionBar.newTab().setText(R.string.Ano);
        ActionBar.Tab tabFacturas = actionBar.newTab().setText(R.string.buscar_factura);
        
        //Creamos los fragments de cada pestana
        Fragment tabMonthFrag = new TabMonthFragment();
        Fragment tabYearFrag = new TabYearFragment();
        Fragment tabSearchFrag = new TabSearchFragment();
        
        //Anadimos el listener para cada una de las pestanas.
        tabMonth.setTabListener(new WalletDroidTabListener(tabMonthFrag));
        tabYear.setTabListener(new WalletDroidTabListener(tabYearFrag));
        tabFacturas.setTabListener(new WalletDroidTabListener(tabSearchFrag));
 
        //Finalmente se anaden las pestanas a la actionBar.
        actionBar.addTab(tabMonth);
        actionBar.addTab(tabYear);
        actionBar.addTab(tabFacturas);
        
    	if (!mostrarTabFacturas) {
    		//Si no volvemos desde el Tab de BuscarFacturas, ponemos el tab de meses, el inicial
    		//tabHost.setCurrentTab(0);
    		actionBar.setSelectedNavigationItem(0);
    	}
    	else {
    		//Sivolvemos desde el Tab de BuscarFacturas, ponemos el tab de Buscar Facturas
    		//tabHost.setCurrentTab(2);
    		actionBar.setSelectedNavigationItem(2);
    	}
    	
      }
      catch (Exception e) {
        	Log.e("PetProjectActivity", "Excepcion: " + e.toString());
        	Log.e("PetProjectActivity", "Mensaje: " + e.getMessage());
        	e.printStackTrace();
      }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_configuracion:
                Log.i("ActionBarWalletDroidActivity", "Configuracion");
                mostrarMenuConfiguracion(ctx);
                return true;
            case R.id.menu_exportar_excel:
            	mostrarMenuExportarExcel(ctx);
            	Log.i("ActionBarWalletDroidActivity", "Exportar a Excel");
                return true;
            case R.id.menu_salir:
            	Log.i("ActionBarWalletDroidActivity", "Salir");
            	System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void mostrarMenuExportarExcel(Context ctx) {
    	 FragmentManager fragmentManager = getSupportFragmentManager();
    	 ExcelDialogFragment dialogo = new ExcelDialogFragment();
         dialogo.show(fragmentManager, ctx.getResources().getString(R.string.menu_exportar_excel));
    }
    
    private void mostrarMenuConfiguracion(Context ctx) {
   	 	FragmentManager fragmentManager = getSupportFragmentManager();
   	 	ConfigurationDialogFragment dialogo = new ConfigurationDialogFragment();
   	 	dialogo.show(fragmentManager, ctx.getResources().getString(R.string.menu_configuracion));
   }
    
    
}