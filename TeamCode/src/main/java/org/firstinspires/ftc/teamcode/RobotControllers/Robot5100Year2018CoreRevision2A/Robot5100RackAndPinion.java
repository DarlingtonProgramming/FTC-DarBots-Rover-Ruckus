package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderServo;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotRackAndPinion;

public class Robot5100RackAndPinion implements RobotRackAndPinion {
    private RobotEncoderServo m_Servo;
    public Robot5100RackAndPinion(DcMotor RackAndPinionMotor){
        RackAndPinionMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RobotEncoderMotor Motor = new RobotEncoderMotor(RackAndPinionMotor,Robot5100Settings.rackAndPinionMotorCountsPerRev,Robot5100Settings.rackAndPinionRevPerSec,Robot5100Settings.rackAndPinionTimeControl,Robot5100Settings.rackAndPinionTimeControlPercent);
        this.m_Servo = new RobotEncoderServo(Motor,0,Robot5100Settings.rackAndPinionBiggestPos,Robot5100Settings.rackAndPinionSmallestPos,true);
    }
    @Override
    public double getPosition() {
        return this.m_Servo.getPosition();
    }

    @Override
    public boolean isBusy() {
        return this.m_Servo.isBusy();
    }

    @Override
    public void setPosition(double position) {
        this.m_Servo.setPosition(position,Robot5100Settings.rackAndPinionSpeed);
    }

    @Override
    public void waitRackAndPinionFinish() {
        while(this.isBusy()){
            this.doLoop();
        }
    }

    @Override
    public void doLoop() {
        this.m_Servo.doLoop();
    }

    @Override
    public double getBiggestPos() {
        return this.m_Servo.getBiggestPos();
    }

    @Override
    public double getSmallestPos() {
        return this.m_Servo.getBiggestPos();
    }
}
