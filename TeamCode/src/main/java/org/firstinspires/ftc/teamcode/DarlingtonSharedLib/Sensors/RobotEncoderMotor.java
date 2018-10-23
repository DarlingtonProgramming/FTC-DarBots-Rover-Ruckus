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

/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program.
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;
 * Additional Information:
 * I recommend you to use the X,Y values in CM.
 */

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Internals.MotorCountsSpecificJob;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Internals.MotorSpeedSpecificJob;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Internals.RobotMotorInternalJob;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.DcMotorCountsTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.DcMotorCyclesTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.DcMotorSpeedTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

import java.util.ArrayList;

public class RobotEncoderMotor implements RobotNonBlockingDevice, RobotMotor, RobotEventLoopable {
    public class RobotEncoderSpecificData{
        int m_StartCount = 0;
        public RobotEncoderSpecificData(int StartCount){
            this.m_StartCount = StartCount;
        }
        public int getStartCount(){
            return this.m_StartCount;
        }
        public void setStartCount(int StartCount){
            this.m_StartCount = StartCount;
        }
    }
    private DcMotor m_Motor;
    private ArrayList<RobotMotorInternalJob<RobotEncoderSpecificData>> m_WaitingJobs;
    private RobotMotorInternalJob<RobotEncoderSpecificData> m_CurrentJob;
    private double m_CountsPerRev;
    private double m_RevPerSec;
    private int m_MovedCounts;
    private double m_MovedTimeInSec;

    public RobotEncoderMotor(@NonNull DcMotor Motor, double CountsPerRev, double RevPerSec){
        this.setDcMotor(Motor);
        this.m_CountsPerRev = CountsPerRev;
        this.m_RevPerSec = RevPerSec;
        this.m_WaitingJobs = new ArrayList<RobotMotorInternalJob<RobotEncoderSpecificData>>();
        this.m_CurrentJob = null;
        this.m_MovedCounts = 0;
        this.m_MovedTimeInSec = 0;
    }

    @Override
    public boolean isBusy(){
        return this.m_WaitingJobs.isEmpty() && m_CurrentJob == null;
    }
    @Override
    public void waitUntilFinish(){
        while(this.isBusy()){
            this.doLoop();
        }
    }

    @Override
    public boolean isRunningFixedSpeed() {
        return m_CurrentJob instanceof MotorSpeedSpecificJob;
    }

    @Override
    public DcMotor getDcMotor() {
        return this.m_Motor;
    }

