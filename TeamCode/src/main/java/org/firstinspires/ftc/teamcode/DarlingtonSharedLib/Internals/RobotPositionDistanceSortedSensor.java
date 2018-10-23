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
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotPositionDistanceSortedSensor;
 * Only for Internal Use
 */

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Internals;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.XYPlaneCalculations;

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
        double[] origin = {0,0};
        double[] relativeRobotRotationFixedPoint = XYPlaneCalculations.rotatePointAroundFixedPoint(robotAxis, origin, Turning);
        return relativeRobotRotationFixedPoint;
    }
}
