package com.example.quranapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.quranapp.model.Bookmark;
import java.util.ArrayList;
import java.util.List;

public class BookmarkDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuranApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_BOOKMARK = "bookmarks";
    private static final String COLUMN_SURAH_NUMBER = "surah_number";
    private static final String COLUMN_AYAT_NUMBER = "ayat_number";
    private static final String COLUMN_SURAH_NAME = "surah_name";
    private static final String COLUMN_ARABIC = "arabic";
    private static final String COLUMN_TRANSLATION = "translation";

    public BookmarkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_BOOKMARK + " (" +
                COLUMN_SURAH_NUMBER + " INTEGER, " +
                COLUMN_AYAT_NUMBER + " INTEGER, " +
                COLUMN_SURAH_NAME + " TEXT, " +
                COLUMN_ARABIC + " TEXT, " +
                COLUMN_TRANSLATION + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_SURAH_NUMBER + ", " + COLUMN_AYAT_NUMBER + "))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);
        onCreate(db);
    }

    public void addBookmark(Bookmark bookmark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SURAH_NUMBER, bookmark.getSurahNumber());
        values.put(COLUMN_AYAT_NUMBER, bookmark.getAyatNumber());
        values.put(COLUMN_SURAH_NAME, bookmark.getSurahName());
        values.put(COLUMN_ARABIC, bookmark.getArabic());
        values.put(COLUMN_TRANSLATION, bookmark.getTranslation());
        db.insertWithOnConflict(TABLE_BOOKMARK, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public void deleteBookmark(int surahNumber, int ayatNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKMARK,
                COLUMN_SURAH_NUMBER + " = ? AND " + COLUMN_AYAT_NUMBER + " = ?",
                new String[]{String.valueOf(surahNumber), String.valueOf(ayatNumber)});
        db.close();
    }

    public List<Bookmark> getAllBookmarks() {
        List<Bookmark> bookmarks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKMARK, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int surahNumber = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SURAH_NUMBER));
                int ayatNumber = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AYAT_NUMBER));
                String surahName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURAH_NAME));
                String arabic = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARABIC));
                String translation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRANSLATION));
                bookmarks.add(new Bookmark(surahNumber, ayatNumber, surahName, arabic, translation));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookmarks;
    }

    public boolean isBookmarked(int surahNumber, int ayatNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKMARK,
                new String[]{COLUMN_SURAH_NUMBER},
                COLUMN_SURAH_NUMBER + " = ? AND " + COLUMN_AYAT_NUMBER + " = ?",
                new String[]{String.valueOf(surahNumber), String.valueOf(ayatNumber)},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}