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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision1A;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotMotionSystem;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotNonBlockingWheel;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotSensorWrapper;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotWheel;

import java.lang.reflect.Field;
public class Robot5100MotionSystem implements RobotMotionSystem, RobotEventLoopable {
    private RobotPositionTracker m_PositionTracker;
    private RobotSensorWrapper<RobotNonBlockingWheel> m_Motor0, m_Motor1, m_Motor2, m_Motor3;
    private boolean m_isDriving = false;
    private double[] m_MotorXFactor = {0,0,0,0};
    private double[] m_MotorYFactor = {0,0,0,0};
    private boolean m_isTurningAround = false;

    @Override
    public boolean isBusy(){
        return (this.m_Motor0.getSensor().isBusy() && this.m_Motor1.getSensor().isBusy() && this.m_Motor2.getSensor().isBusy() && this.m_Motor3.getSensor().isBusy());
    }

    @Override
    public void waitUntilMotionFinish(){
        while(this.isBusy()){
            this.doLoop();
        }
    }

    public Robot5100MotionSystem(RobotPositionTracker PositionTracker, DcMotor Motor0, DcMotor Motor1, DcMotor Motor2, DcMotor Motor3){
        this.m_PositionTracker = PositionTracker;
        //Robot CountsPerRev: 560
        //http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
        //Motor Speed 300RPM
        //Diagonal Motor Distance Apart: 46.228 cm
        //Motor Position: {16.34, 16.34}
        //RobotWheel Timeout Control: false
        RobotWheel m_Wheel0 = new RobotWheel(5.0,-135);
        RobotWheel m_Wheel1 = new RobotWheel(5.0,135);
        RobotWheel m_Wheel2 = new RobotWheel(5.0,-45);
        RobotWheel m_Wheel3 = new RobotWheel(5.0, 45);
        RobotNonBlockingMotor NBMotor0 = new RobotNonBlockingMotor(Motor0,560,5.0*560,false);
        RobotNonBlockingMotor NBMotor1 = new RobotNonBlockingMotor(Motor1,560,5.0*560,false);
        RobotNonBlockingMotor NBMotor2 = new RobotNonBlockingMotor(Motor2,560,5.0*560,false);
        RobotNonBlockingMotor NBMotor3 = new RobotNonBlockingMotor(Motor3,560,5.0*560,false);
        double[] Motor0Pos = {-16.34, -16.34};
        double[] Motor1Pos = {16.34, -16.34};
        double[] Motor2Pos = {-16.34,16.34};
        double[] Motor3Pos = {16.34,16.34};
        this.m_Motor0 = new RobotSensorWrapper<RobotNonBlockingWheel>(new RobotNonBlockingWheel(m_Wheel0,NBMotor0),Motor0Pos);
        this.m_Motor1 = new RobotSensorWrapper<RobotNonBlockingWheel>(new RobotNonBlockingWheel(m_Wheel1,NBMotor1),Motor1Pos);
        this.m_Motor2 = new RobotSensorWrapper<RobotNonBlockingWheel>(new RobotNonBlockingWheel(m_Wheel2,NBMotor2),Motor2Pos);
        this.m_Motor3 = new RobotSensorWrapper<RobotNonBlockingWheel>(new RobotNonBlockingWheel(m_Wheel3,NBMotor3),Motor3Pos);

    }

    public RobotSensorWrapper<RobotNonBlockingWheel> getMotor0() {
        return this.m_Motor0;
    }

    public void setMotor0(RobotSensorWrapper<RobotNonBlockingWheel> newMotor){
        this.m_Motor0 = newMotor;
    }

    public RobotSensorWrapper<RobotNonBlockingWheel> getMotor1() {
        return this.m_Motor1;
    }

    public void setMotor1(RobotSensorWrapper<RobotNonBlockingWheel> Motor){
        this.m_Motor1 = Motor;
    }

    public RobotSensorWrapper<RobotNonBlockingWheel> getMotor2() {
        return this.m_Motor2;
    }

    public void setMotor2(RobotSensorWrapper<RobotNonBlockingWheel> Motor){
        this.m_Motor2 = Motor;
    }

    public RobotSensorWrapper<RobotNonBlockingWheel> getMotor3() {
        return this.m_Motor3;
    }

    public void setMotor3(RobotSensorWrapper<RobotNonBlockingWheel> Motor){
        this.m_Motor3 = Motor;
    }

    @Override
    public RobotPositionTracker getPositionTracker(){
        return this.m_PositionTracker;
    }

    @Override
    public void setPositionTracker(RobotPositionTracker newPositionTracker){
        this.m_PositionTracker = newPositionTracker;
    }

    @Override
    public double[] getCurrentFieldPos(){
        return this.m_PositionTracker.getCurrentPos();
    }

