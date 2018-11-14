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

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;

public abstract class RobotMotionSystem implements RobotNonBlockingDevice,RobotEventLoopable {
    public enum motionType{
        turningFixedAngle,
        keepingTurningWithSpeed,
        movingFixedDistance,
        keepingMovingWithFixedSpeed,
        stopped
    }
    public enum RobotMotionDirection{
        inX,
        inY,
        rotating,
        inCustomDirection
    }
    @Override
    public abstract boolean isBusy();
    @Override
    public abstract void waitUntilFinish();
    @Override
    public abstract void doLoop();
    public abstract RobotPositionTracker getPositionTracker();
    public abstract void setPositionTracker(RobotPositionTracker newPositionTracker);
    public abstract void turnToAbsFieldAngle(double AngleInDegree, double Speed);
    public abstract void turnOffsetAroundCenter(double AngleInDegree, double Speed);
    public abstract void keepTurningOffsetAroundCenter(double Speed);
    public abstract void driveTo(double[] fieldPos, double Speed);
    public abstract void driveForward(double Distance, double Speed);
    public abstract void driveBackward(double Distance, double Speed);
    public abstract void driveToLeft(double Distance, double Speed);
    public abstract void driveToRight(double Distance, double Speed);
    public abstract void driveForwardWithSpeed(double Speed);
    public abstract motionType getCurrentMotionType();
    public abstract RobotMotionDirection getMovingDirection();
    public abstract void driveBackwardWithSpeed(double Speed);
    public abstract void driveToLeftWithSpeed(double Speed);
    public abstract void driveToRightWithSpeed(double Speed);
    public abstract void stopMoving();
}
