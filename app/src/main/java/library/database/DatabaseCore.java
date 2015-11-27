package library.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import model.Bookmark;

public class DatabaseCore extends SQLiteOpenHelper
{
    // All Static variables
    // Database Version
    private  final static int DATABASE_VERSION = 1;
    // Database Name
    // Contacts table name
    public  final static String TABLE_Bookmark = "Bookmark";
    // Contacts Table Columns names
    private  final String KEY_ID = "id";
    private  final String userId = "userId";
    private  final String IdArtical = "IdArtical";
    private  final String thumbImg = "thumbImg";
    private  final String title = "title";
    private  final String subtitle = "subtitle";
    private  final String sources = "sources";
    private  final String createDate = "createDate";
    private  final String bookmark_status = "bookmark_status";
    private  final String favorite = "favorite";
    private SQLiteDatabase mDb;
    int number_page = 20;

    private static String DB_NAME ="Bongda365";// Database name
    private final Context mContext;


    public DatabaseCore(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);// 1? its Database Version
        this.mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CREATE_BOOKMARK_TABLE = "CREATE TABLE " + TABLE_Bookmark + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +
                userId + " TEXT,"
                + IdArtical + " TEXT," + thumbImg + " TEXT,"
                + title + " TEXT,"+ subtitle +" TEXT," + sources + " TEXT," +
                createDate + " TEXT," +
                favorite + " TEXT,"   +
                bookmark_status + " TEXT" + ")";

        db.execSQL(CREATE_BOOKMARK_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Bookmark);
        // Create tables again
        onCreate(db);
    }
    public void addBookmark(Bookmark bookmark) {
        synchronized (this) {
            Cursor c = null;
            try {
                if (!isOpen())
                    open();
                if (mDb != null) {
                    if (!mDb.isOpen()) {
                        mDb = getWritableDatabase();
                    }
                } else {
                    mDb = getWritableDatabase();
                }
                ContentValues values = new ContentValues();
                values.put(IdArtical, bookmark.getIDNews()); // Contact Name
                values.put(userId, bookmark.getUserID());
                values.put(thumbImg, bookmark.getThumbImg());
                values.put(title, bookmark.getTitle()); //
                values.put(subtitle, bookmark.getSubTitle());
                values.put(sources, bookmark.getSources());
                values.put(createDate, bookmark.getCreateDate());
                values.put(favorite, bookmark.getFavorite());
                values.put(bookmark_status, bookmark.getBookmark_status());
                // Inserting Row
                mDb.insert(TABLE_Bookmark, null, values);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (isOpen())
                    close();
                if (c != null)
                    c.close();
            }
        }
    }
    public ArrayList<Bookmark> getAllBookMark() {
        synchronized (this) {
            Cursor cursor = null;
            try {
                if (!isOpen())
                    open();
                if (mDb != null) {
                    if (!mDb.isOpen())
                        mDb = getWritableDatabase();
                } else
                    mDb = getWritableDatabase();
                ArrayList<Bookmark> array_bookmark = new ArrayList<Bookmark>();
                // Select All Query
                String selectQuery = "SELECT  * FROM " + TABLE_Bookmark  + " order by id desc";

                cursor = mDb.rawQuery(selectQuery, null);

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Bookmark bookmark = new Bookmark();
                        bookmark.setIDNews(cursor.getString(cursor.getColumnIndex(IdArtical)));
                        bookmark.setUserID(cursor.getString(cursor.getColumnIndex(userId)));
                        bookmark.setThumbImg(cursor.getString(cursor.getColumnIndex(thumbImg)));
                        bookmark.setTitle(cursor.getString(cursor.getColumnIndex(title)));
                        bookmark.setSubTitle(cursor.getString(cursor.getColumnIndex(subtitle)));
                        bookmark.setSources(cursor.getString(cursor.getColumnIndex(sources)));
                        bookmark.setCreateDate(cursor.getString(cursor.getColumnIndex(createDate)));
                        bookmark.setFavorite(cursor.getString(cursor.getColumnIndex(favorite)));
                        bookmark.setBookmark_status(cursor.getString(cursor.getColumnIndex(bookmark_status)));
                        // Adding contact to list
                        array_bookmark.add(bookmark);
                    } while (cursor.moveToNext());
                }

                // return contact list
                return array_bookmark;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (isOpen())
                    close();
                if (cursor != null)
                    cursor.close();
            }
        }
        return null;
    }
    public int GetTotalBookMark()
    {
        synchronized (this) {
            Cursor c = null;
            try {
                if (!isOpen())
                    open();
                if (mDb != null) {
                    if (!mDb.isOpen())
                        mDb = getWritableDatabase();
                } else
                    mDb = getWritableDatabase();
                String selectQuery = "SELECT  * FROM " + TABLE_Bookmark ;

                c = mDb.rawQuery(selectQuery, null);
                int size = c.getCount();
                return size;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (isOpen())
                    close();
                if (c != null)
                    c.close();
            }
        }
        return 0;
    }
    public boolean deleteOneBookamrk(String IdArtical)
    {
        synchronized (this) {
            Cursor c = null;
            try {
                if (!isOpen())
                    open();
                if (mDb != null) {
                    if (!mDb.isOpen()) {
                        mDb = getWritableDatabase();
                    }
                } else {
                    mDb = getWritableDatabase();
                }
                // Select All Query
                String selectQuery = "SELECT  id FROM " + TABLE_Bookmark +" where "+this.IdArtical+" = '" + IdArtical + "'";
                c = mDb.rawQuery(selectQuery, null);

                // looping through all rows and adding to list
                if(c.moveToFirst())
                {
                    mDb.delete(TABLE_Bookmark, " id = "+ c.getString(c.getColumnIndex(KEY_ID)), null);
                    return true;
                }
                else
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (isOpen())
                    close();
                if (c != null)
                    c.close();
            }
        }
        return false;
    }
    public boolean checkArticalBookmarkExist(String IdArtical )
    {
        synchronized (this) {
            Cursor c = null;
            try {
                if (!isOpen())
                    open();
                if (mDb != null) {
                    if (!mDb.isOpen()) {
                        mDb = getWritableDatabase();
                    }
                } else {
                    mDb = getWritableDatabase();
                }
                // Select All Query
                String selectQuery = "SELECT  id FROM " + TABLE_Bookmark +" where "+ this.IdArtical+" = '" + IdArtical + "'";
                c = mDb.rawQuery(selectQuery, null);
                // looping through all rows and adding to list
                if(c.moveToFirst())
                    return true;
                else
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (isOpen())
                    close();
                if (c != null)
                    c.close();
            }
        }
        return false;
    }
    public void DeteleAllBookmark(String table)
    {
        synchronized (this) {
            Cursor c = null;
            try {
                if (!isOpen())
                    open();
                if (mDb != null) {
                    if (!mDb.isOpen()) {
                        mDb = getWritableDatabase();
                    }
                } else {
                    mDb = getWritableDatabase();
                }
                mDb.delete(table, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (isOpen())
                    close();
                if (c != null)
                    c.close();
            }
        }

    }
    public synchronized DatabaseCore open() throws SQLiteException {
        // if (mDbHelper == null)
        // mDbHelper = new DatabaseHelper(mCtx);
        if (mDb != null) {
            if (!mDb.isOpen()) {
                mDb = getWritableDatabase();
            }
        } else {
            mDb = getWritableDatabase();
        }
        return this;
    }

    public synchronized boolean isOpen() {
        if (mDb != null)
            return mDb.isOpen();
        else
            return false;
    }

    public synchronized void close() {
        if (mDb != null)
            mDb.close();
    }





} 