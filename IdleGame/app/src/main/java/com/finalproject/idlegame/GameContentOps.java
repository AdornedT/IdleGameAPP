package com.finalproject.idlegame;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import android.os.RemoteException;
import android.util.Log;

/**
 * Uses the gitlab project https://gitlab.com/vandy-aad-3/HobbitContentProvider from Vanderbilt University
 * as the base and was changed to better accommodate this project.
 */

public class GameContentOps {

    private final static String TAG = "GameContentOps";

    private final MainActivity mActivity;

    private Cursor mCursor;

    private ContentResolver mContentResolver;

    public GameContentOps(MainActivity activity) {
        mActivity = activity;
        mContentResolver = mActivity.getContentResolver();
    }

    public Uri insertHelper(String dataName, int value) throws RemoteException {
        final ContentValues cvs = new ContentValues();

        // Insert data.
        cvs.put(GameDatabaseHelper.GameEntry.COLUMN_NAME, dataName);
        cvs.put(GameDatabaseHelper.GameEntry.COLUMN_VALUE, value);

        // Insert the content at the designated URI.
        return insert(GameDatabaseHelper.GameEntry.CONTENT_URI, cvs);
    }

    //Called to insert data into table from various helper functions.
    protected Uri insert(Uri uri, ContentValues cvs) {
        return mContentResolver.insert(uri, cvs);
    }

    //Query for the cursor to find the data
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        return mContentResolver.query(uri,
                projection,
                selection,
                selectionArgs,
                sortOrder);

    }

    //Updates the value by the name. Ex: giving money as the argument
    //will let the method know you want to update the value column that
    //is in the same row.
    public int updateValueByName(String name,
                                int value) throws RemoteException {
        // Initialize the content values.
        final ContentValues cvs = new ContentValues();
        cvs.put(GameDatabaseHelper.GameEntry.COLUMN_NAME, name);
        cvs.put(GameDatabaseHelper.GameEntry.COLUMN_VALUE, value);

        // Update the content at the designated URI.
        return update(GameDatabaseHelper.GameEntry.CONTENT_URI, cvs, GameDatabaseHelper.GameEntry.COLUMN_NAME, new String[]{name});
    }

    //Update function called by helper update methods
    public int update(Uri uri,
                      ContentValues cvs,
                      String selection,
                      String[] selectionArgs) {


        return mContentResolver.update(uri, cvs, selection, selectionArgs);
    }

    //Used to see if it is a new game.
    public boolean areThereValuesInTable(String[] nameOfValues) throws RemoteException{
        // Query for all characters in the HobbitContentProvider by their race.
        mCursor = query(GameDatabaseHelper.GameEntry.CONTENT_URI,
                GameDatabaseHelper.GameEntry.sColumnsToDisplay,
                GameDatabaseHelper.GameEntry.COLUMN_NAME,
                nameOfValues,
                null);

        //If this is true the game data is already at the SQL table.
        Log.d(TAG, "Count table: " + mCursor.getCount());
        if(mCursor.getCount() > 0){
            Log.d(TAG, "Old game found");
            return true;
        }
        return false;
    }

    //Gathers data from table to restart game where it was left of after saving.
    public double[] getValueFromTable(String[] nameOfValues) throws RemoteException {
        // Query for all characters in the HobbitContentProvider by their race.
        mCursor = query(GameDatabaseHelper.GameEntry.CONTENT_URI,
                GameDatabaseHelper.GameEntry.sColumnsToDisplay,
                GameDatabaseHelper.GameEntry.COLUMN_NAME,
                nameOfValues,
                null);

        int walker = 0;
        //{money, factories, upgradesBought}
        //Starting values are just for debugging purposes.
        double[] gameDataDouble = {8.0, 9.0, 1.0};

        if (mCursor != null ) {
            if  (mCursor.moveToFirst()) {
                do {
                    int test = mCursor.getColumnIndex("value");
                    @SuppressLint("Range") int auxValue = mCursor.getInt(test);
                    Log.d(TAG, "TESTING VALUES: " +auxValue+ " index id: " + mCursor.getColumnIndex("value"));
                    gameDataDouble[walker] = (double)auxValue;
                    walker++;
                }while (mCursor.moveToNext());
            }
        }
        for (double aux: gameDataDouble) {
            Log.d(TAG, "double: " + aux);
        }
        return gameDataDouble;
    }
}