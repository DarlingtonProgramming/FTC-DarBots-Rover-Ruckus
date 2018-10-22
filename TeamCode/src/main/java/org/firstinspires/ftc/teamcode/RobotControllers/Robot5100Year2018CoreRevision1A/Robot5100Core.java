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

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.BNO055IMUGyro;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotMotionSystem;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotNonBlockingServoUsingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingNoEncoderMotor;

public class Robot5100Core implements RobotMotionSystem, RobotEventLoopable {
    public static final double LinearApproachBiggestVal = 3.2;
    private static final double LinearApproachSmallestVal = -200;
    private static final double RackAndPinionHookPos = 2.480;
    public static final double CollectorServoBiggestVal = 0.40;
    private static final double CollectorServoSmallestVal = 0;
    private Robot5100MotionSystem m_MotionSystem;
    private RobotPositionTracker m_PositionTracker;
    private Robot5100RackAndPinion m_RackAndPinion;
    private Servo m_DumperServo;
    private RobotNonBlockingNoEncoderMotor m_CollectorMotor;
    private RobotNonBlockingServoUsingMotor m_CollectorServo;
    private RobotNonBlockingServoUsingMotor m_LinearApproachMotor;
    private ColorSensor m_ColorSensor;
    private Servo m_ColorSensorServo;
    private BNO055IMUGyro m_GyroSensor;

    public Robot5100Core(@NonNull OpMode opModeController, double initialX, double initialY, double initialRotation, double initialRackAndPinionPos, double CollectorServoInitialPos, double LinearApproachInitialPos){
        //FIELD: 365.76 * 365.76 CM^2
        double[] leftTopExtreme = {-18, 18.0};
        double[] rightTopExtreme = {18.0, 18.0};
        double[] leftBotExtreme = {-18.0,-18.0};
        double[] rightBotExtreme = {18.0,-18.0};
        this.m_PositionTracker = new RobotPositionTracker(365.76,365.76,initialX,initialY,initialRotation,leftTopExtreme,rightTopExtreme,leftBotExtreme,rightBotExtreme);
        this.m_MotionSystem = new Robot5100MotionSystem(this.m_PositionTracker,opModeController.hardwareMap.dcMotor.get("motor0"), opModeController.hardwareMap.dcMotor.get("motor1"),opModeController.hardwareMap.dcMotor.get("motor2"), opModeController.hardwareMap.dcMotor.get("motor3"));
        DcMotor rackAndPinionMotor = opModeController.hardwareMap.dcMotor.get("rackMotor");
        this.m_RackAndPinion = new Robot5100RackAndPinion(rackAndPinionMotor,initialRackAndPinionPos);
        this.m_DumperServo = opModeController.hardwareMap.servo.get("dumperServo");
        this.m_CollectorMotor = new RobotNonBlockingNoEncoderMotor(opModeController.hardwareMap.dcMotor.get("collectorMotor"),288,2.08);
        DcMotor CollectorServoDcMotor = opModeController.hardwareMap.dcMotor.get("collectorServo");
        CollectorServoDcMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RobotNonBlockingMotor CollectorServoNonBlockingMotor = new RobotNonBlockingMotor(CollectorServoDcMotor,288,2.08,false,200);
        this.m_CollectorServo = new RobotNonBlockingServoUsingMotor(CollectorServoNonBlockingMotor,CollectorServoInitialPos,CollectorServoBiggestVal,CollectorServoSmallestVal);
        DcMotor LinearApproachDCMotor = opModeController.hardwareMap.dcMotor.get("linearApproachMotor");
        //LinearApproachDCMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RobotNonBlockingMotor LinearApproachServoNonBlockingMotor = new RobotNonBlockingMotor(LinearApproachDCMotor,1120,2.67,false,20);
        this.m_LinearApproachMotor = new RobotNonBlockingServoUsingMotor(LinearApproachServoNonBlockingMotor,0,LinearApproachBiggestVal,LinearApproachSmallestVal);
        //this.m_ColorSensor = opModeController.hardwareMap.colorSensor.get("colorSensor");
        //this.m_ColorSensorServo = opModeController.hardwareMap.servo.get("colorServo");
        this.m_GyroSensor = new BNO055IMUGyro(opModeController.hardwareMap,"imu");
    }

    public Robot5100RackAndPinion getRackAndPinion(){
        return this.m_RackAndPinion;
    }

    public Servo getDumperServo(){
        return this.m_DumperServo;
    }

    public RobotNonBlockingNoEncoderMotor getCollectorMotor(){
        return this.m_CollectorMotor;
    }

    public RobotNonBlockingServoUsingMotor getCollectorServo(){
        return this.m_CollectorServo;
    }

    public RobotNonBlockingServoUsingMotor getLinearApproachMotor(){
        return this.m_LinearApproachMotor;
    }

    public BNO055IMUGyro getGyroSensor(){
        return this.m_GyroSensor;
    }

    public ColorSensor getColorSensor(){
        return this.m_ColorSensor;
    }

    public Servo getColorSensorServo(){
        return this.m_ColorSensorServo;
    }

    public void setCollectingServoStayPos(){
        this.getCollectorServo().setPosition(0.2,1.0);
    }
    public void setCollectingServoOut(){
        this.getCollectorServo().setPosition(this.getCollectorServo().getBiggestPos(),0.5);
    }

    public void setCollectingServoIn(){
        this.getCollectorServo().setPosition(this.getCollectorServo().getSmallestPos(),0.5);
    }

    public void openLinearAppraoch(){
        this.getLinearApproachMotor().setPosition(this.getLinearApproachMotor().getBiggestPos(),1.0);
    }

    public void closeLinearApproach(){
        this.getLinearApproachMotor().setPosition(0.0,1.0);
    }

