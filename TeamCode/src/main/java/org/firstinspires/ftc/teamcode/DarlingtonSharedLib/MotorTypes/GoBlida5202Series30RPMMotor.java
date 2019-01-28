package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.MotorTypes;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorType;

public class GoBlida5202Series30RPMMotor extends RobotMotorType {
    @Override
    public String getMotorName() {
        return "GoBlida 5202 Series 30RPM Motor";
    }

    @Override
    public double getCountsPerRev() {
        return 1316;
    }

    @Override
    public double getRevPerSec() {
        return 0.5;
    }
}
