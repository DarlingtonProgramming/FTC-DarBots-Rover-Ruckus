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
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotNonBlockingServoUsingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotRackAndPinion;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;

public class Robot5100RackAndPinion implements RobotEventLoopable, RobotRackAndPinion{
    private RobotNonBlockingServoUsingMotor m_RackAndPinionMotor;
    private final static double RNPCycleNum = 4.80;
    private final static int RNPMotorCountsPerCycle = 288;
    private final static double RNPMotorRevPerSec = 2.08;
    public Robot5100RackAndPinion(DcMotor RackAndPinionMotor, double CurrentPos){
        RackAndPinionMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RobotNonBlockingMotor RNPNonBlockingMotor = new RobotNonBlockingMotor(RackAndPinionMotor,RNPMotorCountsPerCycle,RNPMotorRevPerSec,true);
        this.m_RackAndPinionMotor = new RobotNonBlockingServoUsingMotor(RNPNonBlockingMotor,CurrentPos,RNPCycleNum,0);
    }
    public double getPosition(){
        return this.m_RackAndPinionMotor.getPosition();
    }
    public boolean isBusy(){
        return this.m_RackAndPinionMotor.isBusy();
    }
    public void setPosition(double Position){
        this.m_RackAndPinionMotor.setPosition(Position,1);
    }

    public void waitRackAndPinionFinish(){
        while(this.isBusy()){
            this.doLoop();
        }
    }

    public void doLoop(){
        this.m_RackAndPinionMotor.doLoop();
    }
    public double getBiggestPos(){
        return RNPCycleNum;
    }
    public double getSmallestPos(){
        return 0;
    }
}
