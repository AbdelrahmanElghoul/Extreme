package extreme.extreme;

import android.app.job.JobParameters;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.firebase.jobdispatcher.JobService;

import extreme.extreme.Server.Sync;

/**
 * Only Executed devices higher than API 21
 * */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


public class JobSchedulerEvent extends JobService {

    private  boolean JobCanceled=false;

    /**
     * Return true for long Operation not to kill the app else if its short return false
     * */
    @Override
    public boolean onStartJob(@NonNull com.firebase.jobdispatcher.JobParameters job) {
        Log.e( "Job","Started" );
        doBackGroundWork(job);
        return true;
    }

    /**
     * called when job canceled
     * return reShedule or not
     *
     * Don't forget to stop ur background
     * */
    @Override
    public boolean onStopJob(@NonNull com.firebase.jobdispatcher.JobParameters params) {
        JobCanceled=true;
        return true;
    }

    /**
     * Using Threas as JobService works on Main Thread
     * JobFinished notify the app that the background operation ended
     * and true for reSchedule / false to end
     * */
    private void doBackGroundWork(final com.firebase.jobdispatcher.JobParameters params){

        new Thread( new Runnable() {
            @Override
            public void run() {
                new Sync().upload( getApplicationContext() );
                    if(JobCanceled)
                        return;

                Log.e("Run","Finished");
                jobFinished( params,true );
            }
        } ).start();
    }


}
