package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotion;

public abstract class RobotMotionSystemTask implements RobotNonBlockingDevice {
    protected RobotMotionSystem m_MotionSystem;
    protected boolean m_IsWorking;
    public RobotMotionSystemTask(){
        this.m_IsWorking = false;
    }
    public RobotMotionSystemTask(@NonNull RobotMotionSystemTask Task){
        this.m_MotionSystem = Task.m_MotionSystem;
        this.m_IsWorking = false;
    }
    public RobotMotionSystem getMotionSystem(){
        return this.m_MotionSystem;
    }
    public void setMotionSystem(RobotMotionSystem MotionSystem){
        this.m_MotionSystem = MotionSystem;
    }
    public void startTask(){
        if(this.m_IsWorking){
            return;
        }
        this.m_IsWorking = true;
        this.__startTask();
    }
    protected abstract void __startTask();
    public void stopTask(){
        if(!this.m_IsWorking){
            return;
        }
        this.m_IsWorking = false;
        this.m_MotionSystem.__stopMotion();
        this.m_MotionSystem.deleteCurrentTask();
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
}
