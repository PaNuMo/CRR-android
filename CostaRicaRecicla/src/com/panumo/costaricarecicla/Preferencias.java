package com.panumo.costaricarecicla;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class Preferencias extends PreferenceActivity {

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias); 
        
        Preference customPref = (Preference) findPreference("customPref");
        customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        	public boolean onPreferenceClick(Preference preference) {
        		new AlertDialog.Builder(Preferencias.this)
    	        .setTitle(R.string.title_dialog_acercade)
    	        .setMessage(R.string.mensaje_acercade)
    	        .setNegativeButton(R.string.cerrar, new DialogInterface.OnClickListener() {
    	                public void onClick(DialogInterface dialog, int whichButton) { 
    	                	//the system dismisses the dialog
    	                }
    	         })
    	        .create().show();
        		return true;
            }
        });
    }
}
