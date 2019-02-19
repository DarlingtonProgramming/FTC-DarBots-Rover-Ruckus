package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

import java.util.ArrayList;
import java.util.TimerTask;

public class NonBlockingTimer implements RobotNonBlockingDevice {
    public static interface timerTask{
        void run();
    }

    private static class timerTaskInfo{
        double SecondsLeft = 0;
        timerTask Task;
        public timerTaskInfo(double secondsAfter, @NonNull timerTask Task){
            this.SecondsLeft = secondsAfter;
            this.Task = Task;
        }
        public timerTaskInfo(timerTaskInfo info){
            this.SecondsLeft = info.SecondsLeft;
            this.Task = info.Task;
        }
    }

    private ArrayList<timerTaskInfo> m_Tasks;
    private ElapsedTime m_Time;

    public NonBlockingTimer(){
        m_Tasks = new ArrayList<>();
        m_Time = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
    }

    public void cancel(){
        this.m_Tasks.clear();
    }

    @Override
    public boolean isBusy() {
        return (!this.m_Tasks.isEmpty());
    }

    @Override
    public void updateStatus() {
        if(this.isBusy()) {
            double secondsPassed = this.m_Time.seconds();
            for (int i =0; i<this.m_Tasks.size();i++) {
                this.m_Tasks.get(i).SecondsLeft -= secondsPassed;
                if (this.m_Tasks.get(i).SecondsLeft <= 0) {
                    timerTask mTask = this.m_Tasks.get(i).Task;
                    this.m_Tasks.remove(i);
                    i--;
                    mTask.run();
                }
            }
        }
        this.m_Time.reset();
    }

    public void addTask(@NonNull timerTask task, double timeInSec){
        this.m_Tasks.add(new timerTaskInfo(timeInSec + this.m_Time.seconds(),task));
    }

    @Override
    public void waitUntilFinish() {
        while(this.isBusy()){
            this.updateStatus();
        }
    }
}
