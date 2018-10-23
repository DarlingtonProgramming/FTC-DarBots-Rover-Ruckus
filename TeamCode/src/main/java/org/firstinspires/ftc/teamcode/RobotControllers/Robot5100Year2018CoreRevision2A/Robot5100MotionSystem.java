package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotSensorWrapper;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderMotion;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotion;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotWheel;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.DcMotorSpeedTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;

public class Robot5100MotionSystem implements RobotMotionSystem, RobotEventLoopable {
    private RobotSensorWrapper<RobotEncoderMotion> m_FrontWheel;
    private RobotSensorWrapper<RobotEncoderMotion> m_LeftBackWheel;
    private RobotSensorWrapper<RobotEncoderMotion> m_RightBackWheel;
    private RobotPositionTracker m_PositionTracker;
    private motionType m_CurrentMotionType;
    private RobotMotionDirection m_CurrentDirection;

    public Robot5100MotionSystem(DcMotor FrontDC, DcMotor LeftBackDC, DcMotor RightBackDC, RobotPositionTracker PositionTracker){
        FrontDC.setDirection(DcMotorSimple.Direction.REVERSE);
        LeftBackDC.setDirection(DcMotorSimple.Direction.REVERSE);
        RightBackDC.setDirection(DcMotorSimple.Direction.REVERSE);
        RobotEncoderMotor FrontWheelMotor = new RobotEncoderMotor(FrontDC,Robot5100Settings.frontMotorCountsPerRev,Robot5100Settings.frontMotorRevPerSec);
        RobotEncoderMotor LeftBackWheelMotor = new RobotEncoderMotor(LeftBackDC,Robot5100Settings.leftBackMotorCountsPerRev,Robot5100Settings.leftBackMotorRevPerSec);
        RobotEncoderMotor RightBackWheelMotor = new RobotEncoderMotor(RightBackDC,Robot5100Settings.rightBackMotorCountsPerRev,Robot5100Settings.rightBackMotorRevPerSec);
        RobotWheel FrontWheel = new RobotWheel(Robot5100Settings.frontWheelRadius,Robot5100Settings.frontWheelAngle);
        RobotWheel LeftBackWheel = new RobotWheel(Robot5100Settings.leftBackWheelRadius,Robot5100Settings.leftBackWheelAngle);
        RobotWheel RightBackWheel = new RobotWheel(Robot5100Settings.rightBackWheelRadius,Robot5100Settings.rightBackWheelAngle);
        this.m_FrontWheel = new RobotSensorWrapper<RobotEncoderMotion>(new RobotEncoderMotion(FrontWheelMotor,FrontWheel),Robot5100Settings.frontWheelPos);
        this.m_LeftBackWheel = new RobotSensorWrapper<RobotEncoderMotion>(new RobotEncoderMotion(LeftBackWheelMotor,LeftBackWheel),Robot5100Settings.leftBackWheelPos);
        this.m_RightBackWheel = new RobotSensorWrapper<RobotEncoderMotion>(new RobotEncoderMotion(RightBackWheelMotor,RightBackWheel),Robot5100Settings.rightBackWheelPos);
    }

    public RobotMotionDirection getMovingDirection(){
        return this.m_CurrentDirection;
    }

    public RobotSensorWrapper<RobotEncoderMotion> getFrontWheel(){
        return this.m_FrontWheel;
    }

    public RobotSensorWrapper<RobotEncoderMotion> getLeftBackWheel(){
        return this.m_LeftBackWheel;
    }

    public RobotSensorWrapper<RobotEncoderMotion> getRightBackWheel(){
        return this.m_RightBackWheel;
    }

    @Override
    public RobotPositionTracker getPositionTracker(){
        return this.m_PositionTracker;
    }

    @Override
    public boolean isBusy() {
        return this.getFrontWheel().getSensor().isBusy() || this.getLeftBackWheel().getSensor().isBusy() || this.getRightBackWheel().getSensor().isBusy();
    }

    @Override
    public void waitUntilMotionFinish() {
        while(this.isBusy()){
            this.doLoop();
        }
    }

    @Override
    public void setPositionTracker(RobotPositionTracker newPositionTracker) {
        this.m_PositionTracker = newPositionTracker;
    }

    @Override
    public void turnToAbsFieldAngle(double AngleInDegree, double Speed) {
        double deltaAngle = AngleInDegree - this.getPositionTracker().getRobotRotation();
        this.turnOffsetAroundCenter(AngleInDegree,Speed);
    }

