package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ChassisControllers.OmniWheel4SideDiamondShaped;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystemTeleOpControlTask;

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
        this.m_RobotCore.getGyro().updateData();
        double startAng = this.m_RobotCore.getGyro().getHeading();

        if(this.opModeIsActive()){
            AutonomousState = "Sampling Start";
            double sampleXDistance = 0;
            if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Left){
                sampleXDistance = -80;
            }else if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Right){
                sampleXDistance = 80;
            }else{
                sampleXDistance = 0;
            }
            AutonomousState = "Sampling - Forwarding & Moving";
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(40,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.getGyro().updateData();
            double tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(-(tempAng-startAng),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            startAng = tempAng;

            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(sampleXDistance,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(-(tempAng-startAng),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            startAng = tempAng;

            AutonomousState = "Sampling - Pushing Sample";
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(65,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(-55,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(-(tempAng-startAng),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            startAng = tempAng;

            AutonomousState = "Sampling - Push Finish - Moving Crater";
            double veryLeftLength = sampleXDistance - (-80) + 110;
            this.m_RobotCore.getMotionSystem().replaceTask(m_RobotCore.getMotionSystem().getFixedTurnTask(85,0.1));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(-8,0.1));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(veryLeftLength,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }

            //this.m_RobotCore.goRightToWall(0.1,0.2);
            while(!gamepad1.x){

            }

            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(-10,0.1));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            RobotMotionSystemTeleOpControlTask m_TeleOpTask =  m_RobotCore.getMotionSystem().getTeleOpTask();
            this.m_RobotCore.getMotionSystem().addTask(m_TeleOpTask);
            m_TeleOpTask.setDriveZSpeed(0.5);
            boolean isMineralInRightSide = false;
            while(!isMineralInRightSide && this.opModeIsActive()){
                m_RobotCore.getMotionSystem().updateStatus();
                FTC2018GameSpecificFunctions.MineralInformation[] infos = this.m_RobotCore.get2018MineralDetection().detectAllBlocksInCamera();
                if(infos == null || infos.length < 1){
                    continue;
                }
                for(FTC2018GameSpecificFunctions.MineralInformation i : infos){
                    if(i.getLeft() >= (float) (i.getImageWidth()) / 2.0f){
                        isMineralInRightSide = true;
                    }
                }
            }
            m_TeleOpTask.setDriveZSpeed(0);
            m_RobotCore.getMotionSystem().deleteAllTasks();

            m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(140,0.1));
            while(this.m_RobotCore.getMotionSystem().isBusy() && this.opModeIsActive()){
                m_RobotCore.getMotionSystem().updateStatus();
            }

            //this.m_RobotCore.getDrawerSlide().setTargetPercent(100,Robot4100Setting.AUTONOMOUS_DRAWERSLIDESPEED,null);


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
