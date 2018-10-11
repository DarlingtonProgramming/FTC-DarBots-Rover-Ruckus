package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;

public class RobotNonBlockingServoUsingMotor {
    private DcMotor m_Motor;
    private double m_CountsPerRev;
    private int m_StartCount;
    private double m_StartPos;
    public RobotNonBlockingServoUsingMotor(DcMotor Motor, double CountsPerRev, double StartPosition){
        this.setMotor(Motor);
        this.setCountsPerRev(CountsPerRev);
    }
    public DcMotor getMotor(){
        return m_Motor;
    }
    public void setMotor(DcMotor Motor){
        Motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.m_StartCount = Motor.getCurrentPosition();
        Motor.setTargetPosition(this.m_StartCount);
        Motor.setPower(1.0);
        this.m_Motor = Motor;
    }
    public double getCountsPerRev(){
        return this.m_CountsPerRev;
    }
    public void setCountsPerRev(double CountsPerRev){
        this.m_CountsPerRev = CountsPerRev;
    }
    public double getPosition(){
        int movedCounts = this.m_Motor.getTargetPosition() - this.m_StartCount;
        int validCounts = movedCounts % ((int) Math.round(this.getCountsPerRev()));
        return (validCounts / this.getCountsPerRev());
    }
    public void setPosition(double Pos){
        int absCount = (int) Math.round(Pos * this.getCountsPerRev());
        int movedCounts = this.m_Motor.getTargetPosition() - this.m_StartCount;
        int validCounts = (int) Math.round(movedCounts % this.getCountsPerRev());
        int countsOffset = absCount - validCounts;
        this.m_Motor.setTargetPosition(this.m_Motor.getTargetPosition() + countsOffset);
    }
}
