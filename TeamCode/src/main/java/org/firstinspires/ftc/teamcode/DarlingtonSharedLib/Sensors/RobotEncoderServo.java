package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;


import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;



public class RobotEncoderServo implements RobotEventLoopable {
    public static final double ignoreValue = 0.01;
    private RobotEncoderMotor m_Motor;
    private int m_StartCount = 0;
    private double m_SmallestPos = 0;
    private double m_BiggestPos = 0;
    private boolean m_isWorking = false;
    private boolean m_Lock = false;
    public RobotEncoderServo(@NonNull RobotEncoderMotor Motor, double CurrentPosition, double BiggestPos, double SmallestPos, boolean lockPosition){
        this.m_Motor = Motor;
        this.m_StartCount = Motor.getDcMotor().getCurrentPosition() - (int) Math.round(CurrentPosition * Motor.getCountsPerRev());
        this.m_BiggestPos = BiggestPos;
        this.m_SmallestPos = SmallestPos;
        this.m_Lock = lockPosition;
        //Locking Position from the beginning.
        if(lockPosition) {
            this.m_Motor.getDcMotor().setTargetPosition(this.m_Motor.getDcMotor().getCurrentPosition());
            this.m_Motor.getDcMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
            this.m_Motor.getDcMotor().setPower(1.0);
        }
        this.m_isWorking = false;
    }
    public boolean isLockingPosition(){
        return this.m_Lock;
    }
    public void setLockingPosition(boolean Enabled){
        this.m_Lock = Enabled;
        if(Enabled){
            this.m_Motor.getDcMotor().setTargetPosition(this.m_Motor.getDcMotor().getCurrentPosition());
            this.m_Motor.getDcMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
            this.m_Motor.getDcMotor().setPower(1.0);
        }else{
            this.m_Motor.getDcMotor().setPower(0);
            this.m_Motor.getDcMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
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
    public RobotEncoderMotor getMotor(){
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
        if(Pos > this.getBiggestPos()){
            if(Pos - this.getBiggestPos() >= ignoreValue){
                Pos = this.getBiggestPos();
            }else{
                return;
            }
        }
        if(Pos < this.getSmallestPos()){
            if(this.getSmallestPos() - Pos >= ignoreValue){
                Pos = this.getSmallestPos();
            }else{
                return;
            }
        }
        int TargetPos = 0;
        TargetPos = this.m_StartCount + (int) Math.round(Pos * this.m_Motor.getCountsPerRev());
        int DeltaCount = TargetPos - this.m_Motor.getDcMotor().getCurrentPosition();
        this.m_Motor.moveCounts(DeltaCount,Speed);
        this.m_isWorking = true;
    }

    public void doLoop(){
        this.m_Motor.doLoop();
        if(this.m_isWorking && !this.m_Motor.isBusy()){
            if(this.isLockingPosition()) {
                this.m_Motor.getDcMotor().setTargetPosition(this.m_Motor.getDcMotor().getCurrentPosition());
                this.m_Motor.getDcMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                this.m_Motor.getDcMotor().setPower(1.0);
            }
            this.m_isWorking = false;
        }
    }
    public void adjustPosition(double currentPos){
        this.m_StartCount = this.m_Motor.getDcMotor().getCurrentPosition() - (int) Math.round(currentPos * this.m_Motor.getCountsPerRev());
    }
}
