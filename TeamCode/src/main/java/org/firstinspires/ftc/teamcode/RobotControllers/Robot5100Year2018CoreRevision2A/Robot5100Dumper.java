package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderServo;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotRackAndPinion;

public class Robot5100Dumper implements RobotRackAndPinion {
    private RobotEncoderServo m_Servo;
    public Robot5100Dumper(DcMotor RackAndPinionMotor, double CurrentPos){
        RackAndPinionMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RobotEncoderMotor Motor = new RobotEncoderMotor(RackAndPinionMotor,Robot5100Settings.dumperMotorCountsPerRev,Robot5100Settings.dumperRevPerSec,Robot5100Settings.dumperTimeControl,Robot5100Settings.dumperTimeControlPercent);
        this.m_Servo = new RobotEncoderServo(Motor,CurrentPos, Robot5100Settings.dumperBiggestPos,Robot5100Settings.dumperSmallestPos,true);
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
        this.m_Servo.setPosition(position,Robot5100Settings.dumperSpeed);
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

    @Override
    public void adjustPosition(double currentPos){
        this.m_Servo.adjustPosition(currentPos);
    }
}
