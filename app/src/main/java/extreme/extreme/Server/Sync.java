package extreme.extreme.Server;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import extreme.extreme.Data;
import extreme.extreme.DataBase.AppliedDB;
import extreme.extreme.DataBase.TrackDB;
import extreme.extreme.MainActivity;

public class Sync {

    /**
     * upload data to server
     */
    private void UploadData(final Context context, final Data data)   {



        Response.Listener<String> ResponeListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.e( "Data", response );
                    JSONObject jsonObject = new JSONObject( response );

                    boolean success = jsonObject.getBoolean( "success" );
                    if (success) {
                        new AppliedDB( context).updateSync( data );
                        Log.e( "Success", "Success" );
                    } else {
                        Log.e( "Success", "Fail" );
                    Toast.makeText( context, "something went wrong", Toast.LENGTH_SHORT ).show();
                    }
                } catch (JSONException e) {

                    Log.e( "UploadDataResponse", e.getMessage() );
                    Toast.makeText( context, "Something Went Wrong \n please try Again", Toast.LENGTH_SHORT ).show();

                }
            }
        };

        UploadData upload = new UploadData( data.getName(), data.getPhone()
                , data.getEmail(),data.getYear(), data.getTrack(),data.getInterViewDate(),data.getInterViewTime(), ResponeListener );

        RequestQueue queue = Volley.newRequestQueue( context );
        queue.add( upload );
        Log.e( "Query", "Added" );
    }

    public void upload(Context context){


       AppliedDB db=new AppliedDB(context);
       List<Data> list=db.getData("0");
        if(list.isEmpty()){
            return;
        }
        for (int i=0;i<list.size();i++){
            UploadData( context,list.get( i ) );
        }


    }

    /**
     * get tracks from server
     */
    public List<String> DownloadTracks(final Context context, final Spinner spinner) {
        final List<String> data = new ArrayList<>();
        final boolean[] success = new boolean[1];
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONObject( response ).getJSONArray( "data" );
                    success[0] = new JSONObject( response ).getJSONObject( "response" ).getBoolean( "success" );

                    if (success[0]) {

                        for (int i = 0; i < array.length(); i++)
                            data.add( array.get( i ).toString() );

                        new TrackDB( context ).Save( data );
                        MainActivity.adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,data);
                        spinner.setAdapter( MainActivity.adapter );

                        Log.e( "downloading_tracks", "Succeeded" );
                    } else
                        Log.e( "downloading_tracks", "failed" );

                } catch (JSONException e) {
                    Log.e( "DownloadTracksFailed", e.getMessage() );
                }
            }
        };
        DownloadTracks downloadTracks = new DownloadTracks( listener );
        RequestQueue queue = Volley.newRequestQueue( context );
        queue.add( downloadTracks );

        return data;
    }
}
