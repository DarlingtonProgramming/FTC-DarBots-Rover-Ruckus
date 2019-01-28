package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorController;

public interface RobotMotorTaskCallBack {
    void finishRunning(RobotMotorController Controller, boolean timeOut, double timeUsedInSec, int CountsMoved);
}
