package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.BNO055IMUGyro;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;

public class GyroWrapper implements RobotEventLoopable {
    private BNO055IMUGyro m_GyroSensor;
    private boolean m_DeltaAngleReverseDirection;
    private float m_CurrentAngle = 0;
    private float m_PreviousRead = 0;
    public GyroWrapper(OpMode myOpMode, String gyroSensorName, boolean isDirectionReversed, float initialAngle){
        this.m_DeltaAngleReverseDirection = isDirectionReversed;
        this.m_GyroSensor = new BNO055IMUGyro(myOpMode.hardwareMap,gyroSensorName);
        this.m_CurrentAngle = initialAngle;
        this.m_GyroSensor.updateData();
        this.m_PreviousRead = this.m_GyroSensor.getHeading();
    }

    public BNO055IMUGyro getGyro(){
        return this.m_GyroSensor;
    }

    public void updateData(){
        this.m_GyroSensor.updateData();
        float currentRead = this.m_GyroSensor.getHeading();
        float deltaAngle = currentRead - m_PreviousRead;
        if(m_DeltaAngleReverseDirection){
            deltaAngle = -deltaAngle;
        }
        this.m_CurrentAngle += deltaAngle;
        this.m_PreviousRead = currentRead;
    }

    public float getCurrentAngle(){
        return this.m_CurrentAngle;
    }

    public void adjustCurrentAngle(float currentAngle){
        this.updateData();
        this.m_CurrentAngle = currentAngle;
    }

    public boolean isDirectionReversed(){
        return this.m_DeltaAngleReverseDirection;
    }

    public void setDirectionReversed(boolean isReversed){
        this.m_DeltaAngleReverseDirection = isReversed;
    }

    @Override
    public void doLoop(){
        this.updateData();
    }
}
