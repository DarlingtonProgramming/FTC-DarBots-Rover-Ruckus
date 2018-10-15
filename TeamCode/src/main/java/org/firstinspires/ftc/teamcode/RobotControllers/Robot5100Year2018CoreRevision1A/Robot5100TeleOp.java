package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision1A;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;

@TeleOp (name = "5100TeleOp", group = "David Cao")
//@Disabled
public class Robot5100TeleOp extends LinearOpMode{
    private Robot5100Core m_RobotController;

    private void hardwareInitialize(){
        m_RobotController = new Robot5100Core(this,100,100,0,0,0);
    }

    private void dumpingServoControl(){
        if(gamepad1.right_bumper){
            this.m_RobotController.dump();
        }
    }

    private void collectorServoControl(){
        if(gamepad1.dpad_left){
            this.m_RobotController.getCollectorServo().setPosition(this.m_RobotController.getCollectorServo().getPosition() + 0.05);
        }else if(gamepad1.dpad_right){
            this.m_RobotController.getCollectorServo().setPosition(this.m_RobotController.getCollectorServo().getPosition() - 0.05);
        }
    }

    private void collectorControl(){
        boolean leftTrigger = false, rightTrigger = false;
        if(gamepad1.left_trigger < 0.05){
            leftTrigger = false;
        }else{
            leftTrigger = true;
        }
        if(gamepad1.right_trigger < 0.05){
            rightTrigger = false;
        }else{
            rightTrigger = true;
        }
        if(leftTrigger){
            this.m_RobotController.startVomitingMinerals();
        }else if(rightTrigger){
            this.m_RobotController.startSuckingMinerals();
        }else{
            this.m_RobotController.stopSuckingMinerals();
        }
    }

    private void movementControl(){
        boolean isControllingX = false;
        boolean isRotating = false;
        boolean isDriving = false;

        if(Math.abs(gamepad1.left_stick_x) > Math.abs(gamepad1.left_stick_y)){
            isControllingX = true;
        }else{
            isControllingX = false;
        }
        if(Math.abs(gamepad1.right_stick_x) < 0.05){
            isRotating = false;
        }else{
            isRotating = true;
        }
        if(Math.max(Math.abs(gamepad1.left_stick_x),Math.abs(gamepad1.left_stick_y)) < 0.05){
            isDriving = false;
        }else{
            isDriving = true;
        }

        if(this.m_RobotController.isDrivingInDirectionWithSpeed() && !isDriving){
            this.m_RobotController.stopDrivingWithSpeed();
        }
        if(this.m_RobotController.isKeepingTurningOffsetAroundCenter() && !isRotating){
            this.m_RobotController.stopTurningOffsetAroundCenter();
        }
        if(isRotating){
            this.m_RobotController.keepTurningOffsetAroundCenter(gamepad1.right_stick_x);
        }else{
            if(isControllingX){
                this.m_RobotController.driveToRightWithSpeed(gamepad1.left_stick_x);
            }else{
                this.m_RobotController.driveForwardWithSpeed(-gamepad1.left_stick_y);
            }
        }
    }

    private void rackAndPinionControl(){
        if(this.m_RobotController.getRackAndPinion().isBusy()){
            return;
        }
        if(gamepad1.dpad_up){
            double newPct = this.m_RobotController.getRackAndPinion().getCurrentPercent() + 5;
            if(newPct > 100){
                newPct = 100;
            }
            this.m_RobotController.getRackAndPinion().setPosition(newPct);
        }else if(gamepad1.dpad_down){
            double newPct = this.m_RobotController.getRackAndPinion().getCurrentPercent() - 5;
            if(newPct < 0){
                newPct = 0;
            }
            this.m_RobotController.getRackAndPinion().setPosition(newPct);
        }
    }

    public void runOpMode(){
        RobotDebugger.setTelemetry(this.telemetry);
        RobotDebugger.setDebug(true);
        this.hardwareInitialize();
        waitForStart();
        while(this.opModeIsActive()){
            movementControl();
            rackAndPinionControl();
            dumpingServoControl();
            collectorServoControl();
            collectorControl();
            this.m_RobotController.doLoop();
            RobotDebugger.doLoop();
        }
    }
}
