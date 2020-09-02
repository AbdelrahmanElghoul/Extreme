package extreme.extreme.DataBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;
import extreme.extreme.R;

public class TrackDB  {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public  TrackDB(Context context) {
        sharedPreferences=context.getSharedPreferences( String.valueOf( R.string.app_name ),Context.MODE_PRIVATE );
        editor =sharedPreferences.edit();
    }

    public void Save(List<String> data){
        //Clear();
        Gson gson=new Gson();
        String json=gson.toJson( data );
        editor.putString( "Tracks", json );
        editor.apply();

    }

    public ArrayList<String> Load(){
        Gson gson=new Gson();
        String json=sharedPreferences.getString("Tracks","" );
        if(!json.isEmpty()) {

            Type  type = new TypeToken<List<String>>() {}.getType();

            return ( gson.fromJson( json, type ) );
        }
        return new ArrayList<>(  );
    }

    private void Clear(){
        editor.remove( "Tracks" );
        editor.apply();
    }
}
