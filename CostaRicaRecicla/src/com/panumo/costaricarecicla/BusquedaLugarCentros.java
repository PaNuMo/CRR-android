package com.panumo.costaricarecicla;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BusquedaLugarCentros extends ListActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_busqueda_lugar_centros);
	    
	    Intent intent = getIntent();
	    String canton = intent.getStringExtra(BusquedaLugarCantones.BUSQUEDA_LUGAR_CANTON);
	    
        DataBaseAdapter mDbHelper = new DataBaseAdapter(this);        
    	String[] centros = mDbHelper.getCentrosXCanton(canton);
    	mDbHelper.close();
    	
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, centros);
	    setListAdapter(adapter);
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
    
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ((TextView) findViewById(R.id.textViewEmpty)).setMovementMethod(LinkMovementMethod.getInstance());
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    Intent intent = new Intent(this, CentroAcopio.class);
    	String centro = (String) getListAdapter().getItem(position);
    	intent.putExtra("com.panumo.costaricarecicla.NOMBRE_CENTRO_ACOPIO", centro);
    	startActivity(intent);
	}

}
