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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotSensorWrapper;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTasks.FixedSpeedTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotion;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotWheel;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;

public class Robot5100MotionSystem extends RobotMotionSystem {
    private RobotSensorWrapper<RobotMotion> m_LeftFrontMotion;
    private RobotSensorWrapper<RobotMotion> m_RightFrontMotion;
    private RobotSensorWrapper<RobotMotion> m_LeftBackMotion;
    private RobotSensorWrapper<RobotMotion> m_RightBackMotion;
    private RobotPositionTracker m_PosTracker;
    private RobotMotionSystem.motionType m_MotionType;
    private RobotMotionSystem.RobotMotionDirection m_MovingDirection;

    public Robot5100MotionSystem(@NonNull OpMode controllingOpMode, @NonNull RobotPositionTracker PosTracker){
        DcMotor LeftFrontDcMotor = controllingOpMode.hardwareMap.dcMotor.get(Robot5100Setting.LEFTFRONTWHEEL_CONFIGURATIONNAME);
        DcMotor RightFrontDcMotor = controllingOpMode.hardwareMap.dcMotor.get(Robot5100Setting.RIGHTFRONTWHEEL_CONFIGURATIONNAME);
        DcMotor LeftBackDcMotor = controllingOpMode.hardwareMap.dcMotor.get(Robot5100Setting.LEFTBACKWHEEL_CONFIGURATIONNAME);
        DcMotor RightBackDcMotor = controllingOpMode.hardwareMap.dcMotor.get(Robot5100Setting.RIGHTBACKWHEEL_CONFIGURATIONNAME);
        LeftFrontDcMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RightFrontDcMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LeftBackDcMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RightBackDcMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RobotWheel LeftFrontWheel = new RobotWheel(Robot5100Setting.LEFTFRONTWHEEL_RADIUS,Robot5100Setting.LEFTFRONTWHEEL_INSTALLEDANGLE);
        RobotWheel RightFrontWheel = new RobotWheel(Robot5100Setting.RIGHTFRONTWHEEL_RADIUS,Robot5100Setting.RIGHTFRONTWHEEL_INSTALLEDANGLE);
        RobotWheel LeftBackWheel = new RobotWheel(Robot5100Setting.LEFTBACKWHEEL_RADIUS,Robot5100Setting.LEFTBACKWHEEL_INSTALLEDANGLE);
        RobotWheel RightBackWheel = new RobotWheel(Robot5100Setting.RIGHTBACKWHEEL_RADIUS,Robot5100Setting.RIGHTBACKWHEEL_INSTALLEDANGLE);
        RobotMotor LeftFrontMotor = new RobotMotor(LeftFrontDcMotor,Robot5100Setting.LEFTFRONTWHEEL_COUNTSPERREV,Robot5100Setting.LEFTFRONTWHEEL_REVPERSEC,Robot5100Setting.MOTIONSYSTEM_TIMECONTROL,Robot5100Setting.MOTIONSYSTEM_TIMECONTROLPCT);
        RobotMotor RightFrontMotor = new RobotMotor(RightFrontDcMotor,Robot5100Setting.RIGHTFRONTWHEEL_COUNTSPERREV,Robot5100Setting.RIGHTFRONTWHEEL_REVPERSEC,Robot5100Setting.MOTIONSYSTEM_TIMECONTROL,Robot5100Setting.MOTIONSYSTEM_TIMECONTROLPCT);
        RobotMotor LeftBackMotor = new RobotMotor(LeftBackDcMotor,Robot5100Setting.LEFTBACKWHEEL_COUNTSPERREV,Robot5100Setting.LEFTBACKWHEEL_REVPERSEC,Robot5100Setting.MOTIONSYSTEM_TIMECONTROL,Robot5100Setting.MOTIONSYSTEM_TIMECONTROLPCT);
        RobotMotor RightBackMotor = new RobotMotor(RightBackDcMotor,Robot5100Setting.RIGHTBACKWHEEL_COUNTSPERREV,Robot5100Setting.RIGHTBACKWHEEL_REVPERSEC,Robot5100Setting.MOTIONSYSTEM_TIMECONTROL,Robot5100Setting.MOTIONSYSTEM_TIMECONTROLPCT);
        this.m_LeftFrontMotion = new RobotSensorWrapper<RobotMotion>(new RobotMotion(LeftFrontWheel,LeftFrontMotor),Robot5100Setting.LEFTFRONTWHEEL_POSITION);
        this.m_RightFrontMotion = new RobotSensorWrapper<RobotMotion>(new RobotMotion(RightFrontWheel,RightFrontMotor),Robot5100Setting.RIGHTFRONTWHEEL_POSITION);
        this.m_LeftBackMotion = new RobotSensorWrapper<RobotMotion>(new RobotMotion(LeftBackWheel,LeftBackMotor),Robot5100Setting.LEFTBACKWHEEL_POSITION);
        this.m_RightBackMotion = new RobotSensorWrapper<RobotMotion>(new RobotMotion(RightBackWheel,RightBackMotor),Robot5100Setting.RIGHTBACKWHEEL_POSITION);

        this.m_PosTracker = PosTracker;

        this.m_MotionType = motionType.stopped;
        this.m_MovingDirection = RobotMotionDirection.inX;
    }
    public RobotSensorWrapper<RobotMotion> getLeftFrontMotion(){
        return this.m_LeftFrontMotion;
    }
    public RobotSensorWrapper<RobotMotion> getRightFrontMotion(){
        return this.m_RightFrontMotion;
    }
    public RobotSensorWrapper<RobotMotion> getLeftBackMotion(){
        return this.m_LeftBackMotion;
    }
    public RobotSensorWrapper<RobotMotion> getRightBackMotion(){
        return this.m_RightBackMotion;
    }
    @Override
    public boolean isBusy() {
        return this.getLeftFrontMotion().getSensor().getMotor().isBusy() || this.getRightFrontMotion().getSensor().getMotor().isBusy() || this.getLeftBackMotion().getSensor().getMotor().isBusy() || this.getRightBackMotion().getSensor().getMotor().isBusy();
    }

