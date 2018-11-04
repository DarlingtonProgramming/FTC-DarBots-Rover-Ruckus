package org.firstinspires.ftc.teamcode.Darlington2018SharedLib;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.XYPlaneCalculations;

public class FTC2018GameFieldConvert {
    public static double[] convert2DFieldAxisFrom2DVuforiaField(@NonNull double[] VuforiaField, @NonNull double[] FieldAxisFieldSize){
        double[] FieldAxis = {0,0};
        FieldAxis[0] = FieldAxisFieldSize[0] / 2.0 - VuforiaField[1];
        FieldAxis[1] = FieldAxisFieldSize[1] / 2.0 + VuforiaField[0];
        return FieldAxis;
    }
    public static double[] convert2DVuforiaFieldFrom2DFieldAxis(@NonNull double[] FieldAxis, @NonNull double[] FieldAxisFieldSize){
        double[] VuforiaField = {0,0};
        VuforiaField[0] = FieldAxis[1] - FieldAxisFieldSize[1] / 2.0;
        VuforiaField[1] = FieldAxisFieldSize[0] / 2.0 - FieldAxis[0];
        return VuforiaField;
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
    public static double[] convert2DRobotAxisFrom2DVuforiaRobot(@NonNull double[] VuforiaRobot) {
        double[] RobotAxis = {0,0};
        RobotAxis[0] = -VuforiaRobot[1];
        RobotAxis[1] = VuforiaRobot[0];
        return RobotAxis;
    }
    public static double[] convert2DVuforiaRobotFrom2DRobotAxis(@NonNull double[] RobotAxis){
        double[] VuforiaRobot = {0,0};
        VuforiaRobot[0] = RobotAxis[1];
        VuforiaRobot[1] = -RobotAxis[0];
        return VuforiaRobot;
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
