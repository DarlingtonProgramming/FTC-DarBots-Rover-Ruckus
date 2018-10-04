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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision1A;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotRackAndPinion;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;

public class Robot5100RackAndPinion implements RobotEventLoopable, RobotRackAndPinion{
    private RobotNonBlockingMotor m_myReckAndPinion;
    private final static double RNPCycleNum = 10.50;
    private final static double RNPMotorRevPerCycle = 1120;
    private final static double RNPMotorRevPerSec = 2.67*RNPMotorRevPerCycle;
    private double m_CurrentCycle = 0;
    private boolean m_isBusy = false;
    public Robot5100RackAndPinion(DcMotor ReckAndPinionMotor, double CurrentPercent){
        this.m_myReckAndPinion = new RobotNonBlockingMotor(ReckAndPinionMotor,RNPMotorRevPerCycle,RNPMotorRevPerSec,false);
        this.m_CurrentCycle = CurrentPercent / 100 * RNPCycleNum;
    }
    public double getCurrentPercent(){
        return this.m_CurrentCycle / RNPCycleNum * 100.0;
    }
    public boolean isBusy(){
        return m_isBusy;
    }
    public void setPosition(double Percent){
        if(Percent < 0 || Percent > 100){
            return;
        }
        double TargetCycle = Percent / 100 * RNPCycleNum;
        this.m_myReckAndPinion.moveCycle(TargetCycle - m_CurrentCycle,1.0);
        this.m_isBusy = true;
    }

    public void waitReckAndPinionFinish(){
        while(this.isBusy()){
            this.doLoop();
        }
    }

    public void doLoop(){
        this.m_myReckAndPinion.doLoop();
        if(this.m_isBusy && !this.m_myReckAndPinion.isBusy()){
            this.m_isBusy = false;
            double movedCycle = this.m_myReckAndPinion.getLastMovedCycle();
            this.m_CurrentCycle += movedCycle;
        }
    }
}