    @Override
    public void waitUntilFinish() {
        while(this.isBusy()){
            this.doLoop();
        }
    }

    @Override
    public void doLoop() {
        this.getLeftFrontMotion().getSensor().getMotor().doLoop();
        this.getRightFrontMotion().getSensor().getMotor().doLoop();
        this.getLeftBackMotion().getSensor().getMotor().doLoop();
        this.getRightBackMotion().getSensor().getMotor().doLoop();
        this.checkIfMotionFinish();
    }

    protected void checkIfMotionFinish(){
        if(!this.isBusy() && this.getCurrentMotionType() != motionType.stopped){
            this.m_MotionType = motionType.stopped;
        }
    }

    protected void checkAndStopCurrentMotion(){
        if(this.m_MotionType != motionType.stopped) {
            this.stopMoving();
        }
    }

    @Override
    public RobotPositionTracker getPositionTracker() {
        return this.m_PosTracker;
    }

    @Override
    public void setPositionTracker(RobotPositionTracker newPositionTracker) {
        this.m_PosTracker = newPositionTracker;
    }

    @Override
    public void turnToAbsFieldAngle(double AngleInDegree, double Speed) {
        double deltaAngle = AngleInDegree - this.getPositionTracker().getRobotRotation();
        this.turnOffsetAroundCenter(AngleInDegree,Speed);
    }

    private class TurningPosTrackerCB implements RobotMotion.RobotMotionFinishCallBack{
        @Override
        public void finished(RobotMotion Motion, boolean timeOut, double timeUsed, double DistanceMoved) {
            double[] rotatePoint = {0,0};
            Robot5100MotionSystem.this.getPositionTracker().rotateWithRobotFixedPointAndPowerPoint(rotatePoint,Robot5100MotionSystem.this.getLeftFrontMotion().getPos(),DistanceMoved);
        }
    }

    @Override
    public void turnOffsetAroundCenter(double AngleInDegree, double Speed) {
        checkAndStopCurrentMotion();
        this.m_MotionType = motionType.turningFixedAngle;
        this.m_MovingDirection = RobotMotionDirection.rotating;
        RobotMotion.RobotMotionFinishCallBack MotionFinishCB = new TurningPosTrackerCB();
        double[] rotatePoint = {0,0};
        double LeftFrontDistance = this.getPositionTracker().calculateDistanceToRotateAroundRobotPoint(rotatePoint,this.getLeftFrontMotion().getPos(),AngleInDegree);
        this.getLeftFrontMotion().getSensor().getMotor().addTask(this.getLeftFrontMotion().getSensor().new FixedDistanceMotionTask(LeftFrontDistance,Speed,MotionFinishCB));
        //double RightFrontDistance = this.getPositionTracker().calculateDistanceToRotateAroundRobotPoint(rotatePoint,this.getRightFrontMotion().getPos(),AngleInDegree);
        double RightFrontDistance = LeftFrontDistance;
        this.getRightFrontMotion().getSensor().getMotor().addTask(this.getRightFrontMotion().getSensor().new FixedDistanceMotionTask(RightFrontDistance,Speed,null));
        //double LeftBackDistance = this.getPositionTracker().calculateDistanceToRotateAroundRobotPoint(rotatePoint,this.getLeftBackMotion().getPos(),AngleInDegree);
        double LeftBackDistance = LeftFrontDistance;
        this.getLeftBackMotion().getSensor().getMotor().addTask(this.getLeftBackMotion().getSensor().new FixedDistanceMotionTask(LeftBackDistance,Speed,null));
        //double RightBackDistance = this.getPositionTracker().calculateDistanceToRotateAroundRobotPoint(rotatePoint,this.getRightBackMotion().getPos(),AngleInDegree);
        double RightBackDistance = LeftFrontDistance;
        this.getRightBackMotion().getSensor().getMotor().addTask(this.getRightBackMotion().getSensor().new FixedDistanceMotionTask(RightBackDistance,Speed,null));
    }


