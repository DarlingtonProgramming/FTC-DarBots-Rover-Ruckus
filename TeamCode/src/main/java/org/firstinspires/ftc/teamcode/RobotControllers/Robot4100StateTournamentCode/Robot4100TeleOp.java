package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.TeleOps.Gamepad1Drive;

@TeleOp(name = "Robot4100TeleOp", group = "4100")
public class Robot4100TeleOp extends Gamepad1Drive {
    public Robot4100TeleOp() {
        super(Robot4100Setting.TELEOP_GAMEPADTRIGGERVALUE,Robot4100Setting.TELEOP_BIGGESTDRIVINGSPEED);
    }

    protected void hardwareInit(){
        super.robotCoreInit(new Robot4100Core(this,null,true));
    }

    @Override
    public void runOpMode() {
        this.hardwareInit();
        waitForStart();
        while(this.opModeIsActive()){
            super.driveControl();
            super.getRobotCore().updateStatus();
            super.getRobotCore().getDebugger().addDebug(new RobotDebugger.RobotDebuggerInformation("Status","Running(BiggestDrive:" + super.TeleOpBiggestDrivingSpeed + ", TriggerVal:" + super.GamepadTriggerValue + ")"));
        }
    }
}
