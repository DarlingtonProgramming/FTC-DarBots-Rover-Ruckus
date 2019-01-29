package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ChassisControllers;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.Robot2DPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotion;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystemTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionTaskCallBack;

public class OmniWheel4SideDiamondShaped extends RobotMotionSystem {
    public class OmniWheel4SideFixedXTask extends RobotMotionSystemTask {
        protected double m_XDistance;
        protected double m_Speed;

        public OmniWheel4SideFixedXTask(double XDistance, double Speed){
            this.m_XDistance = XDistance;
            this.m_Speed = Speed;
        }
        public OmniWheel4SideFixedXTask(OmniWheel4SideFixedXTask Task){
            super(Task);
            this.m_XDistance = Task.m_XDistance;
            this.m_Speed = Task.m_Speed;
        }
        public double getSpeed(){
            return this.m_Speed;
        }
        public void setSpeed(double Speed){
            this.m_Speed = Speed;
        }
        public double getXDistance(){
            return this.m_XDistance;
        }
        public void setXDistance(double Distance){
            this.m_XDistance = Distance;
        }

        @Override
        protected void __startTask() {
            double sqrt2 = Math.sqrt(2);
            double FLDistance = -this.m_XDistance * sqrt2; //turning clockwise, the installed angle must be 45 deg
            double FRDistance = -this.m_XDistance * sqrt2;
            double BLDistance = this.m_XDistance * sqrt2;
            double BRDistance = this.m_XDistance * sqrt2;
            RobotMotionTaskCallBack FLCallBack = new RobotMotionTaskCallBack() {
                @Override
                public void finishRunning(RobotMotion Motion, boolean timeOut, double timeUsedInSec, int CountsMoved, double DistanceMoved) {
                    OmniWheel4SideDiamondShaped.this.getPositionTracker().drive_MoveThroughRobotAngle(0,-DistanceMoved / (Math.sqrt(2)));
                }
            };
            OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.getMotorController().addTask(OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.new FixedDistanceSpeedCtlTask(FLDistance,this.m_Speed,FLCallBack));
            OmniWheel4SideDiamondShaped.this.m_RightFrontMotor.getMotorController().addTask(OmniWheel4SideDiamondShaped.this.m_RightFrontMotor.new FixedDistanceSpeedCtlTask(FRDistance,this.m_Speed,null));
            OmniWheel4SideDiamondShaped.this.m_LeftBackMotor.getMotorController().addTask(OmniWheel4SideDiamondShaped.this.m_LeftBackMotor.new FixedDistanceSpeedCtlTask(BLDistance,this.m_Speed,null));
            OmniWheel4SideDiamondShaped.this.m_RightBackMotor.getMotorController().addTask(OmniWheel4SideDiamondShaped.this.m_RightBackMotor.new FixedDistanceSpeedCtlTask(BRDistance,this.m_Speed,null));
        }

        @Override
        public void updateStatus() {
            if((!OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.getMotorController().isBusy())
                    && (!OmniWheel4SideDiamondShaped.this.m_RightFrontMotor.getMotorController().isBusy())
                    && (!OmniWheel4SideDiamondShaped.this.m_LeftBackMotor.getMotorController().isBusy())
                    && (!OmniWheel4SideDiamondShaped.this.m_RightBackMotor.getMotorController().isBusy())
                    ){
                this.stopTask();
            }
        }
    }
    public class OmniWheel4SideFixedZTask extends RobotMotionSystemTask {
        protected double m_ZDistance;
        protected double m_Speed;

        public OmniWheel4SideFixedZTask(double ZDistance, double Speed){
            this.m_ZDistance = ZDistance;
            this.m_Speed = Speed;
        }
        public OmniWheel4SideFixedZTask(OmniWheel4SideFixedZTask Task){
            super(Task);
            this.m_ZDistance = Task.m_ZDistance;
            this.m_Speed = Task.m_Speed;
        }
        public double getSpeed(){
            return this.m_Speed;
        }
        public void setSpeed(double Speed){
            this.m_Speed = Speed;
        }
        public double getZDistance(){
            return this.m_ZDistance;
        }
        public void setZDistance(double Distance){
            this.m_ZDistance = Distance;
        }

