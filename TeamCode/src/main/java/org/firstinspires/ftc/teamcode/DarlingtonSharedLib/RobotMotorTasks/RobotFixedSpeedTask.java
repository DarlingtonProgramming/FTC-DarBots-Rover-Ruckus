package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.RobotMotorTasks;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTaskCallBack;

public class RobotFixedSpeedTask extends RobotMotorTask {
    double m_TimeInSeconds = 0;
    double m_Speed = 0;
    public RobotFixedSpeedTask(double timeInSeconds, double Speed, RobotMotorTaskCallBack TaskCallBack) {
        super(TaskCallBack);
        this.m_TimeInSeconds = timeInSeconds;
        this.m_Speed = Speed;
    }
    public RobotFixedSpeedTask(@NonNull RobotFixedSpeedTask SpeedTask){
        super(SpeedTask);
        this.m_TimeInSeconds = SpeedTask.m_TimeInSeconds;
        this.m_Speed = SpeedTask.m_Speed;
    }

    public double getSpeed(){
        return this.m_Speed;
    }

    public void setSpeed(double Speed){
        this.m_Speed = Speed;
        if(this.isBusy()){
            this.getMotorController().getMotor().setPower(this.m_Speed);
        }
    }

    public double getTimeInSeconds(){
        return this.m_TimeInSeconds;
    }

    public void setTimeInSeconds(double TimeInSeconds){
        this.m_TimeInSeconds = TimeInSeconds;
    }

    @Override
    protected void __startTask() {
        this.getMotorController().getMotor().setCurrentMovingType(RobotMotor.MovingType.withSpeed);
        this.getMotorController().getMotor().setPower(this.m_Speed);
    }

    @Override
    public void updateStatus() {
        if(this.getSecondsSinceStart() >= this.m_TimeInSeconds && this.m_TimeInSeconds > 0){
            this.endTask(true);
        }
    }
}
