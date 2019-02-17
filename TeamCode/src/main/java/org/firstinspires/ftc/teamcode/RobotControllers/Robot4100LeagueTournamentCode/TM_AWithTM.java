package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100LeagueTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorREVColorDistance;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.RobotControllers.DarbotsPrivateInfo.PrivateSettings;

import java.util.List;

/**
 * Created by iwu on 4/10/18.
 */

@Autonomous (name = "TM_AWithTM", group = "4100")
@Disabled

public class TM_AWithTM extends LinearOpMode {

    DcMotor frontleft, frontright, backleft, backright, lift, slides, collector, dump;
    Servo  bucket, teammark;
    TouchSensor maxSlide, minSlide;
    private ElapsedTime     runtime = new ElapsedTime();


    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;
    static final double     Motor_GEAR_REDUCTION    = 0.5 ;
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * Motor_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    //    static final double     Motor_SPEED             = 0.3;
    static final double     TURN_SPEED              = 0.5;
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = PrivateSettings.VUFORIALICENSE;

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;
    private int key = -1;

    @Override
    public void runOpMode() {
        hardwareInitialize();
        initVuforia();
        bucket.setPosition(0.8);
        teammark.setPosition(0.6);

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();

        waitForStart();
        teammark.setPosition(0.8);
        bucket.setPosition(0.8);

        if (opModeIsActive() ) {
            if (tfod != null) {
                tfod.activate();
                runtime.reset();
            }

            while (opModeIsActive() && runtime.seconds()<2) {
                if (tfod != null) {

                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 2) {
                            int goldMineralX = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    goldMineralX = (int) recognition.getLeft();
                                } else if (silverMineral1X == -1) {
                                    silverMineral1X = (int) recognition.getLeft();
                                } else {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
                            }
                            if ((goldMineralX != -1 && silverMineral1X != -1) || (silverMineral2X != -1 && silverMineral1X != -1)) {
                                if (goldMineralX == -1 && silverMineral1X != -1) {
                                    key = 2;
                                    telemetry.addData("Gold Mineral Position", "Right" + key);
                                } else if ((goldMineralX != -1 && silverMineral1X !=-1)&&(goldMineralX < silverMineral1X)) {
                                    key = 0;
                                    telemetry.addData("Gold Mineral Position", "Left" + key);
                                } else {
                                    key = 1;
                                    telemetry.addData("Gold Mineral Position", "Center" + key);
                                }
                            }
                        }
                        telemetry.update();
                    }
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }

        WeakDescend();

        if (tfod != null) {
            tfod.shutdown();
        }

        weakChugou();
        sleep(500);


        switch (key) {
            case (0):
                telemetry.addLine("zuo");
                telemetry.update();
                encoderDrive(0.6,26,26,5);
                sleep(50);
                encoderDriveCe(0.3,-27,27,5);
                sleep(50);
                encoderDrive(0.5,47,47,5);
                sleep(50);
                encoderDrive(0.3,-5,-5,2);
                sleep(250);
                encoderDrive(0.3,-45,45,5);
                sleep(250);
                encoderDriveCe(0.5,15,-15,5);
                sleep(50);
                encoderDrive(0.5,-19,-19,2);
                teammark.setPosition(0);
                sleep(500);
                teammark.setPosition(0.8);
                encoderDrive(0.7,45,45,5);
                sleep(250);
                encoderDriveCe(0.3,7,-7,3);
                encoderDrive(0.7,50,50,5);
                bucket.setPosition(0);
                stop();

                break;
            case (1):
                telemetry.addLine("zhong");
                telemetry.update();
                encoderDrive(0.5,90,90,8);
                sleep(50);
                encoderDrive(0.5,-6,-6,3);
                sleep(50);
                encoderDrive(0.3, -41,41,5);
                sleep(250);
                encoderDriveCe(0.3,16,-16,3);
                sleep(50);
                encoderDrive(0.3,-7,-7,3);
                sleep(50);
                teammark.setPosition(0);
                sleep(500);
                teammark.setPosition(0.8);
                encoderDrive(0.7,103,103,5);
                bucket.setPosition(0);
                stop();

                break;
            case (2):
                telemetry.addLine("you");
                telemetry.update();
                encoderDrive(0.6,25,25,5);
                sleep(50);
                encoderDriveCe(0.3,22,-22,5);
                sleep(50);
                encoderDrive(0.5,47,47,5);
                sleep(50);
                encoderDrive(0.3,-5,-5,2);
                sleep(50);
                encoderDrive(0.3,-40,40,5);
                sleep(250);
                encoderDriveCe(0.5,55,-55,5);
                sleep(50);
                encoderDrive(0.5,-7,-7,2);
                teammark.setPosition(0);
                sleep(500);
                teammark.setPosition(0.8);
                encoderDrive(0.5,50,50,2);
                bucket.setPosition(0);
                stop();


                break;
            case (-1):
                telemetry.addLine("default");
                telemetry.update();
                encoderDrive(0.5,90,90,8);
                sleep(50);
                encoderDrive(0.5,-6,-6,3);
                sleep(50);
                encoderDrive(0.3, -41,41,5);
                sleep(250);
                encoderDriveCe(0.3,16,-16,3);
                sleep(50);
                encoderDrive(0.3,-7,-7,3);
                sleep(50);
                teammark.setPosition(0);
                sleep(500);
                teammark.setPosition(0.8);
                encoderDrive(0.7,103,103,5);
                bucket.setPosition(0);
                stop();
                break;

        }

