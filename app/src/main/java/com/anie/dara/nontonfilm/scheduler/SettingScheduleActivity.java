package com.anie.dara.nontonfilm.scheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anie.dara.nontonfilm.R;

public class SettingScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnOn, btnOff;
    private int jobId = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_schedule);

        btnOn = (Button)findViewById(R.id.btn_on);
        btnOff = (Button)findViewById(R.id.btn_off);
        btnOn.setOnClickListener(this);
        btnOff.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_on:
               JobOn();
                break;
            case R.id.btn_off:
                JobOff();
                break;
        }
    }

    private void JobOn(){
        if (isJobRunning(this)) {
            Toast.makeText(this, getString(R.string.on), Toast.LENGTH_SHORT).show();
            return;
        }
        ComponentName mServiceComponent = new ComponentName(this, DailyReminder.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, mServiceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);

        // 1000 ms = 1 detik
        builder.setPeriodic(10000);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        Toast.makeText(this, getString(R.string.on), Toast.LENGTH_SHORT).show();
    }

    private void JobOff(){
        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancel(jobId);
        Toast.makeText(this, getString(R.string.off), Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean isJobRunning(Context context) {
        boolean isScheduled = false;

        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (scheduler != null) {
            for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == jobId) {
                    isScheduled = true;
                    break;
                }
            }
        }

        return isScheduled;
    }
}
