package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

public class RobotEncoderMotion extends RobotMotion implements RobotNonBlockingDevice{
    public RobotEncoderMotion(@NonNull RobotEncoderMotor Motor, @NonNull RobotWheel Wheel){
        super(Motor,Wheel);
    }
    public RobotEncoderMotor getEncoderMotor(){
        return (RobotEncoderMotor) super.getMotor();
    }
    public void setEncoderMotor(RobotEncoderMotor EncoderMotor){
        super.setMotor(EncoderMotor);
    }

    @Override
    public boolean isBusy() {
        return this.getEncoderMotor().isBusy();
    }

    @Override
    public void waitUntilFinish() {
        this.getEncoderMotor().waitUntilFinish();
    }
}