        sleep(3000);
        ka();

        bucket.setPosition(1.0);
        stop();
    }


    void hardwareInitialize(){
        frontleft = hardwareMap.dcMotor.get("FL");
        frontright = hardwareMap.dcMotor.get("FR");
        backleft = hardwareMap.dcMotor.get("BL");
        backright = hardwareMap.dcMotor.get("BR");

        frontleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        lift = hardwareMap.dcMotor.get("lift");

        collector = hardwareMap.dcMotor.get("collector");
        slides = hardwareMap.dcMotor.get("slides");
        bucket = hardwareMap.servo.get("bucket");
        teammark = hardwareMap.servo.get("tm");

        minSlide = hardwareMap.touchSensor.get("minSlide");
        maxSlide = hardwareMap.touchSensor.get("maxSlide");

        dump = hardwareMap.dcMotor.get("dump");

//        maxSlide = hardwareMap.touchSensor.get("maxSlide");
//        minSlide = hardwareMap.touchSensor.get("minSlide");

        slides.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.REVERSE);
        backright.setDirection(DcMotor.Direction.REVERSE);
        lift.setDirection(DcMotor.Direction.REVERSE);

    }

/*
//    public void descend(double speed, int count, double Counts_per_rev, double timeseout){
//        int maxPos= (int)(count* Counts_per_rev);
//        int newTarget;
//        ElapsedTime runtime = new ElapsedTime();
//
//        if (opModeIsActive()) {
//            newTarget = lift.getCurrentPosition() + maxPos;
//            telemetry.addData("distance", lift.getCurrentPosition());
//            telemetry.addData("pos", maxPos);
////        telemetry.addData("neg", NegDistanceToGo);
//            telemetry.addData("counts", lift.getCurrentPosition() / Counts_per_rev);
//            telemetry.update();
//
//            lift.setTargetPosition(maxPos);
//            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            runtime.reset();
//            lift.setPower(speed);
//            lift.setPower(0);
//            lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
//
//    }
*/

    public void WeakDescend(){
        lift.setPower(1.0);
        sleep(6800);
        lift.setPower(0);
    }


    //
    public void ka(){
        frontleft.setPower(0);
        backleft.setPower(0);
        frontright.setPower(0);
        backright.setPower(0);
        sleep(500);
    }


    public void encoderDriveCe(double speed,
                               double leftInches, double rightInches,
                               double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        if (opModeIsActive()) {

            newLeftTarget = frontleft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = frontright.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            frontleft.setTargetPosition(newLeftTarget);
            backright.setTargetPosition(newLeftTarget);
            frontright.setTargetPosition(newRightTarget);
            backleft.setTargetPosition(newRightTarget);

            frontleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            frontleft.setPower(Math.abs(speed));
            backright.setPower(Math.abs(speed));
            frontright.setPower(Math.abs(speed));
            backleft.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (frontleft.isBusy() && frontright.isBusy())) {

                telemetry.addData("FL",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("FR",  "Running at %7d :%7d",
                        frontleft.getCurrentPosition(),
                        frontright.getCurrentPosition());

                telemetry.update();
            }

            frontleft.setPower(0);
            backright.setPower(0);
            frontright.setPower(0);
            backleft.setPower(0);

            frontleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //  sleep(250);
        }
    }

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        if (opModeIsActive()) {

            newLeftTarget = frontleft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = frontright.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            frontleft.setTargetPosition(newLeftTarget);
            backleft.setTargetPosition(newLeftTarget);
            frontright.setTargetPosition(newRightTarget);
            backright.setTargetPosition(newRightTarget);

            frontleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            frontleft.setPower(Math.abs(speed));
            backright.setPower(Math.abs(speed));
            frontright.setPower(Math.abs(speed));
            backleft.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (frontleft.isBusy() && frontright.isBusy())) {

                telemetry.addData("FL",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("FR",  "Running at %7d :%7d",
                        frontleft.getCurrentPosition(),
                        frontright.getCurrentPosition());

                telemetry.update();
            }

            frontleft.setPower(0);
            backright.setPower(0);
            frontright.setPower(0);
            backleft.setPower(0);

            frontleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);
        }
    }

    public void initVuforia() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    public void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void weakChugou(){
        encoderDriveCe(0.3,-5,5,2);
    }
}
