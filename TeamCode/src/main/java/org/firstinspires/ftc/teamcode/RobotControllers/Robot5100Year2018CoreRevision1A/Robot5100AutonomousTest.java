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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision1A;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;

@Autonomous (name = "5100Test", group = "David Cao")
//@Disabled
public class Robot5100AutonomousTest extends LinearOpMode {
    private Robot5100MotionSystem m_MotionSystem;
    private RobotPositionTracker m_PositionTracker;

    private void hardwareInit(){
        //FIELD: 365.76 * 365.76 CM^2
        double[] Motor0Pos = {-16.34, -16.34};
        double[] Motor1Pos = {16.34, -16.34};
        double[] Motor2Pos = {-16.34,16.34};
        double[] Motor3Pos = {16.34,16.34};
        this.m_PositionTracker = new RobotPositionTracker(365.76,365.76,100,100,0,Motor0Pos,Motor1Pos,Motor2Pos,Motor3Pos);
        this.m_MotionSystem = new Robot5100MotionSystem(this.m_PositionTracker,this.hardwareMap.dcMotor.get("motor0"), this.hardwareMap.dcMotor.get("motor1"),this.hardwareMap.dcMotor.get("motor2"), this.hardwareMap.dcMotor.get("motor3"));
    }

    public void runOpMode(){
        RobotDebugger.setTelemetry(this.telemetry);
        RobotDebugger.setDebug(true);
        this.hardwareInit();
        waitForStart();
        while(this.opModeIsActive()){
            this.m_MotionSystem.driveForward(30);
            this.m_MotionSystem.waitUntilMotionFinish();
            this.m_MotionSystem.turnOffsetAroundCenter(90);
            this.m_MotionSystem.waitUntilMotionFinish();
            this.m_MotionSystem.turnOffsetAroundCenter(-90);
            this.m_MotionSystem.waitUntilMotionFinish();
            this.m_MotionSystem.driveBackward(30);
            this.m_MotionSystem.waitUntilMotionFinish();

            this.m_MotionSystem.doLoop();
            RobotDebugger.doLoop();
        }
    }
}
