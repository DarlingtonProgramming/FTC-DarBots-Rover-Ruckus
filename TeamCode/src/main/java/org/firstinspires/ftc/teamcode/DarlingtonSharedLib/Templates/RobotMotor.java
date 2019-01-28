package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

public interface RobotMotor extends RobotNonBlockingDevice {
    public enum MovingType{
        toCount,
        withSpeed,
        reset
    }
    DcMotor getDcMotor();
    void setDcMotor(@NonNull DcMotor Motor);
    RobotMotorType getMotorType();
    void setMotorType(@NonNull RobotMotorType MotorType);
    int getCurrentCount();
    int getTargetCount();
    void setTargetCount(int Count);
    double getPower();
    void setPower(double Pwr);
    boolean isDirectionReversed();
    void setDirectionReversed(boolean isReversed);
    DcMotor.ZeroPowerBehavior getZeroPowerBehavior();
    void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior ZeroPwrBehavior);
    MovingType getCurrentMovingType();
    void setCurrentMovingType(MovingType movingType);
}
