package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name ="Robot5100TeleOP_NoMemory", group = "5100")
public class Robot5100TeleOp_NoMemory extends Robot5100TeleOp {
    @Override
    protected void hardwareInit(){
        this.m_RobotCore = new Robot5100Core(false,this,100,100,0,0);
    }
}
