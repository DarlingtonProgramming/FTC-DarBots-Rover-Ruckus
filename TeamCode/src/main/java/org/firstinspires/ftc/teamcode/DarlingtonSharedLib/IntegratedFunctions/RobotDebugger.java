/*
MIT License

Copyright (c) 2018 DarBots Collaborators

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/

/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;
 * This is for internal use.
 */

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotDebugger {
    private static boolean m_isDebugging = false;
    private static Telemetry m_Telemetry;
    public static Telemetry getTelemetry(){
        return m_Telemetry;
    }
    public static void setTelemetry(Telemetry telemetry){
        m_Telemetry = telemetry;
    }
    public static void setDebugOn(boolean isDebug){
        m_isDebugging = isDebug;
    }
    public static boolean isDebugOn(){
        return m_isDebugging;
    }

    public static void clearDebug(){
        if(isDebugOn()) {
            m_Telemetry.clear();
        }
    }
    public static void addDebug(String title, String Content){
        if(isDebugOn()) {
            m_Telemetry.addData(title, Content);
        }
    }
    public static void doLoop(){
        if(isDebugOn()) {
            m_Telemetry.update();
        }
    }
}
