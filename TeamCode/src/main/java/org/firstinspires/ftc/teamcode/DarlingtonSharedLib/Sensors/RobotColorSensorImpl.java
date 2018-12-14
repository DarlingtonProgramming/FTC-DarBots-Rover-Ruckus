package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;


import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotColorSensor;

public class RobotColorSensorImpl implements RobotColorSensor {
    private ColorSensor m_ColorSensor;
    private int m_Alpha;
    private int m_Green;
    private int m_Red;
    private int m_Blue;

    public RobotColorSensorImpl(@NonNull ColorSensor ColorSensor){
        this.m_ColorSensor = ColorSensor;
    }
    @Override
    public ColorSensor getColorSensor(){
        return this.m_ColorSensor;
    }
    @Override
    public void setColorSensor(ColorSensor ColorSensor){
        this.m_ColorSensor = ColorSensor;
    }
    @Override
    public void updateData(){
        m_Alpha = this.getColorSensor().alpha();
        int redRaw = this.getColorSensor().red();
        int blueRaw = this.getColorSensor().blue();
        int greenRaw = this.getColorSensor().green();
        int MaxRGBVal = Math.max(Math.max(redRaw,greenRaw),blueRaw);
        int R = (int) Math.round(mapValue(redRaw,0,MaxRGBVal,0,255));
        int G = (int) Math.round(mapValue(greenRaw,0,MaxRGBVal,0,255));
        int B = (int) Math.round(mapValue(blueRaw,0,MaxRGBVal,0,255));
        this.m_Red = R;
        this.m_Green = G;
        this.m_Blue = B;
    }
    @Override
    public int alpha(){
        return this.m_Alpha;
    }
    @Override
    public int red(){
        return this.m_Red;
    }
    @Override
    public int green(){
        return this.m_Green;
    }
    @Override
    public int blue(){
        return this.m_Blue;
    }
    protected static double mapValue(double currentValue, double currentSmallest, double currentBiggest, double ProjectedSmallest, double ProjectedBiggest){
        double currentAbs = currentValue - currentSmallest;
        return (currentAbs / (currentBiggest - currentSmallest) * (ProjectedBiggest - ProjectedSmallest)) + ProjectedSmallest;
    }
}
