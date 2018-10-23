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

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Internals;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.DcMotorSpeedTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotorCallBack;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MotorSpeedSpecificJob<T> extends RobotMotorInternalJob<T> {
    DcMotorSpeedTask m_SpeedTask;
    RobotMotor m_Motor;
    boolean m_isWorking;
    private Timer m_TimerScheduller;
    private ElapsedTime m_ElpasedTime;

    public MotorSpeedSpecificJob(DcMotorSpeedTask SpeedTask, RobotMotor Motor){
        this.m_SpeedTask = SpeedTask;
        this.m_ElpasedTime = new ElapsedTime();
        this.m_TimerScheduller = new Timer();
        this.m_Motor = Motor;
        this.m_isWorking = false;
    }
    public DcMotorSpeedTask getSpeedTask(){
        return this.m_SpeedTask;
    }
    public void setSpeedTask(DcMotorSpeedTask Task){
        this.m_SpeedTask = Task;
    }
    public RobotMotor getMotor(){
        return this.m_Motor;
    }
    public void setMotor(RobotMotor Motor){
        this.m_Motor = Motor;
    }
    @Override
    public boolean isWorking(){
        return this.m_isWorking;
    }
    @Override
    public void StartDoingJob(){
        this.m_isWorking = true;
        this.m_ElpasedTime.reset();
        if(this.getSpeedTask().isTaskTimeControlEnabled()){
            this.m_TimerScheduller.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            getMotor().stopCurrentTask();
                        }
                    },
                    (int) Math.round(this.getSpeedTask().getTaskDurationInSec() * 1000.0)
            );
        }
    }
    @Override
    public double JobFinished(int CountsMoved){
        if(!this.isWorking()){
            return 0;
        }
        this.m_isWorking = false;
        this.m_TimerScheduller.cancel();
        RobotMotorCallBack CallBack = this.getSpeedTask().getMotorJobCallBack();
        if(CallBack != null){
            CallBack.motionFinish(CountsMoved,this.m_ElpasedTime.time(TimeUnit.SECONDS));
        }
        return this.m_ElpasedTime.time(TimeUnit.SECONDS);
    }
}