    @Override
    public void keepTurningOffsetAroundCenter(double Speed) {
        if(this.m_MotionType != motionType.keepingTurningWithSpeed){
            checkAndStopCurrentMotion();
            this.m_MotionType = motionType.keepingTurningWithSpeed;
            this.m_MovingDirection = RobotMotionDirection.rotating;
            RobotMotion.RobotMotionFinishCallBack MotionFinishCB = new TurningPosTrackerCB();
            this.getLeftFrontMotion().getSensor().getMotor().addTask(this.getLeftFrontMotion().getSensor().new FixedSpeedMotionTask(Speed,MotionFinishCB));
            this.getRightFrontMotion().getSensor().getMotor().addTask(this.getRightFrontMotion().getSensor().new FixedSpeedMotionTask(Speed,null));
            this.getLeftBackMotion().getSensor().getMotor().addTask(this.getLeftBackMotion().getSensor().new FixedSpeedMotionTask(Speed,null));
            this.getRightBackMotion().getSensor().getMotor().addTask(this.getRightBackMotion().getSensor().new FixedSpeedMotionTask(Speed,null));
        }else{
            RobotMotion.FixedSpeedMotionTask LeftFrontMotion = (RobotMotion.FixedSpeedMotionTask) this.getLeftFrontMotion().getSensor().getMotor().getCurrentTask();
            RobotMotion.FixedSpeedMotionTask RightFrontMotion = (RobotMotion.FixedSpeedMotionTask) this.getRightFrontMotion().getSensor().getMotor().getCurrentTask();
            RobotMotion.FixedSpeedMotionTask LeftBackMotion = (RobotMotion.FixedSpeedMotionTask) this.getLeftBackMotion().getSensor().getMotor().getCurrentTask();
            RobotMotion.FixedSpeedMotionTask RightBackMotion = (RobotMotion.FixedSpeedMotionTask) this.getRightBackMotion().getSensor().getMotor().getCurrentTask();
            LeftFrontMotion.setRunningSpeed(Speed);
            RightFrontMotion.setRunningSpeed(Speed);
            LeftBackMotion.setRunningSpeed(Speed);
            RightBackMotion.setRunningSpeed(Speed);
        }
    }

    @Override
    public void driveTo(double[] fieldPos, double Speed) {
        double angleToTurn = this.getPositionTracker().calculateAngleDeltaMovingToFieldPoint(fieldPos);
        this.turnOffsetAroundCenter(angleToTurn,Speed);
        this.waitUntilFinish();
        double distanceToGo = this.getPositionTracker().calculateDistanceToFixedPoint(fieldPos);
        this.driveForward(distanceToGo,Speed);
    }

    private class MoveXPosTrackerCB implements RobotMotion.RobotMotionFinishCallBack{
        @Override
        public void finished(RobotMotion Motion, boolean timeOut, double timeUsed, double DistanceMoved) {
            double XMoved = Motion.getWheel().getXPerDistance() * DistanceMoved;
            Robot5100MotionSystem.this.getPositionTracker().moveThroughRobotAngle(90,XMoved);
        }
    }
    private class MoveYPosTrackerCB implements RobotMotion.RobotMotionFinishCallBack{
        @Override
        public void finished(RobotMotion Motion, boolean timeOut, double timeUsed, double DistanceMoved) {
            double XMoved = Motion.getWheel().getYPerDistance() * DistanceMoved;
            Robot5100MotionSystem.this.getPositionTracker().moveThroughRobotAngle(0,XMoved);
        }
    }

