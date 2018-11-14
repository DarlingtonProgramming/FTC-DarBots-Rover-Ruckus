package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTasks.FixedCountsTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

public class RobotServoUsingMotor implements RobotEventLoopable, RobotNonBlockingDevice {
    private RobotMotor m_Motor;
    private double m_ZeroPos;
    private double m_BiggestPos;
    private double m_SmallestPos;
    public RobotServoUsingMotor(@NonNull RobotMotor Motor, double currentPos, double biggestPos, double smallestPos){
        this.m_Motor = Motor;
        this.adjustCurrentPosition(currentPos);
        this.m_BiggestPos = biggestPos;
        this.m_SmallestPos = smallestPos;
    }
    public double getCurrentPosition(){
        double absPos = ((double) this.m_Motor.getDcMotor().getCurrentPosition() / this.m_Motor.getCountsPerRev());
        return this.m_ZeroPos + absPos;
    }
    public void setTargetPosition(double Position,double Speed){
        if(Position > this.m_BiggestPos){
            Position = this.m_BiggestPos;
        }else if(Position < this.m_SmallestPos){
            Position = this.m_SmallestPos;
        }
        double deltaPos = Position - this.getCurrentPosition();
        int deltaCount = (int) Math.round(deltaPos * this.m_Motor.getCountsPerRev());
        this.m_Motor.deleteAllTasks();
        this.m_Motor.addTask(new FixedCountsTask(deltaCount,Speed,null));
    }

    public void adjustCurrentPosition(double currentPos){
        double absPos = ((double) this.m_Motor.getDcMotor().getCurrentPosition()) / this.m_Motor.getCountsPerRev();
        this.m_ZeroPos = currentPos - absPos;
    }

    public void stopMotion(){
        this.m_Motor.deleteAllTasks();
    }

    @Override
    public void doLoop() {
        this.m_Motor.doLoop();
    }

    @Override
    public boolean isBusy() {
        return this.m_Motor.isBusy();
    }

    @Override
    public void waitUntilFinish() {
        this.m_Motor.waitUntilFinish();
    }
}
