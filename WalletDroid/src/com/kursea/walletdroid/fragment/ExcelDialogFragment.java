package com.kursea.walletdroid.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kursea.walletdroid.R;
import com.kursea.walletdroid.csv.GenerateCSVFile;
import com.kursea.walletdroid.excel.GenerateExcelSheet;
import com.kursea.walletdroid.utils.VentanaAlerta;

public class ExcelDialogFragment extends DialogFragment {
    
	int mesInicio;
	int anoInicio;
	int mesFin;
	int anoFin;
	TableLayout tableLayoutFiltro = null;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {		
    	
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        final Context ctx = this.getActivity().getApplicationContext();
 
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tableLayoutFiltro = (TableLayout)inflater.inflate(R.layout.searchdatefilters, null);
        CheckBox checkBoxFiltroFechas = (CheckBox)tableLayoutFiltro.findViewById(R.id.checkBoxFiltroFechas);
    	checkBoxFiltroFechas.setVisibility(View.GONE);
    	
    	TableRow rowFormato = new TableRow(ctx);
    	TextView textViewFormato = new TextView(ctx);
    	textViewFormato.setText(ctx.getResources().getString(R.string.formato));
    	
    	final Spinner formatSpinner = new Spinner(ctx);
    	List<String> list = new ArrayList<String>();
    	list.add("Excel (XLS)");
    	list.add("CSV");
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx,
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	formatSpinner.setAdapter(dataAdapter);
    	rowFormato.addView(textViewFormato);
    	rowFormato.addView(formatSpinner);
    	
    	tableLayoutFiltro.addView(rowFormato,0);
    	
    	DatePicker pickerInicio = (DatePicker) tableLayoutFiltro.findViewById(R.id.datePickerFechaInicio);
        try {
            Field f[] = pickerInicio.getClass().getDeclaredFields();
            for (Field field : f) {
            	if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
                    field.setAccessible(true);
                    Object datePicker = new Object();
                    datePicker = field.get(pickerInicio);
                    ((View) datePicker).setVisibility(View.GONE);
                }
            }
        } 
        catch (SecurityException e) {
            Log.d("ERROR", e.getMessage());
        } 
        catch (IllegalArgumentException e) {
            Log.d("ERROR", e.getMessage());
        } 
        catch (IllegalAccessException e) {
            Log.d("ERROR", e.getMessage());
        }
        
        
        DatePicker pickerFin = (DatePicker) tableLayoutFiltro.findViewById(R.id.datePickerFechaFin);
        try {
            Field f[] = pickerFin.getClass().getDeclaredFields();
            for (Field field : f) {               	
                if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
                    field.setAccessible(true);
                    Object datePicker = new Object();
                    datePicker = field.get(pickerFin);
                    ((View) datePicker).setVisibility(View.GONE);
                }
            }
        } 
        catch (SecurityException e) {
            Log.d("ERROR", e.getMessage());
        } 
        catch (IllegalArgumentException e) {
            Log.d("ERROR", e.getMessage());
        } 
        catch (IllegalAccessException e) {
            Log.d("ERROR", e.getMessage());
        }
        
        builder.setView(tableLayoutFiltro);	
        builder.setMessage(R.string.excelDialogMensaje);
        builder.setTitle(R.string.menu_exportar_excel);
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
               public void onClick(DialogInterface dialog, int id) {
                    Log.i("Dialogos", "Confirmacion Aceptada.");
                    	
                    //Hay Fecha Inicio
                    TableLayout tableSearchFilters = (TableLayout)tableLayoutFiltro.findViewById(R.id.tableSearchFilters);
                    TableRow filaFechaInicio = (TableRow)tableSearchFilters.findViewById(R.id.tableRowFechaInicio);
                    DatePicker datePickerFechaInicio = (DatePicker)filaFechaInicio.findViewById(R.id.datePickerFechaInicio);
                    mesInicio=datePickerFechaInicio.getMonth()+1;
                    anoInicio=datePickerFechaInicio.getYear();
                    		
                    //Hay Fecha Fin
                    TableRow filaFechaFin = (TableRow)tableSearchFilters.findViewById(R.id.tableRowFechaFin);
                    DatePicker datePickerFechaFin = (DatePicker)filaFechaFin.findViewById(R.id.datePickerFechaFin);
                    mesFin=datePickerFechaFin.getMonth()+1;
                    anoFin=datePickerFechaFin.getYear();   
                    
                    //Formato
                    String formato = formatSpinner.getSelectedItem().toString();
                        
                    String errores;
                    if (!formato.equalsIgnoreCase("CSV")) {
                    	errores = GenerateExcelSheet.generateExcelSheet(mesInicio, anoInicio, mesFin, anoFin, getActivity().getApplicationContext());
                    }
                    else {
                    	errores = GenerateCSVFile.generateCSVFile(mesInicio, anoInicio, mesFin, anoFin, getActivity().getApplicationContext());
                    }
                    if (errores.length()==0) {
                    	//La Hoja Excel se ha guardado correctamente.
                    	VentanaAlerta.mostrarAlertaLarga(tableSearchFilters.getContext(), 
                    			ctx.getResources().getString(R.string.exitoGenerarExcel));
                    }
                    else {
                    	//Mostramos los errores
                    	VentanaAlerta.mostrarAlertaLarga(tableSearchFilters.getContext(),errores);
                    }
                 }
               });
        
        builder.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                   }
               });
 
        return builder.show();
        
    }
	
	
}