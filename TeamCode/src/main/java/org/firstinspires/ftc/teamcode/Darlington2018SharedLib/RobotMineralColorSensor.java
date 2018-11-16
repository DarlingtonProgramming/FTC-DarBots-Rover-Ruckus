package org.firstinspires.ftc.teamcode.Darlington2018SharedLib;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotColorSensor;

public class RobotMineralColorSensor {
    private RobotColorSensor m_ColorSensor;
    public RobotMineralColorSensor(RobotColorSensor ColorSensor){
        this.m_ColorSensor = ColorSensor;
    }
    public RobotColorSensor getColorSensor(){
        return this.m_ColorSensor;
    }
    public void setColorSensor(RobotColorSensor ColorSensor){
        this.m_ColorSensor = ColorSensor;
    }
    public boolean isGoldMineral(){
        int R = this.getColorSensor().red();
        int G = this.getColorSensor().green();
        int B = this.getColorSensor().blue();
        if(R > B && G>B){
            if(Math.round((R+G)/2.0f) - B >= 60){
                return true;
            }
            else if(Math.abs(R-G) < 60){
                if(B < 190){
                    return true;
                }
            }
        }
        return false;
    }
    protected static double mapValue(double currentValue, double currentSmallest, double currentBiggest, double ProjectedSmallest, double ProjectedBiggest){
        double currentAbs = currentValue - currentSmallest;
        return (currentAbs / (currentBiggest - currentSmallest) * (ProjectedBiggest - ProjectedSmallest)) + ProjectedSmallest;
    }

}
