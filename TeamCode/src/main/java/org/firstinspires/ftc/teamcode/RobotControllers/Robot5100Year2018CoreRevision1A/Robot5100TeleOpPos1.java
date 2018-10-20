package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision1A;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;

@TeleOp (name = "5100TeleOpPos1", group = "David Cao")
//@Disabled
public class Robot5100TeleOpPos1 extends LinearOpMode{
    private static final double TRIGGERVALUE = 0.1;
    private Robot5100Core m_RobotController;
    private static final double MOVEMENTPROPORTION = 0.4;

    private void hardwareInitialize(){
        m_RobotController = new Robot5100Core(this,100,100,0,0,0,0);
    }

    private void dumpingServoControl(){
        if(gamepad2.x){
            this.m_RobotController.getDumperServo().setPosition(Servo.MIN_POSITION);
        }else if(gamepad2.a){
            this.m_RobotController.getDumperServo().setPosition(Servo.MAX_POSITION / 4.0);
            try {
                sleep(500);
            }catch(Exception e){

            }

            this.m_RobotController.getDumperServo().setPosition(Servo.MAX_POSITION / 3.0);
            try {
                sleep(500);
            }catch(Exception e){

            }
            this.m_RobotController.getDumperServo().setPosition(Servo.MAX_POSITION / 2.0);
            try {
                sleep(500);
            }catch(Exception e){

            }
            this.m_RobotController.getDumperServo().setPosition(Servo.MAX_POSITION);
        }
    }
    private void linearApproachControl(){
        boolean isControllingLinearApproach = false;
        if(Math.abs(gamepad2.left_stick_y) < TRIGGERVALUE && Math.abs(gamepad2.left_stick_y) > Math.abs(gamepad2.left_stick_x)){
            isControllingLinearApproach = false;
        }else{
            isControllingLinearApproach = true;
        }
        if(isControllingLinearApproach){
            this.m_RobotController.getLinearApproachMotor().setPosition(this.m_RobotController.getLinearApproachMotor().getPosition() + (0.2 * -gamepad2.left_stick_y),1.0);
        }
        if(gamepad2.y){
            this.m_RobotController.getLinearApproachMotor().adjustPosition(0);
        }
    }

    private void collectorServoControl(){
        boolean isControllingCollectorServo = false;
        if(Math.abs(gamepad2.right_stick_y) < TRIGGERVALUE){
            isControllingCollectorServo = false;
        }else{
            isControllingCollectorServo = true;
        }
        if(isControllingCollectorServo){
            this.m_RobotController.getCollectorServo().setPosition(this.m_RobotController.getCollectorServo().getPosition() + (0.2 * -gamepad2.right_stick_y),1.0);
        }
    }

    private void collectorControl(){
        boolean leftTrigger = false, rightTrigger = false;
        if(gamepad2.left_trigger < TRIGGERVALUE){
            leftTrigger = false;
        }else{
            leftTrigger = true;
        }
        if(gamepad2.right_trigger < TRIGGERVALUE){
            rightTrigger = false;
        }else{
            rightTrigger = true;
        }
        if(leftTrigger){
            this.m_RobotController.startVomitingMinerals(gamepad2.left_trigger);
        }else if(rightTrigger){
            this.m_RobotController.startSuckingMinerals(gamepad2.right_trigger);
        }else{
            this.m_RobotController.stopSuckingMinerals();
        }
    }

    private void movementControl(){
        boolean isControllingX = false;
        boolean isRotating = false;
        boolean isPad1Rotating = false;
        boolean isDriving = false;
        if(Math.abs(gamepad1.left_stick_x) > Math.abs(gamepad1.left_stick_y)){
            isControllingX = true;
        }else{
            isControllingX = false;
        }
        if(Math.abs(gamepad1.right_stick_x) < TRIGGERVALUE && (Math.abs(gamepad2.left_stick_x) < TRIGGERVALUE || Math.abs(gamepad2.left_stick_x) < Math.abs(gamepad2.left_stick_y))){
            isRotating = false;
        }else{
            if(Math.abs(gamepad1.right_stick_x) >= TRIGGERVALUE){
                isPad1Rotating = true;
            }else{
                isPad1Rotating = false;
            }
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
            if(isPad1Rotating) {
                this.m_RobotController.keepTurningOffsetAroundCenter(MOVEMENTPROPORTION * gamepad1.right_stick_x);
            }else{
                this.m_RobotController.keepTurningOffsetAroundCenter(MOVEMENTPROPORTION * gamepad2.left_stick_x * 0.3);
            }
        }else{
            if(isControllingX){
                this.m_RobotController.driveToRightWithSpeed(MOVEMENTPROPORTION * gamepad1.left_stick_x);
            }else{
                this.m_RobotController.driveForwardWithSpeed(MOVEMENTPROPORTION * (-gamepad1.left_stick_y));
            }
        }
    }

    private void rackAndPinionControl(){
        if(this.m_RobotController.getRackAndPinion().isBusy()){
            return;
        }
        boolean isLeftTrigger = false, isRightTrigger = false;
        if(gamepad1.left_trigger < TRIGGERVALUE){
            isLeftTrigger = false;
        }else{
            isLeftTrigger = true;
        }
        if(gamepad1.right_trigger < TRIGGERVALUE){
            isRightTrigger = false;
        }else{
            isRightTrigger = true;
        }

        if(isLeftTrigger){
            double newPct = this.m_RobotController.getRackAndPinion().getPosition() + (0.2 * gamepad1.left_trigger);
            this.m_RobotController.getRackAndPinion().setPosition(newPct);
        }else if(isRightTrigger){
            double newPct = this.m_RobotController.getRackAndPinion().getPosition() - (0.2 * gamepad1.right_trigger);
            this.m_RobotController.getRackAndPinion().setPosition(newPct);
        }
        if(gamepad1.left_bumper){
            this.m_RobotController.openRackAndPinion();
        }else if(gamepad1.right_bumper){
            this.m_RobotController.closeRackAndPinion();
        }
        if(gamepad1.a){
            this.m_RobotController.setRackAndPinionHook();
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
            linearApproachControl();
            this.m_RobotController.doLoop();
            RobotDebugger.doLoop();
        }
    }
}
