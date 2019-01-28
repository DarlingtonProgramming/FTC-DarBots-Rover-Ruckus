package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions;

public class Robot2DPositionIndicator {
    protected double m_X;
    protected double m_Z;
    protected double m_RotationY;
    public Robot2DPositionIndicator(double X, double Z, double YRotation){
        this.m_X = X;
        this.m_Z = Z;
        this.m_RotationY = YRotation;
    }
    public Robot2DPositionIndicator(Robot2DPositionIndicator Pos2D){
        this.m_X = Pos2D.m_X;
        this.m_Z = Pos2D.m_Z;
        this.m_RotationY = Pos2D.m_RotationY;
    }
    public double getX(){
        return this.m_X;
    }
    public void setX(double X){
        this.m_X = X;
    }
    public double getZ(){
        return this.m_Z;
    }
    public void setZ(double Z){
        this.m_Z = Z;
    }
    public double getDistanceToOrigin(){
        return (Math.sqrt(Math.pow(this.getX(),2) + Math.pow(this.getZ(),2)));
    }
    public double getRotationY(){
        return this.m_RotationY;
    }
    public void setRotationY(double RotationY){
        this.m_RotationY = RotationY;
    }
}
