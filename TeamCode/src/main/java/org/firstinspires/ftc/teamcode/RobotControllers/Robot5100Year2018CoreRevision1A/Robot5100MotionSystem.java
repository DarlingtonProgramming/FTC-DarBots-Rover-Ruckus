package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision1A;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotMotionSystem;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotNonBlockingWheel;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotSensorWrapper;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotWheel;

import java.lang.reflect.Field;
public class Robot5100MotionSystem implements RobotMotionSystem, RobotEventLoopable {
    private RobotPositionTracker m_PositionTracker;
    private RobotSensorWrapper<RobotNonBlockingWheel> m_Motor0, m_Motor1, m_Motor2, m_Motor3;

    @Override
    public boolean isBusy(){
        return (this.m_Motor0.getSensor().isBusy() && this.m_Motor1.getSensor().isBusy() && this.m_Motor2.getSensor().isBusy() && this.m_Motor3.getSensor().isBusy());
    }

    @Override
    public void waitUntilMotionFinish(){
        while(this.isBusy()){
            this.doLoop();
        }
    }

    public Robot5100MotionSystem(RobotPositionTracker PositionTracker, DcMotor Motor0, DcMotor Motor1, DcMotor Motor2, DcMotor Motor3){
        this.m_PositionTracker = PositionTracker;
        //Robot CountsPerRev: 865
        //Motor Speed 150RPM
        //Diagonal Motor Distance Apart: 46.228 cm
        //Motor Position: {16.34, 16.34}
        //RobotWheel Timeout Control: false
        RobotWheel m_Wheel0 = new RobotWheel(5.0,45);
        RobotWheel m_Wheel1 = new RobotWheel(5.0,-45);
        RobotWheel m_Wheel2 = new RobotWheel(5.0,135);
        RobotWheel m_Wheel3 = new RobotWheel(5.0, -135);
        RobotNonBlockingMotor NBMotor0 = new RobotNonBlockingMotor(Motor0,865,2.5,false);
        RobotNonBlockingMotor NBMotor1 = new RobotNonBlockingMotor(Motor1,865,2.5,false);
        RobotNonBlockingMotor NBMotor2 = new RobotNonBlockingMotor(Motor2,865,2.5,false);
        RobotNonBlockingMotor NBMotor3 = new RobotNonBlockingMotor(Motor3,865,2.5,false);
        double[] Motor0Pos = {-16.34, 16.34};
        double[] Motor1Pos = {16.34, 16.34};
        double[] Motor2Pos = {-16.34,-16.34};
        double[] Motor3Pos = {16.34,-16.34};
        this.m_Motor0 = new RobotSensorWrapper<RobotNonBlockingWheel>(new RobotNonBlockingWheel(m_Wheel0,NBMotor0),Motor0Pos);
        this.m_Motor1 = new RobotSensorWrapper<RobotNonBlockingWheel>(new RobotNonBlockingWheel(m_Wheel1,NBMotor1),Motor1Pos);
        this.m_Motor2 = new RobotSensorWrapper<RobotNonBlockingWheel>(new RobotNonBlockingWheel(m_Wheel2,NBMotor2),Motor2Pos);
        this.m_Motor3 = new RobotSensorWrapper<RobotNonBlockingWheel>(new RobotNonBlockingWheel(m_Wheel3,NBMotor3),Motor3Pos);
    }

    public RobotSensorWrapper<RobotNonBlockingWheel> getMotor0() {
        return this.m_Motor0;
    }

    public void setMotor0(RobotSensorWrapper<RobotNonBlockingWheel> newMotor){
        this.m_Motor0 = newMotor;
    }

    public RobotSensorWrapper<RobotNonBlockingWheel> getMotor1() {
        return this.m_Motor1;
    }

    public void setMotor1(RobotSensorWrapper<RobotNonBlockingWheel> Motor){
        this.m_Motor1 = Motor;
    }

    public RobotSensorWrapper<RobotNonBlockingWheel> getMotor2() {
        return this.m_Motor2;
    }

    public void setMotor2(RobotSensorWrapper<RobotNonBlockingWheel> Motor){
        this.m_Motor2 = Motor;
    }

    public RobotSensorWrapper<RobotNonBlockingWheel> getMotor3() {
        return this.m_Motor3;
    }

    public void setMotor3(RobotSensorWrapper<RobotNonBlockingWheel> Motor){
        this.m_Motor3 = Motor;
    }

    @Override
    public RobotPositionTracker getPositionTracker(){
        return this.m_PositionTracker;
    }

