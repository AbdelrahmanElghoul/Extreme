package extreme.extreme.Server;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 *download the tracks from the server
 * */
class DownloadTracks extends StringRequest {
        private static final String Download_URL="http://extremetest.000webhostapp.com/DownloadTracks.php";

    DownloadTracks(Response.Listener<String> listener){
        super(Request.Method.POST,Download_URL,listener,null);
    }

}
