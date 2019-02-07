package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;

@TeleOp(name = "Robot4100TeleOp-NoMemory", group = "4100")
public class Robot4100TeleOp_NoMemory extends Robot4100TeleOp {
    @Override
    public void hardwareInit(){
        super.robotCoreInit(new Robot4100Core(this,null,false,false));
    }
}
