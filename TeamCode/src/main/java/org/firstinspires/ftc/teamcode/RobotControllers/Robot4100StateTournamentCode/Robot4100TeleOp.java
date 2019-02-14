package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.LED;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.TeleOps.Gamepad1Drive;

@TeleOp(name = "Robot4100TeleOp", group = "4100")
public class Robot4100TeleOp extends Gamepad1Drive {
    double previousDumperServoPosition = 0;
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
                if(!this.get4100Core().getDumperSlide().isBusy())
                    this.get4100Core().getDumperSlide().setTargetPercent(100,Robot4100Setting.TELEOP_DUMPERSLIDESPEED * Math.abs(gamepad2.left_stick_y));
            }else if((-gamepad2.left_stick_y) < -Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                if(!this.get4100Core().getDumperSlide().isBusy())
                    this.get4100Core().getDumperSlide().setTargetPercent(0,Robot4100Setting.TELEOP_DUMPERSLIDESPEED * Math.abs(gamepad2.left_stick_y));
            }else{
                this.get4100Core().getDumperSlide().stopMotion();
            }
            if(gamepad2.dpad_up){
                if(!this.get4100Core().getLinearActuator().isBusy())
                    this.get4100Core().getLinearActuator().setTargetPercent(100,Robot4100Setting.TELEOP_LINEARACTUATORSPEED);
            }else if(gamepad2.dpad_down){
                if(!this.get4100Core().getLinearActuator().isBusy())
                    this.get4100Core().getLinearActuator().setTargetPercent(0,Robot4100Setting.TELEOP_LINEARACTUATORSPEED);
            }else{
                this.get4100Core().getLinearActuator().stopMotion();
            }
            if(gamepad2.left_bumper){
                if(!this.get4100Core().getDrawerSlide().isBusy())
                    this.get4100Core().getDrawerSlide().setTargetPercent(0,Robot4100Setting.TELEOP_DRAWERSLIDESPEED);
            }else if(gamepad2.right_bumper){
                if(!this.get4100Core().getDrawerSlide().isBusy())
                    this.get4100Core().getDrawerSlide().setTargetPercent(100,Robot4100Setting.TELEOP_DRAWERSLIDESPEED);
            }else{
                this.get4100Core().getDrawerSlide().stopMotion();
            }
            if(gamepad2.left_trigger > Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                this.get4100Core().getCollectorSweeper().setPower(-gamepad2.left_trigger);
            }else if(gamepad2.right_trigger > Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                this.get4100Core().getCollectorSweeper().setPower(gamepad2.right_trigger);
            }else{
                this.get4100Core().getCollectorSweeper().setPower(0);
            }
            if((-gamepad2.right_stick_y) > Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                this.get4100Core().getCollectorSetOutServo().setPosition((-gamepad2.right_stick_y) / 2.0 + 0.5);
            }else if((-gamepad2.right_stick_y) < -Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE){
                this.get4100Core().getCollectorSetOutServo().setPosition((-gamepad2.right_stick_y) / 2.0 + 0.5);
            }
            if(gamepad2.a){
                this.get4100Core().getDumperServo().setPosition(0.2);
            }else{
                this.get4100Core().getDumperServo().setPosition(0.8);
            }

            super.getRobotCore().updateStatus();
        }
    }
}
