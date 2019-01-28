package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTypes;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorType;

public class GoBlida5202Series43RPMMotor extends RobotMotorType {
    @Override
    public String getMotorName() {
        return "GoBlida 5202 Series 43RPM Motor";
    }

    @Override
    public double getCountsPerRev() {
        return 973;
    }

    @Override
    public double getRevPerSec() {
        return 0.717;
    }
}
