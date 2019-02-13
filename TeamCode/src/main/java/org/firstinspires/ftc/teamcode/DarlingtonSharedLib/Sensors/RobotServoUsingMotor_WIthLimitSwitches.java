package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.TouchSensor;

public class RobotServoUsingMotor_WIthLimitSwitches extends RobotServoUsingMotor {
    protected TouchSensor m_MinTouch;
    protected TouchSensor m_MaxTouch;
    public RobotServoUsingMotor_WIthLimitSwitches(@NonNull RobotMotorController MotorController, TouchSensor minValTouchSensor, TouchSensor maxValTouchSensor, double currentPos, double biggestPos, double smallestPos) {
        super(MotorController, currentPos, biggestPos, smallestPos);
        this.m_MaxTouch = maxValTouchSensor;
        this.m_MinTouch = minValTouchSensor;
    }
    public TouchSensor getMinValTouch(){
        return this.m_MinTouch;
    }
    public void setMinValTouch(TouchSensor minValTouch){
        this.m_MinTouch = minValTouch;
    }
    public TouchSensor getMaxValTouch(){
        return this.m_MaxTouch;
    }
    public void setMaxValTouch(TouchSensor maxValTouch){
        this.m_MaxTouch = maxValTouch;
    }
    @Override
    public void setTargetPosition(double Position,double Speed){
        if(this.m_MaxTouch != null) {
            if (Position > this.getCurrentPosition() && this.m_MaxTouch.isPressed()) {
                this.adjustCurrentPercent(100);
                return;
            }
        }
        if(this.m_MinTouch != null) {
            if (Position < this.getCurrentPosition() && this.m_MinTouch.isPressed()) {
                this.adjustCurrentPercent(0);
                return;
            }
        }
        super.setTargetPosition(Position,Speed);
    }
    @Override
    public void updateStatus(){
        if(this.m_MaxTouch != null){
            if(this.getTargetPosition() >= this.getCurrentPosition() && this.m_MaxTouch.isPressed()){
                this.stopMotion();
                this.adjustCurrentPercent(100);
            }
        }
        if(this.m_MinTouch != null) {
            if (this.getTargetPosition() <= this.getCurrentPosition() && this.m_MinTouch.isPressed()) {
                this.stopMotion();
                this.adjustCurrentPercent(0);
            }
        }
        super.updateStatus();
    }
}
