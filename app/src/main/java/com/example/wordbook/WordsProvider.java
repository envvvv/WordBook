package com.example.wordbook;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class WordsProvider extends ContentProvider {

    WordsDBHelper  mDbHelper=null;
    private static final int MULTIPLE_WORDS = 1;
    //UriMathcher匹配结果码
    private static final int SINGLE_WORD = 2;
    private  static final int TABLE_WORD=1;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(Words.AUTHORITY, Words.Word.TABLE_NAME, TABLE_WORD);
//        uriMatcher.addURI(Words.AUTHORITY, Words.Word.PATH_MULTIPLE, MULTIPLE_WORDS);
    }
    public WordsProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count  = 0;
        switch (uriMatcher.match(uri)) {
            case TABLE_WORD:
                    String whereClause=Words.Word._ID+"=?";
                    count = db.delete(Words.Word.TABLE_NAME,  whereClause, selectionArgs);
                return count;
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS://多条数据记录
                return Words.Word.MINE_TYPE_MULTIPLE;

            case SINGLE_WORD://单条数据记录
                return Words.Word.MINE_TYPE_SINGLE;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper=new WordsDBHelper(getContext());
        switch (uriMatcher.match(uri)){
            case TABLE_WORD:
                long rowID=db.insert(Words.Word.TABLE_NAME,null,values);
                Uri newUri=ContentUris.withAppendedId(Words.Word.CONTENT_URI,rowID);
                return newUri;
        }
    return null;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        mDbHelper=new WordsDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case TABLE_WORD:
                Cursor c = db.query(
                        Words.Word.TABLE_NAME,  // The table to query
                        projection,                               // The columns to return
                        selection,
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        sortOrder                                 // The sort order
                );
                return  c;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case TABLE_WORD:
                count = db.update(Words.Word.TABLE_NAME, values, Words.Word._ID+"=?", selectionArgs);
        }
        return count;
    }
}
