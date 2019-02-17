package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

@Autonomous(name = "Robot4100Auto-Crater",group = "4100")
public class Robot4100Auto_Crater extends Robot4100Auto_OffHook {
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
                sampleXDistance = -35 + 5;
            }else if(super.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Right){
                sampleXDistance = 35 + 5;
            }else{
                sampleXDistance = 5;
            }
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(25,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.setCollectorServoToCollect(true);
            this.m_RobotCore.getCollectorSweeper().setPower(1.0);
            this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(sampleXDistance,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(15,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(-8,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
            this.m_RobotCore.setCollectorServoToCollect(false);
            sleep(500);
            m_RobotCore.getCollectorSweeper().setPower(0);
            double veryLeftLength = sampleXDistance - (-35+5);
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedTurnTask(90,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedXDistanceTask(13,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().addTask(m_RobotCore.getMotionSystem().getFixedZDistanceTask(veryLeftLength,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
            this.m_RobotCore.getMotionSystem().waitUntilFinish();
        }
        this.m_RobotCore.getLinearActuator().waitUntilFinish();
        this.m_RobotCore.save();
    }
}
