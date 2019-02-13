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

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.RobotMotorTasks.RobotFixCountTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

public class RobotServoUsingMotor implements RobotNonBlockingDevice {
    public interface RobotMotorUsingServoCallBackBeforeAssigning{
        boolean setPositionPreCheck(RobotServoUsingMotor servo, double Position, double Speed);
    }
    private static final double HOWMANYREVMARGIN = 0.02;
    private RobotMotorController m_MotorCtl;
    private double m_ZeroPos;
    private double m_BiggestPos;
    private double m_SmallestPos;
    private RobotMotorUsingServoCallBackBeforeAssigning m_PreCheckCallBack = null;
    public double convertPercentToPos(double Percent){
        return (Percent / 100.0 * (this.getBiggestPos() - this.getSmallestPos()) + this.getSmallestPos());
    }
    public double convertPosToPercent(double Pos){
        return (Pos - this.getSmallestPos()) / (this.getBiggestPos() - this.getSmallestPos()) * 100.0;
    }
    public RobotServoUsingMotor(@NonNull RobotMotorController MotorController, double currentPos, double biggestPos, double smallestPos){
        this.m_MotorCtl = MotorController;
        this.adjustCurrentPosition(currentPos);
        this.m_BiggestPos = biggestPos;
        this.m_SmallestPos = smallestPos;
    }

    public RobotMotorUsingServoCallBackBeforeAssigning getPreCheckCallBack() {
        return m_PreCheckCallBack;
    }

    public void setPreCheckCallBack(RobotMotorUsingServoCallBackBeforeAssigning callBack){
        this.m_PreCheckCallBack = callBack;
    }

    public RobotMotorController getMotorController(){
        return this.m_MotorCtl;
    }

    public double getCurrentPosition(){
        double absPos = ((double) this.m_MotorCtl.getMotor().getCurrentCount() / this.m_MotorCtl.getMotor().getMotorType().getCountsPerRev());
        return this.m_ZeroPos + absPos;
    }
    public double getCurrentPercent(){
        return this.convertPosToPercent(this.getCurrentPosition());
    }
    public double getTargetPosition(){
        if(this.m_MotorCtl.getCurrentTask() == null){
            return this.getCurrentPosition();
        }
        RobotFixCountTask CountsTask = (RobotFixCountTask) this.m_MotorCtl.getCurrentTask();
        double absPos = ((double) CountsTask.getCounts() / this.m_MotorCtl.getMotor().getMotorType().getCountsPerRev());
        return this.m_ZeroPos + absPos;
    }
    public double getTargetPercent(){
        return this.convertPosToPercent(this.getTargetPosition());
    }
    public void setTargetPosition(double Position,double Speed){
        if(Position >= this.getBiggestPos()){
            /*
            if(Math.abs(this.getCurrentPosition() - this.getBiggestPos()) <= this.HOWMANYREVMARGIN) {
                this.getMotorController().deleteAllTasks();
                return;
            }else{
                Position = this.getBiggestPos();
            }
            */
            Position = this.getBiggestPos();
        }else if(Position <= this.getSmallestPos()){
            /*
            if(Math.abs(this.getCurrentPosition() - this.getSmallestPos()) <= this.HOWMANYREVMARGIN){
                this.getMotorController().deleteAllTasks();
                return;
            }else {
                Position = this.getSmallestPos();
            }
            */
            Position = this.getSmallestPos();
        }
        /*
        if(this.m_PreCheckCallBack != null){
            if(!this.m_PreCheckCallBack.setPositionPreCheck(this,Position,Speed)){
                this.stopMotion();
                return;
            }
        }
        */
        if(this.isBusy()){
            this.stopMotion();
        }
        double deltaPos = Position - this.getCurrentPosition();
        int deltaCount = (int) Math.round(deltaPos * this.m_MotorCtl.getMotor().getMotorType().getCountsPerRev());
        if(deltaCount == 0){
            this.stopMotion();
        }
        this.m_MotorCtl.replaceTask(new RobotFixCountTask(deltaCount,Speed,null));
    }
    public void setTargetPercent(double Percent, double Speed){
        this.setTargetPosition(this.convertPercentToPos(Percent),Speed);
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
        double absPos = ((double) this.m_MotorCtl.getMotor().getCurrentCount()) / this.m_MotorCtl.getMotor().getMotorType().getCountsPerRev();
        this.m_ZeroPos = currentPos - absPos;
    }
    public void adjustCurrentPercent(double currentPercent){
        this.adjustCurrentPosition(this.convertPercentToPos(currentPercent));
    }

    public void stopMotion(){
        this.m_MotorCtl.deleteAllTasks();
    }

    @Override
    public void updateStatus() {
        this.m_MotorCtl.updateStatus();
    }

    @Override
    public boolean isBusy() {
        return this.m_MotorCtl.isBusy();
    }

    @Override
    public void waitUntilFinish() {
        this.m_MotorCtl.waitUntilFinish();
    }
}
