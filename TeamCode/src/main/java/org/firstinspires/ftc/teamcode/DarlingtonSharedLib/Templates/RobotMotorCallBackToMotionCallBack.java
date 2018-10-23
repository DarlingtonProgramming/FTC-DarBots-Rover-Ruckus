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
 * To use this class, add the following code to your program.
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;
 * Additional Information:
 * I recommend you to use the X,Y values in CM.
 */

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotWheel;

public class RobotMotorCallBackToMotionCallBack implements RobotMotorCallBack {
    private RobotMotor m_Motor;
    private RobotWheel m_Wheel;
    private RobotMotionCallBack m_MotionCB;
    public RobotMotorCallBackToMotionCallBack(RobotMotor Motor, RobotWheel Wheel, RobotMotionCallBack MotionCallBack){
        this.m_Motor = Motor;
        this.m_Wheel = Wheel;
        this.m_MotionCB = MotionCallBack;
    }
    public RobotMotionCallBack getMotionCallBack(){
        return this.m_MotionCB;
    }
    public void setMotionCallBack(RobotMotionCallBack MotionCallBack){
        this.m_MotionCB = MotionCallBack;
    }
    public RobotMotor getMotor(){
        return this.m_Motor;
    }
    public void setMotor(RobotMotor Motor){
        this.m_Motor = Motor;
    }
    public RobotWheel getWheel(){
        return this.m_Wheel;
    }
    public void setWheel(RobotWheel Wheel){
        this.m_Wheel = Wheel;
    }

    @Override
    public void motionFinish(int countsMoved, double secondsSpent) {
        if(this.m_MotionCB == null){
            return;
        }
        this.m_MotionCB.motionFinish(countsMoved / this.getMotor().getCountsPerRev() * this.getWheel().getPerimeter(),secondsSpent);
    }
}
