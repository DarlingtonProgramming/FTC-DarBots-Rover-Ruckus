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

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

import static android.os.SystemClock.sleep;

public class BN055IMUGyro implements RobotNonBlockingDevice {
    private BNO055IMU m_Gyro;
    private Orientation angles;
    private float m_XOffset = 0.0f, m_YOffset = 0.0f, m_ZOffset = 0.0f;
    public BN055IMUGyro(@NonNull HardwareMap hardwareMap, String GyroName){
        m_Gyro = hardwareMap.get(BNO055IMU.class,GyroName);
        initGyro();
    }

    protected void initGyro(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        m_Gyro.initialize(parameters);
        while(!this.m_Gyro.isGyroCalibrated()){
            sleep(50);
        }
        this.m_XOffset = 0.0f;
        this.m_YOffset = 0.0f;
        this.m_ZOffset = 0.0f;
        this.updateData();
    }

    public void resetGyro(){
        this.m_XOffset = this.getRawX();
        this.m_YOffset = this.getRawY();
        this.m_ZOffset = this.getRawZ();
    }

    public float getX(){
        return this.getRawX() - this.m_XOffset;
    }

    public float getY(){
        return this.getRawY() - this.m_YOffset;
    }

    public float getZ(){
        return this.getRawZ() - this.m_ZOffset;
    }

    public void updateData(){
        angles = m_Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }

    public float getHeading() {
        return this.getZ();
    }

    public float getRawX(){
        return AngleUnit.DEGREES.normalize(angles.angleUnit.toDegrees(angles.thirdAngle));
    }

    public float getRawY(){
        return AngleUnit.DEGREES.normalize(angles.angleUnit.toDegrees(angles.secondAngle));
    }

    public float getRawZ(){
        return AngleUnit.DEGREES.normalize(angles.angleUnit.toDegrees(angles.firstAngle));
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void updateStatus(){
        this.updateData();
    }

    @Override
    public void waitUntilFinish() {
        return;
    }
}