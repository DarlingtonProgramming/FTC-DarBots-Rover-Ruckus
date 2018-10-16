package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision1A;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.BNO055IMUGyro;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotMotionSystem;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotNonBlockingServoUsingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingMotor;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotNonBlockingNoEncoderMotor;

public class Robot5100Core implements RobotMotionSystem, RobotEventLoopable {
<<<<<<< HEAD
    private static final double LinearApproachRev = 8.0;
    private static final double RackAndPinionHookPos = 0.5;
=======
    private static final double LinearApproachRev = 1;
    private static final double RackAndPinionHookPct = 50;
>>>>>>> 5f93bab7f8562e7b5a27bb79b757a848ea2da695
    private Robot5100MotionSystem m_MotionSystem;
    private RobotPositionTracker m_PositionTracker;
    private Robot5100RackAndPinion m_RackAndPinion;
    private Servo m_DumperServo;
    private RobotNonBlockingNoEncoderMotor m_CollectorMotor;
    private RobotNonBlockingServoUsingMotor m_CollectorServo;
    private RobotNonBlockingServoUsingMotor m_LinearAppraochMotor;
    private ColorSensor m_ColorSensor;
    private Servo m_ColorSensorServo;
    private BNO055IMUGyro m_GyroSensor;

    public Robot5100Core(@NonNull OpMode opModeController, double initialX, double initialY, double initialRotation, double initialRackAndPinionPos, double CollectorServoInitialPos){
        //FIELD: 365.76 * 365.76 CM^2
        double[] leftTopExtreme = {-16.34, 16.34};
        double[] rightTopExtreme = {16.34, 16.34};
        double[] leftBotExtreme = {-16.34,-16.34};
        double[] rightBotExtreme = {16.34,-16.34};
        this.m_PositionTracker = new RobotPositionTracker(365.76,365.76,initialX,initialY,initialRotation,leftTopExtreme,rightTopExtreme,leftBotExtreme,rightBotExtreme);
        this.m_MotionSystem = new Robot5100MotionSystem(this.m_PositionTracker,opModeController.hardwareMap.dcMotor.get("motor0"), opModeController.hardwareMap.dcMotor.get("motor1"),opModeController.hardwareMap.dcMotor.get("motor2"), opModeController.hardwareMap.dcMotor.get("motor3"));
        DcMotor rackAndPinionMotor = opModeController.hardwareMap.dcMotor.get("rackMotor");
        this.m_RackAndPinion = new Robot5100RackAndPinion(rackAndPinionMotor,initialRackAndPinionPos);
        this.m_DumperServo = opModeController.hardwareMap.servo.get("dumperServo");
        this.m_CollectorMotor = new RobotNonBlockingNoEncoderMotor(opModeController.hardwareMap.dcMotor.get("collectorMotor"),288,2.08,false);
        this.m_CollectorServo = new RobotNonBlockingServoUsingMotor(opModeController.hardwareMap.dcMotor.get("collectorServo"),288, CollectorServoInitialPos);
        this.m_LinearAppraochMotor = new RobotNonBlockingServoUsingMotor(opModeController.hardwareMap.dcMotor.get("linearApproachMotor"),560*LinearApproachRev,0);
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

    public RobotNonBlockingServoUsingMotor getLinearAppraochMotor(){
        return this.m_LinearAppraochMotor;
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
        this.getCollectorServo().setPosition(0.5);
    }
    public void setCollectingServoOut(){
        this.getCollectorServo().setPosition(1.0);
    }

    public void setCollectingServoIn(){
        this.getCollectorServo().setPosition(0.0);
    }

    public void openLinearAppraoch(){
        this.setCollectingServoStayPos();
        this.getLinearAppraochMotor().setPosition(1.0);
    }

    public void closeLinearApproach(){
        this.setCollectingServoStayPos();
        this.getLinearAppraochMotor().setPosition(0.0);
    }

    public void startSuckingMinerals(){
        this.getCollectorMotor().moveWithFixedSpeed(-1.0);
    }

    public void startVomitingMinerals(){
        this.getCollectorMotor().moveWithFixedSpeed(1.0);
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
        try{
            wait(3000);
        }catch(Exception e){

        }
        this.getDumperServo().setPosition(1.0);
        this.closeRackAndPinion();
        this.waitUntilMotionFinish();
        this.setCollectingServoIn();
        this.waitUntilMotionFinish();
    }

    public void openRackAndPinion(){
        this.getRackAndPinion().setPosition(1.0);
    }

    public void closeRackAndPinion(){
        this.getRackAndPinion().setPosition(0.0);
    }

    public void setRackAndPinionHook(){
        this.getRackAndPinion().setPosition(this.RackAndPinionHookPos);
    }

    @Override
    public boolean isBusy(){
        return (this.m_MotionSystem.isBusy() || this.getRackAndPinion().isBusy() || this.getCollectorServo().isBusy() || this.getLinearAppraochMotor().isBusy());
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
    public double[] getCurrentFieldPos(){
        return this.m_PositionTracker.getCurrentPos();
    }

    @Override
    public double[] getRobotAxisFromFieldAxis(double[] FieldPosition){
        return this.getRobotAxisFromFieldAxis(FieldPosition);
    }

    @Override
    public double[] getFieldAxisFromRobotAxis(double[] RobotPosition){
        return this.m_MotionSystem.getFieldAxisFromRobotAxis(RobotPosition);
    }

    @Override
    public void setCurrentFieldPos(double[] Position){
        this.m_MotionSystem.setCurrentFieldPos(Position);
    }

    @Override
    public void turnToAbsFieldAngle(double AngleInDegree){
        this.m_MotionSystem.turnToAbsFieldAngle(AngleInDegree);
    }

    @Override
    public void turnOffsetAroundCenter(double AngleInDegree){
        this.m_MotionSystem.turnOffsetAroundCenter(AngleInDegree);
    }

    @Override
    public void driveTo(double[] fieldPos){
        this.m_MotionSystem.driveTo(fieldPos);
    }

    @Override
    public void driveForward(double Distance) throws RuntimeException{
        this.m_MotionSystem.driveForward(Distance);
    }

    @Override
    public void driveBackward(double Distance) throws RuntimeException{
        this.m_MotionSystem.driveBackward(Distance);
    }

    @Override
    public void driveToLeft(double Distance) throws RuntimeException{
        this.m_MotionSystem.driveToLeft(Distance);
    }

    @Override
    public void driveToRight(double Distance) throws RuntimeException{
        this.m_MotionSystem.driveToRight(Distance);
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
        this.m_PositionTracker.doLoop();
        this.m_RackAndPinion.doLoop();
        this.m_GyroSensor.updateData();
        RobotDebugger.addDebug("GyroX", "" + this.m_GyroSensor.getRawX());
        RobotDebugger.addDebug("GyroY", "" + this.m_GyroSensor.getRawY());
        RobotDebugger.addDebug("GyroZ", "" + this.m_GyroSensor.getRawZ());
        RobotDebugger.addDebug("CollectorServoPos","" + this.m_CollectorServo.getPosition());
        RobotDebugger.addDebug("LinearAppraochPos", "" + this.m_LinearAppraochMotor.getPosition());
        RobotDebugger.doLoop();
    }
}
