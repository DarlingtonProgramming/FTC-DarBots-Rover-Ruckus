package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Runtime;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotCore;

public class RobotGlobal {
    private static RobotCore m_RobotCore;
    public static RobotCore getRunningRobotCore(){
        return m_RobotCore;
    }
    public static void setRunningRobotCore(RobotCore robotCore){
        m_RobotCore = robotCore;
    }
}
