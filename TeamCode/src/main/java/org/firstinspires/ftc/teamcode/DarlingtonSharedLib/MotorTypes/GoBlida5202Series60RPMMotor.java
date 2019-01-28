package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTypes;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorType;

public class GoBlida5202Series60RPMMotor extends RobotMotorType {
    @Override
    public String getMotorName() {
        return "GoBlida 5202 Series 60RPM Motor";
    }

    @Override
    public double getCountsPerRev() {
        return 696.5;
    }

    @Override
    public double getRevPerSec() {
        return 1;
    }
}
