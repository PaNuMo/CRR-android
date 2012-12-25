package com.panumo.costaricarecicla;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BusquedaMaterialCategorias extends ListActivity {
	
	public final static String BUSQUEDA_MATERIAL_CATEGORIA = "com.panumo.costaricarecicla.BUSQUEDA_MATERIAL_CATEGORIA";
	
	@Override
	public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
        DataBaseAdapter mDbHelper = new DataBaseAdapter(this);               
    	String[] materiales = mDbHelper.getMateriales();
    	mDbHelper.close();
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, materiales);
	    setListAdapter(adapter);	    
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    Intent intent = new Intent(this, BusquedaMaterialCentros.class);
    	String categoria = (String) getListAdapter().getItem(position);
    	intent.putExtra(BUSQUEDA_MATERIAL_CATEGORIA, categoria);
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
