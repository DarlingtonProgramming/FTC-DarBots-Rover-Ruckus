package org.Andrew;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;

@TeleOp(name = "Mecanum TeleOp Test", group = "Linear OpMode")
public class TestSpining extends LinearOpMode {

    private DcMotor SP = null;
    private DcMotor LS = null;
    private ArrayList<Double[]> speedList = new ArrayList<Double[]>();
    private ElapsedTime runtime = new ElapsedTime();

    double rotate = 0;
    double speed = 0.5;
    boolean reverse = false;

    public TestSpining() {

    }

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //LF  = hardwareMap.get(DcMotor.class, "LF");
        //RF = hardwareMap.get(DcMotor.class, "RF");
        //LB  = hardwareMap.get(DcMotor.class, "LB");
        //RB = hardwareMap.get(DcMotor.class, "RB");

        SP = hardwareMap.get(DcMotor.class,"SP");
        LS = hardwareMap.get(DcMotor.class,"LS");
        //LF.setDirection(DcMotor.Direction.REVERSE);
        //RF.setDirection(DcMotor.Direction.FORWARD);
        //LB.setDirection(DcMotor.Direction.REVERSE);
        //RB.setDirection(DcMotor.Direction.FORWARD);

        double LFPower;
        double RFPower;
        double LBPower;
        double RBPower;
        double SPPower;
        double LSPower;


        waitForStart();

        boolean releasedRightBumper = true;
        boolean releasedLeftBumper = true;
        boolean releasedGamePad1 = true;
        boolean releasedA = true;

        boolean toggleGamePad1 = true;

        while (opModeIsActive()) {
            runtime.reset();

            double drive = -gamepad1.left_stick_y;
            double strafe  = -gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;

            if(gamepad1.right_bumper) {
                if(releasedRightBumper && releasedLeftBumper) {
                    increaseSpeed(0.05);
                    releasedRightBumper = false;
                }
            } else if(!releasedRightBumper){
                releasedRightBumper = true;
            }

            if(gamepad1.left_bumper){
                if(releasedRightBumper && releasedLeftBumper) {
                    decreaseSpeed(0.05);
                    releasedLeftBumper = false;
                }
            } else if (!releasedLeftBumper){
                releasedLeftBumper = true;
            }

            if(gamepad1.a){
                if(releasedA) {
                    decreaseSpeed(speed / 2.0);
                    releasedA = false;
                }

            } else if(!releasedA){
                increaseSpeed(speed);
                releasedA = true;
            }

            /*
            if (gamepad1.x) {
                if (releasedGamePad1){
                    if (toggleGamePad1) {
                        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                        telemetry.addLine("BREAK");
                        toggleGamePad1 = false;
                    } else {
                        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                        telemetry.addLine("FLOAT");
                        toggleGamePad1 = true;
                    }
                    releasedGamePad1 = false;
                }
            } else if (!releasedGamePad1){
                releasedGamePad1 = true;
            }*/
            SPPower  = Range.clip(gamepad1.left_trigger + speed*(drive), -1.0, 1.0) ;
            LSPower  = Range.clip(gamepad1.left_trigger + speed*(drive), -1.0, 1.0) ;


            Double currentSpeed[] = {SPPower};
            speedList.add(currentSpeed);

            SP.setPower(SPPower);

            telemetry.addData("Spining Motors", "SP (%.2f)", SPPower);
            telemetry.addData("Speed:", speed);


            telemetry.update();
        }
    }

    private void decreaseSpeed(double s) {
        double decreased = speed - s;
        if (decreased < 0) {
            speed = 0;
            return;
        }
        speed = decreased;
    }

    private void increaseSpeed(double s) {
        double increased = speed + s;
        if (1 < increased) {
            speed = 1;
            return;
        }
        speed = increased;
    }
}