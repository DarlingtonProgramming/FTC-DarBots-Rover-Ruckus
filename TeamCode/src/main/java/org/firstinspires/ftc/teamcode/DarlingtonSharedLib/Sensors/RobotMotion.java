package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

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
        public FixedDistanceMotionTask(double Distance, double Speed, boolean timeControl, double timeControlPercent, RobotMotionFinishCallBack CallBack){
            super((int) Math.abs(RobotMotion.this.getWheel().calculateCycleForDistance(Distance)*RobotMotion.this.getMotor().getCountsPerRev()),Speed,timeControl,timeControlPercent,new MotionCallBackConverter(CallBack,RobotMotion.this));
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
    public void setWheel(RobotWheel Wheel){
        this.m_Wheel = Wheel;
    }
    public RobotMotor getMotor(){
        return this.m_Motor;
    }
    public void setMotor(RobotMotor Motor){
        this.m_Motor = Motor;
    }
}
