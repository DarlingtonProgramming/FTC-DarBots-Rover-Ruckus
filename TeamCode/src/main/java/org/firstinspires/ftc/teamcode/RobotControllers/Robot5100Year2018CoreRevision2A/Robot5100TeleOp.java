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

@TeleOp (name = "Robot5100TeleOp",group = "David Cao")
public class Robot5100TeleOp extends LinearOpMode {
    private Robot5100Core m_RobotCore;

    public void hardWareInitialize(){
        this.m_RobotCore = new Robot5100Core(this,100,100,0,true);
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
            this.m_RobotCore.getRackAndPinion().setPosition(this.m_RobotCore.getRackAndPinion().getPosition() + 0.05);
        }else{
            this.m_RobotCore.getRackAndPinion().setPosition(this.m_RobotCore.getRackAndPinion().getPosition() - 0.05);
        }
    }

    @Override
    public void runOpMode() {
        this.hardWareInitialize();
        this.waitForStart();
        while(this.opModeIsActive()){
            this.movementControl();
            this.rackAndPinionControl();
            this.m_RobotCore.doLoop();
        }
    }
}
