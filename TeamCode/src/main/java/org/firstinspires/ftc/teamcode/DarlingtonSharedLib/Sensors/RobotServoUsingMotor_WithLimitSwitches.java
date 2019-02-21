package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

public class RobotServoUsingMotor_WithLimitSwitches extends RobotServoUsingMotor {
    protected TouchSensor m_MinTouch;
    protected TouchSensor m_MaxTouch;
    public RobotServoUsingMotor_WithLimitSwitches(@NonNull RobotMotorController MotorController, TouchSensor minValTouchSensor, TouchSensor maxValTouchSensor, double currentPos, double biggestPos, double smallestPos) {
        super(MotorController, currentPos, biggestPos, smallestPos);
        this.m_MaxTouch = maxValTouchSensor;
        this.m_MinTouch = minValTouchSensor;
    }

    @Override
    public RobotDebugger.RobotDebuggerCallable getDebuggerCallable(String partName) {
        return new RobotDebugger.ObjectDebuggerWrapper<>(partName,new Object(){
            @Override
            public String toString(){
                String limitSwitchMinState = "MinSwitch: " + (RobotServoUsingMotor_WithLimitSwitches.this.getMinValTouch() == null ? "null" : (RobotServoUsingMotor_WithLimitSwitches.this.getMinValTouch().isPressed() ? "pressed" : "unPressed"));
                String limitSwitchMaxState = "MaxSwitch: " + (RobotServoUsingMotor_WithLimitSwitches.this.getMaxValTouch() == null ? "null" : (RobotServoUsingMotor_WithLimitSwitches.this.getMaxValTouch().isPressed() ? "pressed" : "unPressed"));
                return "" + RobotServoUsingMotor_WithLimitSwitches.this.getCurrentPosition() + "(" + RobotServoUsingMotor_WithLimitSwitches.this.getCurrentPercent() + "%) " + limitSwitchMinState + " ; " + limitSwitchMaxState;
            }
        });
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
    public boolean isMaxTouchPressed(){
        return getMaxValTouch().isPressed();
    }
    public boolean isMinTouhPressed(){
        return getMinValTouch().isPressed();
    }
    @Override
    public void setTargetPosition(double Position,double Speed, RobotServoUsingMotorPositionCallBack TaskCallBack){
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
        super.setTargetPosition(Position,Speed, TaskCallBack);
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
