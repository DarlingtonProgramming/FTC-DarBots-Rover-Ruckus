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

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTasks.FixedCountsTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

public class RobotServoUsingMotor implements RobotEventLoopable, RobotNonBlockingDevice {
    private RobotMotor m_Motor;
    private double m_ZeroPos;
    private double m_BiggestPos;
    private double m_SmallestPos;
    public RobotServoUsingMotor(@NonNull RobotMotor Motor, double currentPos, double biggestPos, double smallestPos){
        this.m_Motor = Motor;
        this.adjustCurrentPosition(currentPos);
        this.m_BiggestPos = biggestPos;
        this.m_SmallestPos = smallestPos;
    }

    public double getCurrentPosition(){
        double absPos = ((double) this.m_Motor.getDcMotor().getCurrentPosition() / this.m_Motor.getCountsPerRev());
        return this.m_ZeroPos + absPos;
    }
    public double getCurrentPercent(){
        return this.getCurrentPosition() / (this.getBiggestPos() - this.getSmallestPos()) * 100.0;
    }
    public double getTargetPosition(){
        FixedCountsTask CountsTask = (FixedCountsTask) this.m_Motor.getCurrentTask();
        double absPos = ((double) CountsTask.getTargetCount() / this.m_Motor.getCountsPerRev());
        return this.m_ZeroPos + absPos;
    }
    public double getTargetPercent(){
        return this.getTargetPosition() / (this.getBiggestPos() - this.getSmallestPos()) * 100.0;
    }
    public void setTargetPosition(double Position,double Speed){
        if(Position > this.m_BiggestPos){
            Position = this.m_BiggestPos;
        }else if(Position < this.m_SmallestPos){
            Position = this.m_SmallestPos;
        }
        if(this.isBusy()){
            this.stopMotion();
        }
        double deltaPos = Position - this.getCurrentPosition();
        int deltaCount = (int) Math.round(deltaPos * this.m_Motor.getCountsPerRev());
        this.m_Motor.addTask(new FixedCountsTask(deltaCount,Speed,null));
    }
    public void setTargetPercent(double Percent, double Speed){
        this.setTargetPosition(Percent / 100.0 * (this.getBiggestPos() - this.getSmallestPos()) + this.getSmallestPos(),Speed);
    }
    public double getBiggestPos(){
        return this.m_BiggestPos;
    }

    public void setBiggestPos(double biggestPos){
        this.m_BiggestPos = biggestPos;
    }

    public double getSmallestPos(){
        return this.m_SmallestPos;
    }

    public void setSmallestPos(double smallestPos){
        this.m_SmallestPos = smallestPos;
    }

    public void adjustCurrentPosition(double currentPos){
        double absPos = ((double) this.m_Motor.getDcMotor().getCurrentPosition()) / this.m_Motor.getCountsPerRev();
        this.m_ZeroPos = currentPos - absPos;
    }

    public void stopMotion(){
        this.m_Motor.deleteAllTasks();
    }

    @Override
    public void doLoop() {
        this.m_Motor.doLoop();
    }

    @Override
    public boolean isBusy() {
        return this.m_Motor.isBusy();
    }

    @Override
    public void waitUntilFinish() {
        this.m_Motor.waitUntilFinish();
    }
}
