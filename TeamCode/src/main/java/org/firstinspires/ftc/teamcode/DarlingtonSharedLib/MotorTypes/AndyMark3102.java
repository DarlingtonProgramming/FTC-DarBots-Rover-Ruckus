package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTypes;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorType;

public class AndyMark3102 extends RobotMotorType {
    @Override
    public String getMotorName() {
        return "AndyMark3102 am-3102";
    }

    @Override
    public double getCountsPerRev() {
        return 560;
    }

    @Override
    public double getRevPerSec() {
        return 4.58;
    }
}
