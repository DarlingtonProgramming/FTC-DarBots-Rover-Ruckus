package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;

@TeleOp(name = "Robot5100TeleOP", group = "5100")
public class Robot5100TeleOp extends LinearOpMode {
    protected Robot5100Core m_RobotCore;
    protected void hardwareInit(){
        this.m_RobotCore = new Robot5100Core(true,this,100,100,0,0);
    }
    protected void movementControl(){
        boolean isMoving = false;
        boolean isControllingX = false;
        boolean isTurning = false;
        if(Math.abs(gamepad1.left_stick_x) >= Robot5100Setting.TELEOP_GAMEPADTRIGGERVALUE || Math.abs(gamepad1.left_stick_y) >= Robot5100Setting.TELEOP_GAMEPADTRIGGERVALUE || Math.abs(gamepad1.right_stick_x) >= Robot5100Setting.TELEOP_GAMEPADTRIGGERVALUE){
            isMoving = true;
            if(Math.abs(gamepad1.right_stick_x) >= Math.max(Math.abs(gamepad1.left_stick_x),Math.abs(gamepad1.left_stick_y))){
                isTurning = true;
            }else{
                if(Math.abs(gamepad1.left_stick_x) >= Math.abs(gamepad1.left_stick_y)){
                    isControllingX = true;
                }
            }
        }
        if(isMoving){
            if(isTurning){
                this.m_RobotCore.getMotionSystem().keepTurningOffsetAroundCenter(gamepad1.right_stick_x * Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
            }else{
                if(isControllingX){
                    this.m_RobotCore.getMotionSystem().driveToRightWithSpeed(gamepad1.left_stick_x * Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
                }else{
                    this.m_RobotCore.getMotionSystem().driveForwardWithSpeed(-gamepad1.left_stick_y * Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
                }
            }
        }else{
            this.m_RobotCore.getMotionSystem().stopMoving();
        }
    }
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        this.waitForStart();
        while(this.opModeIsActive()){
            if(this.gamepad1.x){
                if(!this.m_RobotCore.getLinearActuator().isBusy()) {
                    this.m_RobotCore.getLinearActuator().setTargetPosition(this.m_RobotCore.getLinearActuator().getBiggestPos(), Robot5100Setting.TELEOP_LINEARACTUATORSPEED);
                }
            }else if(this.gamepad1.y) {
                if (!this.m_RobotCore.getLinearActuator().isBusy()) {
                    this.m_RobotCore.getLinearActuator().setTargetPosition(this.m_RobotCore.getLinearActuator().getSmallestPos(), Robot5100Setting.TELEOP_LINEARACTUATORSPEED);
                }
            }else{
                this.m_RobotCore.getLinearActuator().stopMotion();
            }
            this.movementControl();
            this.m_RobotCore.doLoop();
        }
    }
}
