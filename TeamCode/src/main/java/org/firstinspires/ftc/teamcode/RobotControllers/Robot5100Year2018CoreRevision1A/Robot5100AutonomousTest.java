package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision1A;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;

@Autonomous (name = "5100Test", group = "David Cao")
//@Disabled
public class Robot5100AutonomousTest extends LinearOpMode {


    private Robot5100MotionSystem m_MotionSystem;
    private RobotPositionTracker m_PositionTracker;

    private void hardwareInit(){
        //FIELD: 365.76 * 365.76 CM^2
        double[] Motor0Pos = {-16.34, 16.34};
        double[] Motor1Pos = {16.34, 16.34};
        double[] Motor2Pos = {-16.34,-16.34};
        double[] Motor3Pos = {16.34,-16.34};
        this.m_PositionTracker = new RobotPositionTracker(365.76,365.76,100,100,0,Motor0Pos,Motor1Pos,Motor2Pos,Motor3Pos);
        this.m_MotionSystem = new Robot5100MotionSystem(this.m_PositionTracker,this.hardwareMap.dcMotor.get("motor0"), this.hardwareMap.dcMotor.get("motor1"),this.hardwareMap.dcMotor.get("motor2"), this.hardwareMap.dcMotor.get("motor3"));
    }

    public void runOpMode(){
        this.hardwareInit();
        waitForStart();
        while(this.opModeIsActive()){
            this.m_MotionSystem.driveForward(30);
            while(this.m_MotionSystem.isBusy()){
                this.m_MotionSystem.doLoop();
            }
            this.m_MotionSystem.turnOffsetAroundCenter(90);
            while(this.m_MotionSystem.isBusy()){
                this.m_MotionSystem.doLoop();
            }
            this.m_MotionSystem.turnOffsetAroundCenter(-90);
            while(this.m_MotionSystem.isBusy()){
                this.m_MotionSystem.doLoop();
            }
            this.m_MotionSystem.driveBackward(30);
            while(this.m_MotionSystem.isBusy()){
                this.m_MotionSystem.doLoop();
            }
            this.m_MotionSystem.doLoop();
        }
    }
}
