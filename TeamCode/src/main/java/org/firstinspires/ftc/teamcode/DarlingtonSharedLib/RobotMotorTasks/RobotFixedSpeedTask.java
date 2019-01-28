package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.RobotMotorTasks;

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

    public double getSpeed(){
        return this.m_Speed;
    }

    public void setSpeed(double Speed){
        if(this.isBusy()){
            throw new RuntimeException("You cannot change the speed of a task when the task has started");
        }
        this.m_Speed = Speed;
    }

    public double getTimeInSeconds(){
        return this.m_TimeInSeconds;
    }

    public void setTimeInSeconds(double TimeInSeconds){
        if(this.isBusy()){
            throw new RuntimeException("You cannot change the duration of a task when the task has started");
        }
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
