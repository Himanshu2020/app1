package com.musipo.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.musipo.dao.DBAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

//import temp.TempData;

public class DatabaseUtils extends android.database.DatabaseUtils{

	public static final String TAG = DatabaseUtils.class.getSimpleName();

	public static void pullDatabaseFile(Context context){

        try {
            File sd = Environment.getExternalStorageDirectory();
//            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = context.getDatabasePath(DBAdapter.DATABASE_NAME).getPath();
                String backupDBPath = DBAdapter.DATABASE_NAME;

                Log.i(TAG, "currentDBPath :: "+currentDBPath
                		  +"\nbackupDBPath :: "+backupDBPath);

                Log.i(TAG, "database path :: "+context.getDatabasePath(DBAdapter.DATABASE_NAME).getPath());

               /* Toast.makeText(context, "currentDBPath :: "+currentDBPath
                		  +"\nbackupDBPath :: "+backupDBPath, Toast.LENGTH_LONG).show();*/

                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.i(TAG, "Database pulled successfully");

                }
            }
        } catch (Exception e) {
        	Log.e(TAG, "Exception while pulling Database file : "+e.getLocalizedMessage(), e);
        }
    }

	public static final void initializeDatabase(Context context, boolean isSessionRestored){
		// Initialize database adapter
		DBAdapter db = DBAdapter.initialize(context);
		if(!isSessionRestored){
			db.open();
            pullDatabaseFile(context);
			db.reCreateTables();
			db.close();
		}
	}

	public static final void insertTempData(Context context,Activity activity) throws IOException {
        MarshmallowPermission marshmallowPermission = new MarshmallowPermission();

        if(!marshmallowPermission.checkPermissionForExternalStorage(activity)) {
            marshmallowPermission.requestPermissionForExternalStorage(activity);
        } else {
            // can write to external
            File sd = Environment.getExternalStorageDirectory();
            String currentDBPath = context.getDatabasePath(DBAdapter.DATABASE_NAME).getPath();
            String backupDBPath = DBAdapter.DATABASE_NAME;

            Log.i(TAG, "currentDBPath :: "+currentDBPath
                    +"\nbackupDBPath :: "+backupDBPath);

            Log.i(TAG, "database path :: "+context.getDatabasePath(DBAdapter.DATABASE_NAME).getPath());

               /* Toast.makeText(context, "currentDBPath :: "+currentDBPath
                		  +"\nbackupDBPath :: "+backupDBPath, Toast.LENGTH_LONG).show();*/
            File currentDB = new File(currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.i(TAG, "Database pulled successfully");

            }
        }
	}




}
