package com.kursea.walletdroid.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import com.kursea.walletdroid.R;
import com.kursea.walletdroid.activity.ActionBarWalletDroidActivity;
import com.kursea.walletdroid.listener.SpinnerLanguageListener;

public class ConfigurationDialogFragment extends DialogFragment {
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        final Context ctx = this.getActivity().getApplicationContext();
        
        
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.configuration, null);
        Spinner spinnerLanguage = (Spinner) v.findViewById(R.id.spinner_language);
		spinnerLanguage.setOnItemSelectedListener(new SpinnerLanguageListener(this.getActivity().getApplicationContext()));
		spinnerLanguage.setSelection(0);
        
		builder.setView(v);	
        builder.setMessage(R.string.configurationDialogMensaje);
        builder.setTitle(R.string.menu_configuracion);
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
        	public void onClick(DialogInterface dialog, int id) {
        		Intent i = new Intent(ctx,ActionBarWalletDroidActivity.class);
        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(i);
        	}
        	
        });
        
        
        return builder.show();
        
	}

}
