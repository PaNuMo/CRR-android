package com.panumo.costaricarecicla;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BusquedaMaterialCentros extends ListActivity {
	
	private List<String[]> centros;
	private List<ListViewItem> centrosFinal;
	private BusquedaMaterialCentrosAdapter mAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	    
	    Intent intent = getIntent();
	    String categoria = intent.getStringExtra(BusquedaMaterialCategorias.BUSQUEDA_MATERIAL_CATEGORIA);	    
        DataBaseAdapter DbHelper = new DataBaseAdapter(this);                
    	centros = DbHelper.getCentrosXCategoria(categoria);
    	DbHelper.close();
    	
    	centrosFinal = new ArrayList<ListViewItem>();
    	addCentros();
    	mAdapter = new BusquedaMaterialCentrosAdapter(this, R.layout.layout_listview_busqueda_material_centros, centrosFinal);
	    setListAdapter(mAdapter);	        
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    Intent intent = new Intent(this, CentroAcopio.class);
    	ListViewItem centro = (ListViewItem)getListAdapter().getItem(position);
    	intent.putExtra("com.panumo.costaricarecicla.NOMBRE_CENTRO_ACOPIO", centro.getName());
    	startActivity(intent);
	}
    
    private void addCentros(){
    	String provinciaTemp = "";
    	for(String[] centro : centros){
    		if(!centro[0].equals(provinciaTemp)){
    			provinciaTemp = centro[0];
    			centrosFinal.add(new ListViewItem(centro[0], tipoListViewItem.TITULO));
    			centrosFinal.add(new ListViewItem(centro[1], tipoListViewItem.NORMAL));
    		}
    		else{
    			centrosFinal.add(new ListViewItem(centro[1], tipoListViewItem.NORMAL));
    		}
    	}
    }
    
    static class BusquedaMaterialCentrosHolder {
        TextView separator;
        TextView normal;
    }
    
    private class BusquedaMaterialCentrosAdapter extends ArrayAdapter<ListViewItem>{
 
        public BusquedaMaterialCentrosAdapter(Context context, int layoutResourceId, List<ListViewItem> items) {
            super(context, layoutResourceId, items);
        }


       @Override
        public View getView(int position, View convertView, ViewGroup parent) {

    	   BusquedaMaterialCentrosHolder holder;

            if (convertView == null) {
            	LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            	convertView = li.inflate(R.layout.layout_listview_busqueda_material_centros, parent, false);
            	holder = new BusquedaMaterialCentrosHolder();
                holder.separator = (TextView) convertView.findViewById(R.id.separator);
                holder.normal = (TextView) convertView.findViewById(R.id.normal);
                convertView.setTag(holder);
            } else {
            	holder = (BusquedaMaterialCentrosHolder) convertView.getTag();
            }
            
            ListViewItem lvi = getItem(position);
            switch(lvi.getTipo()){
            	case TITULO:
            		holder.separator.setText(lvi.getName());
            		holder.separator.setVisibility(View.VISIBLE);
            		holder.normal.setVisibility(View.GONE);
            		break;
            	case NORMAL:
                    holder.normal.setText(lvi.getName());
                    holder.normal.setVisibility(View.VISIBLE);
                    holder.separator.setVisibility(View.GONE);
            		break;
            }

            return convertView;
        }
       
    }
    
    private static class ListViewItem{ 	
    	private String _name;
    	private tipoListViewItem _tipo;
	   
    	ListViewItem(String name, tipoListViewItem tipo){
	   		_name = name;
	   		_tipo = tipo;
	   	}
	   	
	   	public tipoListViewItem getTipo(){return _tipo;}
	   	public String getName(){return _name;}	   	
    }
    
    private enum tipoListViewItem {
    	TITULO, NORMAL
	}
  
}
