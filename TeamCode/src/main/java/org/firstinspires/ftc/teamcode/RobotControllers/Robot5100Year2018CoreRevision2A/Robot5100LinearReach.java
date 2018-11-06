package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderServo;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotRackAndPinion;

public class Robot5100LinearReach implements RobotRackAndPinion {
    private RobotEncoderServo m_Servo;
    public Robot5100LinearReach(DcMotor LinearReachMotor, double CurrentPos){
        LinearReachMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RobotEncoderMotor Motor = new RobotEncoderMotor(LinearReachMotor,Robot5100Settings.linearReachCountsPerRev,Robot5100Settings.linearReachRevPerSec,Robot5100Settings.linearReachTimeControl,Robot5100Settings.linearReachTimeControlPercent);
        this.m_Servo = new RobotEncoderServo(Motor,CurrentPos, Robot5100Settings.linearReachBiggestPos,Robot5100Settings.linearReachSmallestPos,false);
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
        this.m_Servo.setPosition(position,Robot5100Settings.linearReachSpeed);
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
