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

package org.firstinspires.ftc.teamcode.RobotControllers.littleCar2;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.sun.tools.javac.util.Position;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotMotionSystem;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotNonBlockingWheel;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotSensorWrapper;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotWheel;

import java.lang.reflect.Field;

public class littleCarMotionSystem implements RobotMotionSystem, RobotEventLoopable {
    private RobotPositionTracker m_PositionTracker;
    private RobotSensorWrapper<RobotNonBlockingWheel> m_LeftMotor, m_RightMotor;

    @Override
    public boolean isBusy(){
        return this.m_LeftMotor.getSensor().isBusy() && this.m_RightMotor.getSensor().isBusy();
    }

    @Override
    public void waitUntilMotionFinish(){
        while(this.isBusy()){
            this.doLoop();
        }
    }

    public littleCarMotionSystem(RobotPositionTracker PositionTracker, DcMotor LeftMotor, DcMotor RightMotor){
        this.m_PositionTracker = PositionTracker;
        //RobotWheel Radius: 5 cm
        //RobotWheelOffset: (+-16.58,0)
        //Robot CountsPerRev: 865
        //RobotWheel Timeout Control: false
        RobotWheel LeftWheel = new RobotWheel(5,0);
        RobotWheel RightWheel = new RobotWheel(5 ,0);
        boolean RobotWheelTimeControl = false;
        RobotNonBlockingMotor mLeftMotor = new RobotNonBlockingMotor(LeftMotor,865,200, RobotWheelTimeControl);
        RobotNonBlockingMotor mRightMotor = new RobotNonBlockingMotor(RightMotor,865,200, RobotWheelTimeControl);
        double[] mLeftMotorPos = {-16.6, 0};
        double[] mRightMotorPos = {16.6, 0};
        this.m_LeftMotor = new RobotSensorWrapper<RobotNonBlockingWheel>(new RobotNonBlockingWheel(LeftWheel,mLeftMotor),mLeftMotorPos);
        this.m_RightMotor = new RobotSensorWrapper<RobotNonBlockingWheel>(new RobotNonBlockingWheel(RightWheel,mRightMotor),mRightMotorPos);
    }

    public RobotSensorWrapper<RobotNonBlockingWheel> getLeftMotor() {
        return this.m_LeftMotor;
    }

    public void setLeftMotor(RobotSensorWrapper<RobotNonBlockingWheel> leftMotor){
        this.m_LeftMotor = leftMotor;
    }

    public RobotSensorWrapper<RobotNonBlockingWheel> getRightMotor() {
        return this.m_RightMotor;
    }

    public void setRighttMotor(RobotSensorWrapper<RobotNonBlockingWheel> rightMotor){
        this.m_RightMotor = rightMotor;
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
        double turningDistance = this.m_PositionTracker.calculateDistanceToRotateAroundRobotPoint(robotPoint,this.m_RightMotor.getPos(),AngleInDegree);
        this.m_RightMotor.getSensor().moveDistance(-turningDistance,1.0);
        this.m_LeftMotor.getSensor().moveDistance(turningDistance, 1.0);
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
    public void driveForward(double Distance){
        if((this.m_LeftMotor.getSensor().getRemainingDistance() < Distance) && (this.m_RightMotor.getSensor().getRemainingDistance() < Distance)) {
            this.m_LeftMotor.getSensor().moveDistance(Distance, 1.0);
            this.m_RightMotor.getSensor().moveDistance(-Distance, 1.0);
            this.m_PositionTracker.moveThroughRobotAngle(0, Distance);
        }
    }

    @Override
    public void driveBackward(double Distance){
        this.driveForward(-Distance);
    }

    @Override
    public void driveToLeft(double Distance) throws RuntimeException{
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public void driveToRight(double Distance) throws RuntimeException{
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public void doLoop(){
        this.m_LeftMotor.getSensor().doLoop();
        this.m_RightMotor.getSensor().doLoop();
    }
}
