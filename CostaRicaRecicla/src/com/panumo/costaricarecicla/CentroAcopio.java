package com.panumo.costaricarecicla;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class CentroAcopio extends ListActivity {
	
	private CentroAcopioAdapter mAdapter;
	private Map<String,String[]> materiales;
	private List<String> detalles;
	private List<ListViewItem> finalList; // materiales + detalles 
    private String[] numeros;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centro_acopio);
        
        Intent intent = getIntent();
	    String centro = intent.getStringExtra("com.panumo.costaricarecicla.NOMBRE_CENTRO_ACOPIO");	    
        
        ((TextView) findViewById(R.id.textViewNombreCentroAcopio)).setText(centro);
        
        DataBaseAdapter DbHelper = new DataBaseAdapter(this);                
    	materiales = DbHelper.getMaterialesXCentro(centro);
    	detalles = DbHelper.getDetallesXCentro(centro);
    	numeros = DbHelper.getNumeroTelefonoXCentro(centro);
    	DbHelper.close();
    	
    	finalList = new ArrayList<ListViewItem>();
    	addMateriales(materiales.keySet().toArray());
    	addDetalles();
    	
    	mAdapter = new CentroAcopioAdapter(finalList);
	    setListAdapter(mAdapter);
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    String material = (String) getListAdapter().getItem(position);
    	String[] detallesMaterial = materiales.get(material); // [0]cantidad [1]forma [2]costo   	
    	String detallesMaterialFinal = new StringBuilder("Cantidad: ").append(detallesMaterial[0])
										    	.append("\n\nCómo llevarlo: ").append(detallesMaterial[1])
										    	.append("\n\nCosto por recibirlo: ").append(detallesMaterial[2]).toString();
    	new AlertDialog.Builder(CentroAcopio.this)
    	.setMessage(detallesMaterialFinal)
    	.setTitle(R.string.title_dialog_centro_acopio)
    	.setNegativeButton(R.string.cerrar, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int id) {
    			//the system dismisses the dialog
            }
         })
    	.create().show();
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

    private void addMateriales(Object[] objects){
    	ListViewItem temp = new ListViewItem("Materiales", tipoListViewItem.TITULO);
    	finalList.add(temp);
    	for (Object value : objects){
    		temp = new ListViewItem(value.toString(), tipoListViewItem.MATERIAL);
    		finalList.add(temp);
    	}
    }
    
    private void addDetalles(){
    	ListViewItem temp = new ListViewItem("Dirección", tipoListViewItem.TITULO);
    	finalList.add(temp);
    	temp = new ListViewItem(detalles.get(0), tipoListViewItem.DESCRIPCION);
    	finalList.add(temp);
    	temp = new ListViewItem("Horario", tipoListViewItem.TITULO);
    	finalList.add(temp);
    	temp = new ListViewItem(detalles.get(1), tipoListViewItem.DESCRIPCION);
    	finalList.add(temp);
    	if(detalles.get(2)!=null){
    		temp = new ListViewItem("Contacto", tipoListViewItem.TITULO);
        	finalList.add(temp);
        	temp = new ListViewItem(detalles.get(2), tipoListViewItem.DESCRIPCION);
        	finalList.add(temp);
    	}
    }

    private static class CentroAcopioViewHolder {
        public TextView separator;
        public TextView material;
        public TextView descripcion;
    }

    private class CentroAcopioAdapter extends BaseAdapter {

        public List<ListViewItem> mData;
       

        public CentroAcopioAdapter(List<ListViewItem> data) {
            mData = data;
        }

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public String getItem(int position) {
			return mData.get(position).getName();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

       @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CentroAcopioViewHolder holder = null;

            if (convertView == null) {
            	convertView = getLayoutInflater().inflate(R.layout.layout_listview_centro_acopio, null);
            	holder = new CentroAcopioViewHolder();
                holder.separator = (TextView) convertView.findViewById(R.id.separator);
                holder.material = (TextView) convertView.findViewById(R.id.material);
                holder.descripcion = (TextView) convertView.findViewById(R.id.descripcion);
                convertView.setTag(holder);
            } else {
            	holder = (CentroAcopioViewHolder) convertView.getTag();
            }
            
            switch(mData.get(position).getTipo()){
            	case TITULO:
            		putTitulo(holder, getItem(position));
            		break;
            	case MATERIAL:
            		putMaterial(holder, getItem(position));
            		break;
            	case DESCRIPCION:
            		putDescripcion (holder, getItem(position));
            }

            return convertView;
        }
    }
    
    private void putTitulo(CentroAcopioViewHolder holder, String titulo){
	    holder.separator.setText(titulo);
	    holder.separator.setVisibility(View.VISIBLE);
   		holder.material.setVisibility(View.GONE);
   		holder.descripcion.setVisibility(View.GONE);
   }
    
    private void putMaterial(CentroAcopioViewHolder holder, String material){
    	holder.material.setText(material);
    	holder.separator.setVisibility(View.GONE);
    	holder.material.setVisibility(View.VISIBLE);
        holder.descripcion.setVisibility(View.GONE);
    }
    
    private void putDescripcion(CentroAcopioViewHolder holder, String descripcion){
    	holder.descripcion.setText(descripcion);
    	holder.separator.setVisibility(View.GONE);
        holder.material.setVisibility(View.GONE);
        holder.descripcion.setVisibility(View.VISIBLE);
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
    	TITULO, MATERIAL, DESCRIPCION
	}
   
    
    public void llamarCentroAcopio(View v){
    	if (numeros.length > 0){
	    	new AlertDialog.Builder(CentroAcopio.this)
	        .setTitle(R.string.title_dialog_telefonos)
	        .setItems(numeros, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    String numero = numeros[which];
	                    Intent callIntent = new Intent(Intent.ACTION_CALL , Uri.parse("tel:" + numero));
	                    CentroAcopio.this.startActivity(callIntent);
	                }
	         })
	         .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) { 
	                	//the system dismisses the dialog
	                }
	         })
	        .create().show();
	    	}
    	else{
    		AlertDialog.Builder builder = new AlertDialog.Builder(CentroAcopio.this);
    		builder.setTitle(R.string.title_dialog_telefonos)
	        .setMessage(R.string.mensaje_sin_telefono)
	        .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) { 
	                	//the system dismisses the dialog
	                }
	         });
    		
    		AlertDialog alert = builder.create();
    		alert.show();
    		((TextView)alert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    	}
    }
}