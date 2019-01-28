package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTypes;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorType;

public class RevCoreHexMotor extends RobotMotorType {
    @Override
    public String getMotorName() {
        return "Rev Core Hex Motor";
    }

    @Override
    public double getCountsPerRev() {
        return 288;
    }

    @Override
    public double getRevPerSec() {
        return 2.08;
    }
}
