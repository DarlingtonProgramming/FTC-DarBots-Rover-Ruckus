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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;

@TeleOp (name = "Robot5100TeleOp",group = "5100")
public class Robot5100TeleOp extends LinearOpMode {
    private Robot5100Core m_RobotCore;

    public void hardWareInitialize(){
        this.m_RobotCore = new Robot5100Core(this,100,100,0,Robot5100Settings.rackAndPinionInitialPos,Robot5100Settings.dumperInitialPos,Robot5100Settings.linearReachInitialPos,Robot5100Settings.collectorServoInitialPos,false);
    }

    protected void Gamepad1Control(){
        if(gamepad1.right_trigger >= Robot5100Settings.TeleOP_GamepadTriggerValue){
            this.m_RobotCore.getRackAndPinion().setPosition(this.m_RobotCore.getRackAndPinion().getPosition() + Robot5100Settings.TeleOP_RackAndPinionDelta);
        }else if(gamepad1.left_trigger >= Robot5100Settings.TeleOP_GamepadTriggerValue){
                this.m_RobotCore.getRackAndPinion().setPosition(this.m_RobotCore.getRackAndPinion().getPosition() - Robot5100Settings.TeleOP_RackAndPinionDelta);
        }
        if(gamepad1.right_bumper){
            this.m_RobotCore.openRackAndPinion();
        }else if(gamepad1.left_bumper){
            this.m_RobotCore.closeRackAndPinion();
        }
        if(gamepad1.a){
            this.m_RobotCore.openRackAndPinionToHook();
        }
    }
    protected void Gamepad2Control(){
        if(gamepad2.right_trigger >= Robot5100Settings.TeleOP_GamepadTriggerValue){
            this.m_RobotCore.startSuckingMinerals();
        }else if(gamepad2.left_trigger >= Robot5100Settings.TeleOP_GamepadTriggerValue){
            this.m_RobotCore.startVomitingMinerals();
        }else{
            this.m_RobotCore.closeMouth();
        }
        if(gamepad2.b){
            this.m_RobotCore.getCollectorServo().setPosition(this.m_RobotCore.getCollectorServo().getPosition() + Robot5100Settings.TeleOP_CollectorServoDelta,Robot5100Settings.collectorServoSpeed);
        }else if(gamepad2.a){
            this.m_RobotCore.getCollectorServo().setPosition(this.m_RobotCore.getCollectorServo().getPosition() - Robot5100Settings.TeleOP_CollectorServoDelta,Robot5100Settings.collectorServoSpeed);
        }
        if(gamepad2.x){
            ElapsedTime waittime = new ElapsedTime();
            this.m_RobotCore.setCollectorServoToRes();
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.closeLinearReach();
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.closeCollectorServo();
            this.m_RobotCore.waitUntilFinish();
            waittime.reset();
            this.m_RobotCore.startSuckingMinerals();
            while(waittime.seconds()<1){
                this.m_RobotCore.doLoop();
            }
            this.m_RobotCore.closeMouth();

        }
        if(gamepad2.y){
            this.m_RobotCore.setCollectorServoToRes();
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.openRackAndPinion();
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.raiseDumper();
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.getMotionSystem().driveForward(20,1.0);
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.getMotionSystem().driveBackward(20,1.0);
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.descendDumper();
            this.m_RobotCore.closeRackAndPinion();
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.closeCollectorServo();
        }

        if((-this.gamepad2.right_stick_y) >= Robot5100Settings.TeleOP_GamepadTriggerValue){
            if(this.m_RobotCore.getLinearReach().getPosition() + Robot5100Settings.TeleOP_LinearReachDelta <= this.m_RobotCore.getLinearReach().getBiggestPos()) {
                this.m_RobotCore.getLinearReach().setPosition(this.m_RobotCore.getLinearReach().getPosition() + Robot5100Settings.TeleOP_LinearReachDelta);
            }
        }else if((this.gamepad2.right_stick_y) >= Robot5100Settings.TeleOP_GamepadTriggerValue ){
            if(this.m_RobotCore.getLinearReach().getPosition() - Robot5100Settings.TeleOP_LinearReachDelta >= this.m_RobotCore.getLinearReach().getSmallestPos()) {
                this.m_RobotCore.getLinearReach().setPosition(this.m_RobotCore.getLinearReach().getPosition() - Robot5100Settings.TeleOP_LinearReachDelta);
            }
        }

    }
    protected void potentialConflictControl(){
        boolean isControlMotion = false;
        boolean gamepad2ControllingMotion = false;
        boolean controllingTurning = false;
        boolean isControllingY = false;
        double controlValue = 0;

        if(Math.abs(this.gamepad2.right_stick_y) >= Robot5100Settings.TeleOP_GamepadTriggerValue){
            if(m_RobotCore.getLinearReach().getPosition() + Robot5100Settings.TeleOP_LinearReachDelta > this.m_RobotCore.getLinearReach().getBiggestPos() && (-gamepad2.right_stick_y) > 0){
                gamepad2ControllingMotion = true;
                controlValue = -this.gamepad2.right_stick_y;
                isControllingY = true;
                isControlMotion = true;
            }else if(m_RobotCore.getLinearReach().getPosition() - Robot5100Settings.TeleOP_LinearReachDelta < this.m_RobotCore.getLinearReach().getSmallestPos() && (-gamepad2.right_stick_y) < 0) {
                gamepad2ControllingMotion = true;
                controlValue = -this.gamepad2.right_stick_y;
                isControllingY = true;
                isControlMotion = true;
            }
        }else if(Math.abs(this.gamepad2.right_stick_x) >= Robot5100Settings.TeleOP_GamepadTriggerValue){
            gamepad2ControllingMotion = true;
            controlValue = this.gamepad2.right_stick_x;
            controllingTurning = true;
            isControlMotion = true;
        }
        if(!gamepad2ControllingMotion){
            double leftStickBiggestNum = Math.max(Math.abs(gamepad1.left_stick_x),Math.abs(gamepad1.left_stick_y));
            if(leftStickBiggestNum >= Robot5100Settings.TeleOP_GamepadTriggerValue || Math.abs(gamepad1.right_stick_x) >= Robot5100Settings.TeleOP_GamepadTriggerValue) {
                isControlMotion = true;
                if (Math.abs(gamepad1.right_stick_x) >= Math.abs(leftStickBiggestNum)) {
                    controllingTurning = true;
                    controlValue = gamepad1.right_stick_x;
                } else {
                    if(Math.abs(gamepad1.left_stick_y) >= Math.abs(gamepad1.left_stick_x)){
                        isControllingY = true;
                        controlValue = -gamepad1.left_stick_y;
                    }else{
                        isControllingY = false;
                        controlValue = gamepad1.left_stick_x;
                    }
                }
            }
        }
        if(isControlMotion){
            if(!controllingTurning) {
                if (isControllingY) {
                    this.m_RobotCore.getMotionSystem().driveForwardWithSpeed(controlValue * Robot5100Settings.TeleOP_BiggestSpeed);
                } else {
                    this.m_RobotCore.getMotionSystem().driveToRightWithSpeed(controlValue * Robot5100Settings.TeleOP_BiggestSpeed);
                }
            }else{
                this.m_RobotCore.getMotionSystem().keepTurningOffsetAroundCenter(controlValue * Robot5100Settings.TeleOP_BiggestSpeed);
            }
        }else{
            this.m_RobotCore.getMotionSystem().stopMoving();
        }
    }
    @Override
    public void runOpMode() {
        this.hardWareInitialize();
        this.waitForStart();
        while(this.opModeIsActive()){
            this.Gamepad1Control();
            this.Gamepad2Control();
            this.potentialConflictControl();
            this.m_RobotCore.doLoop();
        }
    }
}
