package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTypes;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorType;

public class RevHDHex20Motor extends RobotMotorType {

    @Override
    public String getMotorName() {
        return "Rev HD Hex 20 Motor";
    }

    @Override
    public double getCountsPerRev() {
        return 560;
    }

    @Override
    public double getRevPerSec() {
        return 5;
    }
}