    @Override
    public void turnOffsetAroundCenter(double AngleInDegree, double Speed) {
        if(this.isBusy()){
            this.stopMoving();
        }
        this.m_CurrentMotionType = motionType.turningFixedAngle;
        this.m_CurrentDirection = RobotMotionDirection.rotating;
        double[] robotFixedPoint = {0,0};
        double[] frontPowerPoint = this.getFrontWheel().getPos();
        double distanceFrontWheel = this.getPositionTracker().calculateDistanceToRotateAroundRobotPoint(robotFixedPoint,frontPowerPoint,AngleInDegree);
        double[] leftBackPowerPoint = this.getLeftBackWheel().getPos();
        double distanceLeftBackWheel = this.getPositionTracker().calculateDistanceToRotateAroundRobotPoint(robotFixedPoint,leftBackPowerPoint,AngleInDegree);
        double[] rightBackPowerPoint = this.getRightBackWheel().getPos();
        double distanceRightBackWheel = this.getPositionTracker().calculateDistanceToRotateAroundRobotPoint(robotFixedPoint,rightBackPowerPoint,AngleInDegree);
        this.getFrontWheel().getSensor().addDistanceTask(distanceFrontWheel,Speed,null,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
        this.getLeftBackWheel().getSensor().addDistanceTask(distanceLeftBackWheel,Speed,null,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
        this.getRightBackWheel().getSensor().addDistanceTask(distanceRightBackWheel,Speed,null,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
    }

    @Override
    public void keepTurningOffsetAroundCenter(double Speed) {
        if(this.isBusy()){
            this.stopMoving();
        }
        this.m_CurrentMotionType = motionType.keepingTurningWithSpeed;
        this.m_CurrentDirection = RobotMotionDirection.rotating;
        double[] robotFixedPoint = {0,0};
        double[] frontPowerPoint = this.getFrontWheel().getPos();
        double[] leftBackPowerPoint = this.getLeftBackWheel().getPos();
        double[] rightBackPowerPoint = this.getRightBackWheel().getPos();
        double distanceFront = this.getPositionTracker().calculateDistanceToRotateAroundRobotPoint(robotFixedPoint,frontPowerPoint,1);
        double distanceLeftBack = this.getPositionTracker().calculateDistanceToRotateAroundRobotPoint(robotFixedPoint,leftBackPowerPoint,1);
        double distanceRightBack = this.getPositionTracker().calculateDistanceToRotateAroundRobotPoint(robotFixedPoint,rightBackPowerPoint,1);
        double commonFactor = Speed / Math.max(Math.max(Math.abs(distanceFront),Math.abs(distanceLeftBack)),Math.abs(distanceRightBack));
        double speedFrontMoving = distanceFront * commonFactor;
        double speedLeftBackMoving = distanceLeftBack * commonFactor;
        double speedRightBackMoving = distanceRightBack * commonFactor;
        this.getFrontWheel().getSensor().getEncoderMotor().addFixedSpeedTask(new DcMotorSpeedTask(speedFrontMoving,null,false,0));
        this.getLeftBackWheel().getSensor().getEncoderMotor().addFixedSpeedTask(new DcMotorSpeedTask(speedLeftBackMoving,null,false,0));
        this.getRightBackWheel().getSensor().getEncoderMotor().addFixedSpeedTask(new DcMotorSpeedTask(speedRightBackMoving,null,false,0));
    }

    @Override
    public void driveTo(double[] fieldPos, double Speed) {
        double angleToTurn = this.m_PositionTracker.calculateAngleDeltaMovingToFieldPoint(fieldPos);
        this.turnOffsetAroundCenter(angleToTurn,Speed);
        this.waitUntilMotionFinish();
        double distanceToGo = this.m_PositionTracker.calculateDistanceToFixedPoint(fieldPos);
        this.driveForward(distanceToGo,Speed);
    }

    @Override
    public void driveForward(double Distance, double Speed) {
        if(this.isBusy()){
            this.stopMoving();
        }
        this.m_CurrentMotionType = motionType.movingFixedDistance;
        this.m_CurrentDirection = RobotMotionDirection.inY;
        double leftBackDistance = this.getLeftBackWheel().getSensor().getWheel().calculateDistanceByRobotAxisY(Distance);
        double rightBackDistance = this.getRightBackWheel().getSensor().getWheel().calculateDistanceByRobotAxisY(Distance);
        this.m_LeftBackWheel.getSensor().addDistanceTask(leftBackDistance,Speed,null,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
        this.m_RightBackWheel.getSensor().addDistanceTask(rightBackDistance,Speed,null,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
    }

    @Override
    public void driveBackward(double Distance, double Speed) {
        this.driveForward(-Distance,Speed);
    }

    @Override
    public void driveToLeft(double Distance, double Speed) {
        this.driveToRight(-Distance,Speed);
    }

    @Override
    public void driveToRight(double Distance, double Speed) {
        if(this.isBusy()){
            this.stopMoving();
        }
        this.m_CurrentMotionType = motionType.movingFixedDistance;
        this.m_CurrentDirection = RobotMotionDirection.inX;
        double leftBackDistance = this.getLeftBackWheel().getSensor().getWheel().calculateDistanceByRobotAxisX(Distance);
        double rightBackDistance = this.getRightBackWheel().getSensor().getWheel().calculateDistanceByRobotAxisX(Distance);
        double frontDistance = this.getFrontWheel().getSensor().getWheel().calculateDistanceByRobotAxisX(Distance);
        double maxDistance = Math.max(Math.abs(leftBackDistance),Math.max(Math.abs(rightBackDistance),Math.abs(frontDistance)));
        double speedFactor = Speed / maxDistance;
        double frontSpeed = frontDistance * speedFactor;
        double leftBackSpeed = leftBackDistance * speedFactor;
        double rightBackSpeed = rightBackDistance * speedFactor;
        this.getFrontWheel().getSensor().addDistanceTask(frontDistance,frontSpeed,null,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
        this.getLeftBackWheel().getSensor().addDistanceTask(leftBackDistance,leftBackSpeed,null,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
        this.getRightBackWheel().getSensor().addDistanceTask(rightBackDistance,rightBackSpeed,null,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
    }

    @Override
    public void driveForwardWithSpeed(double Speed) {
        if(this.isBusy()){
            this.stopMoving();
        }
        this.m_CurrentMotionType = motionType.keepingMovingWithFixedSpeed;
        this.m_CurrentDirection = RobotMotionDirection.inY;
        this.getLeftBackWheel().getSensor().getMotor().addFixedSpeedTask(new DcMotorSpeedTask(Speed,null,false,0));
        this.getRightBackWheel().getSensor().getMotor().addFixedSpeedTask(new DcMotorSpeedTask(-Speed,null,false,0));
    }

    @Override
    public motionType getCurrentMotionType(){
        return this.m_CurrentMotionType;
    }

    @Override
    public void driveBackwardWithSpeed(double Speed) {
        this.driveForwardWithSpeed(Speed);
    }

    @Override
    public void driveToLeftWithSpeed(double Speed) {
        this.driveToRightWithSpeed(Speed);
    }

    @Override
    public void driveToRightWithSpeed(double Speed) {
        if(this.isBusy()){
            this.stopMoving();
        }
        this.m_CurrentMotionType = motionType.keepingMovingWithFixedSpeed;
        this.m_CurrentDirection = RobotMotionDirection.inX;
        double frontDistance = this.getFrontWheel().getSensor().getWheel().calculateDistanceByRobotAxisX(1);
        double leftBackDistance = this.getLeftBackWheel().getSensor().getWheel().calculateDistanceByRobotAxisX(1);
        double rightBackDistance = this.getRightBackWheel().getSensor().getWheel().calculateDistanceByRobotAxisX(1);
        double biggestDistance = Math.max(Math.max(Math.abs(frontDistance),Math.abs(leftBackDistance)),Math.abs(rightBackDistance));
        double speedFactor = Speed / biggestDistance;
        double frontSpeed = frontDistance * speedFactor;
        double leftBackSpeed = leftBackDistance * speedFactor;
        double rightBackSpeed = rightBackDistance * speedFactor;
        this.getFrontWheel().getSensor().getMotor().addFixedSpeedTask(new DcMotorSpeedTask(frontSpeed,null,false,0));
        this.getLeftBackWheel().getSensor().getMotor().addFixedSpeedTask(new DcMotorSpeedTask(leftBackSpeed,null,false,0));
        this.getRightBackWheel().getSensor().getMotor().addFixedSpeedTask(new DcMotorSpeedTask(rightBackSpeed,null,false,0));
    }

    @Override
    public void stopMoving() {
        if(!this.isBusy()){
            this.m_CurrentMotionType = motionType.stopped;
            return;
        }
        double[] robotOrigin = {0,0};
        this.getFrontWheel().getSensor().getMotor().deleteAllTasks();
        this.getLeftBackWheel().getSensor().getMotor().deleteAllTasks();
        this.getRightBackWheel().getSensor().getMotor().deleteAllTasks();
        switch(this.m_CurrentMotionType){
            case turningFixedAngle:
            case keepingTurningWithSpeed:
                this.getPositionTracker().moveWithRobotFixedPointAndPowerPoint(robotOrigin,this.getFrontWheel().getPos(),this.getFrontWheel().getSensor().getLastTaskMovedDistance());
                break;
            case movingFixedDistance:
            case keepingMovingWithFixedSpeed:
                switch(this.m_CurrentDirection){
                    case inY:
                        double YMoved = this.getLeftBackWheel().getSensor().getLastTaskMovedDistance() * this.getLeftBackWheel().getSensor().getWheel().getYPerDistance();
                        this.getPositionTracker().moveThroughRobotAngle(0,YMoved);
                        break;
                    case inX:
                        double XMoved = this.getLeftBackWheel().getSensor().getLastTaskMovedDistance() * this.getLeftBackWheel().getSensor().getWheel().getXPerDistance();
                        this.getPositionTracker().moveThroughRobotAngle(90,XMoved);
                        break;
                }
                break;
        }
        this.m_CurrentMotionType = motionType.stopped;
    }

    @Override
    public void doLoop() {
        this.getFrontWheel().getSensor().getEncoderMotor().doLoop();
        this.getLeftBackWheel().getSensor().getEncoderMotor().doLoop();
        this.getRightBackWheel().getSensor().getEncoderMotor().doLoop();
        if(!this.isBusy() && this.m_CurrentMotionType != motionType.stopped){
            this.stopMoving();
        }
    }
}