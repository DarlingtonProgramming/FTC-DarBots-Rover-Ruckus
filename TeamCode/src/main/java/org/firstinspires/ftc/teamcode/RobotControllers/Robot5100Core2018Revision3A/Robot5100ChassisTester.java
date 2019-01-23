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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;

@TeleOp(name = "Robot5100ChassisTester", group = "Test")
public class Robot5100ChassisTester extends LinearOpMode {
    protected Robot5100Core m_RobotCore;
    protected String m_Path = "";
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
    protected void movementControl() {
        boolean isMoving = false;
        boolean isControllingX = false;
        boolean isTurning = false;
        double controlValue = 0;
        double gamepad1MaxCtl = Math.max(Math.abs(gamepad1.left_stick_x), Math.max(Math.abs(gamepad1.left_stick_y), Math.abs(gamepad1.right_stick_x)));
        if(gamepad1MaxCtl > Robot5100Setting.TELEOP_GAMEPADTRIGGERVALUE) {
            //Controlling Gamepad1
            if (Math.abs(gamepad1.right_stick_x) >= gamepad1MaxCtl) {
                isMoving = true;
                isTurning = true;
                controlValue = gamepad1.right_stick_x;
            } else {
                //Controlling left stick(X&Y Axis)
                if (Math.abs(gamepad1.left_stick_x) >= Math.abs(gamepad1.left_stick_y)) {
                    //Controlling Left&Right Motion in RobotAxis
                    isMoving = true;
                    isControllingX = true;
                    controlValue = gamepad1.left_stick_x;
                } else {
                    //Controlling Front & Back Motion in RobotAxis
                    isMoving = true;
                    isControllingX = false;
                    controlValue = -gamepad1.left_stick_y; //Since the gamepad Y is negative when pushed up, we need to reverse the sign of y value to get a positive Y for pushing up the stick
                }
            }
        }
        if (isMoving) {
            controlValue *= Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED;
            if (isTurning) {
                if(controlValue > 0){
                    this.m_RobotCore.getMotionSystem().turnOffsetAroundCenter(10,Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
                    this.m_Path += "turn(10);";
                }else{
                    this.m_RobotCore.getMotionSystem().turnOffsetAroundCenter(-10,Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
                    this.m_Path += "turn(-10);";
                }
            } else {
                if (isControllingX) {
                    if(controlValue > 0){
                        this.m_RobotCore.getMotionSystem().driveToRight(10,Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
                        this.m_Path += "driveToRight(10);";
                    }else{
                        this.m_RobotCore.getMotionSystem().driveToLeft(10,Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
                        this.m_Path += "driveToRight(-10);";
                    }
                } else {
                    if(controlValue > 0){
                        this.m_RobotCore.getMotionSystem().driveForward(10,Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
                        this.m_Path += "driveForward(10);";
                    }else{
                        this.m_RobotCore.getMotionSystem().driveBackward(10,Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
                        this.m_Path += "driveForward(-10);";
                    }
                }
            }
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
        }else{
            this.m_RobotCore.getMotionSystem().stopMoving();
        }
    }
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        this.waitForStart();
        while(this.opModeIsActive()){
            if(this.gamepad1.y){
                if(!this.m_RobotCore.getLinearActuator().isBusy()) {
                    this.m_RobotCore.getLinearActuator().setTargetPosition(this.m_RobotCore.getLinearActuator().getBiggestPos(), Robot5100Setting.TELEOP_LINEARACTUATORSPEED);
                }
            }else if(this.gamepad1.x) {
                if (!this.m_RobotCore.getLinearActuator().isBusy()) {
                    this.m_RobotCore.getLinearActuator().setTargetPosition(this.m_RobotCore.getLinearActuator().getSmallestPos(), Robot5100Setting.TELEOP_LINEARACTUATORSPEED);
                }
            }else if(this.gamepad1.right_bumper){
                if(!this.m_RobotCore.getLinearActuator().isBusy()){
                    this.m_RobotCore.setLinearActuatorToHook(Robot5100Setting.TELEOP_LINEARACTUATORSPEED);
                }
            }else{
                this.m_RobotCore.getLinearActuator().stopMotion();
            }
            if(this.gamepad1.dpad_left){
                this.m_Path = "";
            }
            this.movementControl();
            if(this.gamepad2.y){
                if(!this.m_RobotCore.getArmReach().isBusy()){
                    this.m_RobotCore.getArmReach().setTargetPosition(this.m_RobotCore.getArmReach().getBiggestPos(),Robot5100Setting.TELEOP_ARMREACHSPEED);
                }
            }else if(this.gamepad2.x){
                if(!this.m_RobotCore.getArmReach().isBusy()){
                    this.m_RobotCore.getArmReach().setTargetPosition(this.m_RobotCore.getArmReach().getSmallestPos(),Robot5100Setting.TELEOP_ARMREACHSPEED);
                }
            }else{
                this.m_RobotCore.getArmReach().stopMotion();
            }
            if(this.gamepad2.dpad_right) {
                if (!this.m_RobotCore.getArm().isBusy()) {
                    this.m_RobotCore.getArm().setTargetPosition(this.m_RobotCore.getArm().getBiggestPos(), Robot5100Setting.TELEOP_ARMSPEED);
                }
            }else if(this.gamepad2.dpad_left){
                if(!this.m_RobotCore.getArm().isBusy()){
                    this.m_RobotCore.getArm().setTargetPosition(this.m_RobotCore.getArm().getSmallestPos(),Robot5100Setting.TELEOP_ARMSPEED);
                }
            }else{
                this.m_RobotCore.getArm().stopMotion();
            }
            if(this.gamepad2.left_bumper){
                this.m_RobotCore.startVomitingMineral();
            }else if(this.gamepad2.right_bumper){
                this.m_RobotCore.startSuckingMineral();
            }else{
                this.m_RobotCore.stopSuckingMineral();
            }
            RobotDebugger.addDebug("Path",this.m_Path);
            this.m_RobotCore.doLoop();
        }
        this.hardwareDestroy();
    }
}