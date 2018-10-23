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

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Internals.RobotUUIDGen;

public class DcMotorCountsTask {
    private int m_MovingCounts = 0;
    private RobotMotorCallBack m_CallBack;
    private boolean m_TaskTimeControl;
    private double m_TaskExcessTimePct;
    private String m_TaskID;
    private double m_MovingSpeed;

    public DcMotorCountsTask(double Speed,int MovingCounts, RobotMotorCallBack MotorJobCallBack, boolean timeControl, double TaskExcessTimePercent){
        this.m_MovingCounts = MovingCounts;
        this.m_CallBack = MotorJobCallBack;
        this.m_TaskTimeControl = timeControl;
        this.m_TaskExcessTimePct = TaskExcessTimePercent;
        this.m_TaskID = RobotUUIDGen.getUUID();
        this.m_MovingSpeed = Speed;
    }
    public int getMovingCounts(){
        return this.m_MovingCounts;
    }
    public void setMovingCounts(int Counts){
        this.m_MovingCounts = Counts;
    }
    public RobotMotorCallBack getMotorJobCallBack(){
        return this.m_CallBack;
    }
    public void setMotorJobCallBack(RobotMotorCallBack JobCallBack){
        this.m_CallBack = JobCallBack;
    }
    public double getMovingSpeed(){
        return this.m_MovingSpeed;
    }
    public void setMovingSpeed(double Speed){
        this.m_MovingSpeed = Speed;
    }
    public boolean isTaskTimeControlEnabled(){
        return this.m_TaskTimeControl;
    }
    public void setTaskTimeControlEnabled(boolean Enabled){
        this.m_TaskTimeControl = Enabled;
    }
    public double getTaskTimeControlExcessPercent(){
        return this.m_TaskExcessTimePct;
    }
    public void setTaskTimeControlExcessPercent(double Percent){
        this.m_TaskExcessTimePct = Percent;
    }
    public String getTaskID(){
        return this.m_TaskID;
    }
    @Override
    public String toString(){
        return "DcMotorCountsTask(TaskID:" + this.getTaskID() + ")[Counts:" + this.getMovingCounts() + ",timeControl=" + (this.isTaskTimeControlEnabled() ? this.getTaskTimeControlExcessPercent() + "%" : "off") + "]";
    }
}
