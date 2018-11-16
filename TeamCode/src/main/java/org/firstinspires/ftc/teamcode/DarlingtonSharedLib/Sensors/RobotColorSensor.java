package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;


import com.qualcomm.robotcore.hardware.ColorSensor;

public class RobotColorSensor {
    private ColorSensor m_ColorSensor;
    private int m_Alpha;
    private int m_Green;
    private int m_Red;
    private int m_Blue;

    public RobotColorSensor(ColorSensor ColorSensor){
        this.m_ColorSensor = ColorSensor;
        this.updateData();
    }
    public ColorSensor getColorSensor(){
        return this.m_ColorSensor;
    }
    public void setColorSensor(ColorSensor ColorSensor){
        this.m_ColorSensor = ColorSensor;
    }
    public void updateData(){
        m_Alpha = this.getColorSensor().alpha();
        int MaxRGBVal = Math.max(Math.max(this.getColorSensor().red(),this.getColorSensor().green()),this.getColorSensor().blue());
        int R = (int) Math.round(mapValue(this.getColorSensor().red(),0,MaxRGBVal,0,255));
        int G = (int) Math.round(mapValue(this.getColorSensor().green(),0,MaxRGBVal,0,255));
        int B = (int) Math.round(mapValue(this.getColorSensor().blue(),0,MaxRGBVal,0,255));
        this.m_Red = R;
        this.m_Green = G;
        this.m_Blue = B;
    }
    public int alpha(){
        return this.m_Alpha;
    }
    public int red(){
        return this.m_Red;
    }
    public int green(){
        return this.m_Green;
    }
    public int blue(){
        return this.m_Blue;
    }
    protected static double mapValue(double currentValue, double currentSmallest, double currentBiggest, double ProjectedSmallest, double ProjectedBiggest){
        double currentAbs = currentValue - currentSmallest;
        return (currentAbs / (currentBiggest - currentSmallest) * (ProjectedBiggest - ProjectedSmallest)) + ProjectedSmallest;
    }
}
