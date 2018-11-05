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

import android.support.annotation.NonNull;

import com.vuforia.Vuforia;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.XYPlaneCalculations;

public class FTC2018GameFieldConvert {
    public static double[] convertFieldAxisFromVuforiaField(@NonNull double[] VuforiaField, @NonNull double[] FieldAxisFieldSize){
        if(VuforiaField.length == 2) {
            double[] FieldAxis = {0, 0};
            FieldAxis[0] = FieldAxisFieldSize[0] / 2.0 - VuforiaField[1];
            FieldAxis[1] = FieldAxisFieldSize[1] / 2.0 + VuforiaField[0];
            return FieldAxis;
        }else if(VuforiaField.length == 3){
            double[] FieldAxis = {0,0,0};
            FieldAxis[0] = FieldAxisFieldSize[0] / 2.0 - VuforiaField[1];
            FieldAxis[1] = FieldAxisFieldSize[1] / 2.0 + VuforiaField[0];
            FieldAxis[2] = VuforiaField[2];
            return FieldAxis;
        }else{
            throw new RuntimeException("You cannot pass an coordination that has length of neither 2 or 3!");
        }
    }

    public static double[] convertVuforiaFieldFromFieldAxis(@NonNull double[] FieldAxis, @NonNull double[] FieldAxisFieldSize){
        if(FieldAxis.length == 2) {
            double[] VuforiaField = {0, 0};
            VuforiaField[0] = FieldAxis[1] - FieldAxisFieldSize[1] / 2.0;
            VuforiaField[1] = FieldAxisFieldSize[0] / 2.0 - FieldAxis[0];
            return VuforiaField;
        }else if(FieldAxis.length == 3){
            double[] VuforiaField = {0, 0, 0};
            VuforiaField[0] = FieldAxis[1] - FieldAxisFieldSize[1] / 2.0;
            VuforiaField[1] = FieldAxisFieldSize[0] / 2.0 - FieldAxis[0];
            VuforiaField[2] = FieldAxis[2];
            return VuforiaField;
        }else{
            throw new RuntimeException("You cannot pass an coordination that has length of neither 2 or 3!");
        }
    }
    public static double[] convert3DFieldRotationFrom3DVuforiaFieldRotation(@NonNull double[] VuforiaRotation){
        double[] FieldRotation = {0,0,0};
        FieldRotation[0] = XYPlaneCalculations.normalizeDeg(-VuforiaRotation[1]);
        FieldRotation[1] = VuforiaRotation[0];
        FieldRotation[2] = XYPlaneCalculations.normalizeDeg(VuforiaRotation[2] - 90);
        return FieldRotation;
    }
    public static double[] convert3DVuforiaFieldRotationFrom3DFieldRotation(@NonNull double[] FieldRotation){
        double[] VuforiaRotation = {0,0,0};
        VuforiaRotation[0] = FieldRotation[1];
        VuforiaRotation[1] = XYPlaneCalculations.normalizeDeg(-FieldRotation[0]);
        VuforiaRotation[2] = XYPlaneCalculations.normalizeDeg(FieldRotation[2] + 90);
        return VuforiaRotation;
    }
    public static double[] convertRobotAxisFromVuforiaRobot(@NonNull double[] VuforiaRobot) {
        if(VuforiaRobot.length == 2) {
            double[] RobotAxis = {0, 0};
            RobotAxis[0] = -VuforiaRobot[1];
            RobotAxis[1] = VuforiaRobot[0];
            return RobotAxis;
        }else if(VuforiaRobot.length == 3){
            double[] RobotAxis = {0, 0, 0};
            RobotAxis[0] = -VuforiaRobot[1];
            RobotAxis[1] = VuforiaRobot[0];
            RobotAxis[2] = VuforiaRobot[2];
            return RobotAxis;
        }else{
            throw new RuntimeException("You cannot pass an coordination that has length of neither 2 or 3!");
        }
    }
    public static double[] convertVuforiaRobotFromRobotAxis(@NonNull double[] RobotAxis){
        if(RobotAxis.length == 2) {
            double[] VuforiaRobot = {0, 0};
            VuforiaRobot[0] = RobotAxis[1];
            VuforiaRobot[1] = -RobotAxis[0];
            return VuforiaRobot;
        }else if(RobotAxis.length == 3){
            double[] VuforiaRobot = {0,0,0};
            VuforiaRobot[0] = RobotAxis[1];
            VuforiaRobot[1] = -RobotAxis[0];
            VuforiaRobot[2] = RobotAxis[2];
            return VuforiaRobot;
        }else{
            throw new RuntimeException("You cannot pass an coordination that has length of neither 2 or 3!");
        }
    }
    public static double[] convert3DRobotRotationFrom3DVuforiaRobotRotation(@NonNull double[] VuforiaRotation){
        double[] RobotRotation = {0,0,0};
        RobotRotation[0] = XYPlaneCalculations.normalizeDeg(-VuforiaRotation[1]);
        RobotRotation[1] = VuforiaRotation[0];
        RobotRotation[2] = XYPlaneCalculations.normalizeDeg(VuforiaRotation[2] - 90);
        return RobotRotation;
    }
    public static double[] convert3DVuforiaRobotRotationFrom3DRobotRotation(@NonNull double[] RobotRotation){
        double[] VuforiaRotation = {0,0,0};
        VuforiaRotation[0] = RobotRotation[1];
        VuforiaRotation[1] = XYPlaneCalculations.normalizeDeg(-RobotRotation[0]);
        VuforiaRotation[2] = XYPlaneCalculations.normalizeDeg(RobotRotation[2] + 90);
        return VuforiaRotation;
    }
}
