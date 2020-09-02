package extreme.extreme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;

import java.util.List;

import extreme.extreme.DataBase.AppliedDB;
import extreme.extreme.DataBase.TrackDB;
import extreme.extreme.InternetConnection.Internet;
import extreme.extreme.Server.Sync;

public class MainActivity extends AppCompatActivity {


    EditText txtName, txtPhone, txtEmail;
    Spinner SpinnerTracks,SpinnerDate,SpinnerTime,SpinnerYear;
    AppliedDB appliedDB = new AppliedDB( this );
    public static ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        txtName = findViewById( R.id.txtName );
        txtPhone = findViewById( R.id.txtPhone );
        txtEmail = findViewById( R.id.txtEmail );
        SpinnerTracks = findViewById( R.id.SpinnerTracks );
        SpinnerDate=findViewById( R.id.SpinnerDate );
        SpinnerTime=findViewById( R.id.SpinnerTime );
        SpinnerYear=findViewById( R.id.SpinnerYear );

        String[] date={"Tue 12-3","Wed 13-3","Thur 14-3"};
        String[] Time={"11:00","11:15","11:30","11:45",
                       "12:00","12:15","12:30","12:45",
                       "1:00","1:15","1:30","1:45",
                       "2:00","2:15","2:30","2:45",
                       "3:00","3:15","3:30","3:45","4:00"};
        String[] Year={"1","2","3","4"};

        ArrayAdapter<String> dateAdapter =
                new ArrayAdapter<>( this, android.R.layout.simple_spinner_item, date );
        ArrayAdapter<String> TimeAdapter =
                new ArrayAdapter<>( this, android.R.layout.simple_spinner_item, Time );
        ArrayAdapter<String> YearAdapter =
                new ArrayAdapter<>( this, android.R.layout.simple_spinner_item, Year );
        SpinnerDate.setAdapter( dateAdapter );
        SpinnerTime.setAdapter( TimeAdapter );
        SpinnerYear.setAdapter( YearAdapter );
        Tracks();

    }

    /**
     * save data to local database
     */
    public void Savebtn(View view) {
        if (!Validate()) {
            Log.e( "Validate", "Invalid" );
            return;
        }

        appliedDB.Add( txtName.getText().toString(), txtPhone.getText().toString()
                , txtEmail.getText().toString(),SpinnerYear.getSelectedItem().toString(), SpinnerTracks.getSelectedItem().toString(),SpinnerDate.getSelectedItem().toString(),SpinnerTime.getSelectedItem().toString()   );
Log.e( "SavePhone",txtPhone.getText().toString() );
        clearText();
        startService();

    }

    public void Cancelbtn(View view) {
        clearText();
    }

    void clearText() {
        txtName.setText( null );
        txtPhone.setText( null );
        txtEmail.setText( null );
    }

    /**
     * check email format
     */
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher( email ).matches();
    }

    /**
     * check all data valid or no
     */
    boolean Validate() {
        boolean check = true;
        if (!isEmailValid( txtEmail.getText().toString() )) {
            txtEmail.setError( "Invalid email Format" );
            check = false;
        }

        if (txtName.getText().toString().isEmpty()) {
            txtName.setError( "Empty" );
            check = false;
        }

        if (txtPhone.getText().toString().length() < 9
                || txtPhone.getText().toString().contains( " " )
                || txtPhone.getText().toString().contains( "*" ) ) {
            txtPhone.setError( "Invalid number" );
            check = false;
        }

        return check;
    }

    void SetSpinnerAdapter(List<String> data) {
        if(data.isEmpty()){
            data.add( "No data found \n connect to network" );
        }
        adapter = new ArrayAdapter<>( this, android.R.layout.simple_spinner_item, data );
        SpinnerTracks.setAdapter( adapter );
    }

    public void Tracks() {
        if (new Internet().hasInternetAccess( this )) {
            Log.e( "Tracks", "Online" );
            new Sync().DownloadTracks( this, SpinnerTracks );
        } else {
            Log.e( "Tracks", "Offline" );
            SetSpinnerAdapter( new TrackDB( this ).Load() );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.sync, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (new Internet().hasInternetAccess( this ) && item.getItemId() == R.id.Sync) {
            if(appliedDB.getData( "0" ).isEmpty()){
                Toast.makeText( this, "No New Data to Upload", Toast.LENGTH_SHORT ).show();
                Log.e( "dataCount0", String.valueOf( appliedDB.getData( "0" ) ) );
                Log.e( "dataCount1", String.valueOf( appliedDB.getData( "1" ) ) );
            }
            else{
                Toast.makeText( this, "uploading", Toast.LENGTH_SHORT ).show();
                new Sync().upload( this );
            }
        } else {
            Toast.makeText( this, "No Internet please check your connection then try again", Toast.LENGTH_LONG ).show();
        }
        return false;
    }

    public void startService(){
        Log.e( "Job","called    " );
        FirebaseJobDispatcher dispatcher=new FirebaseJobDispatcher( new GooglePlayDriver( this ) );
        Job myJob = dispatcher.newJobBuilder()
                .setService(JobSchedulerEvent.class) // the JobService that will be called
                .setTag("Upload")// uniquely identifies the job
                .setReplaceCurrent(true)//replace the current job (if there is current
                .setConstraints( Constraint.ON_ANY_NETWORK )//network type
                .build();

        dispatcher.mustSchedule(myJob);

    }


}




