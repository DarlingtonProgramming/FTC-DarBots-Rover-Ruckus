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