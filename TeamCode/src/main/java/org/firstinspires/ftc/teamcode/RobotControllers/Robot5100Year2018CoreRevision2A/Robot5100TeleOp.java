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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;

@TeleOp (name = "Robot5100TeleOp",group = "5100")
public class Robot5100TeleOp extends LinearOpMode {
    private Robot5100Core m_RobotCore;

    public void hardWareInitialize(){
        this.m_RobotCore = new Robot5100Core(this,100,100,0,Robot5100Settings.rackAndPinionInitialPos,Robot5100Settings.dumperInitialPos,Robot5100Settings.linearReachInitialPos,Robot5100Settings.collectorServoInitialPos,true);
    }

    protected void dumperControl(){
        if(gamepad1.right_bumper){
            this.m_RobotCore.getDumper().setPosition(this.m_RobotCore.getDumper().getPosition() + 0.1);
        }else if(gamepad1.left_bumper){
            this.m_RobotCore.getDumper().setPosition(this.m_RobotCore.getDumper().getPosition() - 0.1);
        }
    }

    protected void linearReachControl(){
        if(gamepad1.right_trigger >= Robot5100Settings.TeleOP_GamepadTriggerValue){
            this.m_RobotCore.getLinearReach().setPosition(this.m_RobotCore.getLinearReach().getPosition() + 0.1);
        }else if(gamepad1.left_trigger >= Robot5100Settings.TeleOP_GamepadTriggerValue){
            this.m_RobotCore.getLinearReach().setPosition(this.m_RobotCore.getLinearReach().getPosition() - 0.1);
        }
    }

    protected void collectorServoControl(){
        if(gamepad1.dpad_right){
            this.m_RobotCore.getCollectorServo().setPosition(this.m_RobotCore.getCollectorServo().getPosition() + 0.1,Robot5100Settings.collectorServoSpeed);
        }else if(gamepad1.dpad_left){
            this.m_RobotCore.getCollectorServo().setPosition(this.m_RobotCore.getCollectorServo().getPosition() - 0.1,Robot5100Settings.collectorServoSpeed);
        }
    }

    protected void mineralSuckingControl(){
        if(gamepad1.a){
            this.m_RobotCore.startSuckingMinerals();
        }else if(gamepad1.b){
            this.m_RobotCore.startVomitingMinerals();
        }else{
            this.m_RobotCore.closeMouth();
        }
    }

    protected void movementControl(){
        boolean isMoving = false;
        boolean isRotating = false;
        boolean isControllingX = false;
        if(Math.abs(gamepad1.left_stick_x) >= Robot5100Settings.TeleOP_GamepadTriggerValue || Math.abs(gamepad1.left_stick_y) >= Robot5100Settings.TeleOP_GamepadTriggerValue){
            isMoving = true;
            if(Math.abs(gamepad1.left_stick_x) > Math.abs(gamepad1.left_stick_y)){
                isControllingX = true;
            }else{
                isControllingX = false;
            }
        }else{
            isMoving = false;
        }
        if(Math.abs(gamepad1.right_stick_x) >= Robot5100Settings.TeleOP_GamepadTriggerValue){
            isRotating = true;
        }
        if(!isMoving && !isRotating){
            if(this.m_RobotCore.getMotionSystem().getCurrentMotionType() != RobotMotionSystem.motionType.stopped){
                this.m_RobotCore.getMotionSystem().stopMoving();
            }
        }
        if(isMoving && !isRotating){
            if(isControllingX){
                this.m_RobotCore.getMotionSystem().driveToRightWithSpeed(gamepad1.left_stick_x * Robot5100Settings.TeleOP_BiggestSpeed);
            }else{
                this.m_RobotCore.getMotionSystem().driveForwardWithSpeed(-gamepad1.left_stick_y * Robot5100Settings.TeleOP_BiggestSpeed);
            }
        }else if(!isMoving && isRotating){
            this.m_RobotCore.getMotionSystem().keepTurningOffsetAroundCenter(gamepad1.right_stick_x * Robot5100Settings.TeleOP_BiggestSpeed);
        }
    }

    protected void rackAndPinionControl(){
        if(gamepad1.dpad_up){
            this.m_RobotCore.getRackAndPinion().setPosition(this.m_RobotCore.getRackAndPinion().getPosition() + 0.1);
        }else{
            this.m_RobotCore.getRackAndPinion().setPosition(this.m_RobotCore.getRackAndPinion().getPosition() - 0.1);
        }
    }

    @Override
    public void runOpMode() {
        this.hardWareInitialize();
        this.waitForStart();
        while(this.opModeIsActive()){
            this.movementControl();
            this.rackAndPinionControl();
            this.dumperControl();
            this.linearReachControl();
            this.collectorServoControl();
            this.mineralSuckingControl();
            this.m_RobotCore.doLoop();
        }
    }
}
