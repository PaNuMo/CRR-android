package com.panumo.costaricarecicla;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BusquedaLugarProvincias extends ListActivity {
	
	public final static String BUSQUEDA_LUGAR_PROVINCIA = "com.panumo.costaricarecicla.BUSQUEDA_LUGAR_PROVINCIA";
	
	@Override
	public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    
        DataBaseAdapter mDbHelper = new DataBaseAdapter(this);        
    	String[] provincias = mDbHelper.getProvincias();
    	mDbHelper.close();
    	
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, provincias);
	    setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    Intent intent = new Intent(this, BusquedaLugarCantones.class);
    	String provincia = (String) getListAdapter().getItem(position);
    	intent.putExtra(BUSQUEDA_LUGAR_PROVINCIA, provincia);
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
