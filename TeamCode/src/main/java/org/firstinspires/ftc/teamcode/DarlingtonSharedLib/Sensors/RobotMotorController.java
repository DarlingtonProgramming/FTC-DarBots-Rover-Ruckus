package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

import java.util.ArrayDeque;

public class RobotMotorController implements RobotNonBlockingDevice {
    protected RobotMotor m_Motor;
    protected ArrayDeque<RobotMotorTask> m_TaskLists;
    protected double m_TimeOutFactor = 0;
    protected boolean m_TimeoutControl = false;

    public RobotMotorController(@NonNull RobotMotor Motor, boolean timeOutControlEnabled, double timeOutFactor){
        this.m_Motor = Motor;
        m_TaskLists = new ArrayDeque<RobotMotorTask>();
        this.m_TimeoutControl = timeOutControlEnabled;
        this.m_TimeOutFactor = timeOutFactor;
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
    public RobotMotor getMotor(){
        return this.m_Motor;
    }
    public void setMotor(@NonNull RobotMotor Motor){
        this.m_Motor = Motor;
    }

    @Override
    public boolean isBusy() {
        return (!this.m_TaskLists.isEmpty());
    }

    @Override
    public void updateStatus() {
        this.checkTasks();
        if(!this.m_TaskLists.isEmpty()){
            this.m_TaskLists.getFirst().updateStatus();
        }
    }

    @Override
    public void waitUntilFinish() {
        while(this.isBusy()){
            this.updateStatus();
        }
    }

    public void checkTasks(){
        if(this.m_TaskLists.isEmpty()){
            return;
        }
        if(!this.m_TaskLists.getFirst().isBusy()){
            this.deleteCurrentTask();
        }
    }

    public void addTask(@NonNull RobotMotorTask MotorTask){
        this.m_TaskLists.addLast(MotorTask);
        this.scheduleTasks();
    }

    public void deleteCurrentTask(){
        if(!this.m_TaskLists.isEmpty()){
            if(this.m_TaskLists.getFirst().isBusy()){
                this.m_TaskLists.getFirst().endTask(true);
            }
            this.m_TaskLists.removeFirst();
            scheduleTasks();
        }
    }

    public ArrayDeque<RobotMotorTask> getTaskLists(){
        return this.m_TaskLists;
    }

    protected void scheduleTasks(){
        if(!this.m_TaskLists.isEmpty() && !this.m_TaskLists.getFirst().isBusy()) {
            this.m_TaskLists.getFirst().setMotorController(this);
            this.m_TaskLists.getFirst().setTimeControlEnabled(this.isTimeControlEnabled());
            this.m_TaskLists.getFirst().setTimeOutFactor(this.getTimeOutFactor());
            this.m_TaskLists.getFirst().startTask();
        }else if(this.m_TaskLists.isEmpty()){
            stopMotor();
        }
    }

    public void deleteAllTasks(){
        if(!this.m_TaskLists.isEmpty()){
            if(this.m_TaskLists.getFirst().isBusy()){
                this.m_TaskLists.getFirst().endTask(true);
            }
            this.m_TaskLists.clear();
        }
        this.stopMotor();
    }

    public RobotMotorTask getCurrentTask(){
        return this.m_TaskLists.isEmpty() ? null : this.m_TaskLists.getFirst();
    }

    protected void stopMotor(){
        this.m_Motor.setCurrentMovingType(RobotMotor.MovingType.withSpeed);
        this.m_Motor.setPower(0);
    }
}
