package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;

import java.lang.annotation.Target;

public class RobotNonBlockingServoUsingMotor {
    private DcMotor m_Motor;
    private int m_CountsPerRev;
    private int m_StartCount;
    private boolean m_Reversed;
    private double m_TotalRotation;
    //private int m_EndCount;
    public RobotNonBlockingServoUsingMotor(DcMotor Motor, int CountsPerRev, double StartPosition, boolean reversed, double totalRotation){

        this.m_CountsPerRev = CountsPerRev;
        this.m_Reversed = reversed;
        if(!reversed) {
            this.setMotor(Motor, (int) -Math.round(StartPosition * this.getCountsPerRev()));
        }else{
            this.setMotor(Motor, (int) Math.round(StartPosition * this.getCountsPerRev()));
        }
    }
    public boolean isBusy(){
        return this.m_Motor.isBusy();
    }
    public double getTotalRotation(){
        return this.m_TotalRotation;
    }
    public void setTotalRotation(double TotalRotation){
        this.m_TotalRotation = TotalRotation;
    }
    public DcMotor getMotor(){
        return m_Motor;
    }
    public boolean isReversed(){
        return this.m_Reversed;
    }
    protected void setMotor(DcMotor Motor, int CountsOffset){
        Motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.m_StartCount = Motor.getCurrentPosition() + CountsOffset;
        //this.m_EndCount = this.m_StartCount + this.getCountsPerRev() + CountsOffset;
        Motor.setTargetPosition(this.m_StartCount);
        Motor.setPower(1.0);
        this.m_Motor = Motor;
    }
    public int getCountsPerRev(){
        return this.m_CountsPerRev;
    }
    public void setCountsPerRev(int CountsPerRev){
        this.m_CountsPerRev = CountsPerRev;
        //this.m_EndCount = this.m_StartCount + CountsPerRev;
    }
    protected int fixCounts(int Counts){
        return Counts % this.getCountsPerRev();
    }
    protected static double fixPosition(double Pos){
        double tempPos = Pos;
        while(tempPos > 1.0){
            tempPos--;
        }
        while(tempPos < 0.0){
            tempPos++;
        }
        return tempPos;
    }
    public double getPosition(){
        int movedCounts = 0;
        if(!this.isReversed()){
            movedCounts = this.m_Motor.getTargetPosition() - this.m_StartCount;
        }else{
            movedCounts = this.m_StartCount - this.m_Motor.getTargetPosition();
        }
        return ((double) movedCounts) / ((double) this.getCountsPerRev());
    }
    public double getCurrentPosition(){
        int movedCounts = 0;
        if(!this.isReversed()){
            movedCounts = this.m_Motor.getCurrentPosition() - this.m_StartCount;
        }else{
            movedCounts = this.m_StartCount - this.m_Motor.getTargetPosition();
        }
        return ((double) movedCounts) / ((double) this.getCountsPerRev());
    }

    public void setPosition(double Pos, double Speed){
        if(Pos > this.getTotalRotation())
            Pos = this.getTotalRotation();
        if(Pos < 0)
            Pos=0;
        int TargetPos = 0;
        if(!this.isReversed()) {
            TargetPos = this.m_StartCount + (int) Math.round(Pos * this.getCountsPerRev());
        }else{
            TargetPos = this.m_StartCount - (int) Math.round(Pos * this.getCountsPerRev());
        }
        this.m_Motor.setPower(Speed);
        this.m_Motor.setTargetPosition(TargetPos);
    }
}
