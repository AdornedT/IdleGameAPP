package com.finalproject.idlegame;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;


/**
 * Support class that consolidates and simplifies operations on the
 * HobbitContentProvider.
 */
public class GameContentOps {

    private final static String TAG = "GameContentOps";
    /**
     * Reference back to the HobbitActivity.
     */
    private final MainActivity mActivity;

    /**
     * Contains the most recent result from a query so the display can
     * be updated after a runtime configuration change.
     */
    private Cursor mCursor;

    /**
     * Define the Proxy for accessing the HobbitContentProvider.
     */
    private ContentResolver mCr;

    /**
     * All the races in our program.
     */
    private static String[] sAllRaces = new String[]{
            "Dwarf",
            "Maia",
            "Hobbit",
            "Dragon",
            "Human",
            "Bear"
    };

    /**
     * Constructor initializes the fields.
     */
    public GameContentOps(MainActivity activity) {
        mActivity = activity;
        mCr = mActivity.getContentResolver();
    }

    /**
     * Insert a Hobbit @a character of a particular @a race into
     * the HobbitContentProvider.
     */
    public Uri insertHelper(String dataName,
                      int value) throws RemoteException {
        final ContentValues cvs = new ContentValues();

        // Insert data.
        cvs.put(GameDatabaseHelper.GameEntry.COLUMN_NAME,
                dataName);
        cvs.put(GameDatabaseHelper.GameEntry.COLUMN_VALUE,
                value);

        // Insert the content at the designated URI.
        return insert(GameDatabaseHelper.GameEntry.CONTENT_URI,
                cvs);
    }

    /**
     * Insert @a ContentValues into the HobbitContentProvider at
     * the @a uri.
     */
    protected Uri insert(Uri uri, ContentValues cvs) {
        return mCr.insert(uri, cvs);
    }

    /**
     * Insert an array of Hobbit @a characters of a particular @a
     * race into the HobbitContentProvider.
     */
    public int bulkInsert(String[] characters,
                          String race) throws RemoteException {
        // Use ContentValues to store the values in appropriate
        // columns, so that ContentResolver can process it.  Since
        // more than one rows needs to be inserted, an Array of
        // ContentValues is needed.
        ContentValues[] cvsArray =
                new ContentValues[characters.length];

        // Index counter.
        int i = 0;

        // Insert all the characters into the ContentValues array.
        for (String character : characters) {
            ContentValues cvs = new ContentValues();
            cvs.put(GameDatabaseHelper.GameEntry.COLUMN_NAME,
                    character);
            cvs.put(GameDatabaseHelper.GameEntry.COLUMN_VALUE,
                    race);
            cvsArray[i++] = cvs;
        }

        // Insert the array of content at the designated URI.
        return bulkInsert
                (GameDatabaseHelper.GameEntry.CONTENT_URI,
                        cvsArray);
    }

    /**
     * Insert an array of @a ContentValues into the
     * HobbitContentProvider at the @a uri.
     */
    protected int bulkInsert(Uri uri,
                             ContentValues[] cvsArray) {
        return mCr.bulkInsert(uri,
                cvsArray);
    }

    /**
     * Return a Cursor from a query on the HobbitContentProvider at
     * the @a uri.
     */
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        return mCr.query(uri,
                projection,
                selection,
                selectionArgs,
                sortOrder);

    }

    /**
     * Update the @a name and @a race of a Hobbit character at a
     * designated @a uri from the HobbitContentProvider.
     */
    public int updateByUri(Uri uri,
                           String name,
                           int value) throws RemoteException {
        // Initialize the content values.
        final ContentValues cvs = new ContentValues();
        cvs.put(GameDatabaseHelper.GameEntry.COLUMN_NAME, name);
        cvs.put(GameDatabaseHelper.GameEntry.COLUMN_VALUE, value);

        // Update the content at the designated URI.
        return update(uri,
                cvs,
                null,
                null);
    }

    /**
     * Update the @a race of a Hobbit character with a given
     *
     * @a name in the HobbitContentProvider.
     */
    public int updateRaceByName(String name,
                                String race) throws RemoteException {
        // Initialize the content values.
        final ContentValues cvs = new ContentValues();
        cvs.put(GameDatabaseHelper.GameEntry.COLUMN_NAME,
                name);
        cvs.put(GameDatabaseHelper.GameEntry.COLUMN_VALUE,
                race);

        // Update the content at the designated URI.
        return update(GameDatabaseHelper.GameEntry.CONTENT_URI,
                cvs,
                GameDatabaseHelper.GameEntry.COLUMN_NAME,
                new String[]{name});
    }

    /**
     * Update the @a selection and @a selectionArgs with the @a
     * ContentValues in the HobbitContentProvider at the @a uri.
     */
    public int update(Uri uri,
                      ContentValues cvs,
                      String selection,
                      String[] selectionArgs) {


        return mCr.update(uri,
                cvs,
                selection,
                selectionArgs);
    }

    /**
     * Delete an array of Hobbit @a characterNames from the
     * HobbitContentProvider.
     */
    public int deleteByName(String[] characterNames)
            throws RemoteException {
        return delete(GameDatabaseHelper.GameEntry.CONTENT_URI,
                GameDatabaseHelper.GameEntry.COLUMN_NAME,
                characterNames);
    }

    /**
     * Delete an array of Hobbit @a characterRaces from the
     * HobbitContentProvider.
     */
    public int deleteByRace(String[] characterRaces)
            throws RemoteException {
        return delete(GameDatabaseHelper.GameEntry.CONTENT_URI,
                GameDatabaseHelper.GameEntry.COLUMN_VALUE,
                characterRaces);
    }

    /**
     * Delete the @a selection and @a selectionArgs from the
     * HobbitContentProvider at the @a uri.
     */
    protected int delete(Uri uri,
                         String selection,
                         String[] selectionArgs) {
        return mCr.delete(uri, selection, selectionArgs);
    }

    /**
     * Delete all characters from the HobbitContentProvider.
     */
    public int deleteAll()
            throws RemoteException {
        return delete(GameDatabaseHelper.GameEntry.CONTENT_URI,
                null,
                null);
    }

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
            Log.d(TAG, "Values found");
            return true;
        }
        return false;
    }

    public double[] getValueFromTable(String[] nameOfValues) throws RemoteException {
        // Query for all characters in the HobbitContentProvider by their race.
        mCursor = query(GameDatabaseHelper.GameEntry.CONTENT_URI,
                GameDatabaseHelper.GameEntry.sColumnsToDisplay,
                GameDatabaseHelper.GameEntry.COLUMN_NAME,
                nameOfValues,
                null);

        int walker = 0;
        //{money, factories, currentFactoryCost}
        double[] gameDataDouble = {8.0, 9.0, 10.0};

        if (mCursor != null ) {
            if  (mCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int auxValue = mCursor.getInt(mCursor.getColumnIndex("value"));
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