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

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.BNO055IMUGyro;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;

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
