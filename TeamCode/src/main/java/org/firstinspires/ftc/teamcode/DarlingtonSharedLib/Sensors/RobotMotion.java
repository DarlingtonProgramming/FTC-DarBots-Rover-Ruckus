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

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTasks.FixedCountsTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTasks.FixedSpeedTask;

public class RobotMotion {
    public interface RobotMotionFinishCallBack{
        void finished(RobotMotion Motion, boolean timeOut, double timeUsed, double DistanceMoved);
    }
    public static class MotionCallBackConverter implements RobotMotor.RobotMotorFinishCallback{
        private RobotMotionFinishCallBack m_MotionCallBack;
        private RobotMotion m_Motion;
        public MotionCallBackConverter(RobotMotionFinishCallBack CallBack, RobotMotion Motion){
            this.m_MotionCallBack = CallBack;
            this.m_Motion = Motion;
        }
        public RobotMotionFinishCallBack getCallBack(){
            return this.m_MotionCallBack;
        }
        public void setCallBack(RobotMotionFinishCallBack CallBack){
            this.m_MotionCallBack = CallBack;
        }
        public RobotMotion getMotion(){
            return this.m_Motion;
        }
        public void setMotion(RobotMotion Motion){
            this.m_Motion = Motion;
        }
        @Override
        public void finished(RobotMotor Motor, boolean timeOut, double timeUsed, int CountsMoved) {
            if(this.m_MotionCallBack != null){
                this.m_MotionCallBack.finished(this.m_Motion,timeOut,timeUsed,((double) CountsMoved) / this.m_Motion.getMotor().getCountsPerRev() * this.m_Motion.getWheel().getPerimeter());
            }
        }
    }
    public class FixedDistanceMotionTask extends FixedCountsTask {
        public FixedDistanceMotionTask(double Distance, double Speed, RobotMotionFinishCallBack CallBack){
            super((int) Math.abs(RobotMotion.this.getWheel().calculateCycleForDistance(Distance)*RobotMotion.this.getMotor().getCountsPerRev()),Speed,new MotionCallBackConverter(CallBack,RobotMotion.this));
        }
        public double getDistance(){
            return ((double) super.getMovingCounts()) / RobotMotion.this.getMotor().getCountsPerRev() * RobotMotion.this.getWheel().getPerimeter();
        }
        public void setDistance(double Distance){
            super.setMovingCounts((int) Math.abs(RobotMotion.this.getWheel().calculateCycleForDistance(Distance)*RobotMotion.this.getMotor().getCountsPerRev()));
        }
        public void setMotionFinishCallBack(RobotMotionFinishCallBack CallBack){
            MotionCallBackConverter Converter = (MotionCallBackConverter) super.getFinishCallBack();
            Converter.setCallBack(CallBack);
        }
        public RobotMotionFinishCallBack getMotionFinishCallBack(){
            return ((MotionCallBackConverter) super.getFinishCallBack()).getCallBack();
        }
    }
    public class FixedSpeedMotionTask extends FixedSpeedTask{
        public FixedSpeedMotionTask(double speed,RobotMotionFinishCallBack CallBack){
            super(speed,new MotionCallBackConverter(CallBack,RobotMotion.this));
        }
        public void setMotionFinishCallBack(RobotMotionFinishCallBack CallBack){
            MotionCallBackConverter Converter = (MotionCallBackConverter) super.getFinishCallBack();
            Converter.setCallBack(CallBack);
        }
        public RobotMotionFinishCallBack getMotionFinishCallBack(){
            return ((MotionCallBackConverter) super.getFinishCallBack()).getCallBack();
        }
    }
    private RobotWheel m_Wheel;
    private RobotMotor m_Motor;
    public RobotMotion(RobotWheel Wheel, RobotMotor Motor){
        this.m_Wheel = Wheel;
        this.m_Motor = Motor;
    }
    public RobotWheel getWheel(){
        return this.m_Wheel;
    }
    public void setWheel(@NonNull RobotWheel Wheel){
        this.m_Wheel = Wheel;
    }
    public RobotMotor getMotor(){
        return this.m_Motor;
    }
    public void setMotor(@NonNull RobotMotor Motor){
        this.m_Motor = Motor;
    }
    public double getLastTaskMovedDistance(){
        return ((double) this.m_Motor.getLastTaskMovedCounts()) / this.m_Motor.getCountsPerRev() * this.m_Wheel.getPerimeter();
    }
    public double getSpeedForMovingRobotX(double SpeedInX){
        double isPositiveOrNegativeDist = this.m_Wheel.calculateDistanceByRobotAxisX(SpeedInX);
        double realPower = 0;
        if(isPositiveOrNegativeDist < 0){
            realPower = -Math.abs(SpeedInX);
        }else{
            realPower = Math.abs(SpeedInX);
        }
        return realPower;
    }
    public double getSpeedForMovingRobotY(double SpeedInY){
        double isPositiveOrNegativeDist = this.m_Wheel.calculateDistanceByRobotAxisY(SpeedInY);
        double realPower = 0;
        if(isPositiveOrNegativeDist < 0){
            realPower = -Math.abs(SpeedInY);
        }else{
            realPower = Math.abs(SpeedInY);
        }
        return realPower;
    }
}
