package com.finalproject.idlegame;

import java.io.File;

import android.content.ContentUris;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

/**
 * The database helper used by the Hobbit Content Provider to create
 * and manage its underlying SQLite database.
 */
public class GameDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "GameDatabaseHelper";
    /**
     * This ContentProvider's unique identifier.
     */
    public static final String CONTENT_AUTHORITY = "com.finalproject.idlegame.gamecontentprovider";

    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "idlegame_db";

    /**
     * Database version number, which is updated with each schema
     * change.
     */
    private static int DATABASE_VERSION = 1;


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Name of the database table.
     */
    public static final String TABLE_NAME = "gameStats_table";

    /*
     * SQL create table statements.
     */

    /**
     * SQL statement used to create the Hobbit table.
     */
    final String SQL_CREATE_TABLE =
            "CREATE TABLE "
                    + TABLE_NAME + " ("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY, "
                    + GameDatabaseHelper.GameEntry.COLUMN_NAME + " TEXT NOT NULL, "
                    + GameDatabaseHelper.GameEntry.COLUMN_VALUE + " TEXT NOT NULL "
                    + " );";

    /**
     * Constructor - initialize database name and version, but don't
     * actually construct the database (which is done in the
     * onCreate() hook method). It places the database in the
     * application's cache directory, which will be automatically
     * cleaned up by Android if the device runs low on storage space.
     *
     * @param context Any context
     */
    public GameDatabaseHelper(Context context) {
        super(context,
                context.getCacheDir()
                        + File.separator
                        + DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

    /**
     * Hook method called when the database is created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table.
        Log.d(TAG, SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * Hook method called when the database is upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        // Delete the existing tables.
        db.execSQL("DROP TABLE IF EXISTS "
                + TABLE_NAME);
        // Create the new tables.
        onCreate(db);
    }

    public static final class GameEntry implements BaseColumns {
        /**
         * Use BASE_CONTENT_URI to create the unique URI for Acronym
         * Table that apps will use to contact the content provider.
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(GameEntry.PATH_CHARACTER).build();

        /**
         * When the Cursor returned for a given URI by the
         * ContentProvider contains 0..x items.
         */
        public static final String CONTENT_ITEMS_TYPE =
                "game.android.cursor.dir/"
                        + CONTENT_AUTHORITY
                        + "/"
                        + GameEntry.PATH_CHARACTER;

        /**
         * When the Cursor returned for a given URI by the
         * ContentProvider contains 1 item.
         */
        public static final String CONTENT_ITEM_TYPE =
                "game.android.cursor.item/"
                        + CONTENT_AUTHORITY
                        + "/"
                        + GameEntry.PATH_CHARACTER;

        /**
         * Columns to display.
         */
        public static final String sColumnsToDisplay [] =
                new String[] {
                        GameDatabaseHelper.GameEntry._ID,
                        GameDatabaseHelper.GameEntry.COLUMN_NAME,
                        GameDatabaseHelper.GameEntry.COLUMN_VALUE
                };

        /**
         * Possible paths (appended to base content URI for possible
         * URI's), e.g., a valid path for looking at Character data is
         * content://vandy.mooc.hobbitcontentprovider/character_table .
         * However, content://vandy.mooc.hobbitcontentprovider/givemeroot
         * will fail, as the ContentProvider hasn't been given any
         * information on what to do with "givemeroot".
         */
        public static final String PATH_CHARACTER = GameDatabaseHelper.TABLE_NAME;

        /**
         * Columns to store data.
         */
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_VALUE = "value";

        /**
         * Return a Uri that points to the row containing a given id.
         *
         * @param id row id
         * @return Uri URI for the specified row id
         */
        public static Uri buildUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
