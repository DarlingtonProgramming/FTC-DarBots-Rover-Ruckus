package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.XYPlaneCalculations;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

import java.util.ArrayList;

@Autonomous(name = "Robot4100Auto_OffHook",group = "4100")
public class Robot4100Auto_OffHook extends LinearOpMode {
    protected Robot4100Core m_RobotCore;
    protected FTC2018GameSpecificFunctions.GoldPosType m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Unknown;
    protected void hardwareInit(){
        this.m_RobotCore = new Robot4100Core(this,new Robot2DPositionIndicator(0,0,0),false,false,true);
        this.m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Unknown;
        this.m_RobotCore.getDebugger().addDebuggerCallable(new RobotDebugger.ObjectDebuggerWrapper<>("GoldPos",new Object(){
            @Override
            public String toString(){
                return Robot4100Auto_OffHook.this.m_GoldPosition.name();
            }
        }));
    }
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        this.m_RobotCore.getDebugger().addDebug(new RobotDebugger.RobotDebuggerInformation("Status","Initialized"));
        this.m_RobotCore.updateStatus();
        this.waitForStart();
        if(this.opModeIsActive()) {
            this.offHookAndDetectSample();
        }
        this.m_RobotCore.waitUntilFinish();
        this.m_RobotCore.save();
    }

    protected void offHookAndDetectSample(){
        this.m_RobotCore.getLinearActuator().setTargetPercent(Robot4100Setting.LINEARACTUATOR_HOOKPCT,Robot4100Setting.AUTONOMOUS_LINEARACTUATORSPEED,null);
        while(this.m_RobotCore.isBusy() && this.m_RobotCore.getLinearActuator().getCurrentPercent() < (Robot4100Setting.LINEARACTUATOR_HOOKPCT - 0.05)){


            if(!this.opModeIsActive()){
                this.m_RobotCore.getLinearActuator().stopMotion();
                return;
            }
            this.m_RobotCore.updateStatus();

            if(this.m_GoldPosition == FTC2018GameSpecificFunctions.GoldPosType.Unknown) {
                FTC2018GameSpecificFunctions.MineralInformation[] infos = this.m_RobotCore.get2018MineralDetection().detectAllBlocksInCamera();
                if (infos == null || infos.length != 2) {
                    continue;
                } else {
                    FTC2018GameSpecificFunctions.MineralInformation GoldInfo = null;
                    ArrayList<FTC2018GameSpecificFunctions.MineralInformation> SilverInfos = new ArrayList<>();
                    for (FTC2018GameSpecificFunctions.MineralInformation m : infos) {
                        if (m.getMineralType() == FTC2018GameSpecificFunctions.MineralType.Gold) {
                            GoldInfo = m;
                        } else if (m.getMineralType() == FTC2018GameSpecificFunctions.MineralType.Silver) {
                            SilverInfos.add(m);
                        }
                    }
                    if (GoldInfo == null) {
                        m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Right;
                    } else if (SilverInfos.size() >= 1) { //if(SilverInfos.size() == 1){
                        if (GoldInfo.getLeft() > SilverInfos.get(0).getLeft()) {
                            m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Center;
                        } else {
                            m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Left;
                        }
                    }
                }
            }
        }
        this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(-15,0.3));
        this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedZDistanceTask(15,0.3));
        this.m_RobotCore.getMotionSystem().addTask(this.m_RobotCore.getMotionSystem().getFixedXDistanceTask(15,0.3));
        this.m_RobotCore.getMotionSystem().waitUntilFinish();

        /*
        this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getFixedTurnTask(-deltaAng,Robot4100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED));
        this.m_RobotCore.getMotionSystem().waitUntilFinish();
        */
        this.m_RobotCore.getLinearActuator().setTargetPercent(0,Robot4100Setting.AUTONOMOUS_LINEARACTUATORSPEED,null);
    }
}
