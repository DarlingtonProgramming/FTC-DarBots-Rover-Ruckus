package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTasks;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTask;

public class FixedSpeedTask extends RobotMotorTask {
    private double m_runningSpeed;
    public FixedSpeedTask(double runningSpeed, RobotMotor.RobotMotorFinishCallback CallBack){
        super(CallBack);
        m_runningSpeed = runningSpeed;
    }
    public double getRunningSpeed(){
        return this.m_runningSpeed;
    }
    public void setRunningSpeed(double speed){
        this.m_runningSpeed = speed;
        if(super.isBusy()){
            this.setUpDcMotor();
        }
    }
    protected void setUpDcMotor(){
        super.getRunningMotor().getDcMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        super.getRunningMotor().getDcMotor().setPower(this.m_runningSpeed);
    }
    @Override
    public void doLoop(){

    }

    @Override
    protected void executeJob() {
        this.setUpDcMotor();
    }

    @Override
    protected void finishJob() {
        super.getRunningMotor().getDcMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        super.getRunningMotor().getDcMotor().setPower(0);
    }

    @Override
    public String getJobTypeName() {
        return "FixedSpeedTask";
    }
}
