package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;

@Disabled
@Autonomous(name = "GASTC_5100BlockDetection", group = "GASTC")
public class GASTC_BlockDetection extends LinearOpMode {
    protected Robot5100Core m_RobotCore;
    protected void hardwareInit(){
        this.m_RobotCore = new Robot5100Core(
                false,
                true,
                this,
                100,
                100,
                0,
                0,
                0,
                0
        );
    }
    protected void hardwareDestroy(){
        this.m_RobotCore = null;
    }
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        RobotDebugger.addDebug("Status", "Initialized");
        RobotDebugger.doLoop();
        this.waitForStart();
        if (this.opModeIsActive()) {
            this.m_RobotCore.getMotionSystem().keepTurningOffsetAroundCenter(0.05);
            boolean yellowBlockFound = false;
            while(!yellowBlockFound && this.opModeIsActive()){
                FTC2018GameSpecificFunctions.MineralInformation mMineral = FTC2018GameSpecificFunctions.MineralInCenterY(this.m_RobotCore.getGameSpecificFunction().detectAllBlocksInCamera());
                if(mMineral.getMineralType() == FTC2018GameSpecificFunctions.MineralType.Gold){
                    yellowBlockFound = true;
                }
            }
            this.m_RobotCore.getMotionSystem().stopMoving();
            if(yellowBlockFound == true){
                this.m_RobotCore.getMotionSystem().driveForward(40,Robot5100Setting.AUTONOMOUS_BIGGESTDRIVINGSPEED);
            }
            while(this.opModeIsActive() && this.m_RobotCore.getMotionSystem().isBusy()){

            }
        }
        this.hardwareDestroy();
    }
}
