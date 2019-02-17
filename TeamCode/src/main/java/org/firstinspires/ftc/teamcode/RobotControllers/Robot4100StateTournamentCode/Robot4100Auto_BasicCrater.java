package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;

@Autonomous(name = "Robot4100Auto-BasicCrater",group = "4100")
public class Robot4100Auto_BasicCrater extends Robot4100Auto_OffHook {
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        this.m_RobotCore.getDebugger().addDebug(new RobotDebugger.RobotDebuggerInformation("Status","Initialized"));
        this.m_RobotCore.updateStatus();
        this.waitForStart();
        if(this.opModeIsActive()) {
            super.offHookAndDetectSample();
        }

        if(this.opModeIsActive()){
            double sampleXDistance = 0;
            if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Left){
                sampleXDistance = -75;
            }else if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Right){
                sampleXDistance = 75;
            }else{
                sampleXDistance = 0;
            }
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(45,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(sampleXDistance,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.setCollectorServoToCollect(true);
            this.m_RobotCore.getCollectorSweeper().setPower(1.0);
            sleep(700);
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(30,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(-15,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.setCollectorServoToCollect(false);
            sleep(500);
            m_RobotCore.getCollectorSweeper().setPower(0);
            double veryLeftLength = sampleXDistance - (-75) + 110;
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(65,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            //this.m_RobotCore.getDrawerSlide().setTargetPercent(Robot4100Setting.DRAWESLIDE_SAFEPCT,Robot4100Setting.AUTONOMOUS_DRAWERSLIDESPEED,null);
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
        }
        while(this.m_RobotCore.isBusy() && this.opModeIsActive()){
            this.m_RobotCore.updateStatus();
            idle();
        }
        this.m_RobotCore.save();
    }
}