    public void startSuckingMinerals(double speed){
        this.getCollectorMotor().moveWithFixedSpeed(speed);
    }

    public void startVomitingMinerals(double speed){
        this.getCollectorMotor().moveWithFixedSpeed(-speed);
    }

    public void stopSuckingMinerals(){
        this.getCollectorMotor().stopRunning_getMovedRev();
    }

    public void dump(){
        this.stopSuckingMinerals();
        this.setCollectingServoOut();
        this.waitUntilMotionFinish();
        this.openRackAndPinion();
        this.waitUntilMotionFinish();
        this.getDumperServo().setPosition(0.0);
        this.getDumperServo().setPosition(1.0);
        this.closeRackAndPinion();
        this.waitUntilMotionFinish();
        this.setCollectingServoIn();
        this.waitUntilMotionFinish();
    }

    public void openRackAndPinion(){
        this.getRackAndPinion().setPosition(this.getRackAndPinion().getBiggestPos());
    }

    public void closeRackAndPinion(){
        this.getRackAndPinion().setPosition(this.getRackAndPinion().getSmallestPos());
    }

    public void setRackAndPinionHook(){
        this.getRackAndPinion().setPosition(this.RackAndPinionHookPos);
    }

    @Override
    public boolean isBusy(){
        return (this.m_MotionSystem.isBusy() || this.getRackAndPinion().isBusy() || this.getCollectorServo().isBusy() || this.getLinearApproachMotor().isBusy());
    }

    @Override
    public void waitUntilMotionFinish(){
        while(this.isBusy()){
            this.doLoop();
        }
    }

    @Override
    public RobotPositionTracker getPositionTracker(){
        return this.m_PositionTracker;
    }

    @Override
    public void setPositionTracker(RobotPositionTracker newPositionTracker){
        this.m_PositionTracker = newPositionTracker;
        this.m_MotionSystem.setPositionTracker(newPositionTracker);
    }

    @Override
    public void turnToAbsFieldAngle(double AngleInDegree, double Speed){
        this.m_MotionSystem.turnToAbsFieldAngle(AngleInDegree, Speed);
    }

    @Override
    public void turnOffsetAroundCenter(double AngleInDegree, double Speed){
        this.m_MotionSystem.turnOffsetAroundCenter(AngleInDegree, Speed);
    }

    @Override
    public void driveTo(double[] fieldPos, double Speed){
        this.m_MotionSystem.driveTo(fieldPos,Speed);
    }

    @Override
    public void driveForward(double Distance, double Speed) throws RuntimeException{
        this.m_MotionSystem.driveForward(Distance,Speed);
    }

    @Override
    public void driveBackward(double Distance, double Speed) throws RuntimeException{
        this.m_MotionSystem.driveBackward(Distance,Speed);
    }

    @Override
    public void driveToLeft(double Distance, double Speed) throws RuntimeException{
        this.m_MotionSystem.driveToLeft(Distance,Speed);
    }

    @Override
    public void driveToRight(double Distance, double Speed) throws RuntimeException{
        this.m_MotionSystem.driveToRight(Distance,Speed);
    }

    @Override
    public void driveForwardWithSpeed(double Speed){
        this.m_MotionSystem.driveForwardWithSpeed(Speed);
    }
    @Override
    public void driveBackwardWithSpeed(double Speed){
        this.m_MotionSystem.driveBackwardWithSpeed(Speed);
    }
    @Override
    public void driveToLeftWithSpeed(double Speed){
        this.m_MotionSystem.driveToLeftWithSpeed(Speed);
    }
    @Override
    public void driveToRightWithSpeed(double Speed){
        this.m_MotionSystem.driveToRightWithSpeed(Speed);
    }
    @Override
    public void stopDrivingWithSpeed(){
        this.m_MotionSystem.stopDrivingWithSpeed();
    }
    @Override
    public void keepTurningOffsetAroundCenter(double Speed){
        this.m_MotionSystem.keepTurningOffsetAroundCenter(Speed);
    }
    @Override
    public void stopTurningOffsetAroundCenter(){
        this.m_MotionSystem.stopTurningOffsetAroundCenter();
    }
    @Override
    public boolean isDrivingInDirectionWithSpeed(){
        return this.m_MotionSystem.isDrivingInDirectionWithSpeed();
    }
    @Override
    public boolean isKeepingTurningOffsetAroundCenter(){
        return this.m_MotionSystem.isKeepingTurningOffsetAroundCenter();
    }
    @Override
    public void doLoop(){
        this.m_CollectorMotor.doLoop();
        this.m_MotionSystem.doLoop();
        this.m_RackAndPinion.doLoop();
        this.m_GyroSensor.updateData();
        this.m_LinearApproachMotor.doLoop();
        this.m_CollectorServo.doLoop();
        RobotDebugger.addDebug("GyroX", "" + this.m_GyroSensor.getRawX());
        RobotDebugger.addDebug("GyroY", "" + this.m_GyroSensor.getRawY());
        RobotDebugger.addDebug("GyroZ", "" + this.m_GyroSensor.getRawZ());
        RobotDebugger.addDebug("CollectorServoPos","" + this.m_CollectorServo.getPosition());
        RobotDebugger.addDebug("LinearApproachPos", "" + this.m_LinearApproachMotor.getPosition());
        RobotDebugger.addDebug("RobotPosition", "" + this.m_PositionTracker.getCurrentPosX() + "," + this.m_PositionTracker.getCurrentPosY() + "(" + this.m_PositionTracker.getRobotRotation() + ")");
        RobotDebugger.addDebug("DumperServoPosition", "" + this.m_DumperServo.getPosition());
        RobotDebugger.addDebug("RackAndPinionPos", "" + this.getRackAndPinion().getPosition());
        RobotDebugger.doLoop();
    }
}
