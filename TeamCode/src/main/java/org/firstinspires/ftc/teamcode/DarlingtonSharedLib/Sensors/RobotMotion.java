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

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Internals.CustomDataCarrier;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.DcMotorCyclesTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionCallBack;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorCallBackToMotionCallBack;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

public class RobotMotion{
    private RobotMotor m_Motor;
    private RobotWheel m_Wheel;
    public RobotMotion(@NonNull RobotMotor Motor, @NonNull RobotWheel Wheel){
        this.m_Motor = Motor;
        this.m_Wheel = Wheel;
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
    public void addDistanceTask(double Distance,double Speed,RobotMotionCallBack MotionCallBack,boolean TimeControl, double TimeControlExcessPercent){
        this.getMotor().addCycleTask(new DcMotorCyclesTask(Speed,this.getMotor(),Distance/this.getWheel().getPerimeter(),new RobotMotorCallBackToMotionCallBack(this.getMotor(),this.getWheel(),MotionCallBack),TimeControl,TimeControlExcessPercent));
    }
    public double getLastTaskMovedDistance(){
        return this.getMotor().getLastTaskMovedCycle() * this.getWheel().getPerimeter();
    }
}
