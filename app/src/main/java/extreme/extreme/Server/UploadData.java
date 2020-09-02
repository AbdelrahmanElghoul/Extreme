package extreme.extreme.Server;


import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Upload data to Applied Table (Info of applicant(Name/phone/email/track)
 *
 * */
class UploadData extends StringRequest {

    private static final String Upload_URl="http://extremetest.000webhostapp.com/UploadData.php";
    private Map<String,String> params;

    UploadData(String name, String phone, String email,String Year, String track,String interviewDate,String interViewTime, Response.Listener<String> listener){
        super(Method.POST,Upload_URl,listener,null);
        params=new HashMap<>();
        params.put("name",name);
        params.put( "phone",phone );
        Log.e( "UploadPhone",phone );
        params.put( "email",email );
        params.put("Year",Year );
        params.put("track",track );
        params.put("interViewDate",interviewDate );
        params.put( "interViewTime",interViewTime );
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