    @Override
    public double[] getRobotAxisFromFieldAxis(double[] FieldPosition){
        return this.m_PositionTracker.robotAxisFromFieldAxis(FieldPosition);
    }

    @Override
    public double[] getFieldAxisFromRobotAxis(double[] RobotPosition){
        return this.m_PositionTracker.fieldAxisFromRobotAxis(RobotPosition);
    }

    @Override
    public void setCurrentFieldPos(double[] Position){
        this.m_PositionTracker.setCurrentPos(Position);
    }

    @Override
    public void turnToAbsFieldAngle(double AngleInDegree){
        double mDeltaAngle = AngleInDegree - this.m_PositionTracker.getRobotRotation();
        this.turnOffsetAroundCenter(mDeltaAngle);
    }

    @Override
    public void turnOffsetAroundCenter(double AngleInDegree){
        double[] robotPoint = {0,0};
        double turningDistance0 = this.m_PositionTracker.calculateDistanceToRotateAroundRobotPoint(robotPoint,this.m_Motor0.getPos(),AngleInDegree);
        double turningDistance1 = this.m_PositionTracker.calculateDistanceToRotateAroundRobotPoint(robotPoint,this.m_Motor1.getPos(),AngleInDegree);
        double turningDistance2 = this.m_PositionTracker.calculateDistanceToRotateAroundRobotPoint(robotPoint,this.m_Motor2.getPos(),AngleInDegree);
        double turningDistance3 = this.m_PositionTracker.calculateDistanceToRotateAroundRobotPoint(robotPoint,this.m_Motor3.getPos(),AngleInDegree);
        this.m_Motor0.getSensor().moveDistance(turningDistance0,1.0);
        this.m_Motor1.getSensor().moveDistance(turningDistance1,1.0);
        this.m_Motor2.getSensor().moveDistance(turningDistance2,1.0);
        this.m_Motor3.getSensor().moveDistance(turningDistance3,1.0);
        this.m_PositionTracker.rotateAroundRobotRelativePoint(robotPoint,AngleInDegree);
    }

    @Override
    public void driveTo(double[] fieldPos){
        double turningDeg = this.m_PositionTracker.calculateAngleDeltaMovingToFieldPoint(fieldPos);
        this.turnOffsetAroundCenter(turningDeg);
        double DistanceMoving = this.m_PositionTracker.calculateDistanceToFixedPoint(fieldPos);
        this.driveForward(DistanceMoving);
    }

    @Override
    public void driveForward(double Distance) throws RuntimeException{
        if(Math.abs(this.m_Motor0.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor1.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor2.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor3.getSensor().getRemainingDistance()) > Math.abs(Distance)){
            //return;
        }
        this.m_Motor0.getSensor().moveRobotY(Distance,1.0);
        this.m_Motor1.getSensor().moveRobotY(Distance, 1.0);
        this.m_Motor2.getSensor().moveRobotY(Distance,1.0);
        this.m_Motor3.getSensor().moveRobotY(Distance,1.0);
        this.m_PositionTracker.moveThroughRobotAngle(0,Distance);
    }

    @Override
    public void driveBackward(double Distance) throws RuntimeException{
        this.driveForward(-Distance);
    }

    @Override
    public void driveToLeft(double Distance) throws RuntimeException{
        this.driveToRight(Distance);
    }

    @Override
    public void driveToRight(double Distance) throws RuntimeException{
        if(Math.abs(this.m_Motor0.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor1.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor2.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor3.getSensor().getRemainingDistance()) > Math.abs(Distance)){
            //return;
        }
        this.m_Motor0.getSensor().moveRobotX(Distance,1.0);
        this.m_Motor1.getSensor().moveRobotX(Distance,1.0);
        this.m_Motor2.getSensor().moveRobotX(Distance,1.0);
        this.m_Motor3.getSensor().moveRobotX(Distance,1.0);
        this.m_PositionTracker.moveThroughRobotAngle(90,Distance);
    }

    @Override
    public void doLoop(){
        this.m_Motor0.getSensor().doLoop();
        this.m_Motor1.getSensor().doLoop();
        this.m_Motor2.getSensor().doLoop();
        this.m_Motor3.getSensor().doLoop();
    }

