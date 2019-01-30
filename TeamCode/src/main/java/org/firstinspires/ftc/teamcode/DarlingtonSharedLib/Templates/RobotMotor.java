/*
MIT License

Copyright (c) 2018 DarBots Collaborators

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/

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
