package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision1A;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotNonBlockingServoUsingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingNoEncoderMotor;

@TeleOp (name = "5100TeleOp", group = "David Cao")
public class Robot5100TeleOp extends LinearOpMode{
    private Robot5100MotionSystem m_MotionSystem;
    private RobotPositionTracker m_PositionTracker;
    private Robot5100RackAndPinion m_RackAndPinion;
    private Servo m_DumperServo;
    private RobotNonBlockingNoEncoderMotor m_CollectorMotor;
    private RobotNonBlockingServoUsingMotor m_CollectorServo;

    private void hardwareInitialize(){
        //FIELD: 365.76 * 365.76 CM^2
        //Change the initialX and initialY
        double[] leftTopExtreme = {-16.34, 16.34};
        double[] rightTopExtreme = {16.34, 16.34};
        double[] leftBotExtreme = {-16.34,-16.34};
        double[] rightBotExtreme = {16.34,-16.34};
        this.m_PositionTracker = new RobotPositionTracker(365.76,365.76,100,100,0,leftTopExtreme,rightTopExtreme,leftBotExtreme,rightBotExtreme);
        this.m_MotionSystem = new Robot5100MotionSystem(this.m_PositionTracker,this.hardwareMap.dcMotor.get("motor0"), this.hardwareMap.dcMotor.get("motor1"),this.hardwareMap.dcMotor.get("motor2"), this.hardwareMap.dcMotor.get("motor3"));
        DcMotor rackAndPinionMotor = hardwareMap.dcMotor.get("rackMotor");
        this.m_RackAndPinion = new Robot5100RackAndPinion(rackAndPinionMotor,0);
        this.m_DumperServo = hardwareMap.servo.get("dumperServo");
        //this.m_CollectorMotor = new RobotNonBlockingNoEncoderMotor(hardwareMap.dcMotor.get("collectorMotor"),288,2.08,false);
        //this.m_CollectorServo = new RobotNonBlockingServoUsingMotor(hardwareMap.dcMotor.get("collectorServo"),288, 0.0);
    }

    private void dumpingServoControl(){
        if(gamepad1.left_bumper){
            this.m_DumperServo.setPosition(this.m_DumperServo.getPosition() - 0.05);
        }else if(gamepad1.right_bumper){
            this.m_DumperServo.setPosition(this.m_DumperServo.getPosition() + 0.05);
        }
    }

    private void collectorServoControl(){
        if(gamepad1.dpad_left){
            this.m_CollectorServo.setPosition(this.m_CollectorServo.getPosition() - 0.05);
        }else if(gamepad1.dpad_right){
            this.m_CollectorServo.setPosition(this.m_CollectorServo.getPosition() + 0.05);
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
            this.m_CollectorMotor.moveWithFixedSpeed(1.0);
        }else if(rightTrigger){
            this.m_CollectorMotor.moveWithFixedSpeed(-1.0);
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

        if(this.m_MotionSystem.isDrivingInDirectionWithSpeed() && !isDriving){
            this.m_MotionSystem.stopDrivingWithSpeed();
        }
        if(this.m_MotionSystem.isKeepingTurningOffsetAroundCenter() && !isRotating){
            this.m_MotionSystem.stopTurningOffsetAroundCenter();
        }
        if(isRotating){
            this.m_MotionSystem.keepTurningOffsetAroundCenter(gamepad1.right_stick_x);
        }else{
            if(isControllingX){
                this.m_MotionSystem.driveToRightWithSpeed(gamepad1.left_stick_x);
            }else{
                this.m_MotionSystem.driveForwardWithSpeed(-gamepad1.left_stick_y);
            }
        }
    }

    private void rackAndPinionControl(){
        if(this.m_RackAndPinion.isBusy()){
            return;
        }
        if(gamepad1.dpad_up){
            double newPct = this.m_RackAndPinion.getCurrentPercent() + 5;
            if(newPct > 100){
                newPct = 100;
            }
            this.m_RackAndPinion.setPosition(newPct);
        }else if(gamepad1.dpad_down){
            double newPct = this.m_RackAndPinion.getCurrentPercent() - 5;
            if(newPct < 0){
                newPct = 0;
            }
            this.m_RackAndPinion.setPosition(newPct);
        }
    }

    public void runOpMode(){
        RobotDebugger.setTelemetry(this.telemetry);
        RobotDebugger.setDebug(false);
        this.hardwareInitialize();
        waitForStart();
        while(this.opModeIsActive()){
            movementControl();
            rackAndPinionControl();
            dumpingServoControl();
            //collectorServoControl();
            //collectorControl();
            this.m_MotionSystem.doLoop();
            this.m_RackAndPinion.doLoop();
            RobotDebugger.doLoop();
        }
    }
}
