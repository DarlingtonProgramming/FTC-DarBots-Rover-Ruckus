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

package org.firstinspires.ftc.teamcode.RobotControllers.littleCar;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;

import java.lang.annotation.Target;

@TeleOp(name = "littleCarTeleOp", group = "littleCAR")
@Disabled
public class littleCar extends LinearOpMode{
    private DcMotor leftMotor, rightMotor;
    private DcMotor liftingMotor;
    private Servo armServo;
    protected float ControlX, ControlY;
    protected boolean isLiftingUp = false, isLiftingDown = false;
    protected boolean isTightingGrab = false, isLoseningGrab = false;
    private static final float LeftMotorFactor = -1.0f;
    private static final float RightMotorFactor = 1.0f;
    private static final float ChangeDirectionFactor = 0.5f;
    private static final float LiftingMotorFactor = -1.0f;
    private static final float LiftingMotorChangeVal = 0.05f;
    private static final float LiftingMotorSpeed = 0.1f;
    private static final float LiftingMotorTimeout = 3.0f;
    private static final float ArmServoFactor = -1.0f;
    private static final double ArmServoChangeVal = 0.001;
    private static final double COUNTS_PER_MOTOR_REV = 865 ;

    protected void hardwareInitialize(){
        this.leftMotor = hardwareMap.dcMotor.get("leftMotor");
        this.rightMotor = hardwareMap.dcMotor.get("rightMotor");
        this.liftingMotor = hardwareMap.dcMotor.get("liftingMotor");
        this.leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.liftingMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.liftingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.armServo = hardwareMap.servo.get("armServo");
    }

    protected void readJoyStickValues(){
        this.ControlX = gamepad1.left_stick_x;
        this.ControlY = gamepad1.left_stick_y;
        this.isLiftingUp = gamepad1.dpad_down;
        this.isLiftingDown = gamepad1.dpad_up;
        this.isTightingGrab = gamepad1.right_bumper;
        this.isLoseningGrab = gamepad1.left_bumper;
    }

    protected void setArmServoPosition(){
        if((!this.isLoseningGrab && !this.isTightingGrab) || (this.isLoseningGrab && this.isTightingGrab)){
            return;
        }
        double newPosition = armServo.getPosition();
        if(this.isLoseningGrab){
            newPosition += (ArmServoChangeVal * ArmServoFactor);
        }else{
            newPosition -= (ArmServoChangeVal * ArmServoFactor);
        }
        newPosition = Range.clip(newPosition,-1.0,1.0);
        this.armServo.setPosition(newPosition);

    }

    protected void setLiftingMotorPower(){
        if((!this.isLiftingUp && !this.isLiftingDown) || (this.isLiftingUp && this.isLiftingDown)){
            return;
        }
        int TargetDirection = this.liftingMotor.getCurrentPosition();
        if(this.isLiftingUp) {
             TargetDirection += (int) ((LiftingMotorChangeVal * COUNTS_PER_MOTOR_REV) * LiftingMotorFactor);
        }else{
            TargetDirection -= (int) ((LiftingMotorChangeVal * COUNTS_PER_MOTOR_REV) * LiftingMotorFactor);
        }

        this.liftingMotor.setTargetPosition(TargetDirection);
        this.liftingMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.liftingMotor.setPower(LiftingMotorSpeed);
        double startTime = getRuntime();
        while(this.liftingMotor.isBusy()){
            if(getRuntime() - startTime >= LiftingMotorTimeout){
                break;
            }
        }
        this.liftingMotor.setPower(0);
        this.liftingMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    protected void setMotorPower(){
        float powerX = Range.clip(ControlX, -1.0f, 1.0f);
        float powerY = Range.clip(ControlY, -1.0f, 1.0f);

        float leftMotorPower = powerY;
        float rightMotorPower = powerY;
        if(powerX < 0){ //Turn Left
            rightMotorPower -= (-powerX) * this.ChangeDirectionFactor;
            leftMotorPower += (-powerX) * this.ChangeDirectionFactor;
        }else{ //Turn Right
            leftMotorPower -= powerX * this.ChangeDirectionFactor;
            rightMotorPower += powerX * this.ChangeDirectionFactor;
        }
        leftMotorPower *= LeftMotorFactor;
        rightMotorPower *= RightMotorFactor;
        leftMotorPower = Range.clip(leftMotorPower,-1.0f,1.0f);
        rightMotorPower = Range.clip(rightMotorPower,-1.0f,1.0f);
        this.leftMotor.setPower(leftMotorPower);
        this.rightMotor.setPower(rightMotorPower);
    }

    @Override
    public void runOpMode(){
        this.hardwareInitialize();
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        telemetry.addData("Status","Started");
        telemetry.update();

        while(opModeIsActive()){
            this.readJoyStickValues();
            this.setMotorPower();
            this.setLiftingMotorPower();
            this.setArmServoPosition();
        }

        telemetry.addData("Status","Stopped");
        telemetry.update();
    }
}