    @Override
    public void driveForward(double Distance, double Speed) {
        checkAndStopCurrentMotion();
        this.m_MotionType = motionType.movingFixedDistance;
        this.m_MovingDirection = RobotMotionDirection.inY;
        double LeftFrontDistance = this.getLeftFrontMotion().getSensor().getWheel().calculateDistanceByRobotAxisY(Distance);
        RobotMotion.RobotMotionFinishCallBack MotionFinishCB = new MoveYPosTrackerCB();
        this.getLeftFrontMotion().getSensor().getMotor().addTask(this.getLeftFrontMotion().getSensor().new FixedDistanceMotionTask(LeftFrontDistance,Speed,MotionFinishCB));
        //double RightFrontDistance = this.getRightFrontMotion().getSensor().getWheel().calculateDistanceByRobotAxisY(Distance);
        double RightFrontDistance = -LeftFrontDistance;
        this.getRightFrontMotion().getSensor().getMotor().addTask(this.getRightFrontMotion().getSensor().new FixedDistanceMotionTask(RightFrontDistance,Speed,null));
        //double LeftBackDistance = this.getLeftBackMotion.getSensor.getWheel.calculateDistanceByRobotAxisY(Distance);
        double LeftBackDistance = LeftFrontDistance;
        this.getLeftBackMotion().getSensor().getMotor().addTask(this.getLeftBackMotion().getSensor().new FixedDistanceMotionTask(LeftBackDistance,Speed,null));
        //double RightBackDistance = this.getRightBackMotion.getSensor.getWheel.calculateDistanceByRobotAxisY(Distance);
        double RightBackDistance = -LeftFrontDistance;
        this.getRightBackMotion().getSensor().getMotor().addTask(this.getRightBackMotion().getSensor().new FixedDistanceMotionTask(RightBackDistance,Speed,null));
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
        checkAndStopCurrentMotion();
        this.m_MotionType = motionType.movingFixedDistance;
        this.m_MovingDirection = RobotMotionDirection.inX;
        double LeftFrontDistance = this.getLeftFrontMotion().getSensor().getWheel().calculateDistanceByRobotAxisX(Distance);
        RobotMotion.RobotMotionFinishCallBack MotionFinishCB = new MoveXPosTrackerCB();
        this.getLeftFrontMotion().getSensor().getMotor().addTask(this.getLeftFrontMotion().getSensor().new FixedDistanceMotionTask(LeftFrontDistance,Speed,MotionFinishCB));
        //double RightFrontDistance = this.getRightFrontMotion().getSensor().getWheel().calculateDistanceByRobotAxisX(Distance);
        double RightFrontDistance = LeftFrontDistance;
        this.getRightFrontMotion().getSensor().getMotor().addTask(this.getRightFrontMotion().getSensor().new FixedDistanceMotionTask(RightFrontDistance,Speed,null));
        //double LeftBackDistance = this.getLeftBackMotion.getSensor.getWheel.calculateDistanceByRobotAxisX(Distance);
        double LeftBackDistance = -LeftFrontDistance;
        this.getLeftBackMotion().getSensor().getMotor().addTask(this.getLeftBackMotion().getSensor().new FixedDistanceMotionTask(LeftBackDistance,Speed,null));
        //double RightBackDistance = this.getRightBackMotion.getSensor.getWheel.calculateDistanceByRobotAxisX(Distance);
        double RightBackDistance = -LeftFrontDistance;
        this.getRightBackMotion().getSensor().getMotor().addTask(this.getRightBackMotion().getSensor().new FixedDistanceMotionTask(RightBackDistance,Speed,null));
    }

