package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTasks;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTask;

public class FixedCountsTask extends RobotMotorTask {
    private int m_MovingCounts;
    private double m_MovingSpeed;
    private boolean m_timeControl;
    private double m_timeControlExcessPct;
    private int m_startCount;

    public FixedCountsTask(int MovingCounts, double speed, boolean timeControl, double timeControlExcessPercent, RobotMotor.RobotMotorFinishCallback CallBack){
        super(CallBack);
        this.m_MovingCounts = MovingCounts;
        this.m_MovingSpeed = speed;
        this.m_timeControl = timeControl;
        this.m_timeControlExcessPct = timeControlExcessPercent;
    }
    public int getMovingCounts(){
        return this.m_MovingCounts;
    }
    public void setMovingCounts(int newMovingCounts){
        this.m_MovingCounts = newMovingCounts;
        if(super.isBusy()){
            this.setupDcMotor();
        }
    }
    public double getMovingSpeed(){
        return this.m_MovingSpeed;
    }
    public void setMovingSpeed(double speed){
        this.m_MovingSpeed = speed;
        if(super.isBusy()){
            this.setupDcMotor();
        }
    }
    public boolean isTimeControl(){
        return this.m_timeControl;
    }
    public void setTimeControl(boolean enabled){
        this.m_timeControl = enabled;
    }
    public double getTimeControlExcessPct(){
        return this.m_timeControlExcessPct;
    }
    public void setTimeControlExcessPct(double Pct){
        this.m_timeControlExcessPct = Pct;
    }

    protected void setupDcMotor(){
        super.getRunningMotor().getDcMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        super.getRunningMotor().getDcMotor().setTargetPosition(m_startCount + this.m_MovingCounts);
        super.getRunningMotor().getDcMotor().setPower(m_MovingSpeed);
    }

    @Override
    public void doLoop() {
        this.checkDcFinished();
    }

    @Override
    protected void executeJob() {
        this.m_startCount = super.getRunningMotor().getDcMotor().getCurrentPosition();
        this.setupDcMotor();
    }

    @Override
    protected void finishJob() {
        super.getRunningMotor().getDcMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        super.getRunningMotor().getDcMotor().setPower(0);
    }

    @Override
    public String getJobTypeName() {
        return "FixedCountsTask";
    }

    protected void checkDcFinished(){
        if(!super.isBusy()){
            return;
        }
        if(!super.getRunningMotor().getDcMotor().isBusy()){
            this._jobFinished();
        }else if(this.m_timeControl) {
            double fineTime = Math.abs(this.m_MovingCounts / (this.m_MovingSpeed * super.getRunningMotor().getRevPerSec() * super.getRunningMotor().getCountsPerRev())) * ((100.0 + this.m_timeControlExcessPct) / 100.0);
            if (super.getTimeElapsed() > fineTime) {
                this._jobFinished_TimedOut();
            }
        }

    }
}
