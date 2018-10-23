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


package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Internals;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.DcMotorCountsTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorCallBack;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MotorCountsSpecificJob<T> extends RobotMotorInternalJob<T>{
    private DcMotorCountsTask m_CountsTask;
    private Timer m_TimerScheduller;
    private ElapsedTime m_ElpasedTime;
    private RobotMotor m_Motor;
    private boolean m_Started;
    public MotorCountsSpecificJob(DcMotorCountsTask CountsTask, RobotMotor Motor){
        this.m_CountsTask = CountsTask;
        this.m_Motor = Motor;
        this.m_ElpasedTime = new ElapsedTime();
        this.m_TimerScheduller = new Timer();
        m_Started = false;
    }
    @Override
    public boolean isWorking(){
        return this.m_Started;
    }
    public DcMotorCountsTask getCountsTask(){
        return this.m_CountsTask;
    }
    public void setCountsTask(DcMotorCountsTask Task){
        this.m_CountsTask = Task;
    }
    public RobotMotor getMotor(){
        return this.m_Motor;
    }
    public void setMotor(RobotMotor Motor){
        this.m_Motor = Motor;
    }
    @Override
    public void StartDoingJob(){
        if(m_Started){
            throw new RuntimeException("You cannot start a task while a task is running");
        }
        this.m_Started = true;
        double FineTime = 0;
        this.m_ElpasedTime.reset();
        if(m_CountsTask.isTaskTimeControlEnabled()){
            FineTime = this.getCountsTask().getMovingCounts() / (this.getMotor().getRevPerSec() * this.getMotor().getCountsPerRev() * this.getCountsTask().getMovingSpeed());
            this.m_TimerScheduller.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            getMotor().stopCurrentTask();
                        }
                    },
                    (int) Math.round(FineTime * (1+(this.getCountsTask().getTaskTimeControlExcessPercent() / 100.0)) * 1000.0)
            );
        }
    }
    @Override
    public double JobFinished(int CountsMoved){
        if(!this.isWorking()){
            return 0;
        }
        this.m_Started = false;
        this.m_TimerScheduller.cancel();
        RobotMotorCallBack CallBack = this.getCountsTask().getMotorJobCallBack();
        if(CallBack != null){
            CallBack.motionFinish(CountsMoved,this.m_ElpasedTime.time(TimeUnit.SECONDS));
        }
        return this.m_ElpasedTime.time(TimeUnit.SECONDS);
    }

    @Override
    public double getElapsedTime() {
        return this.m_ElpasedTime.time(TimeUnit.SECONDS);
    }
}
