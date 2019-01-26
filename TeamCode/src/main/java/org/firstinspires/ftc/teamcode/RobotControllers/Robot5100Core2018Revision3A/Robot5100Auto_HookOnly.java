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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.XYPlaneCalculations;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

@Autonomous(name = "Robot5100Auto-HookOnly", group = "5100")
public class Robot5100Auto_HookOnly extends LinearOpMode {
    protected Robot5100Core m_RobotCore;
    protected void hardwareInit(){
        this.m_RobotCore = new Robot5100Core(
                false,
                false,
                this,
                100,
                100,
                0,
                0,
                0,
                0
        );
    }
    protected void hardwareDestroy(){
        this.m_RobotCore = null;
    }
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        RobotDebugger.addDebug("Status","Initialized");
        RobotDebugger.doLoop();
        this.waitForStart();
        if(this.opModeIsActive()){
            this.m_RobotCore.setLinearActuatorToHook(Robot5100Setting.AUTONOMOUS_LINEARACTUATORSPEED);
            while(this.m_RobotCore.isBusy() && this.m_RobotCore.getLinearActuator().getCurrentPercent() < 99){

            }
            this.m_RobotCore.getMotionSystem().turnOffsetAroundCenter(30,Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            ElapsedTime m_Time = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
            m_Time.reset();
            this.m_RobotCore.getMotionSystem().driveForwardWithSpeed(0.2);
            while(m_Time.seconds() < 0.6){

            }
            this.m_RobotCore.getMotionSystem().stopMoving();
            this.m_RobotCore.getMotionSystem().turnOffsetAroundCenter(-35,Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
        }
        //Ending The Autonomous Program
        this.m_RobotCore.getMotionSystem().waitUntilFinish();
        this.m_RobotCore.waitUntilFinish();
        this.m_RobotCore.save();
        this.hardwareDestroy();
    }
}
