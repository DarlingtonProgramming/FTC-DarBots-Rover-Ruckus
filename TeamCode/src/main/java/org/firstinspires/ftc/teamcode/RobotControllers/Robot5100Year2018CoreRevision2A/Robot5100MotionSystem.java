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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotSensorWrapper;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderMotion;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotWheel;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;

public class Robot5100MotionSystem implements RobotMotionSystem, RobotEventLoopable {
    private RobotSensorWrapper<RobotEncoderMotion> m_FrontWheel;
    private RobotSensorWrapper<RobotEncoderMotion> m_LeftBackWheel;
    private RobotSensorWrapper<RobotEncoderMotion> m_RightBackWheel;
    private RobotPositionTracker m_PositionTracker;
    private motionType m_CurrentMotionType;
    private RobotMotionDirection m_CurrentDirection;

    public Robot5100MotionSystem(@NonNull DcMotor FrontDC, @NonNull DcMotor LeftBackDC, @NonNull DcMotor RightBackDC, @NonNull RobotPositionTracker PositionTracker){
        FrontDC.setDirection(DcMotorSimple.Direction.REVERSE);
        LeftBackDC.setDirection(DcMotorSimple.Direction.REVERSE);
        RightBackDC.setDirection(DcMotorSimple.Direction.REVERSE);
        RobotEncoderMotor FrontWheelMotor = new RobotEncoderMotor(FrontDC,Robot5100Settings.frontMotorCountsPerRev,Robot5100Settings.frontMotorRevPerSec,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
        RobotEncoderMotor LeftBackWheelMotor = new RobotEncoderMotor(LeftBackDC,Robot5100Settings.leftBackMotorCountsPerRev,Robot5100Settings.leftBackMotorRevPerSec,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
        RobotEncoderMotor RightBackWheelMotor = new RobotEncoderMotor(RightBackDC,Robot5100Settings.rightBackMotorCountsPerRev,Robot5100Settings.rightBackMotorRevPerSec,Robot5100Settings.motionTimeControl,Robot5100Settings.motionTimeControlPct);
        RobotWheel FrontWheel = new RobotWheel(Robot5100Settings.frontWheelRadius,Robot5100Settings.frontWheelAngle);
        RobotWheel LeftBackWheel = new RobotWheel(Robot5100Settings.leftBackWheelRadius,Robot5100Settings.leftBackWheelAngle);
        RobotWheel RightBackWheel = new RobotWheel(Robot5100Settings.rightBackWheelRadius,Robot5100Settings.rightBackWheelAngle);
        this.m_FrontWheel = new RobotSensorWrapper<RobotEncoderMotion>(new RobotEncoderMotion(FrontWheel,FrontWheelMotor),Robot5100Settings.frontWheelPos);
        this.m_LeftBackWheel = new RobotSensorWrapper<RobotEncoderMotion>(new RobotEncoderMotion(LeftBackWheel,LeftBackWheelMotor),Robot5100Settings.leftBackWheelPos);
        this.m_RightBackWheel = new RobotSensorWrapper<RobotEncoderMotion>(new RobotEncoderMotion(RightBackWheel,RightBackWheelMotor),Robot5100Settings.rightBackWheelPos);
        this.m_PositionTracker = PositionTracker;
        this.m_CurrentDirection = RobotMotionDirection.inY;
        this.m_CurrentMotionType = motionType.stopped;
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
    public void setPositionTracker(@NonNull RobotPositionTracker newPositionTracker) {
        this.m_PositionTracker = newPositionTracker;
    }

    @Override
    public void turnToAbsFieldAngle(double AngleInDegree, double Speed) {
        double deltaAngle = AngleInDegree - this.getPositionTracker().getRobotRotation();
        this.turnOffsetAroundCenter(AngleInDegree,Speed);
    }

    @Override
    public void turnOffsetAroundCenter(double AngleInDegree, double Speed) {
        if(this.isBusy() && (this.getCurrentMotionType() != motionType.turningFixedAngle || this.getMovingDirection() != RobotMotionDirection.rotating)){
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
        this.getFrontWheel().getSensor().moveDistance(distanceFrontWheel,Speed);
        this.getLeftBackWheel().getSensor().moveDistance(distanceLeftBackWheel,Speed);
        this.getRightBackWheel().getSensor().moveDistance(distanceRightBackWheel,Speed);
    }

    @Override
    public void keepTurningOffsetAroundCenter(double Speed) {
        if(this.isBusy() && (this.getCurrentMotionType() != motionType.keepingTurningWithSpeed || this.getMovingDirection() != RobotMotionDirection.rotating)){
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
        this.getFrontWheel().getSensor().moveWithFixedSpeed(speedFrontMoving);
        this.getLeftBackWheel().getSensor().moveWithFixedSpeed(speedLeftBackMoving);
        this.getRightBackWheel().getSensor().moveWithFixedSpeed(speedRightBackMoving);
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
        if(this.isBusy() && (this.getCurrentMotionType() != motionType.movingFixedDistance || this.getMovingDirection() != RobotMotionDirection.inY)){
            this.stopMoving();
        }
        this.m_CurrentMotionType = motionType.movingFixedDistance;
        this.m_CurrentDirection = RobotMotionDirection.inY;
        double leftBackDistance = this.getLeftBackWheel().getSensor().getWheel().calculateDistanceByRobotAxisY(Distance);
        double rightBackDistance = this.getRightBackWheel().getSensor().getWheel().calculateDistanceByRobotAxisY(Distance);
        this.m_LeftBackWheel.getSensor().moveDistance(leftBackDistance,Speed);
        this.m_RightBackWheel.getSensor().moveDistance(rightBackDistance,Speed);
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
        if(this.isBusy() && (this.getCurrentMotionType() != motionType.movingFixedDistance || this.getMovingDirection() != RobotMotionDirection.inX)){
            this.stopMoving();
        }
        this.m_CurrentMotionType = motionType.movingFixedDistance;
        this.m_CurrentDirection = RobotMotionDirection.inX;
        double frontDistance = this.getFrontWheel().getSensor().getWheel().calculateDistanceByRobotAxisX(Distance);
        double frontArc = this.getFrontWheel().getSensor().getWheel().getArcLength(1);
        double leftBackArc = this.getLeftBackWheel().getSensor().getWheel().getArcLength(1);
        double rightBackArc = this.getRightBackWheel().getSensor().getWheel().getArcLength(1);
        double biggestArc = Math.max(Math.max(Math.abs(frontArc),Math.abs(leftBackArc)),Math.abs(rightBackArc));
        double leftBackDistance = Distance * (leftBackArc / biggestArc) * Robot5100Settings.backMotorFactor;
        double rightBackDistance = Distance * (rightBackArc / biggestArc) * Robot5100Settings.backMotorFactor;
        double maxDistance = Math.max(Math.max(Math.abs(frontDistance),Math.abs(leftBackDistance)),Math.abs(rightBackDistance));
        double frontSpeed = (frontDistance / Distance) * Speed;
        double leftBackSpeed = (leftBackDistance / Distance) * Speed;
        double rightBackSpeed = (rightBackDistance / Distance) * Speed;
        double FrontUnitDistance = this.getFrontWheel().getSensor().getWheel().getXPerDistance();
        double LeftBackUnitDistance = this.getLeftBackWheel().getSensor().getWheel().getXPerDistance();
        double RightBackUnitDistance = this.getRightBackWheel().getSensor().getWheel().getXPerDistance();
        if(FrontUnitDistance < 0){frontDistance = -frontDistance;}
        if(LeftBackUnitDistance < 0){leftBackDistance = -leftBackDistance;}
        if(RightBackUnitDistance < 0){rightBackDistance = -rightBackDistance;}

        this.getFrontWheel().getSensor().moveDistance(frontDistance,frontSpeed);
        this.getLeftBackWheel().getSensor().moveDistance(leftBackDistance,leftBackSpeed);
        this.getRightBackWheel().getSensor().moveDistance(rightBackDistance,rightBackSpeed);
    }

    @Override
    public void driveForwardWithSpeed(double Speed) {
        if(this.isBusy() && (this.getCurrentMotionType() != motionType.keepingMovingWithFixedSpeed || this.getMovingDirection() != RobotMotionDirection.inY)){
            this.stopMoving();
        }
        this.m_CurrentMotionType = motionType.keepingMovingWithFixedSpeed;
        this.m_CurrentDirection = RobotMotionDirection.inY;
        this.getLeftBackWheel().getSensor().getMotor().moveWithFixedSpeed(Speed);
        this.getRightBackWheel().getSensor().getMotor().moveWithFixedSpeed(-Speed);
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
        if(this.isBusy() && (this.getCurrentMotionType() != motionType.keepingMovingWithFixedSpeed || this.getMovingDirection() != RobotMotionDirection.inX)){
            this.stopMoving();
        }
        this.m_CurrentMotionType = motionType.keepingMovingWithFixedSpeed;
        this.m_CurrentDirection = RobotMotionDirection.inX;
        double frontArc = this.getFrontWheel().getSensor().getWheel().getArcLength(1);
        double leftBackArc = this.getLeftBackWheel().getSensor().getWheel().getArcLength(1);
        double rightBackArc = this.getRightBackWheel().getSensor().getWheel().getArcLength(1);
        double biggestArc = Math.max(Math.max(Math.abs(frontArc),Math.abs(leftBackArc)),Math.abs(rightBackArc));
        double frontSpeed = (frontArc / biggestArc) * Speed;
        double leftBackSpeed = (leftBackArc / biggestArc) * Speed * Robot5100Settings.backMotorFactor;
        double rightBackSpeed = (rightBackArc / biggestArc) * Speed * Robot5100Settings.backMotorFactor;

        this.getFrontWheel().getSensor().moveRobotXWithFixedSpeed(frontSpeed);
        this.getLeftBackWheel().getSensor().moveRobotXWithFixedSpeed(leftBackSpeed);
        this.getRightBackWheel().getSensor().moveRobotXWithFixedSpeed(rightBackSpeed);
    }

    @Override
    public void stopMoving() {
        if(this.m_CurrentMotionType == motionType.stopped){
            return;
        }
        double[] robotOrigin = {0,0};
        this.getFrontWheel().getSensor().getMotor().stopRunning_getMovedCounts();
        this.getLeftBackWheel().getSensor().getMotor().stopRunning_getMovedCounts();
        this.getRightBackWheel().getSensor().getMotor().stopRunning_getMovedCounts();
        this.m_CurrentMotionType = motionType.stopped;
        switch(this.m_CurrentMotionType){
            case turningFixedAngle:
            case keepingTurningWithSpeed:
                this.m_PositionTracker.moveWithRobotFixedPointAndPowerPoint(robotOrigin,this.getFrontWheel().getPos(),this.getFrontWheel().getSensor().getLastMovedDistance());
                break;
            case movingFixedDistance:
            case keepingMovingWithFixedSpeed:
                switch(this.m_CurrentDirection){
                    case inY:
                        double YMoved = this.getLeftBackWheel().getSensor().getLastMovedDistance() * this.getLeftBackWheel().getSensor().getWheel().getYPerDistance();
                        this.m_PositionTracker.moveThroughRobotAngle(0,YMoved);
                        break;
                    case inX:
                        double XMoved = this.getFrontWheel().getSensor().getLastMovedDistance() * this.getFrontWheel().getSensor().getWheel().getXPerDistance();
                        this.m_PositionTracker.moveThroughRobotAngle(90,XMoved);
                        break;
                }
                break;
        }
    }

    @Override
    public void doLoop() {
        this.getFrontWheel().getSensor().doLoop();
        this.getLeftBackWheel().getSensor().doLoop();
        this.getRightBackWheel().getSensor().doLoop();
        if(!this.isBusy() && this.m_CurrentMotionType != motionType.stopped){
            this.stopMoving();
        }
    }
}
