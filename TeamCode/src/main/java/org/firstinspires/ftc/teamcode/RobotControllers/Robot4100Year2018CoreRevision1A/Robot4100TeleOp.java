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

import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by iwu on 10/10/18.
 */

//@Disabled
@TeleOp(name = "4100TeleOp", group = "4100")

public class Robot4100TeleOp extends OpMode {

    DcMotor frontleft, frontright, backleft, backright, lift1, lift2, collector, dump;
    Servo  teammark;

    public float x, y, z, w, pwr;
    public static double deadzone = 0.15;

    @Override
    public void init() {
        frontleft = hardwareMap.dcMotor.get("FL");
        frontright = hardwareMap.dcMotor.get("FR");
        backleft = hardwareMap.dcMotor.get("BL");
        backright = hardwareMap.dcMotor.get("BR");

        lift1 = hardwareMap.dcMotor.get("l1");
        lift2 = hardwareMap.dcMotor.get("l2");
        collector = hardwareMap.dcMotor.get("C");
//        extendo = hardwareMap.dcMotor.get("E");

        teammark = hardwareMap.servo.get("TM");

        dump = hardwareMap.dcMotor.get("D");

        frontright.setDirection(DcMotor.Direction.REVERSE);
        backright.setDirection(DcMotor.Direction.REVERSE);
    }



    @Override
    public void loop() {
        getJoyVals();

        teammark.setPosition(0);
        pwr = y;

        if (gamepad1.right_bumper||gamepad1.b) {
            frontright.setPower(Range.clip(pwr +x + z, -0.4, 0.4));
            backleft.setPower(Range.clip(pwr + x - z, -0.4, 0.4));
            frontleft.setPower(Range.clip(pwr - x - z, -0.4, 0.4));
            backright.setPower(Range.clip(pwr - x + z, -0.4, 0.4));
        } else {
            frontright.setPower(Range.clip(pwr + x + z, -1, 1));
            backleft.setPower(Range.clip(pwr + x - z, -1, 1));
            frontleft.setPower(Range.clip(pwr - x - z, -1, 1));
            backright.setPower(Range.clip(pwr - x + z, -1, 1));
        }


        if (gamepad2.right_trigger>0) {
            collector.setPower(gamepad2.right_trigger);
        }
        else if (gamepad2.left_trigger>0){
            collector.setPower(-gamepad2.left_trigger);
        }
        else
            collector.setPower(0);



//        if(gamepad2.a){
//            extendo.setPower(.5);
//        }
//        else if(gamepad2.y){
//            extendo.setPower(-.4);
//        }
//        else
//            extendo.setPower(0);



        if (gamepad2.right_bumper){
            dump.setPower(0.3);
        }
        else if (gamepad2.left_bumper){
            dump.setPower(-0.3);
        }
        else
            dump.setPower(0);




        if (Math.abs(gamepad1.right_trigger)>deadzone){
            lift2.setPower(gamepad1.right_trigger);
        }
        else if (Math.abs(gamepad1.left_trigger)>deadzone){
            lift2.setPower(-gamepad1.left_trigger);
        }
        else
            lift2.setPower(0);



//        if(Math.abs(gamepad2.left_stick_y) > deadzone){
        if(gamepad2.dpad_left){
            lift1.setPower(0.8);
        }
        else if(gamepad2.dpad_right){
            lift1.setPower(-1.0);
        }
        else
            lift1.setPower(0);
    }


//    }



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