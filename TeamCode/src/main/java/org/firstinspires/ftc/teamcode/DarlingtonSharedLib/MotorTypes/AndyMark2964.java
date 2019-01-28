package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTypes;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorType;

public class AndyMark2964 extends RobotMotorType {

    @Override
    public String getMotorName() {
        return "AndyMark2964 am-2964";
    }

    @Override
    public double getCountsPerRev() {
        return 1120;
    }

    @Override
    public double getRevPerSec() {
        return 2.67;
    }
}
