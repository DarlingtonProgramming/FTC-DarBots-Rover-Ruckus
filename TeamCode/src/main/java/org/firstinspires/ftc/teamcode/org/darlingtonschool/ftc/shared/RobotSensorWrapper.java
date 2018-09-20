/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotSensorWrapper;
 * Additional Information:
 * The SensorPosition should be interpreted in the Robot Axis.
 */
package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

public class RobotSensorWrapper<SensorType> {
    private SensorType m_Sensor;
    private double[] m_SensorPosition;

    public RobotSensorWrapper(SensorType mySensor, double[] SensorPosition){
        this.m_Sensor = mySensor;
        this.m_SensorPosition = SensorPosition;
    }

    public SensorType getSensor(){
        return this.m_Sensor;
    }

    public void setSensor(SensorType mySensor){
        this.m_Sensor = mySensor;
    }

    public double getPosX(){
        return this.m_SensorPosition[0];
    }

    public void setPosX(double X){
        this.m_SensorPosition[0] = X;
    }

    public double getPosY(){
        return this.m_SensorPosition[1];
    }

    public void setPosY(double Y){
        this.m_SensorPosition[1] = Y;
    }

    public double[] getPos(){
        return this.m_SensorPosition;
    }

    public void setPos(double[] newPos){
        this.m_SensorPosition = newPos;
    }
}