    @Override
    public void setPositionTracker(RobotPositionTracker newPositionTracker){
        this.m_PositionTracker = newPositionTracker;
    }

    @Override
    public double[] getCurrentFieldPos(){
        return this.m_PositionTracker.getCurrentPos();
    }

    @Override
    public double[] getRobotAxisFromFieldAxis(double[] FieldPosition){
        return this.m_PositionTracker.robotAxisFromFieldAxis(FieldPosition);
    }

    @Override
    public double[] getFieldAxisFromRobotAxis(double[] RobotPosition){
        return this.m_PositionTracker.fieldAxisFromRobotAxis(RobotPosition);
    }

    @Override
    public void setCurrentFieldPos(double[] Position){
        this.m_PositionTracker.setCurrentPos(Position);
    }

    @Override
    public void turnToAbsFieldAngle(double AngleInDegree){
        double mDeltaAngle = AngleInDegree - this.m_PositionTracker.getRobotRotation();
        this.turnOffsetAroundCenter(mDeltaAngle);
    }

    @Override
    public void turnOffsetAroundCenter(double AngleInDegree){
        double[] robotPoint = {0,0};
        double turningDistance0 = this.m_PositionTracker.calculateDistanceToRotateAroundRobotPoint(robotPoint,this.m_Motor0.getPos(),AngleInDegree);
        double turningDistance1 = this.m_PositionTracker.calculateDistanceToRotateAroundRobotPoint(robotPoint,this.m_Motor1.getPos(),AngleInDegree);
        double turningDistance2 = this.m_PositionTracker.calculateDistanceToRotateAroundRobotPoint(robotPoint,this.m_Motor2.getPos(),AngleInDegree);
        double turningDistance3 = this.m_PositionTracker.calculateDistanceToRotateAroundRobotPoint(robotPoint,this.m_Motor3.getPos(),AngleInDegree);
        this.m_Motor0.getSensor().moveDistance(turningDistance0,1.0);
        this.m_Motor1.getSensor().moveDistance(turningDistance1,1.0);
        this.m_Motor2.getSensor().moveDistance(turningDistance2,1.0);
        this.m_Motor3.getSensor().moveDistance(turningDistance3,1.0);
        this.m_PositionTracker.rotateAroundRobotRelativePoint(robotPoint,AngleInDegree);
    }

    @Override
    public void driveTo(double[] fieldPos){
        double turningDeg = this.m_PositionTracker.calculateAngleDeltaMovingToFieldPoint(fieldPos);
        this.turnOffsetAroundCenter(turningDeg);
        double DistanceMoving = this.m_PositionTracker.calculateDistanceToFixedPoint(fieldPos);
        this.driveForward(DistanceMoving);
    }

    @Override
    public void driveForward(double Distance) throws RuntimeException{
        if(Math.abs(this.m_Motor0.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor1.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor2.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor3.getSensor().getRemainingDistance()) > Math.abs(Distance)){
            //return;
        }
        this.m_Motor0.getSensor().moveRobotY(Distance,1.0);
        this.m_Motor1.getSensor().moveRobotY(Distance, 1.0);
        this.m_Motor2.getSensor().moveRobotY(Distance,1.0);
        this.m_Motor3.getSensor().moveRobotY(Distance,1.0);
        this.m_PositionTracker.moveThroughRobotAngle(0,Distance);
    }

    @Override
    public void driveBackward(double Distance) throws RuntimeException{
        this.driveForward(-Distance);
    }

    @Override
    public void driveToLeft(double Distance) throws RuntimeException{
        this.driveToRight(Distance);
    }

    @Override
    public void driveToRight(double Distance) throws RuntimeException{
        if(Math.abs(this.m_Motor0.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor1.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor2.getSensor().getRemainingDistance()) > Math.abs(Distance) || Math.abs(this.m_Motor3.getSensor().getRemainingDistance()) > Math.abs(Distance)){
            //return;
        }
        this.m_Motor0.getSensor().moveRobotX(Distance,1.0);
        this.m_Motor1.getSensor().moveRobotX(Distance,1.0);
        this.m_Motor2.getSensor().moveRobotX(Distance,1.0);
        this.m_Motor3.getSensor().moveRobotX(Distance,1.0);
        this.m_PositionTracker.moveThroughRobotAngle(90,Distance);
    }

    @Override
    public void doLoop(){
        this.m_Motor0.getSensor().doLoop();
        this.m_Motor1.getSensor().doLoop();
        this.m_Motor2.getSensor().doLoop();
        this.m_Motor3.getSensor().doLoop();
    }
}
