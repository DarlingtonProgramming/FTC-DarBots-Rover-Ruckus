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

package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

public interface RobotMotionSystem {
    RobotPositionTracker getPositionTracker();
    boolean isBusy();
    void waitUntilMotionFinish();
    void setPositionTracker(RobotPositionTracker newPositionTracker);
    double[] getCurrentFieldPos();
    double[] getRobotAxisFromFieldAxis(double[] FieldPosition);
    double[] getFieldAxisFromRobotAxis(double[] RobotPosition);
    void setCurrentFieldPos(double[] Position);
    void turnToAbsFieldAngle(double AngleInDegree);
    void turnOffsetAroundCenter(double AngleInDegree);
    void driveTo(double[] fieldPos);
    void driveForward(double Distance);
    void driveBackward(double Distance);
    void driveToLeft(double Distance);
    void driveToRight(double Distance);
    void driveForwardWithSpeed(double Speed);
    void driveBackwardWithSpeed(double Speed);
    void driveToLeftWithSpeed(double Speed);
    void driveToRightWithSpeed(double Speed);
    void stopDrivingWithSpeed();
}
