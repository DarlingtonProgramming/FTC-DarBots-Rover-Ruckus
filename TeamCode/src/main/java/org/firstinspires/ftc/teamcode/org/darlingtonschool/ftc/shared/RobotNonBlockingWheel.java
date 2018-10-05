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

/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotNonBlockingWheel;
 */

package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotWheel;

public class RobotNonBlockingWheel implements RobotEventLoopable{
    private RobotWheel m_Wheel;
    private RobotNonBlockingMotor m_Motor;
    protected static double calculateRotation(double Angle){ // -180 <= Angle < 180
        while(Angle >= 180){
            Angle -= 360;
        }
        while(Angle < -180){
            Angle += 360;
        }
        return Angle;
    }
    public RobotNonBlockingWheel(RobotWheel Wheel, RobotNonBlockingMotor Motor){
        this.m_Wheel = Wheel;
        this.m_Motor = Motor;
    }
    public double getRadius(){
        return this.m_Wheel.getRadius();
    }
    public void setRadius(double newRadius){
        this.m_Wheel.setRadius(newRadius);
    }
    public double getInstalledAngle(){
        return this.m_Wheel.getInstalledAngle();
    }
    public void setInstalledAngle(double newInstalledAngle){
        this.m_Wheel.setInstalledAngle(newInstalledAngle);
    }

    public double getPerimeter(){
        return this.m_Wheel.getPerimeter();
    }
    public double getArcLength(double AngleInDegree){
        return this.m_Wheel.getArcLength(AngleInDegree);
    }
    public double[] getRobotAxisPerCycle(){
        return this.m_Wheel.getRobotAxisPerCycle();
    }
    public double[] getRobotAxisForAngle(double RotatedAngleInDegree){
        return this.m_Wheel.getRobotAxisForAngle(RotatedAngleInDegree);
    }
    public double calculateCycleForDistance(double Distance){
        return this.m_Wheel.calculateCycleForDistance(Distance);
    }
    public DcMotor getDcMotor() {
        return this.m_Motor.getDcMotor();
    }

    public void setDcMotor(DcMotor myDCMotor) {
        this.m_Motor.setDcMotor(myDCMotor);
    }

    public double getCountsPerRev(){
        return this.m_Motor.getCountsPerRev();
    }

    public void setCountsPerRev(double CountsPerRev){
        this.m_Motor.setCountsPerRev(CountsPerRev);
    }

    public double getRevPerSec(){
        return this.m_Motor.getRevPerSec();
    }

    public void setRevPerSec(double revPerSec){
        this.m_Motor.setRevPerSec(revPerSec);
    }

    public void waitMotorOperationFinish(){ this.m_Motor.waitMotorOperationFinish(); }

    public boolean isBusy(){
        return this.m_Motor.isBusy();
    }

    public void moveCounts(int RevTotal, double Power){
        this.m_Motor.moveCounts(RevTotal,Power);
    }

    public void moveWithFixedSpeed(double speed){
        this.m_Motor.moveWithFixedSpeed(speed);
    }

    public void moveCycle(double Cycle, double Power){
        this.m_Motor.moveCycle(Cycle,Power);
    }

    public int stopRunning_getMovedCounts(){
        return this.m_Motor.stopRunning_getMovedCounts();
    }

    public double stopRunning_getMovedCycle(){
        return this.m_Motor.stopRunning_getMovedCycle();
    }

    public double stopRunning_getMovedDistance(){ return this.stopRunning_getMovedCycle() * this.getPerimeter(); }

    public int getLastMovedCounts(){
        return this.m_Motor.getLastMovedCounts();
    }

    public double getLastMovedCycle(){
        return this.m_Motor.getLastMovedCycle();
    }

    public double getLastMovedDistance(){
        return this.getLastMovedCycle() * this.getPerimeter();
    }

    public int getRemainingCounts(){
        return this.m_Motor.getRemainingCounts();
    }

    public double getRemainingDistance(){
        return ((double) this.getRemainingCounts()) / ((double) this.getCountsPerRev()) * this.m_Wheel.getPerimeter();
    }

    public void moveDistance(double Distance, double Power){
        RobotDebugger.addDebug("RobotNonBlockingWheel","moveDistanceStart (" + Distance + ", " + Power + ")");
        double rotationCycles = Distance / this.getPerimeter();
        this.moveCycle(rotationCycles, Power);
    }

    public void moveRobotX(double X, double Power) throws RuntimeException{
        this.moveDistance(this.m_Wheel.calculateDistanceByRobotAxisX(X),Power);
    }

    public void moveRobotY(double Y, double Power) throws RuntimeException{
        this.moveDistance(this.m_Wheel.calculateDistanceByRobotAxisY(Y),Power);
    }

    public double moveRobotXWithFixedSpeed(double Power) throws RuntimeException{ //Returns X / Distance Factor, to get the X axis moved, just multiply the distance by the factor.
        double isPositiveOrNegativeDist = this.m_Wheel.calculateDistanceByRobotAxisX(Power);
        double realPower = 0;
        if(isPositiveOrNegativeDist < 0){
            realPower = -Math.abs(Power);
        }else{
            realPower = Math.abs(Power);
        }
        this.m_Motor.moveWithFixedSpeed(realPower);
        return this.m_Wheel.calculateXToDistanceFactor();
    }

    public double moveRobotYWithFixedSpeed(double Power) throws RuntimeException{ //Returns Y / Distance Factor, to get the Y axis moved, just multiply the distance by the factor.
        double isPositiveOrNegativeDist = this.m_Wheel.calculateDistanceByRobotAxisY(Power);
        double realPower = 0;
        if(isPositiveOrNegativeDist < 0){
            realPower = -Math.abs(Power);
        }else{
            realPower = Math.abs(Power);
        }
        this.m_Motor.moveWithFixedSpeed(realPower);
        return this.m_Wheel.calculateYToDistanceFactor();
    }

    @Override
    public void doLoop(){
        this.m_Motor.doLoop();
    }
}
