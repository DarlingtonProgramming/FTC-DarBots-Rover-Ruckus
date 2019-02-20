package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;

@Autonomous(name = "Robot4100Auto-BasicDepot",group = "4100")
public class Robot4100Auto_BasicDepot extends Robot4100Auto_OffHook {
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
                sampleXDistance = -48;
            }else if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Right){
                sampleXDistance = 48;
            }else{
                sampleXDistance = 0;
            }
            AutonomousState = "Sampling - Forwarding & Moving";
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(45,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(sampleXDistance,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            AutonomousState = "Sampling - Pushing Sample";
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(65,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(-45,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            while(!this.gamepad1.x){

            }

            AutonomousState = "Sampling - Push Finish - Moving Crater";
            double veryLeftLength = sampleXDistance - (-48) + 85;
            this.m_RobotCore.getMotionSystem().replaceTask(m_RobotCore.getMotionSystem().getFixedTurnTask(80,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(-25,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(veryLeftLength,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }

            //this.m_RobotCore.goRightToWall(0.1,0.2);

            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(-10,0.1));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(130,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getDrawerSlide().setTargetPercent(100,Robot4100Setting.AUTONOMOUS_DRAWERSLIDESPEED,null);
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }

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
