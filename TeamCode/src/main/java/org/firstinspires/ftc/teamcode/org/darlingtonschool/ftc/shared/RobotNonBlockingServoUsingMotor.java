package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import android.support.annotation.NonNull;


import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;



public class RobotNonBlockingServoUsingMotor implements RobotEventLoopable {
    private RobotNonBlockingMotor m_Motor;
    private int m_StartCount = 0;
    private double m_SmallestPos = 0;
    private double m_BiggestPos = 0;
    public RobotNonBlockingServoUsingMotor(@NonNull RobotNonBlockingMotor Motor, double CurrentPosition, double BiggestPos, double SmallestPos){
        this.m_Motor = Motor;
        this.m_StartCount = Motor.getDcMotor().getCurrentPosition() - (int) Math.round(CurrentPosition * Motor.getCountsPerRev());
        this.m_BiggestPos = BiggestPos;
        this.m_SmallestPos = SmallestPos;
    }
    public boolean isBusy(){
        return this.m_Motor.isBusy();
    }
    public double getSmallestPos(){
        return this.m_SmallestPos;
    }
    public void setSmallestPos(double SmallestPos){
        this.m_SmallestPos = SmallestPos;
    }
    public double getBiggestPos(){
        return this.m_BiggestPos;
    }
    public void setBiggestPos(double BiggestPos){
        this.m_BiggestPos = BiggestPos;
    }
    public RobotNonBlockingMotor getMotor(){
        return m_Motor;
    }
    public double getPosition(){
        int movedCounts = 0;
        movedCounts = this.m_Motor.getDcMotor().getCurrentPosition() - this.m_StartCount;
        return movedCounts / this.m_Motor.getCountsPerRev();
    }
    public double getTargetPosition(){
        int movedCounts = 0;
        movedCounts = this.m_Motor.getDcMotor().getTargetPosition() - this.m_StartCount;
        return movedCounts / this.m_Motor.getCountsPerRev();
    }

    public void setPosition(double Pos, double Speed){
        if(Pos > this.getBiggestPos())
            Pos = this.getBiggestPos();
        if(Pos < this.getSmallestPos())
            Pos = this.getSmallestPos();
        int TargetPos = 0;
        TargetPos = this.m_StartCount + (int) Math.round(Pos * this.m_Motor.getCountsPerRev());
        int DeltaCount = TargetPos - this.m_Motor.getDcMotor().getCurrentPosition();
        this.m_Motor.moveCounts(DeltaCount,Speed);
    }

    public void doLoop(){
        this.m_Motor.doLoop();
    }
}
