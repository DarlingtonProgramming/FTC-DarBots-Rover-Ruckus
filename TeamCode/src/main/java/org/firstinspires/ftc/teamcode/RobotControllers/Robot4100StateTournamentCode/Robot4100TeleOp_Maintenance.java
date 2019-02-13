package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Robot4100TeleOp-Maintenance",group = "4100")
public class Robot4100TeleOp_Maintenance extends Robot4100TeleOp_NoMemory {
    @Override
    public void hardwareInit(){
        super.hardwareInit();
        Robot4100Core myRobotCore = super.get4100Core();

        myRobotCore.getDrawerSlide().setBiggestPos(1000);
        myRobotCore.getDrawerSlide().setSmallestPos(-1000);

        myRobotCore.getDumperSlide().setBiggestPos(1000);
        myRobotCore.getDumperSlide().setSmallestPos(-1000);

        myRobotCore.getLinearActuator().setBiggestPos(1000);
        myRobotCore.getLinearActuator().setSmallestPos(-1000);

    }

}
