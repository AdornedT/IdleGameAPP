package com.finalproject.idlegame;

import java.io.File;

import android.content.ContentUris;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Uses the gitlab project https://gitlab.com/vandy-aad-3/HobbitContentProvider from Vanderbilt University
 * as the base and was changed to better accommodate this project.
 */
public class GameDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "GameDatabaseHelper";
    public static final String CONTENT_AUTHORITY = "com.finalproject.idlegame.gamecontentprovider";
    private static final String DATABASE_NAME = "idlegame_db";
    public static final String TABLE_NAME = "gameStats_table";
    final String SQL_CREATE_TABLE =
            "CREATE TABLE "
                    + TABLE_NAME + " ("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY, "
                    + GameDatabaseHelper.GameEntry.COLUMN_NAME + " TEXT NOT NULL, "
                    + GameDatabaseHelper.GameEntry.COLUMN_VALUE + " INTEGER NOT NULL "
                    + " );";

    private static int DATABASE_VERSION = 1;

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public GameDatabaseHelper(Context context) {
        super(context, context.getCacheDir() + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the table.
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete the existing tables.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create the new tables.
        onCreate(db);
    }

    public static final class GameEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(GameEntry.PATH_NAME).build();
        public static Uri buildUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String PATH_NAME = GameDatabaseHelper.TABLE_NAME;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_VALUE = "value";
        public static final String CONTENT_ITEMS_TYPE = "game.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + GameEntry.PATH_NAME;
        public static final String CONTENT_ITEM_TYPE = "game.android.cursor.item/" + CONTENT_AUTHORITY + "/" + GameEntry.PATH_NAME;
        public static final String sColumnsToDisplay [] =
                new String[] {
                        GameDatabaseHelper.GameEntry._ID,
                        GameDatabaseHelper.GameEntry.COLUMN_NAME,
                        GameDatabaseHelper.GameEntry.COLUMN_VALUE
                };
    }
}
