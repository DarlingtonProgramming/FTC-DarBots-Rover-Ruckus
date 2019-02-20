package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ChassisControllers;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.Robot2DPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.RobotMotorTasks.RobotFixedSpeedTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotion;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystemFixedTurnTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystemFixedXDistanceTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystemFixedZDistanceTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystemTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystemTeleOpControlTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionTaskCallBack;
import org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode.Robot4100Core;

public class OmniWheel4SideDiamondShaped extends RobotMotionSystem {
    public class OmniWheel4SideFixedXTask extends RobotMotionSystemFixedXDistanceTask {

        public OmniWheel4SideFixedXTask(double XDistance, double Speed){
            super(XDistance,Speed);
        }
        public OmniWheel4SideFixedXTask(OmniWheel4SideFixedXTask Task){
            super(Task);
        }
        @Override
        protected void __startTask() {
            double sqrt2 = Math.sqrt(2);
            double FLDistance = -this.getXDistance() * sqrt2 / 4.0 / OmniWheel4SideDiamondShaped.this.getLinearMotionFrictionFactor(); //turning clockwise, the installed angle must be 45 deg
            /*
            double FRDistance = -this.getXDistance() * sqrt2 / 4.0 / OmniWheel4SideDiamondShaped.this.getLinearMotionFrictionFactor();
            double BLDistance = this.getXDistance() * sqrt2 / 4.0 / OmniWheel4SideDiamondShaped.this.getLinearMotionFrictionFactor();
            double BRDistance = this.getXDistance() * sqrt2 / 4.0 / OmniWheel4SideDiamondShaped.this.getLinearMotionFrictionFactor();
            */
            double PositiveSpeed = this.getXDistance() >= 0 ? Math.abs(this.getSpeed()) : -Math.abs(this.getSpeed());
            RobotMotionTaskCallBack FLCallBack = new RobotMotionTaskCallBack() {
                @Override
                public void finishRunning(RobotMotion Motion, boolean timeOut, double timeUsedInSec, int CountsMoved, double DistanceMoved) {
                    if(OmniWheel4SideFixedXTask.this.getMotionSystem().getPositionTracker() != null){
                        OmniWheel4SideFixedXTask.this.getMotionSystem().getPositionTracker().drive_MoveThroughRobotAngle(0,-DistanceMoved / (Math.sqrt(2)) * 4.0 * OmniWheel4SideDiamondShaped.this.getLinearMotionFrictionFactor());
                    }
                    OmniWheel4SideFixedXTask.this.stopTask();
                }
            };
            OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.getMotorController().replaceTask(OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.new FixedDistanceSpeedCtlTask(FLDistance,this.getSpeed(),FLCallBack,false));
            OmniWheel4SideDiamondShaped.this.m_RightFrontMotor.getMotorController().replaceTask(new RobotFixedSpeedTask(0,-PositiveSpeed,null));
            OmniWheel4SideDiamondShaped.this.m_LeftBackMotor.getMotorController().replaceTask(new RobotFixedSpeedTask(0,PositiveSpeed,null));
            OmniWheel4SideDiamondShaped.this.m_RightBackMotor.getMotorController().replaceTask(new RobotFixedSpeedTask(0,PositiveSpeed,null));
        }

        @Override
        public void updateStatus() {
            return;
        }
    }
    public class OmniWheel4SideFixedZTask extends RobotMotionSystemFixedZDistanceTask {

        public OmniWheel4SideFixedZTask(double ZDistance, double Speed){
            super(ZDistance,Speed);
        }
        public OmniWheel4SideFixedZTask(OmniWheel4SideFixedZTask Task){
            super(Task);
        }

        @Override
        protected void __startTask() {
            double sqrt2 = Math.sqrt(2);
            double FLDistance = -this.getZDistance() * sqrt2 / 4.0 / OmniWheel4SideDiamondShaped.this.getLinearMotionFrictionFactor(); //turning clockwise, the installed angle must be 45 deg
            /*
            double FRDistance = this.getZDistance() * sqrt2 / 4.0 / OmniWheel4SideDiamondShaped.this.getLinearMotionFrictionFactor();
            double BLDistance = -this.getZDistance() * sqrt2 / 4.0 / OmniWheel4SideDiamondShaped.this.getLinearMotionFrictionFactor();
            double BRDistance = this.getZDistance() * sqrt2 / 4.0 / OmniWheel4SideDiamondShaped.this.getLinearMotionFrictionFactor();
            */
            double PositiveSpeed = this.getZDistance() >= 0 ? Math.abs(this.getSpeed()) : -Math.abs(this.getSpeed());
            RobotMotionTaskCallBack FLCallBack = new RobotMotionTaskCallBack() {
                @Override
                public void finishRunning(RobotMotion Motion, boolean timeOut, double timeUsedInSec, int CountsMoved, double DistanceMoved) {
                    if(OmniWheel4SideFixedZTask.this.getMotionSystem().getPositionTracker() != null){
                        OmniWheel4SideFixedZTask.this.getMotionSystem().getPositionTracker().drive_MoveThroughRobotAngle(90,-DistanceMoved / (Math.sqrt(2)) * 4.0 * OmniWheel4SideDiamondShaped.this.getLinearMotionFrictionFactor());
                    }
                    OmniWheel4SideFixedZTask.this.stopTask();
                }
            };
            OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.getMotorController().replaceTask(OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.new FixedDistanceSpeedCtlTask(FLDistance,this.getSpeed() ,FLCallBack,false));
            OmniWheel4SideDiamondShaped.this.m_RightFrontMotor.getMotorController().replaceTask(new RobotFixedSpeedTask(0,PositiveSpeed,null));
            OmniWheel4SideDiamondShaped.this.m_LeftBackMotor.getMotorController().replaceTask(new RobotFixedSpeedTask(0,-PositiveSpeed,null));
            OmniWheel4SideDiamondShaped.this.m_RightBackMotor.getMotorController().replaceTask(new RobotFixedSpeedTask(0,PositiveSpeed,null));
        }

