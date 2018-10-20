package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import android.support.annotation.NonNull;


import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;



public class RobotNonBlockingServoUsingMotor implements RobotEventLoopable {
    private RobotNonBlockingMotor m_Motor;
    private int m_StartCount = 0;
    private double m_SmallestPos = 0;
    private double m_BiggestPos = 0;
    private boolean m_isWorking = false;
    public RobotNonBlockingServoUsingMotor(@NonNull RobotNonBlockingMotor Motor, double CurrentPosition, double BiggestPos, double SmallestPos){
        this.m_Motor = Motor;
        this.m_StartCount = Motor.getDcMotor().getCurrentPosition() - (int) Math.round(CurrentPosition * Motor.getCountsPerRev());
        this.m_BiggestPos = BiggestPos;
        this.m_SmallestPos = SmallestPos;
        //Locking Position from the beginning.
        this.m_Motor.getDcMotor().setTargetPosition(this.m_Motor.getDcMotor().getCurrentPosition());
        this.m_Motor.getDcMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.m_Motor.getDcMotor().setPower(1.0);
        this.m_isWorking = false;
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
            return;
        if(Pos < this.getSmallestPos())
            return;
        int TargetPos = 0;
        TargetPos = this.m_StartCount + (int) Math.round(Pos * this.m_Motor.getCountsPerRev());
        int DeltaCount = TargetPos - this.m_Motor.getDcMotor().getTargetPosition();
        this.m_Motor.moveCounts(DeltaCount,Speed);
        this.m_isWorking = true;
    }

    public void doLoop(){
        this.m_Motor.doLoop();
        if(this.m_isWorking && !this.m_Motor.isBusy()){
            this.m_Motor.getDcMotor().setTargetPosition(this.m_Motor.getDcMotor().getCurrentPosition());
            this.m_Motor.getDcMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
            this.m_Motor.getDcMotor().setPower(1.0);
            this.m_isWorking = false;
        }
    }
    public void adjustPosition(double currentPos){
        this.m_StartCount = this.m_Motor.getDcMotor().getCurrentPosition() - (int) Math.round(currentPos * this.m_Motor.getCountsPerRev());
    }
}
