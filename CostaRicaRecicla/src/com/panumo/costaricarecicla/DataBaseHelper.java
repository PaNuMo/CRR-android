package com.panumo.costaricarecicla;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{
	
	//private static String TAG = "DataBaseHelper"; 
    private static String DB_PATH = "";
    
	private static final String DB_NAME = "CRR";
	
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private static final int DATABASE_VERSION = 1;
	
	
	public DataBaseHelper(Context context){
		super(context, DB_NAME, null, DATABASE_VERSION);
		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/"; 
		this.myContext = context;
	}
	
	/**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase(){
    	boolean dbExist = checkDataBase();
    	if(!dbExist){
    		this.getReadableDatabase();
        	this.close();
        	try { 
    			copyDataBase();
    			//Log.i(TAG, "createDataBase: Database Created"); 
    		} catch(IOException e){
        		throw new Error("Error copying database"); 
        	}
    	}
    }
    
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * and also check if the DB need to be updated.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		//database doesn't exist yet.
    	}
    	if(checkDB != null){
    		if (checkDB.getVersion() != DATABASE_VERSION) {
    			File file = myContext.getDatabasePath(DB_NAME);
    	        file.delete();
    	        //Log.i(TAG, "updateDataBase: Database will be updated"); 
	    		checkDB.close();
	    		return false;
			} 
    		else{
				checkDB.close();
				return true;
    		}
		} 
    	else return false;
    }
    
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{ 
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
    	File outFileName = myContext.getDatabasePath(DB_NAME);
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
    
    public boolean openDataBase() throws SQLException{   	 
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    	return myDataBase != null; 
    }
	
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv){
	}
	
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
	}
	
	@Override
	public synchronized void close() {
	    if(myDataBase != null) myDataBase.close();
	    super.close();
	}
}

