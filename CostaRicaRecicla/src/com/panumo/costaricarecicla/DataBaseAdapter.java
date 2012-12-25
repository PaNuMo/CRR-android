package com.panumo.costaricarecicla;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
 
public class DataBaseAdapter  
{ 
    protected static final String TAG = "DataBaseAdapter"; 
 
    private SQLiteDatabase mDb; 
    private DataBaseHelper mDbHelper; 
 
    public DataBaseAdapter(Context context){  
        mDbHelper = new DataBaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();     
    } 
 
    public void close(){ 
    	SQLiteDatabase.releaseMemory();
        mDbHelper.close();
        mDb.close();
    } 
 
    public String[] getProvincias() { 
		try{ 
			String sql = "SELECT nombre FROM Provincia ORDER BY nombre ASC"; 
            Cursor mCur = mDb.rawQuery(sql, null);
            String[] provincias = new String[mCur.getCount()];
         	for(int i = 0; mCur.moveToNext(); i++){
         		provincias[i] = mCur.getString(0);
         	}
         	mCur.close();
            return provincias; 
	     } 
         catch (SQLException mSQLException){ 
             //Log.e(TAG, "getProvincias >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 } 
     }
    
    public String[] getCantones(String provincia) { 
    	try{ 
		     String sql = new StringBuilder("SELECT nombre FROM Canton WHERE idProvincia = ").append(getIdProvincia(provincia)).append(" ORDER BY nombre ASC").toString(); 
             Cursor mCur = mDb.rawQuery(sql, null);
             String[] cantones = new String[mCur.getCount()];
         	 for(int i = 0; mCur.moveToNext(); i++){
         		cantones[i] = mCur.getString(0);
         	 }
         	 mCur.close();
             return cantones; 
	     } 
         catch (SQLException mSQLException){ 
             //Log.e(TAG, "getCantones >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 } 
     }
    
    public String[] getCentrosXCanton(String canton) { 
    	try{ 
		     String sql = new StringBuilder("SELECT nombre FROM CentroAcopio WHERE idCanton = ").append(getIdCanton(canton)).append(" ORDER BY nombre ASC").toString(); 
             Cursor mCur = mDb.rawQuery(sql, null);
             String[] centros = new String[mCur.getCount()];
         	 for(int i = 0; mCur.moveToNext(); i++){
         		centros[i] = mCur.getString(0);
         	 }
         	 mCur.close();
             return centros; 
	     } 
         catch (SQLException mSQLException){ 
             //Log.e(TAG, "getCentrosXCanton >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 } 
     }
    
    public String[] getMateriales() { 
    	try{ 
		     String sql = "SELECT nombre FROM Material ORDER BY nombre ASC"; 
             Cursor mCur = mDb.rawQuery(sql, null);
         	 String[] materiales = new String[mCur.getCount()];
         	 for(int i = 0; mCur.moveToNext(); i++){
         		materiales[i] = mCur.getString(0);
         	 }
         	 mCur.close();
             return materiales; 
	     } 
         catch (SQLException mSQLException){ 
             //Log.e(TAG, "getMateriales >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 } 
     }
    
    public Map<String,String[]> getMaterialesXCentro(String centro) { 
    	try{ 
		     String sql = new StringBuilder("SELECT nombre, cantidad, forma, costo FROM Material INNER JOIN Material_X_CentroAcopio ON Material_X_CentroAcopio.idCentroAcopio = ").append(getIdCentro(centro)).append(" AND Material_X_CentroAcopio.idMaterial = Material._id ORDER BY nombre ASC").toString(); 
             Cursor mCur = mDb.rawQuery(sql, null);
             Map<String,String[]> materiales = Collections.synchronizedMap(new LinkedHashMap<String, String[]>());;
             String[] array = new String[3];
             for(int i = 0; mCur.moveToNext(); i++){
            	array[0] = mCur.getString(1); //cantidad
            	array[1] = mCur.getString(2); //forma
            	array[2] = mCur.getString(3); //costo
            	materiales.put(mCur.getString(0), array);
         	 }
             mCur.close();
             return materiales; 
	     } 
         catch (SQLException mSQLException){ 
             //Log.e(TAG, "getMaterialesXCentro >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 } 
     }
    
    public List<String> getDetallesXCentro(String centro) { 
    	try{ 
		     String sql = new StringBuilder("SELECT direccion, horario, contacto FROM CentroAcopio WHERE _id = ").append(getIdCentro(centro)).toString(); 
             Cursor mCur = mDb.rawQuery(sql, null);
             List<String> detalles = new ArrayList<String>();
             mCur.moveToNext();
             detalles.add(mCur.getString(0));
             detalles.add(mCur.getString(1));
             detalles.add(mCur.getString(2));
             mCur.close();
             return detalles; 
	     } 
         catch (SQLException mSQLException){ 
             //Log.e(TAG, "getDetallesXCentro >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 } 
     }
    
    public String[] getNumeroTelefonoXCentro(String centro){
    	try{ 
    		String sql = new StringBuilder("SELECT numero FROM NumeroTelefono WHERE idCentroAcopio = ").append(getIdCentro(centro)).toString(); 
            Cursor mCur = mDb.rawQuery(sql, null);
            String[] numeros = new String[mCur.getCount()];
            for(int i = 0; mCur.moveToNext(); i++){
            	numeros[i] = mCur.getString(0);
         	}
            mCur.close();
            return numeros; 
	     } 
        catch (SQLException mSQLException){ 
            //Log.e(TAG, "getNumeroTelefonoXCentro >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 } 
    }
    
    public List<String[]> getCentrosXCategoria(String material) { 
    	try{  
		     String sql = new StringBuilder("SELECT Provincia.nombre, CentroAcopio.nombre  FROM CentroAcopio JOIN Material_X_CentroAcopio ON CentroAcopio._id = Material_X_CentroAcopio.idCentroAcopio AND Material_X_CentroAcopio.idMaterial = ").append(getIdMaterial(material)).append(" JOIN Canton ON Canton._id = CentroAcopio.idCanton JOIN Provincia ON Provincia._id =  Canton.idProvincia ORDER BY Provincia.nombre ASC").toString(); 
 		     Cursor mCur = mDb.rawQuery(sql, null);
             List<String[]> centros = new ArrayList<String[]>();            
             while(mCur.moveToNext()){
            	String[] centro = new String[2];
         		centro[0] = mCur.getString(0); 
         		centro[1] = mCur.getString(1);
         		centros.add(centro);
         	 }
             mCur.close();
             return centros; 
	     } 
         catch (SQLException mSQLException){ 
             //Log.e(TAG, "getCentrosXCategoria >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 } 
     }
    
    private String getIdProvincia(String nombre){
    	try{ 
		    String sql_idProvincia = new StringBuilder("SELECT _id FROM Provincia WHERE nombre = '").append(nombre).append("'").toString(); 
            Cursor cur = mDb.rawQuery(sql_idProvincia, null);
            cur.moveToNext();
            sql_idProvincia = cur.getString(0);
            cur.close();
            return sql_idProvincia; 
	     }
        catch (SQLException mSQLException){ 
            //Log.e(TAG, "getIdProvincia >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 }
    }
    
    private String getIdCanton(String nombre){
    	try{ 
		    String sql_idCanton = new StringBuilder("SELECT _id FROM Canton WHERE nombre = '").append(nombre).append("'").toString(); 
            Cursor cur = mDb.rawQuery(sql_idCanton, null);
            cur.moveToNext();
            sql_idCanton = cur.getString(0);
            cur.close();
            return sql_idCanton; 
	     }
        catch (SQLException mSQLException){ 
            //Log.e(TAG, "getIdCanton >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 }
    }
    
    private String getIdMaterial(String nombre){
    	try{ 
    		String sql_idMaterial = new StringBuilder("SELECT _id FROM Material WHERE nombre = '").append(nombre).append("'").toString(); 
            Cursor cur = mDb.rawQuery(sql_idMaterial, null);
            cur.moveToNext();
            sql_idMaterial = cur.getString(0);
            cur.close();
            return sql_idMaterial; 
	     }
        catch (SQLException mSQLException){ 
            //Log.e(TAG, "getIdMaterial >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 }
    }
    
    private String getIdCentro(String nombre){
    	try{ 
    		String sql_idCentro = new StringBuilder("SELECT _id FROM CentroAcopio WHERE nombre = '").append(nombre).append("'").toString(); 
            Cursor cur = mDb.rawQuery(sql_idCentro, null);
            cur.moveToNext();
            sql_idCentro = cur.getString(0);
            cur.close();
            return sql_idCentro; 
	     }
        catch (SQLException mSQLException){ 
            //Log.e(TAG, "getIdMaterial >>"+ mSQLException.toString()); 
		     throw mSQLException; 
		 }
    }

} 