        @Override
        public void updateStatus() {
            return;
        }
    }
    public class OmniWheel4SideFixedTurnTask extends RobotMotionSystemFixedTurnTask {

        public OmniWheel4SideFixedTurnTask(double TurnDeg, double Speed) {
            super(TurnDeg, Speed);
        }

        public OmniWheel4SideFixedTurnTask(RobotMotionSystemFixedTurnTask Task) {
            super(Task);
        }

        @Override
        protected void __startTask() {
            double FLDistance = Math.toRadians(this.getTurnDeg()) * OmniWheel4SideDiamondShaped.this.getLeftFrontMotor().getRobotWheel().getDistanceFromCenterOfRobot(); //turning clockwise, the installed angle must be 45 deg
            FLDistance /= OmniWheel4SideDiamondShaped.this.getRotationalMotionFrictionFactor();
            double FRDistance = FLDistance;
            double BLDistance = FLDistance;
            double BRDistance = FLDistance;
            double PositiveSpeed = this.getTurnDeg() >= 0 ? Math.abs(this.getSpeed()) : -Math.abs(this.getSpeed());
            RobotMotionTaskCallBack FLCallBack = new RobotMotionTaskCallBack() {
                @Override
                public void finishRunning(RobotMotion Motion, boolean timeOut, double timeUsedInSec, int CountsMoved, double DistanceMoved) {
                    if(OmniWheel4SideFixedTurnTask.this.getMotionSystem().getPositionTracker() == null){
                        OmniWheel4SideFixedTurnTask.this.getMotionSystem().getPositionTracker().drive_RotateAroundRobotPointWithRadiusAndPowerPoint(new Robot2DPositionTracker.Robot2DPositionRobotAxisIndicator(0,0,0),OmniWheel4SideDiamondShaped.this.getLeftFrontMotor().getRobotWheel().getOnRobotPosition().getDistanceToOrigin(),DistanceMoved * OmniWheel4SideDiamondShaped.this.getRotationalMotionFrictionFactor());
                    }
                    OmniWheel4SideFixedTurnTask.this.stopTask();
                }
            };
            OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.getMotorController().replaceTask(OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.new FixedDistanceSpeedCtlTask(FLDistance,this.getSpeed() ,FLCallBack,false));
            OmniWheel4SideDiamondShaped.this.m_RightFrontMotor.getMotorController().replaceTask(new RobotFixedSpeedTask(0,PositiveSpeed,null));
            OmniWheel4SideDiamondShaped.this.m_LeftBackMotor.getMotorController().replaceTask(new RobotFixedSpeedTask(0,PositiveSpeed,null));
            OmniWheel4SideDiamondShaped.this.m_RightBackMotor.getMotorController().replaceTask(new RobotFixedSpeedTask(0,PositiveSpeed,null));
        }

        @Override
        public void updateStatus() {
            return;
        }
    }
    public class OmniWheel4SideTeleOpTask extends RobotMotionSystemTeleOpControlTask{
        protected RobotFixedSpeedTask m_FLTask = null;
        protected RobotFixedSpeedTask m_FRTask = null;
        protected RobotFixedSpeedTask m_BLTask = null;
        protected RobotFixedSpeedTask m_BRTask = null;
        @Override
        protected void __updateDriveSpeedAndPositionTracker() {
            double FLPower = -super.getDriveXSpeed() - super.getDriveZSpeed() + super.getDriveRotationSpeed();
            double FRPower = -super.getDriveXSpeed() + super.getDriveZSpeed() + super.getDriveRotationSpeed();
            double BLPower = super.getDriveXSpeed() - super.getDriveZSpeed() + super.getDriveRotationSpeed();
            double BRPower = super.getDriveXSpeed() + super.getDriveZSpeed() + super.getDriveRotationSpeed();
            FLPower = Range.clip(FLPower,-1.0,1.0);
            FRPower = Range.clip(FRPower,-1.0,1.0);
            BLPower = Range.clip(BLPower,-1.0,1.0);
            BRPower = Range.clip(BRPower,-1.0,1.0);
            this.m_FLTask.setSpeed(FLPower);
            this.m_FRTask.setSpeed(FRPower);
            this.m_BLTask.setSpeed(BLPower);
            this.m_BRTask.setSpeed(BRPower);
        }

