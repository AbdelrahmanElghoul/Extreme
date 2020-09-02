package extreme.extreme.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import extreme.extreme.Data;

public class AppliedDB extends SQLiteOpenHelper {

    private static final int version = 1;
    private static final String DataBase_Name = "applied.dp";
    private static final String Table_Name = "applied";
    private static final String Col_1 = "name";
    private static final String Col_2 = "phone";
    private static final String Col_3 = "email";
    private static final String Col_4 = "year";
    private static final String Col_5 = "track";
    private static final String Col_6 = "InvterViewDate";
    private static final String Col_7 = "InvterViewTime";
    private static final String Col_8 = "sync";
    private SQLiteDatabase db;

    public AppliedDB(Context context) {

        super( context, DataBase_Name, null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "Create Table " + Table_Name + " " +
                "(" + Col_1 + " String , "
                + Col_2 + " String Primary Key unique,"
                + Col_3 + " String ,"
                + Col_4 + " String , "
                + Col_5 + " String , "
                + Col_6 + " String , "
                + Col_7 + " String , "
                + Col_8+" bit)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "Drop Table if exists " + Table_Name );
        onCreate( db );
    }

    public void Add(String name, String phone, String email,String Year, String track,String InterViewDate,String InterViewTime) {
        try {
            SQLiteDatabase dp = getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put( Col_1, name );
            value.put( Col_2, phone );
            value.put( Col_3, email );
            value.put( Col_4, Year );
            value.put( Col_5, track );
            value.put( Col_6, InterViewDate );
            value.put( Col_7, InterViewTime );
            value.put( Col_8,   0 );
            dp.insert( Table_Name, null, value );
            Log.e( "Phone",phone );
            dp.close();
        } catch (Exception e) {
            Log.e( "Add", e.getMessage() );
        }

    }

    /**
     * delete data from its phone (unique)
     */
    public void Delete(String phone) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete( Table_Name, Col_2 + " = " + phone, null );
    }

    /**
     * change sync data value to 1
     */
    public void updateSync(Data tmp) {
     db=getReadableDatabase();
       try {
           ContentValues value = new ContentValues();
           value.put( Col_1, tmp.getName() );
           value.put( Col_2, tmp.getPhone() );
           value.put( Col_3, tmp.getEmail() );
           value.put( Col_4, tmp.getYear() );
           value.put( Col_5, tmp.getTrack() );
           value.put( Col_6, tmp.getInterViewDate() );
           value.put( Col_7, tmp.getInterViewTime() );
           value.put( Col_8,1 );
           db.update( Table_Name, value, Col_2 + " = '" + tmp.getPhone()+"'", null );
           Log.e( "updateSync","uploaded" );
       }catch (Exception e){
           Log.e( "updateSync",e.getMessage() );
       }
    }

    /**
 * get All data not sync (Sync column =0)
 * */
    public List<Data> getData(String num)   {
        List<Data> data = new ArrayList<>();
        try {
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery( "select * from " + Table_Name+" where "+Col_8+" = "+num, null );
            cursor.moveToFirst();
            do {
                Data tmp = new Data();
                tmp.setName ( cursor.getString( 0 ) );
                tmp.setPhone( cursor.getString( 1 ) );
                tmp.setEmail( cursor.getString( 2 ) );
                tmp.setYear( cursor.getString( 3 ) );
                tmp.setTrack( cursor.getString( 4 ) );
                tmp.setInterViewDate( cursor.getString( 5 ) );
                tmp.setInterViewTime( cursor.getString( 6 ) );
            data.add( tmp );
            } while (cursor.moveToNext());
            db.close();

        } catch (Exception e) {
            Log.e( "getData", e.getMessage() );

        }


        return data;

    }
}
