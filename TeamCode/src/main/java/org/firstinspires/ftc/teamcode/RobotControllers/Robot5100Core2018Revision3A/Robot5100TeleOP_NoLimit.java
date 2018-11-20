package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name ="Robot5100TeleOP_Maintenance", group = "5100")
public class Robot5100TeleOP_NoLimit extends Robot5100TeleOp{
    @Override
    protected void hardwareInit(){
        super.hardwareInit();
        super.m_RobotCore.getLinearActuator().setBiggestPos(1000);
        super.m_RobotCore.getLinearActuator().setSmallestPos(-1000);
    }
}
