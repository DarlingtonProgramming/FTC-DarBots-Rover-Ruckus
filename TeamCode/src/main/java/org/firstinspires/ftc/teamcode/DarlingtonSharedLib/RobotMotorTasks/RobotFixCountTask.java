/*
MIT License

Copyright (c) 2018 DarBots Collaborators

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/

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
    public RobotFixCountTask(@NonNull RobotFixCountTask CountTask){
        super(CountTask);
        this.m_Count = CountTask.m_Count;
        this.m_Speed = CountTask.m_Speed;
        this.m_FineTime = CountTask.m_FineTime;
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
        }else{
            FineTime = 0;
        }
        this.m_FineTime = FineTime;
    }

    @Override
    public void updateStatus() {
        if(this.isBusy()){
            if(!this.getMotorController().getMotor().isBusy()){
                this.endTask(false);
            }else if(this.getSecondsSinceStart() > this.m_FineTime && this.m_FineTime > 0){
                this.endTask(true);
            }
        }
    }
}
