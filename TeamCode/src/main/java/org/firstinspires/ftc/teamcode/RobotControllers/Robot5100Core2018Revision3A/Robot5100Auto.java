package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Robot5100Auto extends LinearOpMode {
    protected Robot5100Core m_RobotCore;
    protected void hardwareInit(){
        this.m_RobotCore = new Robot5100Core(
                false,
                this,
                100,
                100,
                0,
                0
        );
    }
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        this.waitForStart();
        if(this.opModeIsActive()){
            this.m_RobotCore.setLinearActuatorToHook(Robot5100Setting.AUTONOMOUS_LINEARACTUATORSPEED);
            this.m_RobotCore.waitUntilFinish();
            this.m_RobotCore.getLinearActuator().setTargetPosition(this.m_RobotCore.getLinearActuator().getSmallestPos(),Robot5100Setting.AUTONOMOUS_LINEARACTUATORSPEED);
            this.m_RobotCore.waitUntilFinish();
        }
        this.m_RobotCore.save();
    }
}
