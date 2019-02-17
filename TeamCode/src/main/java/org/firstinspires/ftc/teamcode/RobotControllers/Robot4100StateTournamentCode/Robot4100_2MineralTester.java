package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

import java.util.ArrayList;

@Disabled
@TeleOp(name = "2MineralTester",group = "4100")
public class Robot4100_2MineralTester extends LinearOpMode {
    protected Robot4100Core m_RobotCore;
    protected FTC2018GameSpecificFunctions.GoldPosType m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Unknown;
    protected void hardwareInit(){
        this.m_RobotCore = new Robot4100Core(this,new Robot2DPositionIndicator(0,0,0),false,false,true);
        this.m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Unknown;
        this.m_RobotCore.getDebugger().addDebuggerCallable(new RobotDebugger.ObjectDebuggerWrapper<>("GoldPos",new Object(){
            @Override
            public String toString(){
                return Robot4100_2MineralTester.this.m_GoldPosition.name();
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
        this.m_RobotCore.save();
    }

    protected void offHookAndDetectSample() {
        while (opModeIsActive()) {
            this.m_RobotCore.updateStatus();
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
                } else if(SilverInfos.size() >= 1) { //if(SilverInfos.size() == 1){
                    if (GoldInfo.getLeft() > SilverInfos.get(0).getLeft()) {
                        m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Center;
                    } else {
                        m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Left;
                    }
                }else{
                    if(GoldInfo.getLeft() < (GoldInfo.getWidth() / 2.0f)){
                        m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Left;
                    }else{
                        m_GoldPosition = FTC2018GameSpecificFunctions.GoldPosType.Center;
                    }
                }
            }
        }
    }
}
