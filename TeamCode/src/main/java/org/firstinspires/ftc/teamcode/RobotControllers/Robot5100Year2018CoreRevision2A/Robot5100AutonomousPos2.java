package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "5100AutoPos2", group = "5100")
public class Robot5100AutonomousPos2 extends LinearOpMode {
    private Robot5100Core m_RobotCore;

    public void hardWareInitialize(){
        this.m_RobotCore = new Robot5100Core(this,100,100,0,0,0,0,0,false);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        this.hardWareInitialize();
        this.waitForStart();
        if(opModeIsActive()) {
            this.m_RobotCore.setCollectorServoToRes();
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.openRackAndPinionToHook();
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.getMotionSystem().driveToLeft(20, Robot5100Settings.Autonomous_BiggestSpeed);
            this.m_RobotCore.getMotionSystem().waitUntilMotionFinish();
            this.m_RobotCore.closeRackAndPinion();
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.closeCollectorServo();
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.savePosition();
        }
        while(this.opModeIsActive()){
            this.m_RobotCore.doLoop();
        }
    }
}
