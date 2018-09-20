/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotMotionSystem;
 */

package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

public interface RobotMotionSystem {
    RobotPositionTracker getPositionTracker();
    boolean isBusy();
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
}
