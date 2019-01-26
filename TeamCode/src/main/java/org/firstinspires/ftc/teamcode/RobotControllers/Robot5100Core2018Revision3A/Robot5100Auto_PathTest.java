package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

@TeleOp(name = "Robot5100Auto-PathTest", group = "Test")
public class Robot5100Auto_PathTest extends LinearOpMode {
    protected Robot5100Core m_RobotCore;
    protected void hardwareInit(){
        this.m_RobotCore = new Robot5100Core(
                false,
                true,
                this,
                100,
                100,
                0,
                0,
                0,
                0
        );
    }
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        RobotDebugger.addDebug("Status","Initialized");
        RobotDebugger.doLoop();
        this.waitForStart();
        String output = "";
        if(this.opModeIsActive()){
            ElapsedTime mTime = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
            mTime.reset();
            this.m_RobotCore.getMotionSystem().driveForwardWithSpeed(Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            while(!this.gamepad1.a){
                idle();
            }
            this.m_RobotCore.getMotionSystem().stopMoving();
            output += "forward: " + mTime.seconds() + " s; \n";
            while(!this.gamepad1.b){
                idle();
            }
            mTime.reset();
            this.m_RobotCore.getMotionSystem().driveToLeftWithSpeed(Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            while(!this.gamepad1.a){
                idle();
            }
            this.m_RobotCore.getMotionSystem().stopMoving();
            output += "left: " + mTime.seconds() + "s; \n";
            while(!this.gamepad1.b){
                idle();
            }
            mTime.reset();
            this.m_RobotCore.getMotionSystem().driveToRightWithSpeed(Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            while(!this.gamepad1.a){
                idle();
            }
            this.m_RobotCore.getMotionSystem().stopMoving();
            output += "right: " + mTime.seconds() + "s; \n";
            while(!this.gamepad1.b){
                idle();
            }
            mTime.reset();
            this.m_RobotCore.getMotionSystem().keepTurningOffsetAroundCenter(Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            while(!this.gamepad1.a){
                idle();
            }
            this.m_RobotCore.getMotionSystem().stopMoving();
            output += "turn: " + mTime.seconds() + "s; \n";
            RobotDebugger.addDebug("debug",output);
            RobotDebugger.doLoop();
        }
    }
}
