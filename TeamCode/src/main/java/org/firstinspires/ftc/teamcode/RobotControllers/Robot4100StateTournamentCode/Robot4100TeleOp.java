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
    double previousDumperServoPosition = 0;
    boolean isCombo = false;
    public Robot4100TeleOp() {
        super(Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE,Robot4100Setting.TELEOP_BIGGESTDRIVINGSPEED);
    }

    protected void hardwareInit(){
        super.robotCoreInit(new Robot4100Core(this,null,true,true,false));
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
                isCombo = false;
                if(!this.get4100Core().getDumperSlide().isBusy())
                    this.get4100Core().getDumperSlide().setTargetPercent(100,Robot4100Setting.TELEOP_DUMPERSLIDESPEED * Math.abs(gamepad2.left_stick_y),null);
            }else if((-gamepad2.left_stick_y) < -Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isCombo = false;
                if(!this.get4100Core().getDumperSlide().isBusy())
                    this.get4100Core().getDumperSlide().setTargetPercent(0,Robot4100Setting.TELEOP_DUMPERSLIDESPEED * Math.abs(gamepad2.left_stick_y),null);
            }else{
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
                isCombo = false;
                if(!this.get4100Core().getDrawerSlide().isBusy())
                    this.get4100Core().getDrawerSlide().setTargetPercent(0,Robot4100Setting.TELEOP_DRAWERSLIDESPEED,null);
            }else if(gamepad2.right_bumper){
                isCombo = false;
                if(!this.get4100Core().getDrawerSlide().isBusy())
                    this.get4100Core().getDrawerSlide().setTargetPercent(100,Robot4100Setting.TELEOP_DRAWERSLIDESPEED, null);
            }else{
                this.get4100Core().getDrawerSlide().stopMotion();
            }
            if(gamepad2.left_trigger > Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isCombo = false;
                this.get4100Core().getCollectorSweeper().setPower(-gamepad2.left_trigger);
            }else if(gamepad2.right_trigger > Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isCombo = false;
                this.get4100Core().getCollectorSweeper().setPower(gamepad2.right_trigger);
            }else{
                this.get4100Core().getCollectorSweeper().setPower(0);
            }
            if((-gamepad2.right_stick_y) > Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isCombo = false;
                this.get4100Core().setCollectorServoToCollect(true);
            }else if((-gamepad2.right_stick_y) < -Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                isCombo = false;
                this.get4100Core().setCollectorServoToCollect(false);
            }
            if(gamepad2.a){
                isCombo = false;
                this.get4100Core().setDumperServoToDump(true);
            }else{
                this.get4100Core().setDumperServoToDump(false);
            }
            if(gamepad2.x){
                if(!isCombo) {
                    isCombo = true;
                    this.get4100Core().getDumperSlide().setTargetPercent(0, Robot4100Setting.TELEOP_DUMPERSLIDESPEED, null);
                    this.get4100Core().setDumperServoToDump(false);
                    this.get4100Core().getDrawerSlide().setTargetPercent(0, Robot4100Setting.TELEOP_DRAWERSLIDESPEED, new RobotServoUsingMotor.RobotServoUsingMotorPositionCallBack() {
                        @Override
                        public void finish(RobotServoUsingMotor Servo) {
                            if (isCombo) {
                                Robot4100TeleOp.this.get4100Core().setCollectorServoToCollect(false);
                                isCombo = false;
                            }
                        }
                    });
                }
            }else if(gamepad2.y){
                if(!isCombo) {
                    isCombo = true;
                    this.get4100Core().getDumperSlide().setTargetPercent(100, Robot4100Setting.TELEOP_DUMPERSLIDESPEED, new RobotServoUsingMotor.RobotServoUsingMotorPositionCallBack() {
                        @Override
                        public void finish(RobotServoUsingMotor Servo) {
                            if(!isCombo){
                                return;
                            }
                            Robot4100TeleOp.this.get4100Core().setDumperServoToDump(true);
                            Robot4100TeleOp.this.get4100Core().getTimer().addTask(new NonBlockingTimer.timerTask() {
                                @Override
                                public void run() {
                                    if(!isCombo) {
                                        Robot4100TeleOp.this.get4100Core().setDumperServoToDump(false);
                                        Robot4100TeleOp.this.get4100Core().getDumperSlide().setTargetPercent(0, Robot4100Setting.TELEOP_DUMPERSLIDESPEED, null);
                                        isCombo = false;
                                    }
                                }
                            },Robot4100Setting.TELEOP_COMBO_DUMPERWAITSEC);
                        }
                    });
                }
            }

            super.getRobotCore().updateStatus();
        }
    }
}
