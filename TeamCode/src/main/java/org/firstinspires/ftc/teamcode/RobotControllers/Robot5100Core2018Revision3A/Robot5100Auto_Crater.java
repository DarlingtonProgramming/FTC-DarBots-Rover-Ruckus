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

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

@Autonomous(name = "Robot5100Auto-Crater", group = "5100")
public class Robot5100Auto_Crater extends LinearOpMode {
    protected Robot5100Core m_RobotCore;
    protected void hardwareInit(){
        this.m_RobotCore = new Robot5100Core(
                false,
                true,
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
            while(this.m_RobotCore.isBusy() && this.m_RobotCore.getLastDetectedGoldPos() == FTC2018GameSpecificFunctions.GoldPosType.Unknown){
                this.m_RobotCore.tryDetectGoldPos();
            }
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.getMotionSystem().driveToLeft(10,Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
        }
        //When finish getting off the hook, check if the gold mineral has been detected.
        this.m_RobotCore.getMotionSystem().driveForward(20,Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
        this.m_RobotCore.getMotionSystem().waitUntilFinish();
        //Starting X is 8 inch, 20.32cm
        double currentPos = 20.32;
        if(this.m_RobotCore.getLastDetectedGoldPos() == FTC2018GameSpecificFunctions.GoldPosType.Unknown){
            //First, move to the very left to check is the left mineral is gold.
            this.m_RobotCore.getMotionSystem().driveToRight(0 - currentPos,Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            currentPos = 0;
            this.m_RobotCore.waitUntilFinish();
            FTC2018GameSpecificFunctions.MineralInformation firstDetectRst = FTC2018GameSpecificFunctions.MineralInCenterY(this.m_RobotCore.getGameSpecificFunction().detectAllBlocksInCamera());
            if(firstDetectRst.getMineralType() == FTC2018GameSpecificFunctions.MineralType.Gold){
                this.m_RobotCore.setLastDetectedGoldPos(FTC2018GameSpecificFunctions.GoldPosType.Left);
            }
        }
        if(this.m_RobotCore.getLastDetectedGoldPos() == FTC2018GameSpecificFunctions.GoldPosType.Unknown){
            //Second, move to the right to check if the center mineral is gold.
            this.m_RobotCore.getMotionSystem().driveToRight(38.1 - currentPos,Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            currentPos = 38.1; //15 inch
            this.m_RobotCore.waitUntilFinish();
            FTC2018GameSpecificFunctions.MineralInformation centerDetectRst = FTC2018GameSpecificFunctions.MineralInCenterY(this.m_RobotCore.getGameSpecificFunction().detectAllBlocksInCamera());
            if(centerDetectRst.getMineralType() == FTC2018GameSpecificFunctions.MineralType.Gold){
                this.m_RobotCore.setLastDetectedGoldPos(FTC2018GameSpecificFunctions.GoldPosType.Center);
            }else{
                this.m_RobotCore.setLastDetectedGoldPos(FTC2018GameSpecificFunctions.GoldPosType.Right);
            }
        }
        double targetPos = 0;
        if(this.m_RobotCore.getLastDetectedGoldPos() == FTC2018GameSpecificFunctions.GoldPosType.Left){
            targetPos = 0;
        }else if(this.m_RobotCore.getLastDetectedGoldPos() == FTC2018GameSpecificFunctions.GoldPosType.Center){
            targetPos = 38.1;
        }else if(this.m_RobotCore.getLastDetectedGoldPos() == FTC2018GameSpecificFunctions.GoldPosType.Right){
            targetPos = 38.1*2;
        }
        this.m_RobotCore.getMotionSystem().driveToRight(targetPos - currentPos,Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
        this.m_RobotCore.getMotionSystem().waitUntilFinish();
        this.m_RobotCore.getMotionSystem().driveForward(60,Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
        this.m_RobotCore.getMotionSystem().waitUntilFinish();
        this.m_RobotCore.getMotionSystem().turnOffsetAroundCenter(90,Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
        this.m_RobotCore.getMotionSystem().waitUntilFinish();
        this.m_RobotCore.getArm().setTargetPercent(100,Robot5100Setting.AUTONOMOUS_ARMSPEED);
        this.m_RobotCore.waitUntilFinish();
        //Ending The Autonomous Program
        this.m_RobotCore.getMotionSystem().waitUntilFinish();
        this.m_RobotCore.waitUntilFinish();
        this.m_RobotCore.save();
        this.hardwareDestroy();
    }
}
