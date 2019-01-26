package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

@Disabled
@Autonomous(name = "GASTC_5100Square", group = "GASTC")
public class GASTC_5100RunInSquare extends LinearOpMode {
    protected Robot5100Core m_RobotCore;
    protected void hardwareInit(){
        this.m_RobotCore = new Robot5100Core(
                false,
                false,
                this,
                100,
                100,
                0,
                0,
                0,
                0
        );
    }
    protected void hardwareDestroy(){
        this.m_RobotCore = null;
    }
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        RobotDebugger.addDebug("Status", "Initialized");
        RobotDebugger.doLoop();
        this.waitForStart();
        while (this.opModeIsActive()) {
            this.m_RobotCore.getMotionSystem().driveForward(20, Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            while (this.opModeIsActive() && this.m_RobotCore.getMotionSystem().isBusy()) {
                this.m_RobotCore.doLoop();
            }
            this.m_RobotCore.getMotionSystem().driveToRight(20, Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            while (this.opModeIsActive() && this.m_RobotCore.getMotionSystem().isBusy()) {
                this.m_RobotCore.doLoop();
            }
            this.m_RobotCore.getMotionSystem().driveBackward(20, Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            while (this.opModeIsActive() && this.m_RobotCore.getMotionSystem().isBusy()) {
                this.m_RobotCore.doLoop();
            }
            this.m_RobotCore.getMotionSystem().driveToLeft(20, Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            while (this.opModeIsActive() && this.m_RobotCore.getMotionSystem().isBusy()) {
                this.m_RobotCore.doLoop();
            }
        }
        this.hardwareDestroy();
    }
}
