package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.Robot2DPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.XYPlaneCalculations;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;

public class RobotWheel {
    private double m_Radius;
    private Robot2DPositionTracker.Robot2DPositionRobotAxisIndicator m_OnRobotPosition;

    public RobotWheel(Robot2DPositionTracker.Robot2DPositionRobotAxisIndicator OnRobotPosition, double radius){
        this.m_OnRobotPosition = OnRobotPosition;
        this.m_Radius = radius;
    }
    public Robot2DPositionTracker.Robot2DPositionRobotAxisIndicator getOnRobotPosition(){
        return this.m_OnRobotPosition;
    }
    public void setOnRobotPosition(Robot2DPositionTracker.Robot2DPositionRobotAxisIndicator OnRobotPosition){
        this.m_OnRobotPosition = OnRobotPosition;
    }
    public double getRadius(){
        return this.m_Radius;
    }
    public void setRadius(double Radius){
        this.m_Radius = Radius;
    }
    public double getCircumference(){
        return this.m_Radius * 2 * Math.PI;
    }
    public double getXPerDistance(){
        return -Math.cos(Math.toRadians(this.m_OnRobotPosition.getRotationY()));
    }
    public double getZPerDistance(){
        return -Math.sin(Math.toRadians(this.m_OnRobotPosition.getRotationY()));
    }
    public double getDistanceFromCenterOfRobot(){
        return this.m_OnRobotPosition.getDistanceToOrigin();
    }
}