    @Override
    public void driveForwardWithSpeed(double Speed) {
        if(this.m_MotionType != motionType.keepingMovingWithFixedSpeed || this.m_MovingDirection != RobotMotionDirection.inY){
            checkAndStopCurrentMotion();
            this.m_MotionType = motionType.keepingMovingWithFixedSpeed;
            this.m_MovingDirection = RobotMotionDirection.inY;
            RobotMotion.RobotMotionFinishCallBack MotionFinishCB = new MoveYPosTrackerCB();
            this.getLeftFrontMotion().getSensor().getMotor().addTask(this.getLeftBackMotion().getSensor().new FixedSpeedMotionTask(Speed,MotionFinishCB));
            this.getRightFrontMotion().getSensor().getMotor().addTask(this.getRightFrontMotion().getSensor().new FixedSpeedMotionTask(-Speed,null));
            this.getLeftBackMotion().getSensor().getMotor().addTask(this.getLeftBackMotion().getSensor().new FixedSpeedMotionTask(Speed,null));
            this.getRightBackMotion().getSensor().getMotor().addTask(this.getRightBackMotion().getSensor().new FixedSpeedMotionTask(-Speed,null));
        }else{
            RobotMotion.FixedSpeedMotionTask LeftFrontMotion = (RobotMotion.FixedSpeedMotionTask) this.getLeftFrontMotion().getSensor().getMotor().getCurrentTask();
            RobotMotion.FixedSpeedMotionTask RightFrontMotion = (RobotMotion.FixedSpeedMotionTask) this.getRightFrontMotion().getSensor().getMotor().getCurrentTask();
            RobotMotion.FixedSpeedMotionTask LeftBackMotion = (RobotMotion.FixedSpeedMotionTask) this.getLeftBackMotion().getSensor().getMotor().getCurrentTask();
            RobotMotion.FixedSpeedMotionTask RightBackMotion = (RobotMotion.FixedSpeedMotionTask) this.getRightBackMotion().getSensor().getMotor().getCurrentTask();
            LeftFrontMotion.setRunningSpeed(Speed);
            RightFrontMotion.setRunningSpeed(-Speed);
            LeftBackMotion.setRunningSpeed(Speed);
            RightBackMotion.setRunningSpeed(-Speed);
        }
    }

    @Override
    public RobotMotionSystem.motionType getCurrentMotionType() {
        return this.m_MotionType;
    }

    @Override
    public RobotMotionSystem.RobotMotionDirection getMovingDirection() {
        return this.m_MovingDirection;
    }

    @Override
    public void driveBackwardWithSpeed(double Speed) {
        this.driveForwardWithSpeed(-Speed);
    }

    @Override
    public void driveToLeftWithSpeed(double Speed) {
        this.driveToRightWithSpeed(-Speed);
    }

    @Override
    public void driveToRightWithSpeed(double Speed) {
        if(this.m_MotionType != motionType.keepingMovingWithFixedSpeed || this.m_MovingDirection != RobotMotionDirection.inX){
            checkAndStopCurrentMotion();
            this.m_MotionType = motionType.keepingMovingWithFixedSpeed;
            this.m_MovingDirection = RobotMotionDirection.inX;
            RobotMotion.RobotMotionFinishCallBack MotionFinishCB = new MoveXPosTrackerCB();
            this.getLeftFrontMotion().getSensor().getMotor().addTask(this.getLeftBackMotion().getSensor().new FixedSpeedMotionTask(Speed,MotionFinishCB));
            this.getRightFrontMotion().getSensor().getMotor().addTask(this.getRightFrontMotion().getSensor().new FixedSpeedMotionTask(Speed,null));
            this.getLeftBackMotion().getSensor().getMotor().addTask(this.getLeftBackMotion().getSensor().new FixedSpeedMotionTask(-Speed,null));
            this.getRightBackMotion().getSensor().getMotor().addTask(this.getRightBackMotion().getSensor().new FixedSpeedMotionTask(-Speed,null));
        }else{
            RobotMotion.FixedSpeedMotionTask LeftFrontMotion = (RobotMotion.FixedSpeedMotionTask) this.getLeftFrontMotion().getSensor().getMotor().getCurrentTask();
            RobotMotion.FixedSpeedMotionTask RightFrontMotion = (RobotMotion.FixedSpeedMotionTask) this.getRightFrontMotion().getSensor().getMotor().getCurrentTask();
            RobotMotion.FixedSpeedMotionTask LeftBackMotion = (RobotMotion.FixedSpeedMotionTask) this.getLeftBackMotion().getSensor().getMotor().getCurrentTask();
            RobotMotion.FixedSpeedMotionTask RightBackMotion = (RobotMotion.FixedSpeedMotionTask) this.getRightBackMotion().getSensor().getMotor().getCurrentTask();
            LeftFrontMotion.setRunningSpeed(Speed);
            RightFrontMotion.setRunningSpeed(Speed);
            LeftBackMotion.setRunningSpeed(-Speed);
            RightBackMotion.setRunningSpeed(-Speed);
        }
    }

    @Override
    public void stopMoving() {
        this.m_LeftFrontMotion.getSensor().getMotor().deleteAllTasks();
        this.m_RightFrontMotion.getSensor().getMotor().deleteAllTasks();
        this.m_LeftBackMotion.getSensor().getMotor().deleteAllTasks();
        this.m_RightBackMotion.getSensor().getMotor().deleteAllTasks();
        this.m_MotionType = motionType.stopped;
    }
}
