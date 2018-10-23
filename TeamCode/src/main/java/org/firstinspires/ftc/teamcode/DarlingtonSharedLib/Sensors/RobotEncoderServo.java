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

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.DcMotorCountsTask;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;


public class RobotEncoderServo implements RobotEventLoopable {
    private RobotEncoderMotor m_Motor;
    private int m_StartCount = 0;
    private double m_SmallestPos = 0;
    private double m_BiggestPos = 0;
    private boolean m_isWorking = false;
    private boolean m_LockPosition;
    public RobotEncoderServo(@NonNull RobotEncoderMotor Motor, double CurrentPosition, double BiggestPos, double SmallestPos, boolean lockPosition){
        this.m_Motor = Motor;
        this.m_StartCount = Motor.getDcMotor().getCurrentPosition() - (int) Math.round(CurrentPosition * Motor.getCountsPerRev());
        this.m_BiggestPos = BiggestPos;
        this.m_SmallestPos = SmallestPos;

        this.m_LockPosition = lockPosition;
        this.m_isWorking = false;

        if(this.isLockingPosition()) {
            //Locking Position from the beginning.
            this.m_Motor.getDcMotor().setTargetPosition(this.m_Motor.getDcMotor().getCurrentPosition());
            this.m_Motor.getDcMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
            this.m_Motor.getDcMotor().setPower(1.0);
        }
    }
    public boolean isLockingPosition(){
        return this.m_LockPosition;
    }

    public void setLockingPosition(boolean lockingPosition){
        this.m_LockPosition = lockingPosition;
    }

    public boolean isBusy(){
        return this.m_Motor.isBusy();
    }
    public double getSmallestPos(){
        return this.m_SmallestPos;
    }
    public void setSmallestPos(double SmallestPos){
        this.m_SmallestPos = SmallestPos;
    }
    public double getBiggestPos(){
        return this.m_BiggestPos;
    }
    public void setBiggestPos(double BiggestPos){
        this.m_BiggestPos = BiggestPos;
    }
    public RobotEncoderMotor getMotor(){
        return m_Motor;
    }
    public double getPosition(){
        int movedCounts = 0;
        movedCounts = this.m_Motor.getDcMotor().getCurrentPosition() - this.m_StartCount;
        return movedCounts / this.m_Motor.getCountsPerRev();
    }
    public double getTargetPosition(){
        int movedCounts = 0;
        movedCounts = this.m_Motor.getDcMotor().getTargetPosition() - this.m_StartCount;
        return movedCounts / this.m_Motor.getCountsPerRev();
    }

    public void setPosition(double Pos, double Speed, boolean timeControl, double TimeControlPct){
        if(Pos > this.getBiggestPos())
            return;
        if(Pos < this.getSmallestPos())
            return;
        int TargetPos = 0;
        TargetPos = this.m_StartCount + (int) Math.round(Pos * this.m_Motor.getCountsPerRev());
        int DeltaCount = TargetPos - this.m_Motor.getDcMotor().getTargetPosition();
        this.m_Motor.deleteAllTasks();
        DcMotorCountsTask CountsTask = new DcMotorCountsTask(Speed,DeltaCount,null,timeControl,TimeControlPct);
        this.m_Motor.addCountsTask(CountsTask);
        this.m_isWorking = true;
    }

    public void doLoop(){
        this.m_Motor.doLoop();
        if(this.m_isWorking && !this.m_Motor.isBusy()){
            if(this.isLockingPosition()) {
                this.m_Motor.getDcMotor().setTargetPosition(this.m_Motor.getDcMotor().getCurrentPosition());
                this.m_Motor.getDcMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                this.m_Motor.getDcMotor().setPower(1.0);
            }
            this.m_isWorking = false;
        }
    }
    public void adjustPosition(double currentPos){
        this.m_StartCount = this.m_Motor.getDcMotor().getCurrentPosition() - (int) Math.round(currentPos * this.m_Motor.getCountsPerRev());
    }
}
