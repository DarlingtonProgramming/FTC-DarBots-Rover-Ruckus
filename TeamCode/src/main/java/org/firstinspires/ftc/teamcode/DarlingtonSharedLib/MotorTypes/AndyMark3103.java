package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTypes;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorType;

public class AndyMark3103 extends RobotMotorType {
    @Override
    public String getMotorName() {
        return "AndyMark3103 am-3103";
    }

    @Override
    public double getCountsPerRev() {
        return 1680;
    }

    @Override
    public double getRevPerSec() {
        return 1.75;
    }
}