        @Override
        protected void __startDrive() {
            this.m_FLTask = new RobotFixedSpeedTask(0,0,null);
            this.m_FRTask = new RobotFixedSpeedTask(0,0,null);
            this.m_BLTask = new RobotFixedSpeedTask(0,0,null);
            this.m_BRTask = new RobotFixedSpeedTask(0,0,null);
            OmniWheel4SideDiamondShaped.this.m_LeftFrontMotor.getMotorController().replaceTask(m_FLTask);
            OmniWheel4SideDiamondShaped.this.m_RightFrontMotor.getMotorController().replaceTask(m_FRTask);
            OmniWheel4SideDiamondShaped.this.m_LeftBackMotor.getMotorController().replaceTask(m_BLTask);
            OmniWheel4SideDiamondShaped.this.m_RightBackMotor.getMotorController().replaceTask(m_BRTask);
        }
    }


    private RobotMotion m_LeftFrontMotor, m_RightFrontMotor, m_LeftBackMotor, m_RightBackMotor;
    public OmniWheel4SideDiamondShaped(@NonNull RobotMotion LeftFrontMotor, @NonNull RobotMotion RightFrontMotor, @NonNull RobotMotion LeftBackMotor, @NonNull RobotMotion RightBackMotor, Robot2DPositionTracker PositionTracker) {
        super(PositionTracker);
        this.m_LeftFrontMotor = LeftFrontMotor;
        this.m_RightFrontMotor = RightFrontMotor;
        this.m_LeftBackMotor = LeftBackMotor;
        this.m_RightBackMotor = RightBackMotor;
    }

    public OmniWheel4SideDiamondShaped(OmniWheel4SideDiamondShaped OmniWheelDrive){
        super(OmniWheelDrive);
        this.m_LeftFrontMotor = OmniWheelDrive.m_LeftFrontMotor;
        this.m_RightFrontMotor = OmniWheelDrive.m_RightFrontMotor;
        this.m_LeftBackMotor = OmniWheelDrive.m_LeftBackMotor;
        this.m_RightBackMotor = OmniWheelDrive.m_RightBackMotor;
    }


    @Override
    public RobotDebugger.RobotDebuggerCallable getDebuggerCallable(String partName) {
        return new RobotDebugger.ObjectDebuggerWrapper<>(partName, new Object() {
            @Override
            public String toString() {
                if (OmniWheel4SideDiamondShaped.this.getPositionTracker() == null)
                    return "Null";

                Robot2DPositionIndicator Position = OmniWheel4SideDiamondShaped.this.getPositionTracker().getPosition();

                if (Position != null)
                    return "(X: " + Position.getX() + ", Z: " + Position.getZ() + ") - Rotation: " + Position.getRotationY() + " deg";
                else
                    return "Null";
            }
        });
    }

    @Override
    protected void __stopMotion() {
        this.m_LeftFrontMotor.getMotorController().deleteAllTasks();
        this.m_LeftBackMotor.getMotorController().deleteAllTasks();
        this.m_RightFrontMotor.getMotorController().deleteAllTasks();
        this.m_RightBackMotor.getMotorController().deleteAllTasks();
    }

    @Override
    public RobotMotionSystemFixedXDistanceTask getFixedXDistanceTask(double XDistance, double Speed) {
        return this.new OmniWheel4SideFixedXTask(XDistance,Speed);
    }

    @Override
    public RobotMotionSystemFixedZDistanceTask getFixedZDistanceTask(double ZDistance, double Speed) {
        return this.new OmniWheel4SideFixedZTask(ZDistance,Speed);
    }

    @Override
    public RobotMotionSystemFixedTurnTask getFixedTurnTask(double Deg, double Speed) {
        return this.new OmniWheel4SideFixedTurnTask(Deg,Speed);
    }

    @Override
    public RobotMotionSystemTeleOpControlTask getTeleOpTask() {
        return this.new OmniWheel4SideTeleOpTask();
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

    @Override
    public void updateStatus(){
        this.m_LeftFrontMotor.getMotorController().updateStatus();
        this.m_RightFrontMotor.getMotorController().updateStatus();
        this.m_LeftBackMotor.getMotorController().updateStatus();
        this.m_RightBackMotor.getMotorController().updateStatus();
        super.updateStatus();
    }
}
