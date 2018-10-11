package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal;

import com.qualcomm.hardware.lynx.commands.core.LynxI2cConfigureChannelCommand;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;

public class RobotNonBlockingNoEncoderMotor implements RobotEventLoopable {
    enum workType{
        ToPosition,
        FixedSpeed
    }
    private workType m_runningType = workType.ToPosition;
    private DcMotor m_DCMotor;
    private double m_CountsPerRev = 0;
    private double m_RevPerSec = 0;
    private ElapsedTime m_MotorOperationTime;
    private double m_DriveSpeed = 0;
    private double m_EstimatedTime = 0;
    private boolean m_isWorking = false;
    private int m_MovedCounts = 0;

    public RobotNonBlockingNoEncoderMotor(DcMotor RobotDcMotor, double CountsPerRev, double RevPerSec, boolean TimeControl){
        this.m_DCMotor = RobotDcMotor;
        this.m_CountsPerRev = CountsPerRev;
        this.m_RevPerSec = RevPerSec;
        this.m_DCMotor.setPower(0.0);
        this.m_DCMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.m_DCMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.m_MotorOperationTime = new ElapsedTime();
        this.m_isWorking = false;
    }

    public void waitMotorOperationFinish(){
        while(this.isBusy()){
            this.doLoop();
        }
    }

    public boolean isBusy(){
        return this.m_isWorking;
    }

    public boolean isRunningFixedSpeed(){
        return (this.isBusy() && this.m_runningType == workType.FixedSpeed);
    }

    public DcMotor getDcMotor() {
        return this.m_DCMotor;
    }

    public void setDcMotor(DcMotor myDCMotor) {
        this.m_DCMotor = myDCMotor;
    }

    public double getCountsPerRev(){
        return this.m_CountsPerRev;
    }

    public void setCountsPerRev(double CountsPerRev){
        this.m_CountsPerRev = CountsPerRev;
    }

    public double getRevPerSec(){
        return this.m_RevPerSec;
    }

    public void setRevPerSec(double revPerSec){
        this.m_RevPerSec = revPerSec;
    }

    public void moveCounts(int RevTotal, double Power) throws RuntimeException{
        RobotDebugger.addDebug("RobotNonBlockingMotor","moveRev (" + RevTotal + ", " + Power + ")");
        if(RevTotal == 0){
            if(!this.m_isWorking) {
                this.m_DriveSpeed = Power;
                this.m_MovedCounts = 0;
            }
            return;
        }

        this.m_DCMotor.setPower(Power);
        this.m_isWorking = true;

        if(!this.m_isWorking || this.m_runningType != workType.ToPosition) {
            this.m_MovedCounts = 0;
            this.m_runningType = workType.ToPosition;
        }else if(this.m_isWorking && this.m_runningType == workType.FixedSpeed){
            this.m_MovedCounts += (int) Math.round(this.m_MotorOperationTime.time() * this.m_DriveSpeed * this.getRevPerSec() * this.getCountsPerRev());
        }
        this.m_MotorOperationTime.reset();
        this.m_DriveSpeed = Power;

        double EstimatedTime = Math.abs(((double) RevTotal) / ((double) this.getRevPerSec()));
        this.m_EstimatedTime += EstimatedTime;
    }

    public void moveWithFixedSpeed(double speed){
        RobotDebugger.addDebug("RobotNonBlockingMotor","moveWithFixedSpeed (" + speed + ")");
        if(speed == 0){
            if(!this.m_isWorking) {
                this.m_DriveSpeed = speed;
                this.m_MovedCounts = 0;
            }
            return;
        }

        this.m_DCMotor.setPower(speed);
        this.m_isWorking = true;
        if(!this.m_isWorking || this.m_runningType != workType.FixedSpeed) {
            this.m_MovedCounts = 0;
            this.m_runningType = workType.FixedSpeed;
        }else{
            this.m_MovedCounts += (int) Math.round(this.m_MotorOperationTime.time() * this.m_DriveSpeed * this.getRevPerSec() * this.getCountsPerRev());
        }
        this.m_DriveSpeed = speed;
        this.m_MotorOperationTime.reset();
    }

    public int stopRunning_getMovedRev(){
        if(!this.m_isWorking){
            return this.getLastMovedCounts();
        }
        this.m_DCMotor.setPower(0.0);
        this.m_isWorking = false;
        this.m_MovedCounts += (int) Math.round(this.m_MotorOperationTime.time() * this.m_DriveSpeed * this.getRevPerSec() * this.getCountsPerRev());
        this.m_DriveSpeed = 0;
        return this.getLastMovedCounts();
    }

    public double stopRunning_getMovedCycle(){
        return ((double) this.stopRunning_getMovedRev()) / ((double) this.getCountsPerRev());
    }

    public void moveCycle(double Cycle, double Power) throws RuntimeException{
        int Rev = (int) Math.round(Cycle * this.getCountsPerRev());
        this.moveCounts(Rev,Power);
    }

    public int getLastMovedCounts(){
        return this.m_MovedCounts;
    }

    public double getLastMovedCycle(){
        return ((double) this.getLastMovedCounts()) / ((double) this.getCountsPerRev());
    }

    public int getRemainingCounts(){
        if(!this.m_isWorking){
            return 0;
        }
        return (int) Math.round((this.m_EstimatedTime - this.m_MotorOperationTime.time()) * this.m_DriveSpeed * this.getCountsPerRev());
    }

    @Override
    public void doLoop(){
        if(!this.m_isWorking){
            return;
        }
        if(this.m_runningType == workType.ToPosition) {
            boolean workFinished = false;
            if (this.m_MotorOperationTime.time() > this.m_EstimatedTime) {
                workFinished = true;
            }
            if (workFinished) {
                this.m_isWorking = false;
                this.m_DriveSpeed = 0;
                this.m_DCMotor.setPower(0.0);
                this.m_MovedCounts += (int) Math.round(this.m_MotorOperationTime.time() * this.m_DriveSpeed * this.getRevPerSec() * this.getCountsPerRev());
                RobotDebugger.addDebug("RobotNonBlockingNoEncoderMotor", "moveRevEnd (" + this.m_MovedCounts + ")");
            }
        }
    }
}
