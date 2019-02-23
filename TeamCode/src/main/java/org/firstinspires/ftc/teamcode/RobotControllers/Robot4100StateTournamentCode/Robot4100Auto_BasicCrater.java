package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.XYPlaneCalculations;

@Autonomous(name = "Robot4100Auto-BasicCrater",group = "4100")
public class Robot4100Auto_BasicCrater extends Robot4100Auto_OffHook {
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        AutonomousState = "Initialized";
        this.m_RobotCore.updateStatus();
        this.waitForStart();
        if(this.opModeIsActive()) {
            AutonomousState = "Off-hook";
            super.offHookAndDetectSample();
            AutonomousState = "Off-hook OK";
            this.m_RobotCore.updateStatus();
        }

        if(this.opModeIsActive()){
            AutonomousState = "Sampling Start";
            double sampleXDistance = 0;
            if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Left){
                sampleXDistance = -78;
            }else if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Right){
                sampleXDistance = 78;
            }else{
                sampleXDistance = 0;
            }
            AutonomousState = "Sampling - Forwarding & Moving";
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(40,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.getGyro().updateData();
            double tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();

            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            startAng = tempAng;
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(sampleXDistance,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();

            AutonomousState = "Sampling - Collecting";
            this.m_RobotCore.setCollectorServoToCollect(2);
            this.m_RobotCore.getCollectorSweeper().setPower(1.0);
            sleep(700);

            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            startAng = tempAng;
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(40,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();

            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            startAng = tempAng;
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(-25,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            startAng = tempAng;

            this.m_RobotCore.setCollectorServoToCollect(0);
            m_RobotCore.getCollectorSweeper().setPower(0);


            AutonomousState = "Sampling - Collect Finish - Pushing";
            double veryLeftLength = sampleXDistance - (-75) + 110;
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(65,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            //this.m_RobotCore.getDrawerSlide().setTargetPercent(Robot4100Setting.DRAWESLIDE_SAFEPCT,Robot4100Setting.AUTONOMOUS_DRAWERSLIDESPEED,null);
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            AutonomousState = "Sampling - end";
        }
        while(this.m_RobotCore.isBusy() && this.opModeIsActive()){
            this.m_RobotCore.updateStatus();
            idle();
        }
        this.m_RobotCore.stop();
        this.m_RobotCore.save();
    }
}
