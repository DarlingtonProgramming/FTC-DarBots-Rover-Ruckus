package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotDebugger {
    private static boolean m_isDebugging = false;
    private static Telemetry m_Telemetry;
    private static int m_LogNum = 0;
    public static Telemetry getTelemetry(){
        return m_Telemetry;
    }
    public static void setTelemetry(Telemetry telemetry){
        m_Telemetry = telemetry;
        m_LogNum = 0;
    }
    public static void setDebug(boolean isDebug){
        m_isDebugging = isDebug;
    }
    public static boolean isDebug(){
        return m_isDebugging;
    }

    public static void clearDebug(){
        if(isDebug()) {
            m_Telemetry.clear();
            m_LogNum = 0;
        }
    }
    public static void addDebug(String title, String Content){
        if(isDebug()) {
            m_Telemetry.addData("Debug" + m_LogNum,title + ":" + Content);
            m_LogNum++;
        }
    }
    public static void doLoop(){
        if(isDebug()) {
            m_Telemetry.update();
        }
    }
}
