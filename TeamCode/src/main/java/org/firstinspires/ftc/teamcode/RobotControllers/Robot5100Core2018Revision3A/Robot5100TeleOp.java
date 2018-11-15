package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;

@TeleOp(name = "Robot5100TeleOP", group = "5100")
public class Robot5100TeleOp extends LinearOpMode {
    private Robot5100Core m_RobotCore;
    protected void hardwareInit(){
        this.m_RobotCore = new Robot5100Core(this,100,100,0,0);
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
            RobotDebugger.addDebug("isMoving","true");
            if(isTurning){
                this.m_RobotCore.getMotionSystem().keepTurningOffsetAroundCenter(gamepad1.right_stick_x);
            }else{
                if(isControllingX){
                    this.m_RobotCore.getMotionSystem().driveToRightWithSpeed(gamepad1.left_stick_x);
                }else{
                    this.m_RobotCore.getMotionSystem().driveForwardWithSpeed(-gamepad1.left_stick_y);
                }
            }
        }else{
            RobotDebugger.addDebug("isMoving","false");
            if(this.m_RobotCore.getMotionSystem().getCurrentMotionType() == RobotMotionSystem.motionType.keepingMovingWithFixedSpeed || this.m_RobotCore.getMotionSystem().getCurrentMotionType() == RobotMotionSystem.motionType.keepingTurningWithSpeed){
                this.m_RobotCore.getMotionSystem().stopMoving();
            }
        }
        if(gamepad1.dpad_up){
            this.m_RobotCore.getMotionSystem().driveForward(10,Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
        }else if(gamepad1.dpad_down){
            this.m_RobotCore.getMotionSystem().driveBackward(10,Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
        }else if(gamepad1.dpad_left){
            this.m_RobotCore.getMotionSystem().driveToLeft(10,Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
        }else if(gamepad1.dpad_right){
            this.m_RobotCore.getMotionSystem().driveToRight(10,Robot5100Setting.TELEOP_BIGGESTDRIVINGSPEED);
        }
    }
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        this.waitForStart();
        while(this.opModeIsActive()){
            if(this.gamepad1.x){
                if(!this.m_RobotCore.getLinearActuator().isBusy()) {
                    this.m_RobotCore.getLinearActuator().setTargetPosition(this.m_RobotCore.getLinearActuator().getBiggestPos(), 0.2);
                }
            }else if(this.gamepad1.y) {
                if (!this.m_RobotCore.getLinearActuator().isBusy()) {
                    this.m_RobotCore.getLinearActuator().setTargetPosition(this.m_RobotCore.getLinearActuator().getSmallestPos(), 0.2);
                }
            }else{
                this.m_RobotCore.getLinearActuator().stopMotion();
            }
            this.movementControl();
            this.m_RobotCore.doLoop();
        }
    }
}
