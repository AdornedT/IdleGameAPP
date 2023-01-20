package com.finalproject.idlegame;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.net.Uri;

import androidx.annotation.NonNull;

/**
 * Uses the gitlab project https://gitlab.com/vandy-aad-3/HobbitContentProvider from Vanderbilt University
 * as the base and was changed to better accommodate this project.
 */
public class GameContentProvider extends ContentProvider {

    protected final static String TAG = "GameContentProvider";

    private GameDatabaseHelper mOpenHelper;

    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mOpenHelper = new GameDatabaseHelper(mContext);
        return true;
    }

    public static final int NAME = 100;

    public static final int NAMES = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    protected static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Creates the URI matches for latter query.
        matcher.addURI(GameDatabaseHelper.CONTENT_AUTHORITY, GameDatabaseHelper.GameEntry.PATH_NAME, NAME);
        matcher.addURI(GameDatabaseHelper.CONTENT_AUTHORITY, GameDatabaseHelper.GameEntry.PATH_NAME + "/#", NAMES);
        return matcher;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // Match the id returned by UriMatcher to return appropriate MIME_TYPE.
        switch (sUriMatcher.match(uri)) {
            case NAME:
                return GameDatabaseHelper.GameEntry.CONTENT_ITEMS_TYPE;
            case NAMES:
                return GameDatabaseHelper.GameEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues cvs) {
        Uri returnUri;

        //Matches URI to path.
        switch (sUriMatcher.match(uri)) {
            case NAME:
                returnUri = insertMultipleRows(uri, cvs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notifies registered observers that a row was inserted.
        mContext.getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    private Uri insertMultipleRows(Uri uri, ContentValues cvs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        long id = db.insert(GameDatabaseHelper.TABLE_NAME, null, cvs);

        // Check if a new row is inserted or not.
        if (id > 0)
            return GameDatabaseHelper.GameEntry.buildUri(id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int bulkInsert(@NonNull Uri uri,
                          ContentValues[] cvsArray) {

        //Matches URI to path.
        switch (sUriMatcher.match(uri)) {
            case NAME:
                int returnCount = bulkInsertCharacters(uri, cvsArray);

                if (returnCount > 0)
                    // Notifies registered observers that row(s) were
                    // inserted.
                    mContext.getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private int bulkInsertCharacters(Uri uri, ContentValues[] cvsArray) {
        // Create and/or open a database that will be used for reading
        // and writing. Once opened successfully, the database is
        // cached, so you can call this method every time you need to
        // write to the database.
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int returnCount = 0;

        // Begins a transaction in EXCLUSIVE mode.
        db.beginTransaction();
        try {
            for (ContentValues cvs : cvsArray) {
                final long id = db.insert(GameDatabaseHelper.TABLE_NAME, null, cvs);
                if (id != -1)
                    returnCount++;
            }

            // Marks the current transaction as successful.
            db.setTransactionSuccessful();
        } finally {
            // End a transaction.
            db.endTransaction();
        }
        return returnCount;
    }

    @Override
    public Cursor query(@NonNull Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;

        // Match the id returned by UriMatcher to query appropriate
        // rows.
        switch (sUriMatcher.match(uri)) {
            case NAME:
                cursor = queryCharacters(uri,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);
                break;
            case NAMES:
                cursor = queryCharacter(uri,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }

        // Register to watch a content URI for changes.
        cursor.setNotificationUri(mContext.getContentResolver(),
                uri);
        return cursor;
    }


    private Cursor queryCharacters(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Expand the selection if necessary.
        selection = addSelectionArgs(selection,
                selectionArgs,
                "OR");
        return mOpenHelper.getReadableDatabase().query
                (GameDatabaseHelper.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
    }

    private Cursor queryCharacter(Uri uri,
                                  String[] projection,
                                  String selection,
                                  String[] selectionArgs,
                                  String sortOrder) {
        // Query the SQLite database for the particular rowId based on
        // (a subset of) the parameters passed into the method.
        return mOpenHelper.getReadableDatabase().query
                (GameDatabaseHelper.TABLE_NAME,
                        projection,
                        addKeyIdCheckToWhereStatement(selection,
                        ContentUris.parseId(uri)),
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues cvs, String selection, String[] selectionArgs) {

        int returnCount;

        // Match the id returned by UriMatcher to query appropriate
        // rows.
        switch (sUriMatcher.match(uri)) {
            case NAME:
                returnCount = updateName(uri,
                        cvs,
                        selection,
                        selectionArgs);
                break;
            case NAMES:
                returnCount =  updateNames(uri,
                        cvs,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        if (returnCount > 0)
            // Notifies registered observers that row(s) were
            // updated.
            mContext.getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    private int updateName(Uri uri, ContentValues cvs, String selection, String[] selectionArgs) {
        // Expand the selection if necessary.
        selection = addSelectionArgs(selection,
                selectionArgs,
                " OR ");
        return mOpenHelper.getWritableDatabase().update
                (GameDatabaseHelper.TABLE_NAME,
                    cvs,
                    selection,
                    selectionArgs);
    }

    private int updateNames(Uri uri, ContentValues cvs, String selection, String[] selectionArgs) {
        // Expand the selection if necessary.
        selection = addSelectionArgs(selection,
                selectionArgs,
                " OR ");
        // Just update a single row in the database.
        return mOpenHelper.getWritableDatabase().update
                (GameDatabaseHelper.TABLE_NAME,
                        cvs,
                        addKeyIdCheckToWhereStatement(selection,
                        ContentUris.parseId(uri)),
                        selectionArgs);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int returnCount;

        // Match the id returned by UriMatcher to query appropriate
        // rows.
        switch (sUriMatcher.match(uri)) {
            case NAME:
                returnCount = deleteCharacters(uri, selection, selectionArgs);
                break;
            case NAMES:
                returnCount =  deleteCharacter(uri, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        if (selection == null || returnCount > 0)
            // Notifies registered observers that row(s) were deleted.
            mContext.getContentResolver().notifyChange(uri, null);

        return returnCount;
    }

    private int deleteCharacters(Uri uri, String selection, String[] selectionArgs) {
        // Expand the selection if necessary.
        selection = addSelectionArgs(selection, selectionArgs, " OR ");
        return mOpenHelper.getWritableDatabase().delete(GameDatabaseHelper.TABLE_NAME, selection, selectionArgs);
    }

    private int deleteCharacter(Uri uri, String selection, String[] selectionArgs) {
        // Expand the selection if necessary.
        selection = addSelectionArgs(selection, selectionArgs, " OR ");
        // Just delete a single row in the database.
        return mOpenHelper.getWritableDatabase().delete(GameDatabaseHelper.TABLE_NAME, addKeyIdCheckToWhereStatement(selection, ContentUris.parseId(uri)), selectionArgs);
    }

    private String addSelectionArgs(String selection,
                                    String [] selectionArgs,
                                    String operation) {
        // Handle the "null" case.
        if (selection == null || selectionArgs == null)
            return null;
        else {
            String selectionResult = "";

            // Properly add the selection args to the selectionResult.
            for (int i = 0; i < selectionArgs.length - 1; ++i)
                selectionResult += (selection + " = ? " + operation + " ");

            // Handle the final selection case.
            selectionResult += (selection + " = ?");

            return selectionResult;
        }
    }

    private static String addKeyIdCheckToWhereStatement(String whereStatement,
                                                        long id) {
        String newWhereStatement;
        if (TextUtils.isEmpty(whereStatement))
            newWhereStatement = "";
        else
            newWhereStatement = whereStatement + " AND ";

        // Append the key id to the end of the WHERE statement.
        return newWhereStatement + GameDatabaseHelper.GameEntry._ID + " = '" + id + "'";
    }
}