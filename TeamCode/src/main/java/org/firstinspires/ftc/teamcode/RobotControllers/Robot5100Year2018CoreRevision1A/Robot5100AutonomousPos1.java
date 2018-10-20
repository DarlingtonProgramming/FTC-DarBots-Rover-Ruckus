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

@Autonomous (name = "5100AutonomousPos1", group = "David Cao")
//@Disabled
public class Robot5100AutonomousPos1 extends LinearOpMode {
    private Robot5100Core m_RobotController;

    private void hardwareInit(){
        this.m_RobotController = new Robot5100Core(this,139.065,139.065,-135,0,0, 0);
    }

    public void runOpMode(){
        RobotDebugger.setTelemetry(this.telemetry);
        RobotDebugger.setDebug(true);
        hardwareInit();
        waitForStart();
        if(this.opModeIsActive()){
            this.m_RobotController.getLinearApproachMotor().setPosition(1.0,1.0);
            this.m_RobotController.waitUntilMotionFinish();
        }
        if(this.opModeIsActive()) {
            this.m_RobotController.setRackAndPinionHook();
            this.m_RobotController.waitUntilMotionFinish();
        }
        if(this.opModeIsActive()) {
            this.m_RobotController.driveToLeft(20, 1.0);
            this.m_RobotController.waitUntilMotionFinish();
        }
        if(this.opModeIsActive()) {
            this.m_RobotController.closeRackAndPinion();
            this.m_RobotController.waitUntilMotionFinish();
            this.m_RobotController.closeLinearApproach();
            this.m_RobotController.waitUntilMotionFinish();
        }
        if(this.opModeIsActive()) {
            this.m_RobotController.turnOffsetAroundCenter(-45, 1.0);
            this.m_RobotController.waitUntilMotionFinish();
        }
        if(this.opModeIsActive()) {
            this.m_RobotController.driveForward(96.52, 1.0);
            this.m_RobotController.waitUntilMotionFinish();
        }
        if(this.opModeIsActive()) {
            this.m_RobotController.turnOffsetAroundCenter(90, 1.0);
            this.m_RobotController.waitUntilMotionFinish();
        }
        if(this.opModeIsActive()) {
            this.m_RobotController.driveForward(60, 1.0);
            this.m_RobotController.waitUntilMotionFinish();
        }
        if(this.opModeIsActive()) {
            this.m_RobotController.setCollectingServoOut();
            this.m_RobotController.waitUntilMotionFinish();
            this.m_RobotController.startVomitingMinerals(1.0);
            try {
                Thread.sleep(2000);
            } catch (Exception e) {

            }
            this.m_RobotController.stopSuckingMinerals();
            this.m_RobotController.setCollectingServoIn();
            this.m_RobotController.waitUntilMotionFinish();
        }
    }
}