        @Override
        protected void __startTask() {
            double sqrt2 = Math.sqrt(2);
            double FLDistance = -this.m_ZDistance * sqrt2; //turning clockwise, the installed angle must be 45 deg
            double FRDistance = this.m_ZDistance * sqrt2;
            double BLDistance = -this.m_ZDistance * sqrt2;
            double BRDistance = this.m_ZDistance * sqrt2;
            RobotMotionTaskCallBack FLCallBack = new RobotMotionTaskCallBack() {
                @Override
                public void finishRunning(RobotMotion Motion, boolean timeOut, double timeUsedInSec, int CountsMoved, double DistanceMoved) {
                    OmniWheel4SideDiamondShaped.this.getPositionTracker().drive_MoveThroughRobotAngle(90,-DistanceMoved / (Math.sqrt(2)));
                }
            };
            OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.getMotorController().addTask(OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.new FixedDistanceSpeedCtlTask(FLDistance,this.m_Speed,FLCallBack));
            OmniWheel4SideDiamondShaped.this.m_RightFrontMotor.getMotorController().addTask(OmniWheel4SideDiamondShaped.this.m_RightFrontMotor.new FixedDistanceSpeedCtlTask(FRDistance,this.m_Speed,null));
            OmniWheel4SideDiamondShaped.this.m_LeftBackMotor.getMotorController().addTask(OmniWheel4SideDiamondShaped.this.m_LeftBackMotor.new FixedDistanceSpeedCtlTask(BLDistance,this.m_Speed,null));
            OmniWheel4SideDiamondShaped.this.m_RightBackMotor.getMotorController().addTask(OmniWheel4SideDiamondShaped.this.m_RightBackMotor.new FixedDistanceSpeedCtlTask(BRDistance,this.m_Speed,null));
        }

        @Override
        public void updateStatus() {
            if((!OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.getMotorController().isBusy())
                    && (!OmniWheel4SideDiamondShaped.this.m_RightFrontMotor.getMotorController().isBusy())
                    && (!OmniWheel4SideDiamondShaped.this.m_LeftBackMotor.getMotorController().isBusy())
                    && (!OmniWheel4SideDiamondShaped.this.m_RightBackMotor.getMotorController().isBusy())
                    ){
                this.stopTask();
            }
        }
    }
    

    protected RobotMotion m_LeftFrontMotor, m_RightFrontMotor, m_LeftBackMotor, m_RightBackMotor;
    public OmniWheel4SideDiamondShaped(Robot2DPositionTracker PositionTracker) {
        super(PositionTracker);
    }

    public OmniWheel4SideDiamondShaped(OmniWheel4SideDiamondShaped MotionSystem) {
        super(MotionSystem);

    }

    public RobotMotion getLeftFrontMotor(){
        return this.m_LeftFrontMotor;
    }
    public void setLeftFrontMotor(@NonNull RobotMotion LeftFrontMotor){
        this.m_LeftFrontMotor = LeftFrontMotor;
    }
    public RobotMotion getRightFrontMotor(){
        return this.m_RightFrontMotor;
    }
    public void setRightFrontMotor(@NonNull RobotMotion RightFrontMotor){
        this.m_RightFrontMotor = RightFrontMotor;
    }
    public RobotMotion getLeftBackMotor(){
        return this.m_LeftBackMotor;
    }
    public void setLeftBackMotor(@NonNull RobotMotion LeftBackMotor){
        this.m_LeftBackMotor = LeftBackMotor;
    }
    public RobotMotion getRightBackMotor(){
        return this.m_RightBackMotor;
    }
    public void setRightBackMotor(@NonNull RobotMotion RightBackMotor){
        this.m_RightBackMotor = RightBackMotor;
    }
}
