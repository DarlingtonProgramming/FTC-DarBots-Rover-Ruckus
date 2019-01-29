package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.RobotMotorTasks;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorTaskCallBack;

public class RobotFixCountSpeedCtlTask extends RobotFixedSpeedTask {
    protected int m_Count;
    public RobotFixCountSpeedCtlTask(int Count, double Speed, RobotMotorTaskCallBack TaskCallBack) {
        super(0,Speed,TaskCallBack);
        this.setCounts(Count);
    }
    public RobotFixCountSpeedCtlTask(RobotFixCountSpeedCtlTask FixCountSpeedCtlTask){
        super(FixCountSpeedCtlTask);
        this.m_Count = FixCountSpeedCtlTask.m_Count;
    }

    public int getCounts(){
        return this.m_Count;
    }

    public void setCounts(int Count){
        if(this.isBusy()){
            throw new RuntimeException("You cannot change the count of a task when the task has started");
        }
        this.m_Count = Count;
        double timeInSeconds = Count / this.getMotorController().getMotor().getMotorType().getCountsPerRev() / this.getMotorController().getMotor().getMotorType().getRevPerSec();
        if(Count < 0){
            this.setSpeed(-Math.abs(this.getSpeed()));
        }else{
            this.setSpeed(Math.abs(this.getSpeed()));
        }
        this.setTimeInSeconds(Math.abs(timeInSeconds));
    }
}