    @Override
    public void driveForwardWithSpeed(double Speed){
        this.m_MotorXFactor[0] = 0;
        this.m_MotorXFactor[1] = 0;
        this.m_MotorXFactor[2] = 0;
        this.m_MotorXFactor[3] = 0;
        this.m_MotorYFactor[0] = this.m_Motor0.getSensor().moveRobotYWithFixedSpeed(Speed);
        this.m_MotorYFactor[1] = this.m_Motor1.getSensor().moveRobotYWithFixedSpeed(Speed);
        this.m_MotorYFactor[2] = this.m_Motor2.getSensor().moveRobotYWithFixedSpeed(Speed);
        this.m_MotorYFactor[3] = this.m_Motor3.getSensor().moveRobotYWithFixedSpeed(Speed);
    }
    @Override
    public void driveBackwardWithSpeed(double Speed){
        this.driveForwardWithSpeed(-Speed);
    }
    @Override
    public void driveToLeftWithSpeed(double Speed){
        this.driveToRightWithSpeed(Speed);
    }
    @Override
    public void driveToRightWithSpeed(double Speed){
        this.m_MotorYFactor[0] = 0;
        this.m_MotorYFactor[1] = 0;
        this.m_MotorYFactor[2] = 0;
        this.m_MotorYFactor[3] = 0;
        this.m_MotorXFactor[0] = this.m_Motor0.getSensor().moveRobotXWithFixedSpeed(Speed);
        this.m_MotorXFactor[1] = this.m_Motor1.getSensor().moveRobotXWithFixedSpeed(Speed);
        this.m_MotorXFactor[2] = this.m_Motor2.getSensor().moveRobotXWithFixedSpeed(Speed);
        this.m_MotorXFactor[3] = this.m_Motor3.getSensor().moveRobotXWithFixedSpeed(Speed);
    }
    @Override
    public void stopDrivingWithSpeed(){
        double movedDistanceX = 0, movedDistanceY = 0;
        movedDistanceX = Math.min(Math.min(Math.min(this.m_MotorXFactor[0]*this.m_Motor0.getSensor().stopRunning_getMovedDistance(),this.m_MotorXFactor[1]*this.m_Motor1.getSensor().stopRunning_getMovedDistance()),this.m_MotorXFactor[2]*this.m_Motor2.getSensor().stopRunning_getMovedDistance()),this.m_MotorXFactor[3]*this.m_Motor3.getSensor().stopRunning_getMovedDistance());
        movedDistanceY = Math.min(Math.min(Math.min(this.m_MotorYFactor[0]*this.m_Motor0.getSensor().stopRunning_getMovedDistance(),this.m_MotorYFactor[1]*this.m_Motor1.getSensor().stopRunning_getMovedDistance()),this.m_MotorYFactor[2]*this.m_Motor2.getSensor().stopRunning_getMovedDistance()),this.m_MotorYFactor[3]*this.m_Motor3.getSensor().stopRunning_getMovedDistance());
        this.m_PositionTracker.moveThroughRobotAngle(90,movedDistanceX);
        this.m_PositionTracker.moveThroughRobotAngle(0,movedDistanceY);
        this.m_MotorXFactor[0] = 0;
        this.m_MotorXFactor[1] = 0;
        this.m_MotorXFactor[2] = 0;
        this.m_MotorXFactor[3] = 0;
        this.m_MotorYFactor[0] = 0;
        this.m_MotorYFactor[1] = 0;
        this.m_MotorYFactor[2] = 0;
        this.m_MotorYFactor[3] = 0;
    }
    @Override
    public void keepTurningOffsetAroundCenter(double Speed){
        this.m_Motor0.getSensor().moveWithFixedSpeed(Speed);
        this.m_Motor1.getSensor().moveWithFixedSpeed(Speed);
        this.m_Motor2.getSensor().moveWithFixedSpeed(Speed);
        this.m_Motor3.getSensor().moveWithFixedSpeed(Speed);
        this.m_isTurningAround = true;
    }
    @Override
    public void stopTurningOffsetAroundCenter(){
        if(!m_isTurningAround){
            return;
        }
        double movedDistance = Math.min(Math.min(Math.min(this.m_Motor0.getSensor().stopRunning_getMovedDistance(),this.m_Motor1.getSensor().stopRunning_getMovedDistance()),this.m_Motor2.getSensor().stopRunning_getMovedDistance()),this.m_Motor3.getSensor().stopRunning_getMovedDistance());
        double[] mPowerPoint = this.m_Motor0.getPos(), mFixedPoint = {0,0};
        this.m_PositionTracker.moveWithRobotFixedPointAndPowerPoint(RobotPositionTracker.RotationType.Clockwise,mFixedPoint,mPowerPoint,movedDistance);
        this.m_isTurningAround = false;
    }
    @Override
    public boolean isDrivingInDirectionWithSpeed(){
        if(m_MotorXFactor[0] == 0 && m_MotorXFactor[1] == 0 && m_MotorXFactor[2] == 0 && m_MotorXFactor[3] == 0 && m_MotorYFactor[0] == 0 && m_MotorYFactor[1] == 0 && m_MotorYFactor[2] == 0 && m_MotorYFactor[3] == 0){
            return false;
        }else{
            return true;
        }
    }
    @Override
    public boolean isKeepingTurningOffsetAroundCenter(){
        return this.m_isTurningAround;
    }
}
