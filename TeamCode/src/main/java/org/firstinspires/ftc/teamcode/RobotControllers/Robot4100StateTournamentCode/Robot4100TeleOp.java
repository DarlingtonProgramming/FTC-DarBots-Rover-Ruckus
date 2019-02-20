package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.LED;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.NonBlockingTimer;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.TeleOps.Gamepad1Drive;

import java.util.Timer;
import java.util.TimerTask;

@TeleOp(name = "Robot4100TeleOp", group = "4100")
public class Robot4100TeleOp extends Gamepad1Drive {
    boolean isComboX = false;
    boolean isComboY = false;

    public Robot4100TeleOp() {
        super(Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE,Robot4100Setting.TELEOP_BIGGESTDRIVINGSPEED);
    }

    protected void hardwareInit(){
        super.robotCoreInit(new Robot4100Core(this,null,true,false,false));
    }

    protected Robot4100Core get4100Core(){
        return ((Robot4100Core) super.getRobotCore());
    }

    @Override
    public void runOpMode() {
        this.hardwareInit();
        this.get4100Core().getDebugger().addDebug(new RobotDebugger.RobotDebuggerInformation("Status","Initialized"));
        this.get4100Core().updateStatus();
        waitForStart();
        while(this.opModeIsActive()){
            super.driveControl();
            if(this.gamepad1.right_bumper){
                super.TeleOpBiggestDrivingSpeed = Robot4100Setting.TELEOP_BIGGESTDRIVINGSPEED * 0.5;
            }else{
                super.TeleOpBiggestDrivingSpeed = Robot4100Setting.TELEOP_BIGGESTDRIVINGSPEED;
            }
            if((-gamepad2.left_stick_y) > Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isComboY = false;
                if(!this.get4100Core().getDumperSlide().isBusy())
                    this.get4100Core().getDumperSlide().setTargetPercent(100,Robot4100Setting.TELEOP_DUMPERSLIDESPEED ,null);
            }else if((-gamepad2.left_stick_y) < -Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isComboY = false;
                if(!this.get4100Core().getDumperSlide().isBusy())
                    this.get4100Core().getDumperSlide().setTargetPercent(0,Robot4100Setting.TELEOP_DUMPERSLIDESPEED,null);
            }else{
                if(!isComboY)
                    this.get4100Core().getDumperSlide().stopMotion();
            }
            if(gamepad2.dpad_up){
                if(!this.get4100Core().getLinearActuator().isBusy())
                    this.get4100Core().getLinearActuator().setTargetPercent(100,Robot4100Setting.TELEOP_LINEARACTUATORSPEED,null);
            }else if(gamepad2.dpad_down){
                if(!this.get4100Core().getLinearActuator().isBusy())
                    this.get4100Core().getLinearActuator().setTargetPercent(0,Robot4100Setting.TELEOP_LINEARACTUATORSPEED,null);
            }else{
                this.get4100Core().getLinearActuator().stopMotion();
            }
            if(gamepad2.left_bumper){
                isComboX = false;
                if(!this.get4100Core().getDrawerSlide().isBusy())
                    this.get4100Core().getDrawerSlide().setTargetPercent(0,Robot4100Setting.TELEOP_DRAWERSLIDESPEED,null);
            }else if(gamepad2.right_bumper){
                isComboX = false;
                if(!this.get4100Core().getDrawerSlide().isBusy())
                    this.get4100Core().getDrawerSlide().setTargetPercent(100,Robot4100Setting.TELEOP_DRAWERSLIDESPEED, null);
            }else{
                if(!isComboX)
                    this.get4100Core().getDrawerSlide().stopMotion();
            }
            if(gamepad2.left_trigger > Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isComboX = false;
                this.get4100Core().getCollectorSweeper().setPower(-gamepad2.left_trigger);
            }else if(gamepad2.right_trigger > Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isComboX = false;
                this.get4100Core().getCollectorSweeper().setPower(gamepad2.right_trigger);
            }else{
                if(!isComboX)
                    this.get4100Core().getCollectorSweeper().setPower(0);
            }

            int previousDumperStage = get4100Core().getCollectorServoStage();
            if((-gamepad2.right_stick_y) > Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isComboX = false;
                if(previousDumperStage < 2){
                    previousDumperStage++;
                    this.get4100Core().setCollectorServoToCollect(previousDumperStage);
                    sleep(500);
                }
            }else if((-gamepad2.right_stick_y) < -Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isComboX = false;
                if(previousDumperStage > 0){
                    previousDumperStage--;
                    this.get4100Core().setCollectorServoToCollect(previousDumperStage);
                    sleep(500);
                }
            }

            if(gamepad2.a){
                isComboY = false;
                this.get4100Core().setDumperServoToDump(true);
            }else{
                if(!isComboY)
                    this.get4100Core().setDumperServoToDump(false);
            }
            if(gamepad2.x){
                if(!isComboX) {
                    isComboY = false;
                    isComboX = true;
                    //Get dumperslide down, dumperservo down and get drawerslide all the way back
                    this.get4100Core().getDumperSlide().setTargetPercent(0, Robot4100Setting.TELEOP_DUMPERSLIDESPEED, null);
                    this.get4100Core().setDumperServoToDump(false);
                    this.get4100Core().setCollectorServoToCollect(1);
                    this.get4100Core().getDrawerSlide().setTargetPercent(0, Robot4100Setting.TELEOP_DRAWERSLIDESPEED, new RobotServoUsingMotor.RobotServoUsingMotorPositionCallBack() {
                        @Override
                        public void finish(RobotServoUsingMotor Servo) {
                            if (!isComboX) {
                                return;
                            }
                            //since drawerslide is all the way in, put collectorservo in as well.
                            Robot4100TeleOp.this.get4100Core().setCollectorServoToCollect(0);
                            //wait for the servo to finish its moving job
                            Robot4100TeleOp.this.get4100Core().getTimer().addTask(new NonBlockingTimer.timerTask() {
                                @Override
                                public void run() {
                                    if(!isComboX){
                                        return;
                                    }
                                    //cubes will automatically fall in to the dumper, now we have to rotate the sweeper motor for the spheres to go in.
                                    Robot4100TeleOp.this.get4100Core().getCollectorSweeper().setPower(0.5);
                                    Robot4100TeleOp.this.get4100Core().getTimer().addTask(new NonBlockingTimer.timerTask() {
                                        @Override
                                        public void run() {
                                            if(!isComboX){
                                                return;
                                            }
                                            //stop the motor, combo key action finished
                                            Robot4100TeleOp.this.get4100Core().getCollectorSweeper().setPower(0);
                                            isComboX = false;
                                        }
                                    },1.0);
                                }
                            },0.8);
                        }
                    });
                }
            }else if(gamepad2.y){
                if(!isComboY) {
                    isComboX = false;
                    isComboY = true;
                    //first move dumperslide all the way up, since dumper slide have a protection mechanism implemented in 4100 core, we dont need to move the collectorsetout servo
                    this.get4100Core().getDumperSlide().setTargetPercent(Robot4100Setting.DUMPERSLIDE_DUMPPCT, Robot4100Setting.TELEOP_DUMPERSLIDESPEED, new RobotServoUsingMotor.RobotServoUsingMotorPositionCallBack() {
                        @Override
                        public void finish(RobotServoUsingMotor Servo) {
                            if(!isComboY){
                                return;
                            }
                            //dumperslide all the way up, now lets just dump
                            Robot4100TeleOp.this.get4100Core().setDumperServoToDump(true);
                            Robot4100TeleOp.this.get4100Core().getTimer().addTask(new NonBlockingTimer.timerTask() {
                                @Override
                                public void run() {
                                    if(!isComboY) {
                                        return;
                                    }
                                    //After the dumper is set to dump for 3 seconds, which lets the minerals to go into the lander, put dumperservo back and bring dumperslide down
                                    Robot4100TeleOp.this.get4100Core().setDumperServoToDump(false);
                                    Robot4100TeleOp.this.get4100Core().getDumperSlide().setTargetPercent(0, Robot4100Setting.TELEOP_DUMPERSLIDESPEED, new RobotServoUsingMotor.RobotServoUsingMotorPositionCallBack() {
                                        @Override
                                        public void finish(RobotServoUsingMotor Servo) {
                                            if(isComboY){
                                                isComboY = false;
                                            }
                                        }
                                    });

                                }
                            },3.0);
                        }
                    });
                }
            }

            super.getRobotCore().updateStatus();
        }
        isComboY = false;
        isComboX = false;
    }
}
