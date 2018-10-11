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
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;
 */

package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;

public class RobotNonBlockingMotor implements RobotEventLoopable{
    enum workType{
        ToPosition,
        FixedSpeed
    }
    private static final double EXCESSTIMEPCT = 30;//Percent of time excess
    private workType m_runningType = workType.ToPosition;
    private DcMotor m_DCMotor;
    private double m_CountsPerRev = 0;
    private double m_RevPerSec = 0;
    private ElapsedTime m_MotorOperationTime;
    private int m_OriginLocation = 0;
    private boolean m_isWorking = false;
    private double m_FineTime = 0;
    private int m_MovedCounts = 0;
    private boolean m_TimeControl = false;

    public RobotNonBlockingMotor(DcMotor RobotDcMotor, double CountsPerRev, double RevPerSec, boolean TimeControl){
        this.m_DCMotor = RobotDcMotor;
        this.m_CountsPerRev = CountsPerRev;
        this.m_RevPerSec = RevPerSec;
        this.m_DCMotor.setPower(0);
        this.m_DCMotor.setTargetPosition(this.m_DCMotor.getCurrentPosition());
        this.m_DCMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.m_DCMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.m_MotorOperationTime = new ElapsedTime();
        this.m_isWorking = false;
        this.m_TimeControl = TimeControl;
    }

    public boolean getTimeControlEnabled(){
        return this.m_TimeControl;
    }

    public void setTimeControlEnabled(boolean Enabled){
        this.m_TimeControl = Enabled;
    }

    public void waitMotorOperationFinish(){
        while(this.isBusy()){
            this.doLoop();
        }
    }

    public boolean isBusy(){
        return this.m_isWorking;
    }

    public boolean isRunningFixedSpeed(){
        return (this.isBusy() && this.m_runningType == workType.FixedSpeed);
    }

    public DcMotor getDcMotor() {
        return this.m_DCMotor;
    }

    public void setDcMotor(DcMotor myDCMotor) {
        this.m_DCMotor = myDCMotor;
    }

    public double getCountsPerRev(){
        return this.m_CountsPerRev;
    }

    public void setCountsPerRev(double CountsPerRev){
        this.m_CountsPerRev = CountsPerRev;
    }

    public double getRevPerSec(){
        return this.m_RevPerSec;
    }

    public void setRevPerSec(double revPerSec){
        this.m_RevPerSec = revPerSec;
    }

    public void moveCounts(int RevTotal, double Power) throws RuntimeException{
        RobotDebugger.addDebug("RobotNonBlockingMotor","moveRev (" + RevTotal + ", " + Power + ")");
        int StartPos = this.m_DCMotor.getTargetPosition();
        if(RevTotal == 0){
            if(!this.m_isWorking) {
                this.m_OriginLocation = StartPos;
                this.m_MovedCounts = 0;
            }
            return;
        }

        this.m_DCMotor.setPower(Power);
        this.m_DCMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.m_DCMotor.setTargetPosition(StartPos + RevTotal);

        if(!this.m_isWorking || this.m_runningType != workType.ToPosition) {
            this.m_OriginLocation = StartPos;
            this.m_MotorOperationTime.reset();
            this.m_runningType = workType.ToPosition;
        }
        this.m_isWorking = true;

        double EstimatedTime = Math.abs(((double) RevTotal) / ((double) this.getRevPerSec()));
        double FineTime = EstimatedTime * (1.0 + (this.EXCESSTIMEPCT / 100.0));

        this.m_FineTime += FineTime;
    }

    public void moveWithFixedSpeed(double speed){
        RobotDebugger.addDebug("RobotNonBlockingMotor","moveWithFixedSpeed (" + speed + ")");
        int StartPos = this.m_DCMotor.getCurrentPosition();
        if(speed == 0){
            if(!this.m_isWorking) {
                this.m_OriginLocation = StartPos;
                this.m_MovedCounts = 0;
            }
            return;
        }

        this.m_DCMotor.setPower(speed);
        this.m_DCMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(!this.m_isWorking || this.m_runningType != workType.FixedSpeed) {
            this.m_OriginLocation = StartPos;
            this.m_MotorOperationTime.reset();
            this.m_runningType = workType.FixedSpeed;
        }
        this.m_isWorking = true;
        this.m_FineTime = 0;
    }

    public int stopRunning_getMovedCounts(){
        if(!this.m_isWorking){
            return this.getLastMovedCounts();
        }
        this.m_isWorking = false;
        this.m_DCMotor.setTargetPosition(this.m_DCMotor.getCurrentPosition());
        this.m_DCMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.m_DCMotor.setPower(0);
        this.m_MovedCounts = this.m_DCMotor.getCurrentPosition() - this.m_OriginLocation;
        this.m_FineTime = 0;
        return this.getLastMovedCounts();
    }

    public double stopRunning_getMovedCycle(){
        return ((double) this.stopRunning_getMovedCounts()) / ((double) this.getCountsPerRev());
    }


    public void moveCycle(double Cycle, double Power) throws RuntimeException{
        int Rev = (int) Math.round(Cycle * this.getCountsPerRev());
        this.moveCounts(Rev,Power);
    }

    public int getLastMovedCounts(){
        return this.m_MovedCounts;
    }

    public double getLastMovedCycle(){
        return ((double) this.getLastMovedCounts()) / ((double) this.getCountsPerRev());
    }

    public int getRemainingCounts(){
        if(!this.m_isWorking){
            return 0;
        }
        return this.m_DCMotor.getTargetPosition() - this.m_DCMotor.getCurrentPosition();
    }

    @Override
    public void doLoop(){
        if(!this.m_isWorking){
            return;
        }
        if(this.m_runningType == workType.ToPosition) {
            boolean workFinished = false;
            if (this.m_DCMotor.isBusy()) {
                if (this.m_MotorOperationTime.time() > this.m_FineTime) {
                    if (this.m_TimeControl) {
                        workFinished = true;
                    }
                }
            } else {
                workFinished = true;
            }
            if (workFinished) {
                this.m_isWorking = false;
                this.m_DCMotor.setTargetPosition(this.m_DCMotor.getCurrentPosition());
                this.m_DCMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                this.m_DCMotor.setPower(0);
                this.m_MovedCounts = this.m_DCMotor.getCurrentPosition() - this.m_OriginLocation;
                this.m_FineTime = 0;
                RobotDebugger.addDebug("RobotNonBlockingMotor", "moveRevEnd (" + this.m_MovedCounts + ")");
            }
        }
    }
}
