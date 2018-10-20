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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100Year2018CoreRevision1A;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by iwu on 10/10/18.
 */

@Autonomous (name = "4100AutonomousPos1", group = "4100")
//@Disabled

public class Robot4100AutonomousPos1 extends LinearOpMode {

    private DcMotor LeftFront, RightFront, LeftBack, RightBack, lift1, lift2;
    private DcMotor collector, linearSlide, hang, SMotor;
    //    private CRServo dumping;
    private Servo teammarking, dump;

//    public RobotNonBlockingMotor m;


    static final double     COUNTS_PER_MOTOR_REV    = 1150 ;
    static final double     Motor_GEAR_REDUCTION    = 0.5 ;
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * Motor_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    //    static final double     Motor_SPEED             = 0.3;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() {
        hardwareInitialize();

        ElapsedTime runtime = new ElapsedTime();
        waitForStart();

        runtime.reset();

        while (opModeIsActive()) {

            teammarking.setPosition(0);



//

            descend();


            sleep(300);
            lift2.setPower(-0.3);
            sleep(500);
            lift2.setPower(0);


            collector.setPower(0.5);
            encoderMotor(0.5,-70,-70,5000);
//            sleep(1500);
            collector.setPower(0);
            lift2.setPower(0.8);
            sleep(700);
            lift2.setPower(0);


            encoderMotor(TURN_SPEED,25,-25,3000);
            LeftFront.setPower(0);
            LeftBack.setPower(0);
            RightFront.setPower(0);
            RightBack.setPower(0);
            sleep(500);

            teammarking.setPosition(0.6);
            sleep(1000);
            teammarking.setPosition(0);
            sleep(300);

            encoderMotor(TURN_SPEED,44.5,-44.5,5000);

            encoderMotor(0.5,-85,-85,5000);
            encoderMotor(TURN_SPEED, 10,-10,1000);
            encoderMotor(0.5,-10,-10,1000);
            sleep(500);


            lift2.setPower(-0.3);
            sleep(1500);
            lift2.setPower(0);


            lift1.setPower(-0.3);
            sleep(2500);
            lift1.setPower(0);

            telemetry.update();

            stop();
        }
    }


    void hardwareInitialize(){
        LeftFront = hardwareMap.dcMotor.get("FL");
        RightFront = hardwareMap.dcMotor.get("FR");
        LeftBack = hardwareMap.dcMotor.get("BL");
        RightBack = hardwareMap.dcMotor.get("BR");

        lift1 = hardwareMap.dcMotor.get("l1");
        lift2 = hardwareMap.dcMotor.get("l2");
        collector = hardwareMap.dcMotor.get("C");
//        extendo = hardwareMap.dcMotor.get("E");

        teammarking = hardwareMap.servo.get("TM");

//        dump = hardwareMap.servo.get("D");


        RightFront.setDirection(DcMotor.Direction.REVERSE);
        RightBack.setDirection(DcMotor.Direction.REVERSE);


        LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        LeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }


    public void descend(){
        lift1.setPower(0.3);
        sleep(3500);
        lift1.setPower(0);
        sleep(1000);
        movement(0.3,500,"left");
        lift1.setPower(-0.5);
        sleep(1200);
        lift1.setPower(0);
        LeftFront.setPower(0);
        LeftBack.setPower(0);
        RightFront.setPower(0);
        RightBack.setPower(0);
        sleep(500);
        movement(0.3,500,"right");
    }


    public void collect(){

    }
    public void movement(double pwr, int timeoutS, String direction) {
        ElapsedTime runtime = new ElapsedTime();
        switch (direction) {
            case "forward":
                LeftFront.setPower(pwr);
                LeftBack.setPower(pwr);
                RightFront.setPower(pwr);
                RightBack.setPower(pwr);
                break;
            case "backward":
                LeftFront.setPower(-pwr);
                LeftBack.setPower(-pwr);
                RightFront.setPower(-pwr);
                RightBack.setPower(-pwr);
                break;
            case "right":
                LeftFront.setPower(pwr);
                LeftBack.setPower(-pwr);
                RightFront.setPower(-pwr);
                RightBack.setPower(pwr);
                break;
            case "left":
                LeftFront.setPower(-pwr);
                LeftBack.setPower(pwr);
                RightFront.setPower(pwr);
                RightBack.setPower(-pwr);
                break;
            case "turnLeft":
                LeftFront.setPower(-pwr);
                LeftBack.setPower(-pwr);
                RightFront.setPower(-pwr);
                RightBack.setPower(-pwr);
                break;
        }

        sleep(timeoutS);
        LeftFront.setPower(0);
        LeftBack.setPower(0);
        RightFront.setPower(0);
        RightBack.setPower(0);


    }

    public void encoderMotor(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget, newRightTarget;
        ElapsedTime runtime = new ElapsedTime();

        if (opModeIsActive()) {

            newLeftTarget = LeftBack.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = RightBack.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

            LeftBack.setTargetPosition(newLeftTarget);
            LeftFront.setTargetPosition(newLeftTarget);
            RightBack.setTargetPosition(newRightTarget);
            RightFront.setTargetPosition(newRightTarget);

            LeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();

            LeftFront.setPower(Math.abs(speed));
            RightBack.setPower(Math.abs(speed));
            LeftBack.setPower(Math.abs(speed));
            RightFront.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (LeftBack.isBusy()&& RightBack.isBusy()&&LeftFront.isBusy()&&RightFront.isBusy())) {
            }

            telemetry.addData("Path2",  "Running at " + LeftBack.getCurrentPosition() + " " + RightBack.getCurrentPosition() + " " + LeftFront.getCurrentPosition() + " " + RightFront.getCurrentPosition());
            telemetry.update();

            LeftFront.setPower(0);
            LeftBack.setPower(0);
            RightFront.setPower(0);
            RightBack.setPower(0);


            LeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }
}