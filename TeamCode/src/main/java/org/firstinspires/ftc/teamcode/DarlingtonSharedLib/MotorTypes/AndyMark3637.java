package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTypes;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorType;

public class AndyMark3637 extends RobotMotorType {
    @Override
    public String getMotorName() {
        return "AndyMark20Motor am-3637";
    }

    @Override
    public double getCountsPerRev() {
        return 537.6;
    }

    @Override
    public double getRevPerSec() {
        return 5.67;
    }
}
