package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTypes;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorType;

public class RevHDHex40Motor extends RobotMotorType {
    @Override
    public String getMotorName() {
        return "Rev HD Hex 40 Motor";
    }

    @Override
    public double getCountsPerRev() {
        return 1120;
    }

    @Override
    public double getRevPerSec() {
        return 2.5;
    }
}
