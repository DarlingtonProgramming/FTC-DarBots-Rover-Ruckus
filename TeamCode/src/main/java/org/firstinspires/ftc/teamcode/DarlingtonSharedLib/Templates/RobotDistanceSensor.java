package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public interface RobotDistanceSensor {
    double getDistance(DistanceUnit unit) throws RuntimeException;
    DistanceSensor getDistanceSensor();
    void setDistanceSensor(DistanceSensor distanceSensor);
    void updateData();
}
