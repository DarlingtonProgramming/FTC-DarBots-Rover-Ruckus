package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.XYPlaneCalculations;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ChassisControllers.OmniWheel4SideDiamondShaped;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystemTeleOpControlTask;

@Autonomous(name = "Robot4100Auto-Crater",group = "4100")
public class Robot4100Auto_Crater extends Robot4100Auto_OffHook {
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
                sampleXDistance = -80;
            }else if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Right){
                sampleXDistance = 80;
            }else{
                sampleXDistance = 0;
            }
            AutonomousState = "Sampling - Forwarding & Moving";
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(40,0.6));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.getGyro().updateData();
            double tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();

            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(sampleXDistance,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();

            AutonomousState = "Sampling - Pushing Sample";
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(55,0.45));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(-55,0.45));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();

            AutonomousState = "Sampling - Push Finish - Moving Crater";
            double veryLeftLength = sampleXDistance - (-80) + 110;
            this.m_RobotCore.getMotionSystem().replaceTask(m_RobotCore.getMotionSystem().getFixedTurnTask(90,0.2));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }

            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            startAng = tempAng;
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(veryLeftLength,1.0));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();

            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedTurnTask(40,0.1));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            this.m_RobotCore.goRightToWall(0.1,0.25);

            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            startAng = tempAng;
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(-15,0.1));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();


            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(180,1.0));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }

            this.m_RobotCore.setCollectorServoToCollect(2);
            sleep(200);

            this.m_RobotCore.getCollectorSweeper().setPower(-1);

            sleep(500);
            this.m_RobotCore.setCollectorServoToCollect(0);

            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            startAng = tempAng;
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(-10,0.2));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();

            this.m_RobotCore.getCollectorSweeper().setPower(0);

            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(-240,1.0));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();

            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(20,0.1));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            this.m_RobotCore.getGyro().updateData();
            startAng = this.m_RobotCore.getGyro().getHeading();

            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(-50,0.15));
            while(this.m_RobotCore.getMotionSystem().isBusy()){
                if(!this.opModeIsActive()){
                    return;
                }
                this.m_RobotCore.getMotionSystem().updateStatus();
            }
            /*
            this.m_RobotCore.getGyro().updateData();
            tempAng = this.m_RobotCore.getGyro().getHeading();
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(XYPlaneCalculations.normalizeDeg(-(tempAng-startAng)),0.1));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            startAng = tempAng;
            */
        }
        while(this.m_RobotCore.isBusy() && this.opModeIsActive()){
            this.m_RobotCore.updateStatus();
            idle();
        }
        this.m_RobotCore.stop();
        this.m_RobotCore.save();
    }
}
