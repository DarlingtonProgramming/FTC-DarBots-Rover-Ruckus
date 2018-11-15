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

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotor;


public abstract class RobotMotorTask implements RobotNonBlockingDevice,RobotEventLoopable {
    public static interface timeControlledTask{
        boolean isTimeControl();
        void setTimeControl(boolean enabled);
        double getTimeControlExcessPct();
        void setTimeControlExcessPct(double Pct);
    }
    private RobotMotor m_RunningMotor;
    private ElapsedTime m_ElapsedTime;
    private double m_TimeElapsed;
    private int m_StartCount;
    private int m_EndCount;
    private boolean m_TimeOut;
    private boolean m_Working;
    private RobotMotor.RobotMotorFinishCallback m_CallBack;

    public RobotMotorTask(RobotMotor.RobotMotorFinishCallback FinishCallBack){
        this.m_ElapsedTime = new ElapsedTime();
        this.m_TimeElapsed = 0;
        this.m_TimeOut = false;
        this.m_StartCount = 0;
        this.m_EndCount = 0;
        this.m_Working = false;
        this.m_CallBack = FinishCallBack;
    }

    public void setRunningMotor(@NonNull RobotMotor RunningMotor){
        this.m_RunningMotor = RunningMotor;
    }
    public RobotMotor getRunningMotor(){
        return this.m_RunningMotor;
    }
    public RobotMotor.RobotMotorFinishCallback getFinishCallBack(){
        return this.m_CallBack;
    }
    public void setFinishCallBack(RobotMotor.RobotMotorFinishCallback NewCallBack){
        this.m_CallBack = NewCallBack;
    }

    @Override
    public boolean isBusy(){
        return this.m_Working;
    }
    @Override
    public void waitUntilFinish(){
        while(this.isBusy()){
            this.doLoop();
        }
    }
    @Override
    public abstract void doLoop();

    public void startJob(){
        if(this.isBusy()){
            return;
        }
        this.m_StartCount = this.getRunningMotor().getDcMotor().getCurrentPosition();
        this.m_TimeElapsed = 0;
        this.m_TimeOut = false;
        this.m_ElapsedTime.reset();
        this.m_Working = true;
        this.executeJob();
    }
    public void stopJob(){
        if(!this.isBusy()){
            return;
        }
        this.finishJob();
        this.m_TimeElapsed = this.m_ElapsedTime.seconds();
        this.m_EndCount = this.getRunningMotor().getDcMotor().getCurrentPosition();
        this.m_Working = false;
        if (this.m_CallBack != null) {
            this.m_CallBack.finished(this.m_RunningMotor,this.isTaskTimedOut(),this.getTimeElapsed(),this.getMovedCounts());
        }
    }
    protected void _jobFinished_TimedOut(){
        if(!this.isBusy()){
            return;
        }
        this.m_TimeOut = true;
        this._jobFinished();
    }
    protected void _jobFinished(){
        if(!this.isBusy()){
            return;
        }
        this.getRunningMotor().stopCurrentJob();
    }
    protected abstract void executeJob();
    protected abstract void finishJob();
    public int getMovedCounts(){
        return this.m_EndCount - this.m_StartCount;
    }
    public double getTimeElapsed(){
        if(!this.isBusy()) {
            return this.m_TimeElapsed;
        }else{
            return this.m_ElapsedTime.seconds();
        }
    }
    public int getJobStartedCount(){
        return this.m_StartCount;
    }
    public boolean isTaskTimedOut(){
        return this.m_TimeOut;
    }
    public abstract String getJobTypeName();
}
