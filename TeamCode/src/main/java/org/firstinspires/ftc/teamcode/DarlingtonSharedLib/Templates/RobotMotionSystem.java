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

/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotMotionSystem;
 */

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;

public interface RobotMotionSystem {
    enum motionType{
        turningFixedAngle,
        keepingTurningWithSpeed,
        movingFixedDistance,
        keepingMovingWithFixedSpeed,
        stopped
    }
    enum RobotMotionDirection{
        inX,
        inY,
        rotating,
        inCustomDirection
    }
    RobotPositionTracker getPositionTracker();
    boolean isBusy();
    void waitUntilMotionFinish();
    void setPositionTracker(RobotPositionTracker newPositionTracker);
    void turnToAbsFieldAngle(double AngleInDegree, double Speed);
    void turnOffsetAroundCenter(double AngleInDegree, double Speed);
    void keepTurningOffsetAroundCenter(double Speed);
    void driveTo(double[] fieldPos, double Speed);
    void driveForward(double Distance, double Speed);
    void driveBackward(double Distance, double Speed);
    void driveToLeft(double Distance, double Speed);
    void driveToRight(double Distance, double Speed);
    void driveForwardWithSpeed(double Speed);
    motionType getCurrentMotionType();
    RobotMotionDirection getMovingDirection();
    void driveBackwardWithSpeed(double Speed);
    void driveToLeftWithSpeed(double Speed);
    void driveToRightWithSpeed(double Speed);
    void stopMoving();
}
