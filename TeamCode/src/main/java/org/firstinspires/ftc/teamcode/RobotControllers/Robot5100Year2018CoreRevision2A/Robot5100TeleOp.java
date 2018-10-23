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

    private void movementControl(){
        boolean isMoving = false;
        boolean isRotating = false;
        boolean isControllingX = false;
        if(Math.abs(gamepad1.left_stick_x) >= Robot5100Settings.gamepadTriggerValue || Math.abs(gamepad2.left_stick_y) >= Robot5100Settings.gamepadTriggerValue){
            isMoving = true;
            if(Math.abs(gamepad1.left_stick_x) > Math.abs(gamepad1.left_stick_y)){
                isControllingX = true;
            }else{
                isControllingX = false;
            }
        }else{
            isMoving = false;
        }
        if(Math.abs(gamepad1.right_stick_x) >= Robot5100Settings.gamepadTriggerValue){
            isRotating = true;
        }
        if(!isMoving && !isRotating){
            if(this.m_RobotCore.getMotionSystem().getCurrentMotionType() != RobotMotionSystem.motionType.stopped){
                this.m_RobotCore.getMotionSystem().stopMoving();
            }
        }
        if(isMoving && !isRotating){
            if(isControllingX){
                this.m_RobotCore.getMotionSystem().driveToRightWithSpeed(gamepad1.left_stick_x);
            }else{
                this.m_RobotCore.getMotionSystem().driveForwardWithSpeed(-gamepad1.left_stick_y);
            }
        }else if(!isMoving && isRotating){
            this.m_RobotCore.getMotionSystem().keepTurningOffsetAroundCenter(gamepad1.right_stick_x);
        }
    }

    @Override
    public void runOpMode() {
        this.hardWareInitialize();
        this.waitForStart();
        while(this.opModeIsActive()){
            this.movementControl();
            this.m_RobotCore.doLoop();
        }
    }
}
