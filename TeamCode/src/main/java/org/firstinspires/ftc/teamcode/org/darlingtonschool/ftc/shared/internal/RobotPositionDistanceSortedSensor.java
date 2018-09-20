/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotPositionDistanceSortedSensor;
 * Only for Internal Use
 */

package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal;

public class RobotPositionDistanceSortedSensor {
    private double m_Distance;
    private double[] m_RobotAxisPos;
    public RobotPositionDistanceSortedSensor(double DistanceMeasured, double[] RobotAxisPos){
        this.m_Distance = DistanceMeasured;
        this.m_RobotAxisPos = RobotAxisPos;
    }
    public double getDistanceMeasured(){
        return this.m_Distance;
    }
    public void setDistanceMeasured(double Distance){
        this.m_Distance = Distance;
    }
    public double[] getRobotAxisPos(){
        return this.m_RobotAxisPos;
    }
    public void setRobotAxisPos(double[] Position){
        this.m_RobotAxisPos = Position;
    }

    public double[] getFieldOffset(double Turning){
        //This is the piece of code copied from the RobotPositionTracker.
        double[] robotAxis = this.getRobotAxisPos();
        if(robotAxis[0] == 0 && robotAxis[1] == 0){
            double[] mTempResult = {0,0};
            return mTempResult;
        }
        double hRobot = Math.sqrt(Math.pow(robotAxis[0],2) + Math.pow(robotAxis[1],2));
        double angleInRobotAxis = Math.asin(robotAxis[0] / hRobot);
        double theta = angleInRobotAxis + Math.toRadians(Turning);
        double hField= hRobot;
        double xRelativeField = Math.sin(theta) * hField;
        double yRelativeField = Math.cos(theta) * hField;
        double[] mResult = {xRelativeField, yRelativeField};
        return mResult;
    }
}
