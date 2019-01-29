package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotion;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorController;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionTaskCallBack;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTaskCallBack;

public class MotionTaskCallBackToMotorTaskCallBack implements RobotMotorTaskCallBack {
    protected RobotMotion m_Motion;
    protected RobotMotionTaskCallBack m_TaskCallBack;
    public MotionTaskCallBackToMotorTaskCallBack(@NonNull RobotMotion motionController, RobotMotionTaskCallBack MotionTaskCB){
        this.m_Motion = motionController;
        this.m_TaskCallBack = MotionTaskCB;
    }
    public RobotMotion getMotionController(){
        return this.m_Motion;
    }
    public void setMotionController(@NonNull RobotMotion motionController){
        this.m_Motion = motionController;
    }
    public RobotMotionTaskCallBack getTaskCallBack(){
        return this.m_TaskCallBack;
    }
    public void setTaskCallBack(RobotMotionTaskCallBack TaskCallBack){
        this.m_TaskCallBack = TaskCallBack;
    }
    @Override
    public void finishRunning(RobotMotorController Controller, boolean timeOut, double timeUsedInSec, int CountsMoved) {
        if(this.m_TaskCallBack != null){
            this.m_TaskCallBack.finishRunning(this.m_Motion,timeOut,timeUsedInSec,CountsMoved,CountsMoved / Controller.getMotor().getMotorType().getCountsPerRev() * this.m_Motion.getRobotWheel().getCircumference());
        }
    }
}
