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


public class DcMotorCyclesTask extends DcMotorCountsTask {
    private double m_CountsPerRev;
    public DcMotorCyclesTask(double Speed, RobotMotor Motor, double Cycles, RobotMotorCallBack MotorJobCallBack, boolean timeControl, double timeControlExcessPercent){
        super(Speed,(int) Math.round(Cycles * Motor.getCountsPerRev()),MotorJobCallBack,timeControl,timeControlExcessPercent);
        m_CountsPerRev = Motor.getCountsPerRev();
    }
    public double getMovingCycles(){
        return super.getMovingCounts() / m_CountsPerRev;
    }
    public void setMovingCycles(double Cycles){
        super.setMovingCounts((int) Math.round(Cycles * this.m_CountsPerRev));
    }
}
