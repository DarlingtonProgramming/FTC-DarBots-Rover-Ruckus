package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorController;

public abstract class RobotMotorTask implements RobotNonBlockingDevice {
    public class MotorTaskFinishInfo{
        public boolean timedOut;
        public int countsMoved;
        public double timeUsedInSec;
        public RobotMotorController robotController;
        public MotorTaskFinishInfo(boolean TimedOut, int CountsMoved, double TimeUsedInSec, RobotMotorController RobotController){
            this.timedOut = TimedOut;
            this.countsMoved = CountsMoved;
            this.timeUsedInSec = TimeUsedInSec;
            this.robotController = RobotController;
        }
        public MotorTaskFinishInfo(MotorTaskFinishInfo Info){
            this.timedOut = Info.timedOut;
            this.countsMoved = Info.countsMoved;
            this.timeUsedInSec = Info.timeUsedInSec;
            this.robotController = Info.robotController;
        }
    }
    private RobotMotorController m_Controller;
    private RobotMotorTaskCallBack m_TaskCallBack = null;
    private int m_StartCount = 0;
    private ElapsedTime m_Time;
    private boolean m_IsWorking = false;
    private double m_TimeOutFactor = 0;
    private boolean m_TimeoutControl = false;

    public RobotMotorTask(RobotMotorTaskCallBack TaskCallBack){
        this.m_TaskCallBack = TaskCallBack;
        this.m_Time = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        m_IsWorking = false;
    }
    public boolean isTimeControlEnabled(){
        return this.m_TimeoutControl;
    }
    public void setTimeControlEnabled(boolean timeOutControlEnabled){
        this.m_TimeoutControl = timeOutControlEnabled;
    }
    public double getTimeOutFactor(){
        return this.m_TimeOutFactor;
    }
    public void setTimeOutFactor(double timeOutFactor){
        this.m_TimeOutFactor = timeOutFactor;
    }
    public RobotMotorController getMotorController(){
        return this.m_Controller;
    }
    public void setMotorController(@NonNull RobotMotorController Controller){
        this.m_Controller = Controller;
    }

    public RobotMotorTaskCallBack getTaskCallBack(){
        return this.m_TaskCallBack;
    }

    public void setTaskCallBack(RobotMotorTaskCallBack TaskCallBack){
        this.m_TaskCallBack = TaskCallBack;
    }


    public void startTask(){
        if(this.m_IsWorking) {
            return;
        }
        this.m_StartCount = this.getMotorController().getMotor().getCurrentCount();
        this.m_IsWorking = true;
        this.m_Time.reset();
        this.__startTask();
    }
    protected abstract void __startTask();
    public void endTask(boolean timedOut){
        if(!this.m_IsWorking){
            return;
        }
        this.m_IsWorking = false;
        int endCount = this.getMotorController().getMotor().getCurrentCount();
        int deltaCount = endCount - this.m_StartCount;
        double timeUsed = this.m_Time.seconds();
        if(this.m_TaskCallBack != null) {
            this.m_TaskCallBack.finishRunning(this.m_Controller,timedOut,timeUsed,deltaCount);
        }
        this.m_Controller.checkTasks();
    }

    @Override
    public boolean isBusy(){
        return this.m_IsWorking;
    }

    @Override
    public void waitUntilFinish(){
        while(this.isBusy()){
            this.updateStatus();
        }
    }

    public double getSecondsSinceStart(){
        if(this.isBusy()) {
            return this.m_Time.seconds();
        }else{
            return 0;
        }
    }

}