    @Override
    public void setDcMotor(DcMotor myDCMotor) {
        this.m_Motor = myDCMotor;
        if(!this.m_Motor.isBusy()) {
            this.m_Motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            this.m_Motor.setPower(0.0);
        }
        this.m_Motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public double getCountsPerRev() {
        return this.m_CountsPerRev;
    }

    @Override
    public void setCountsPerRev(double CountsPerRev) {
        this.m_CountsPerRev = CountsPerRev;
    }

    @Override
    public double getRevPerSec() {
        return this.m_RevPerSec;
    }

    @Override
    public void setRevPerSec(double revPerSec) {
        this.m_RevPerSec = revPerSec;
    }

    @Override
    public void addCountsTask(DcMotorCountsTask CountsTask) {
        MotorCountsSpecificJob<RobotEncoderSpecificData> myJob = new MotorCountsSpecificJob<>(CountsTask,this);
        this.m_WaitingJobs.add(myJob);
        this.scheduleTask();
    }

    protected void scheduleTask(){
        if(!this.m_WaitingJobs.isEmpty() && this.m_CurrentJob == null){
            this.m_CurrentJob = this.m_WaitingJobs.get(0);
            this.m_WaitingJobs.remove(0);
            this.m_CurrentJob.setCustomData(new RobotEncoderSpecificData(this.getDcMotor().getCurrentPosition()));
            if(this.m_CurrentJob instanceof MotorCountsSpecificJob){
                MotorCountsSpecificJob<RobotEncoderSpecificData> CountsJob = (MotorCountsSpecificJob) this.m_CurrentJob;
                this.m_Motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                int movingTo = this.m_Motor.getCurrentPosition() + CountsJob.getCountsTask().getMovingCounts();
                this.m_Motor.setTargetPosition(movingTo);
                this.m_Motor.setPower(CountsJob.getCountsTask().getMovingSpeed());
            }else if(this.m_CurrentJob instanceof  MotorSpeedSpecificJob){
                MotorSpeedSpecificJob<RobotEncoderSpecificData> SpeedJob = (MotorSpeedSpecificJob<RobotEncoderSpecificData>) this.m_CurrentJob;
                this.m_Motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                this.m_Motor.setPower(SpeedJob.getSpeedTask().getMovingSpeed());
            }
            this.m_CurrentJob.StartDoingJob();
        }
    }

    @Override
    public void addFixedSpeedTask(DcMotorSpeedTask SpeedTask) {
        MotorSpeedSpecificJob<RobotEncoderSpecificData> myJob = new MotorSpeedSpecificJob<>(SpeedTask,this);
        this.m_WaitingJobs.add(myJob);
        this.scheduleTask();
    }

    @Override
    public void addCycleTask(DcMotorCyclesTask CycleTask){
        this.addCountsTask(CycleTask);
    }

    @Override
    public int numberTasksLeft() {
        int totalNumber = 0;
        if(this.m_CurrentJob != null){
            totalNumber++;
        }
        totalNumber += this.m_WaitingJobs.size();
        return totalNumber;
    }

    @Override
    public boolean taskExist(int Index) {
        if(Index == 0){
            return this.m_CurrentJob == null;
        }else{
            return this.m_WaitingJobs.size() > Index;
        }
    }

    @Override
    public void deleteTask(int Index) {
        if(taskExist(Index)){
            if(Index == 0){
                this.stopCurrentTask();
            }else{
                this.m_WaitingJobs.remove(Index-1);
            }
        }
    }

    @Override
    public int stopCurrentTaskAndGetMovedCounts() {
        this.stopCurrentTask();
        return this.m_MovedCounts;
    }

    @Override
    public double stopCurrentTaskAndGetMovedCycles() {
        return this.stopCurrentTaskAndGetMovedCounts() / this.getCountsPerRev();
    }

    @Override
    public void stopCurrentTask() {
        if(this.m_CurrentJob != null){
            this.m_MovedCounts = this.getDcMotor().getCurrentPosition() - this.m_CurrentJob.getCustomData().getStartCount();
            this.m_MovedTimeInSec = this.m_CurrentJob.JobFinished(this.m_MovedCounts);
            this.m_CurrentJob = null;
            this.scheduleTask();
        }
    }

    @Override
    public double getLastTaskMovedTimeInSec(){
        return this.m_MovedTimeInSec;
    }

    @Override
    public void deleteAllTasks() {
        this.m_WaitingJobs.clear();
        this.deleteTask(0);
    }

    @Override
    public int deleteAllTasksAndGetMovedCountsForCurrentTask() {
        this.deleteAllTasks();
        return this.getLastTaskMovedCounts();
    }

    @Override
    public double deleteAllTasksAndGetMovedCyclesForCurrentTask() {
        this.deleteAllTasks();
        return this.getLastTaskMovedCycle();
    }

    @Override
    public int getLastTaskMovedCounts() {
        return this.m_MovedCounts;
    }

    @Override
    public double getLastTaskMovedCycle() {
        return this.m_MovedCounts / this.getCountsPerRev();
    }

    @Override
    public void doLoop() {
        if(this.m_CurrentJob != null) {
            if (this.m_CurrentJob instanceof MotorCountsSpecificJob) {
                MotorCountsSpecificJob<RobotEncoderSpecificData> CountsJob = (MotorCountsSpecificJob) this.m_CurrentJob;
                if (!this.getDcMotor().isBusy()) {
                    this.stopCurrentTask();
                }
            }
        }
    }
}
