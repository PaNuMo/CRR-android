package com.panumo.costaricarecicla;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BusquedaLugarCantones extends ListActivity {

	public final static String BUSQUEDA_LUGAR_CANTON = "com.panumo.costaricarecicla.BUSQUEDA_LUGAR_CANTON";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Intent intent = getIntent();
	    String provincia = intent.getStringExtra(BusquedaLugarProvincias.BUSQUEDA_LUGAR_PROVINCIA);
	    
        DataBaseAdapter mDbHelper = new DataBaseAdapter(this);         
    	String[] cantones = mDbHelper.getCantones(provincia);
    	
    	mDbHelper.close();
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cantones);
	    setListAdapter(adapter);
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    Intent intent = new Intent(this, BusquedaLugarCentros.class);
    	String canton = (String) getListAdapter().getItem(position);
    	intent.putExtra(BUSQUEDA_LUGAR_CANTON, canton);
    	startActivity(intent);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent settingsActivity = new Intent(this, Preferencias.class);
    	startActivity(settingsActivity);
        return true;
    }

}
