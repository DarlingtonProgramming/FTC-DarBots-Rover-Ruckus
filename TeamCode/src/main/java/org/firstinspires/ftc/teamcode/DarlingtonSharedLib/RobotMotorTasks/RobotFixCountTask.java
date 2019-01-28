package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.RobotMotorTasks;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorController;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTaskCallBack;

public class RobotFixCountTask extends RobotMotorTask {
    protected int m_Count;
    protected double m_Speed;
    protected double m_FineTime;

    public RobotFixCountTask(int Count, double Speed, RobotMotorTaskCallBack TaskCallBack) {
        super(TaskCallBack);
    }

    public double getSpeed(){
        return this.m_Speed;
    }

    public void setSpeed(double Speed){
        if(this.isBusy()){
            throw new RuntimeException("You cannot change the speed of a task when the task has started");
        }
        this.m_Speed = Speed;
    }

    public int getCounts(){
        return this.m_Count;
    }

    public void setCounts(int Count){
        if(this.isBusy()){
            throw new RuntimeException("You cannot change the count of a task when the task has started");
        }
        this.m_Count = Count;
    }

    @Override
    protected void __startTask() {
        this.getMotorController().getMotor().getDcMotor().setTargetPosition(this.m_Count);
        this.getMotorController().getMotor().getDcMotor().setPower(this.m_Speed);
        this.getMotorController().getMotor().setCurrentMovingType(RobotMotor.MovingType.toCount);

        //Calculate Fine time
        double DistanceTravelled = (this.getCounts() / this.getMotorController().getMotor().getMotorType().getCountsPerRev());
        double speed = this.getMotorController().getMotor().getMotorType().getRevPerSec() * this.getSpeed();
        double FineTime = Math.abs(DistanceTravelled / speed);
        if(this.isTimeControlEnabled()){
            FineTime *= this.getTimeOutFactor();
        }
        this.m_FineTime = FineTime;
    }

    @Override
    public void updateStatus() {
        if(this.isBusy()){
            if(!this.getMotorController().getMotor().isBusy()){
                this.endTask(false);
            }else if(this.getSecondsSinceStart() > this.m_FineTime){
                this.endTask(true);
            }
        }
    }
}
