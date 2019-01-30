package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100LeagueTournamentCode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//@Disabled
@TeleOp(name = "S8teleop", group = "4100")

public class S8_teleop extends OpMode {

    DcMotor frontleft, frontright, backleft, backright, slides, collector, dump, lift;
    Servo  bucket, teammark;
    TouchSensor maxSlide, minSlide;

    public float x, y, z, w, pwr;
    public static double deadzone = 0.15;
    public boolean slidePositive = true;

//    public motorToServo SlideM;

    @Override
    public void init() {
        frontleft = hardwareMap.dcMotor.get("FL");
        frontright = hardwareMap.dcMotor.get("FR");
        backleft = hardwareMap.dcMotor.get("BL");
        backright = hardwareMap.dcMotor.get("BR");


        lift = hardwareMap.dcMotor.get("lift");

        collector = hardwareMap.dcMotor.get("collector");
        slides = hardwareMap.dcMotor.get("slides");
        bucket = hardwareMap.servo.get("bucket");
        teammark = hardwareMap.servo.get("tm");

        dump = hardwareMap.dcMotor.get("dump");

        maxSlide = hardwareMap.touchSensor.get("maxSlide");
        minSlide = hardwareMap.touchSensor.get("minSlide");

        slides.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.REVERSE);
        backright.setDirection(DcMotor.Direction.REVERSE);
    }


    @Override
    public void loop() {
        getJoyVals();

        pwr = y;

        if (gamepad2.right_bumper) {
            if (maxSlide.isPressed() ){
                slides.setPower(0);
                telemetry.addLine("touchedMax");
                telemetry.update();
                slidePositive = false;
            }
            else slides.setPower(-.5);
        } else if (gamepad2.left_bumper ) {
            if (minSlide.isPressed()){
                slides.setPower(0);
                telemetry.addLine("touchedMin");
                telemetry.update();
            }
            else slides.setPower(.5);
        } else
            slides.setPower(0);


        if (gamepad1.right_bumper||gamepad1.b) {
            frontright.setPower(Range.clip(-pwr +x + 0.5*z, -0.4, 0.4));
            backleft.setPower(Range.clip(-pwr + x - 0.5*z, -0.4, 0.4));
            frontleft.setPower(Range.clip(-pwr - x - 0.5*z, -0.4, 0.4));
            backright.setPower(Range.clip(-pwr - x + 0.5*z, -0.4, 0.4));
        } else {
            frontright.setPower(Range.clip(-pwr +x + 0.5*z, -1, 1));
            backleft.setPower(Range.clip(-pwr + x - 0.5*z, -1, 1));
            frontleft.setPower(Range.clip(-pwr - x - 0.5*z, -1, 1));
            backright.setPower(Range.clip(-pwr - x + 0.5*z, -1, 1));
        }

        if (gamepad2.right_trigger > 0) {
            collector.setPower(gamepad2.right_trigger);
        } else if (gamepad2.left_trigger > 0) {
            collector.setPower(-gamepad2.left_trigger);
        } else
            collector.setPower(0);


        if (gamepad2.dpad_down){
            lift.setPower(-1);
        }
        else if (gamepad2.dpad_up){
            lift.setPower(1);
        }
        else
            lift.setPower(0);


        if (gamepad2.right_stick_y >deadzone) {
            bucket.setPosition(Math.abs((gamepad2.right_stick_y)));
        }
        else
            bucket.setPosition(0);
        teammark.setPosition(1);

        if (gamepad2.left_stick_y > deadzone) {
            dump.setPower(-0.5);
        }
        if (gamepad2.left_stick_y < -deadzone) {
            dump.setPower(0.5);
        } else
            dump.setPower(0);
    }

    public void motorToServo(double Counts_per_rev, DcMotor m, double speed,double counts){
        double initialPos = 0;
        double currentPos;
        double maxPos = counts * Counts_per_rev;

        currentPos = m.getCurrentPosition();
        int PosDistanceToGo = (int) (maxPos - currentPos);
        int NegDistanceToGo = (int) (currentPos - initialPos);

        telemetry.addData("distance",m.getCurrentPosition());
        telemetry.addData("pos", PosDistanceToGo);
        telemetry.update();

        if (-100 <= currentPos && currentPos < maxPos+100) {
            if (gamepad2.x) {
                m.setTargetPosition(0);
                m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                m.setPower(speed);
            } else if (gamepad2.b) {
                m.setTargetPosition((int)maxPos);
                m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                m.setPower(speed);
            }
            else{
                m.setPower(0);
                m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);}
        }
        else {
            m.setPower(0);
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }


    public void getJoyVals() {
        x = -gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;
        z = -gamepad1.right_stick_x;
        w = gamepad1.right_stick_y;


        if (Math.abs(x) < deadzone) x = 0;
        if (Math.abs(y) < deadzone) y = 0;
        if (Math.abs(z) < deadzone) z = 0;
        if (Math.abs(w) < deadzone) w = 0;

    }
}
