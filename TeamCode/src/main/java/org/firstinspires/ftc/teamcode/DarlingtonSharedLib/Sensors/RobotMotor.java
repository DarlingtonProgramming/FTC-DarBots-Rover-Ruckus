package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

import java.util.ArrayList;

public class RobotMotor implements RobotEventLoopable,RobotNonBlockingDevice {
    public interface RobotMotorFinishCallback{
        void finished(RobotMotor Motor, boolean timeOut, double timeUsed, int CountsMoved);
    }

    private DcMotor m_DcMotor;
    private double m_CountsPerRev;
    private double m_RevPerSec;
    private RobotMotorTask m_CurrentTask;
    private RobotMotorTask m_LastTask;
    private ArrayList<RobotMotorTask> m_QueueTasks;

    @Override
    public void doLoop() {
        if(!this.m_CurrentTask.isBusy()){
            this.stopCurrentJob();
        }
        if(this.m_CurrentTask != null){
            this.m_CurrentTask.doLoop();
        }
    }

    @Override
    public boolean isBusy() {
        if(this.m_CurrentTask != null){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void waitUntilFinish() {
        if(this.m_CurrentTask != null){
            this.m_CurrentTask.waitUntilFinish();
        }
    }

    public double getCountsPerRev(){
        return this.m_CountsPerRev;
    }
    public void setCountsPerRev(double CountsPerRev){
        this.m_CountsPerRev = CountsPerRev;
    }
    public double getRevPerSec(){
        return this.m_RevPerSec;
    }
    public void setRevPerSec(double RevPerSec){
        this.m_RevPerSec = RevPerSec;
    }
    public DcMotor getDcMotor(){
        return this.m_DcMotor;
    }
    public void setDcMotor(DcMotor motor){
        this.m_DcMotor = motor;
    }
    public RobotMotorTask getCurrentTask(){
        return this.m_CurrentTask;
    }
    public void stopCurrentJob(){
        if(this.m_CurrentTask != null){
            this.m_CurrentTask.stopJob();
        }
        queueUp();
    }
    public ArrayList<RobotMotorTask> getQueueTasks(){
        return this.m_QueueTasks;
    }
    public void setQueueTasks(@NonNull ArrayList<RobotMotorTask> QueueTasks){
        this.m_QueueTasks = QueueTasks;
    }

    public void clearQueuedTasks(){
        this.m_QueueTasks.clear();
    }

    public void addTask(@NonNull RobotMotorTask Task){
        this.m_QueueTasks.add(Task);
        this.organizeTasks();
    }

    public void deleteAllTasks(){
        this.clearQueuedTasks();
        this.stopCurrentJob();
    }

    protected void queueUp(){
        if(this.m_CurrentTask != null){
            this.m_LastTask = m_CurrentTask;
        }
        if(!this.m_QueueTasks.isEmpty()){
            this.m_CurrentTask = this.m_QueueTasks.remove(0);
            this.m_CurrentTask.setRunningMotor(this);
            this.m_CurrentTask.startJob();
        }
    }
    protected void organizeTasks(){
        if(this.m_CurrentTask != null){
            if(!this.m_CurrentTask.isBusy()){
                this.queueUp();
            }
        }else{
            this.queueUp();
        }
    }
}
