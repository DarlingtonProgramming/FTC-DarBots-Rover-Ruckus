package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotion;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorController;

public abstract class RobotMotionTaskCallBack {
    public abstract void finishRunning(RobotMotion Motion, boolean timeOut, double timeUsedInSec, int CountsMoved, double DistanceMoved);
}
