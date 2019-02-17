package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotCore;
import org.firstinspires.ftc.teamcode.RobotControllers.Util.ChassisFrictionCalibration;

@Disabled
@TeleOp(name = "Robot4100TeleOp-FrictionCalibration",group = "4100")
public class Robot4100FrictionCalibration extends ChassisFrictionCalibration {
    Robot4100Core m_RobotCore;

    protected void hardwareInit(){
        this.m_RobotCore = new Robot4100Core(this,null,false,false,false);
    }
    @Override
    public RobotCore getRobotCore() {
        return m_RobotCore;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareInit();
        this.waitForStart();
        super.startChassisTest();
    }
}
