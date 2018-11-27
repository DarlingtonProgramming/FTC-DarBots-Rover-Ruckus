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

package org.firstinspires.ftc.teamcode.Darlington2018SharedLib;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RevColorSensor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotColorSensor;

public class RobotMineralColorSensor {
    public static enum MineralType{
        empty,
        silver,
        gold
    }
    private RevColorSensor m_ColorSensor;
    public RobotMineralColorSensor(RevColorSensor ColorSensor){
        this.m_ColorSensor = ColorSensor;
    }
    public RevColorSensor getColorSensor(){
        return this.m_ColorSensor;
    }
    public void setColorSensor(RevColorSensor ColorSensor){
        this.m_ColorSensor = ColorSensor;
    }
    public MineralType getMineralType(){
        int R = this.getColorSensor().red();
        int G = this.getColorSensor().green();
        int B = this.getColorSensor().blue();
        double CM = 0;
        try {
            CM = this.getColorSensor().getDistance(DistanceUnit.CM);
        }catch(RuntimeException e){
            CM = -1;
        }
        if(CM == -1 || CM >= 25){
            return MineralType.empty;
        }
        if(R > B && G > B){
            if(Math.round((R+G)/2.0f) - B >= 60){
                return MineralType.gold;
            }
            else if(Math.abs(R-G) < 60){
                if(B < 190){
                    return MineralType.gold;
                }
            }
        }
        return MineralType.silver;
    }
    public void updateData(){
        this.m_ColorSensor.updateData();
    }
    protected static double mapValue(double currentValue, double currentSmallest, double currentBiggest, double ProjectedSmallest, double ProjectedBiggest){
        double currentAbs = currentValue - currentSmallest;
        return (currentAbs / (currentBiggest - currentSmallest) * (ProjectedBiggest - ProjectedSmallest)) + ProjectedSmallest;
    }
}
