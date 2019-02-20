package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

@Autonomous(name = "Robot4100Auto-Crater",group = "4100")
public class Robot4100Auto_Crater extends Robot4100Auto_OffHook {
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        AutonomousState = "Initialized";
        this.m_RobotCore.updateStatus();
        this.waitForStart();
        if(this.opModeIsActive()) {
            super.offHookAndDetectSample();
        }

        if(this.opModeIsActive()){
            double sampleXDistance = 0;
            if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Left){
                sampleXDistance = -48;
            }else if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Right){
                sampleXDistance = 48;
            }else{
                sampleXDistance = 0;
            }
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(45,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            if(sampleXDistance != 0) {
                this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(sampleXDistance, Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            }
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            double veryLeftLength = sampleXDistance - (-75) + 130;
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(65,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(-35,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            this.m_RobotCore.getMotionSystem().replaceTask(m_RobotCore.getMotionSystem().getFixedTurnTask(80,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(-32,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(veryLeftLength,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            this.m_RobotCore.getMotionSystem().replaceTask(m_RobotCore.getMotionSystem().getFixedTurnTask(45,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }


            //this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(40,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.goRightToWall(0.1,0.2);



            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(-20,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));

            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(170,0.7));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            this.m_RobotCore.setCollectorServoToCollect(2);
            this.m_RobotCore.getCollectorSweeper().setPower(-1.0);
            sleep(1000);
            this.m_RobotCore.getCollectorSweeper().setPower(0);
            this.m_RobotCore.setCollectorServoToCollect(0);
            this.m_RobotCore.goRightToWall(0.1,0.2);
            while(!gamepad1.x){
                if(!this.opModeIsActive()){
                    return;
                }
                idle();
            }
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(-10,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(175,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));

            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }

            this.m_RobotCore.getDrawerSlide().setTargetPercent(100,Robot4100Setting.AUTONOMOUS_DRAWERSLIDESPEED,null);
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(15,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(200,0.7));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(-20,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            /*
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedTurnTask(45,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(30,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));

            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(40,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(-10,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));

            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(300,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedTurnTask(180,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            while(this.m_RobotCore.isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.updateStatus();
                idle();
            }

            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(-40,0.7));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(10,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(300,0.7));
            while(this.m_RobotCore.isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.updateStatus();
                idle();
            }
            */

        }
        this.m_RobotCore.stop();
        this.m_RobotCore.getLinearActuator().waitUntilFinish();
        this.m_RobotCore.save();
    }
}
