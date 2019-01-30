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
