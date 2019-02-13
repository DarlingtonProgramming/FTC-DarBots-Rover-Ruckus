package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

public interface DebuggerAttachable {
    RobotDebugger.RobotDebuggerCallable getDebuggerCallable(String partName);
}
