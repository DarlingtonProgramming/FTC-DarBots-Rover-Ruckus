package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision1A;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;

@TeleOp (name = "5100TeleOp", group = "David Cao")
//@Disabled
public class Robot5100TeleOp extends LinearOpMode{
    private static final double TRIGGERVALUE = 0.1;
    private Robot5100Core m_RobotController;

    private void hardwareInitialize(){
        m_RobotController = new Robot5100Core(this,100,100,0,0,0);
    }

    private void dumpingServoControl(){
        if(gamepad1.right_bumper){
            this.m_RobotController.getDumperServo().setPosition(this.m_RobotController.getDumperServo().getPosition() + 0.05);
        }else if(gamepad1.left_bumper){
            this.m_RobotController.getDumperServo().setPosition(this.m_RobotController.getDumperServo().getPosition() - 0.05);
        }
    }

    private void collectorServoControl(){
        if(gamepad1.dpad_left){
            this.m_RobotController.getCollectorServo().setPosition(this.m_RobotController.getCollectorServo().getPosition() + 0.02,1.0);
        }else if(gamepad1.dpad_right){
            this.m_RobotController.getCollectorServo().setPosition(this.m_RobotController.getCollectorServo().getPosition() - 0.02,1.0);
        }
    }

    private void collectorControl(){
        boolean leftTrigger = false, rightTrigger = false;
        if(gamepad1.left_trigger < TRIGGERVALUE){
            leftTrigger = false;
        }else{
            leftTrigger = true;
        }
        if(gamepad1.right_trigger < TRIGGERVALUE){
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
        if(Math.abs(gamepad1.right_stick_x) < TRIGGERVALUE){
            isRotating = false;
        }else{
            isRotating = true;
        }
        if(Math.max(Math.abs(gamepad1.left_stick_x),Math.abs(gamepad1.left_stick_y)) < TRIGGERVALUE){
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
            double newPct = this.m_RobotController.getRackAndPinion().getPosition() + 0.1;
            this.m_RobotController.getRackAndPinion().setPosition(newPct);
        }else if(gamepad1.dpad_down){
            double newPct = this.m_RobotController.getRackAndPinion().getPosition() - 0.1;
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
