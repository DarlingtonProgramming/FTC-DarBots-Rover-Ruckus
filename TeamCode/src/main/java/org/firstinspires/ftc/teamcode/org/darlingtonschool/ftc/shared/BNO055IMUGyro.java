package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

public class BNO055IMUGyro {
    private BNO055IMU m_Gyro;
    private Orientation angles;
    public void BN055IMUGyro(HardwareMap hardwareMap, String GyroName){
        m_Gyro = hardwareMap.get(BNO055IMU.class,GyroName);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        m_Gyro.initialize(parameters);
    }

    public void updateData(){
        angles = m_Gyro.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }

    public float getHeading() {
        return this.getRawZ();
    }

    public float getRawX(){
        return angles.angleUnit.toDegrees(angles.thirdAngle);
    }

    public float getRawY(){
        return angles.angleUnit.toDegrees(angles.secondAngle);
    }

    public float getRawZ(){
        return angles.angleUnit.toDegrees(angles.firstAngle);
    }
}