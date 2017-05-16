package com.opensourceassignment.singkaraoke;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Kernel Dang on 12/16/2015.
 */
public class KaraokeSongDBHelper extends SQLiteOpenHelper {
    private static final String DB_PATH = "/data/data/com.opensourceassignment.singkaraoke/databases/";
    private static final String DB_NAME = "KaraokeSongDB.sqlite";

    private static final String TABLE_SONG = "Song";
    private static final String COLUMN_ID = "SongId";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_ARTIST = "Artist";
    private static final String COLUMN_LYRIC = "Lyric";
    private static final String COLUMN_FAVORITE = "Favorite";

    private static final String TABLE_ARIRANG = "Arirang";
    private static final String COLUMN_ARIRANG_CODE = "ArirangCode";

    private static final String TABLE_CALIFORNIA = "California";
    private static final String COLUMN_CALIFORNIA_CODE = "CaliforniaCode";

    private static final String UNKOWN_STRING = "Chưa cập nhật";

    private SQLiteDatabase _karaokeSongDB;
    private final Context _context;

    private static KaraokeSongDBHelper _instance;

    private KaraokeSongDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        _context = context;
    }

    public static KaraokeSongDBHelper getInstance(Context context){
        if(_instance == null){
                _instance = new KaraokeSongDBHelper(context);
                try{
                    _instance.initDataBase();
                }
                catch (Exception e){
                    // close applocation when exception was threw
                }
        }
        return _instance;
    }

    /**
     * Lựa chọn database cho ứng dụng: nếu database đã tồn tại thì lấy ra ngược lại copy db đã add sẵn trong assets folder
     */
    public void initDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            // db đã tồn tại
            return;
        } else {
            // tạo ra 1 db mới rỗng, lấy db này cho ứng dụng
            this.getReadableDatabase();
            try {
                // copy db từ file db đã có sẵn vào trong db hiện tại của ứng dụng
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Kiểm tra xem db đã tồn tại trong bộ nhớ vật lý hay không?
     *
     * @return true nếu db đã tồn tại ngược lại @return false
     */
    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // db không tồn tại
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return (checkDB != null) ? true : false;
    }

    /**
     * Copy db từ db đã tồn tại trong bộ nhớ vật lý ở thư mục assets
     */
    private void copyDataBase() throws IOException {
        InputStream myInput = _context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Mở kết nối tới db
     */
    private void open() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        _karaokeSongDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Override phương thức đóng kết nối tới db
     */
    @Override
    public synchronized void close() {
        if (_karaokeSongDB != null)
            _karaokeSongDB.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // đánh dấu bài hát yêu thích
    public void markFavoriteSong(int songId, boolean favorite){
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Favorite",favorite);
        _karaokeSongDB.update("Song", contentValues, "SongId = " + songId, null);
        close();
    }

    // Lấy tất cả bài hát
    public ArrayList<Song> getAllSong(){
        open();
        ArrayList<Song> songs = new ArrayList<>();
        String sqlStatement = "SELECT SongId, Title, Artist, Favorite FROM Song ORDER BY Title ASC";
        Cursor c = _karaokeSongDB.rawQuery(sqlStatement, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Song song = new Song(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3) == 1);
            songs.add(song);
        }
        close();
        return songs;
    }

    public String getLyricPreview(int songId){
        open();
        String sqlStatement = "SELECT Lyric FROM Song WHERE SongId = " + songId;
        Cursor c = _karaokeSongDB.rawQuery(sqlStatement, null);
        c.moveToFirst();
        String lyric = c.getString(0);
        close();

        if(lyric == null || lyric.equals(""))
            return UNKOWN_STRING;
        if(lyric.length() > 50)
            return lyric.substring(0, 50);
        return lyric;
    }

    public String getLyric(int songId){
        open();
        String sqlStatement = "SELECT Lyric FROM Song WHERE SongId = " + songId + " ORDER BY Title ASC";
        Cursor c = _karaokeSongDB.rawQuery(sqlStatement, null);
        c.moveToFirst();
        String lyric = c.getString(0);
        close();

        if(lyric == null || lyric.equals(""))
            return UNKOWN_STRING;
        return lyric;
    }

    public String getArirangCode(int songId) {
        open();
        String sqlStatement = "SELECT ArirangCode FROM Song as S INNER JOIN Arirang as A ON S.SongId = A.SongId WHERE S.SongId = " + songId;
        Cursor c = _karaokeSongDB.rawQuery(sqlStatement, null);
        c.moveToFirst();
        if(c.isAfterLast()){
            close();
            return UNKOWN_STRING;
        }

        String arirangCode = c.getString(0);
        close();

        if(arirangCode == null || arirangCode.equals(""))
            return UNKOWN_STRING;
        return arirangCode;
    }

    public String getCaliforniaCode(int songId) {
        open();
        String sqlStatement = "SELECT CaliforniaCode FROM Song as S INNER JOIN California as C ON S.SongId = C.SongId WHERE S.SongId = " + songId;
        Cursor c = _karaokeSongDB.rawQuery(sqlStatement, null);
        c.moveToFirst();
        if(c.isAfterLast()){
            close();
            return UNKOWN_STRING;
        }

        String californiaCode = c.getString(0);
        close();
        if(californiaCode == null || californiaCode.equals(""))
            return UNKOWN_STRING;
        return californiaCode;
    }
}
