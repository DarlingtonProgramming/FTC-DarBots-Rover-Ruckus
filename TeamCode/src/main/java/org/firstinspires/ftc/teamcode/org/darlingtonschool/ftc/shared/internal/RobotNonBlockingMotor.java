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
    private static final double EXCESSTIMEPCT = 30;//Percent of time excess
    private DcMotor m_DCMotor;
    private double m_RevPerCycle = 0;
    private double m_RevPerSec = 0;
    private ElapsedTime m_MotorOperationTime;
    private int m_OriginLocation = 0;
    private boolean m_isWorking = false;
    private double m_FineTime = 0;
    private int m_MovedRev = 0;
    private boolean m_TimeControl = false;

    public RobotNonBlockingMotor(DcMotor RobotDcMotor, double RevPerCycle, double RevPerSec, boolean TimeControl){
        this.m_DCMotor = RobotDcMotor;
        this.m_RevPerCycle = RevPerCycle;
        this.m_RevPerSec = RevPerSec;
        this.m_DCMotor.setPower(1.0);
        this.m_DCMotor.setTargetPosition(this.m_DCMotor.getCurrentPosition());
        this.m_DCMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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

    public boolean isBusy(){
        return this.m_isWorking;
    }

    public DcMotor getDcMotor() {
        return this.m_DCMotor;
    }

    public void setDcMotor(DcMotor myDCMotor) {
        this.m_DCMotor = myDCMotor;
    }

    public double getRevPerCycle(){
        return this.m_RevPerCycle;
    }

    public void setRevPerCycle(double revPerCycle){
        this.m_RevPerCycle = revPerCycle;
    }

    public double getRevPerSec(){
        return this.m_RevPerSec;
    }

    public void setRevPerSec(double revPerSec){
        this.m_RevPerSec = revPerSec;
    }

    public void moveRev(int RevTotal, double Power) throws RuntimeException{
        RobotDebugger.addDebug("RobotNonBlockingMotor","moveRev (" + RevTotal + ", " + Power + ")");
        int StartPos = this.m_DCMotor.getTargetPosition();
        if(RevTotal == 0){
            if(!this.m_isWorking) {
                this.m_OriginLocation = StartPos;
                this.m_MovedRev = 0;
            }
            return;
        }

        this.m_DCMotor.setPower(Power);
        this.m_DCMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.m_DCMotor.setTargetPosition(StartPos + RevTotal);

        if(!this.m_isWorking) {
            this.m_OriginLocation = StartPos;
            this.m_MotorOperationTime.reset();
        }
        this.m_isWorking = true;

        double EstimatedTime = Math.abs(((double) RevTotal) / ((double) this.getRevPerSec()));
        double FineTime = EstimatedTime * (1.0 + (this.EXCESSTIMEPCT / 100.0));

        this.m_FineTime += FineTime;
    }



    public void moveCycle(double Cycle, double Power) throws RuntimeException{
        int Rev = (int) Math.round(Cycle * this.getRevPerCycle());
        this.moveRev(Rev,Power);
    }

    public int getLastMovedRev(){
        return this.m_MovedRev;
    }

    public double getLastMovedCycle(){
        return ((double) this.m_MovedRev) / ((double) this.m_RevPerCycle);
    }

    public int getRemainingRev(){
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
        boolean workFinished = false;
        if(this.m_DCMotor.isBusy()){
            if(this.m_MotorOperationTime.time() > this.m_FineTime){
                if(this.m_TimeControl) {
                    workFinished = true;
                }
            }
        }else{
            workFinished = true;
        }
        if(workFinished){
            this.m_isWorking = false;
            this.m_DCMotor.setTargetPosition(this.m_DCMotor.getCurrentPosition());
            this.m_MovedRev = this.m_DCMotor.getCurrentPosition() - this.m_OriginLocation;
            this.m_FineTime = 0;
            RobotDebugger.addDebug("RobotNonBlockingMotor","moveRevEnd (" + this.m_MovedRev + ")");
        }
    }
}
