package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;

public class Robot5100MotionSystem implements RobotMotionSystem {
    @Override
    public RobotPositionTracker getPositionTracker() {
        return null;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void waitUntilMotionFinish() {

    }

    @Override
    public void setPositionTracker(RobotPositionTracker newPositionTracker) {

    }

    @Override
    public void turnToAbsFieldAngle(double AngleInDegree, double Speed) {

    }

    @Override
    public void turnOffsetAroundCenter(double AngleInDegree, double Speed) {

    }

    @Override
    public void keepTurningOffsetAroundCenter(double Speed) {

    }

    @Override
    public void stopTurningOffsetAroundCenter() {

    }

    @Override
    public void driveTo(double[] fieldPos, double Speed) {

    }

    @Override
    public void driveForward(double Distance, double Speed) {

    }

    @Override
    public void driveBackward(double Distance, double Speed) {

    }

    @Override
    public void driveToLeft(double Distance, double Speed) {

    }

    @Override
    public void driveToRight(double Distance, double Speed) {

    }

    @Override
    public void driveForwardWithSpeed(double Speed) {

    }

    @Override
    public boolean isDrivingInDirectionWithSpeed() {
        return false;
    }

    @Override
    public boolean isKeepingTurningOffsetAroundCenter() {
        return false;
    }

    @Override
    public void driveBackwardWithSpeed(double Speed) {

    }

    @Override
    public void driveToLeftWithSpeed(double Speed) {

    }

    @Override
    public void driveToRightWithSpeed(double Speed) {

    }

    @Override
    public void stopDrivingWithSpeed() {

    }
}
