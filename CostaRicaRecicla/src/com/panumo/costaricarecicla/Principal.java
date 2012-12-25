package com.panumo.costaricarecicla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Principal extends Activity {

	public final static String EXTRA_MESSAGE = "com.panumo.costaricarecicla.MESSAGE";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        DataBaseHelper mDbHelper = new DataBaseHelper(this);
        mDbHelper.createDataBase();
    	mDbHelper.close();
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
    
    public void busquedaLugarProvincias(View view) {
    	Intent intent = new Intent(this, BusquedaLugarProvincias.class);
    	startActivity(intent);
    }
    
    public void busquedaMaterialCategorias(View view) {
    	Intent intent = new Intent(this, BusquedaMaterialCategorias.class);
    	startActivity(intent);
    }
    
}
