package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.RobotMotorTasks.RobotFixCountTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

public class RobotServoUsingMotor implements RobotNonBlockingDevice {
    private RobotMotorController m_MotorCtl;
    private double m_ZeroPos;
    private double m_BiggestPos;
    private double m_SmallestPos;
    protected double convertPercentToPos(double Percent){
        return Percent / 100.0 * (this.getBiggestPos() - this.getSmallestPos()) + this.getSmallestPos();
    }
    protected double convertPosToPercent(double Pos){
        return Pos / (this.getBiggestPos() - this.getSmallestPos()) * 100.0;
    }
    public RobotServoUsingMotor(@NonNull RobotMotorController MotorController, double currentPos, double biggestPos, double smallestPos){
        this.m_MotorCtl = MotorController;
        this.adjustCurrentPosition(currentPos);
        this.m_BiggestPos = biggestPos;
        this.m_SmallestPos = smallestPos;
    }

    public double getCurrentPosition(){
        double absPos = ((double) this.m_MotorCtl.getMotor().getCurrentCount() / this.m_MotorCtl.getMotor().getMotorType().getCountsPerRev());
        return this.m_ZeroPos + absPos;
    }
    public double getCurrentPercent(){
        return this.convertPosToPercent(this.getCurrentPosition());
    }
    public double getTargetPosition(){
        if(this.m_MotorCtl.getCurrentTask() == null){
            return this.getCurrentPosition();
        }
        RobotFixCountTask CountsTask = (RobotFixCountTask) this.m_MotorCtl.getCurrentTask();
        double absPos = ((double) CountsTask.getCounts() / this.m_MotorCtl.getMotor().getMotorType().getCountsPerRev());
        return this.m_ZeroPos + absPos;
    }
    public double getTargetPercent(){
        return this.convertPosToPercent(this.getTargetPosition());
    }
    public void setTargetPosition(double Position,double Speed){
        if(Position > this.m_BiggestPos){
            Position = this.m_BiggestPos;
        }else if(Position < this.m_SmallestPos){
            Position = this.m_SmallestPos;
        }
        if(this.isBusy()){
            this.stopMotion();
        }
        double deltaPos = Position - this.getCurrentPosition();
        int deltaCount = (int) Math.round(deltaPos * this.m_MotorCtl.getMotor().getMotorType().getCountsPerRev());
        this.m_MotorCtl.addTask(new RobotFixCountTask(deltaCount,Speed,null));
    }
    public void setTargetPercent(double Percent, double Speed){
        this.setTargetPosition(this.convertPercentToPos(Percent),Speed);
    }
    public double getBiggestPos(){
        return this.m_BiggestPos;
    }

    public void setBiggestPos(double biggestPos){
        this.m_BiggestPos = biggestPos;
    }

    public double getSmallestPos(){
        return this.m_SmallestPos;
    }

    public void setSmallestPos(double smallestPos){
        this.m_SmallestPos = smallestPos;
    }

    public void adjustCurrentPosition(double currentPos){
        double absPos = ((double) this.m_MotorCtl.getMotor().getCurrentCount()) / this.m_MotorCtl.getMotor().getMotorType().getCountsPerRev();
        this.m_ZeroPos = currentPos - absPos;
    }
    public void adjustCurrentPercent(double currentPercent){
        this.adjustCurrentPosition(this.convertPercentToPos(currentPercent));
    }

    public void stopMotion(){
        this.m_MotorCtl.deleteAllTasks();
    }

    @Override
    public void updateStatus() {
        this.m_MotorCtl.updateStatus();
    }

    @Override
    public boolean isBusy() {
        return this.m_MotorCtl.isBusy();
    }

    @Override
    public void waitUntilFinish() {
        this.m_MotorCtl.waitUntilFinish();
    }
}
