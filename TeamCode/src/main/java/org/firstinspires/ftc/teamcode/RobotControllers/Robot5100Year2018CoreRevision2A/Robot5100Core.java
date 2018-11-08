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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.GyroWrapper;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotSetting;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotEncoderServo;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotNoEncoderMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;
import org.firstinspires.ftc.teamcode.RobotControllers.DarbotsPrivateInfo.PrivateSettings;

public class Robot5100Core implements RobotNonBlockingDevice, RobotEventLoopable {
    private RobotPositionTracker m_PositionTracker;
    private Robot5100MotionSystem m_MotionSystem;
    private GyroWrapper m_Gyro;
    private Robot5100RackAndPinion m_RackAndPinion;
    private Robot5100Dumper m_Dumper;
    private Robot5100LinearReach m_LinearReach;
    private RobotEncoderServo m_CollectorServo;
    private RobotNoEncoderMotor m_CollectorSweeper;
    private FTC2018GameSpecificFunctions m_2018Specific;
    private Servo m_DeclarationServo;

    public Robot5100Core(@NonNull OpMode runningOpMode, double initialX, double initialY, double initialRotation, double rackAndPinionPosition, double dumperPosition, double linearReachPosition, double collectorServoPos, boolean readSetting){
        m_Gyro = new GyroWrapper(runningOpMode,Robot5100Settings.gyroConfigurationName,Robot5100Settings.gyroReversed,(float) initialRotation);
        this.m_PositionTracker = new RobotPositionTracker(365.76,365.76,initialX,initialY,initialRotation,Robot5100Settings.leftFrontExtremePos,Robot5100Settings.rightFrontExtremePos,Robot5100Settings.leftBackExtremePos,Robot5100Settings.rightBackExtremePos);
        this.m_MotionSystem = new Robot5100MotionSystem(runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.frontMotorConfigurationName), runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.leftBackMotorConfigurationName), runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.rightBackMotorConfigurationName),this.m_PositionTracker);
        this.m_2018Specific = new FTC2018GameSpecificFunctions(runningOpMode,VuforiaLocalizer.CameraDirection.BACK,Robot5100Settings.phonePos,Robot5100Settings.phoneRotation,PrivateSettings.VUFORIALICENSE);
        this.m_RackAndPinion = new Robot5100RackAndPinion(runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.rackAndPinionConfigurationName),rackAndPinionPosition);
        this.m_Dumper = new Robot5100Dumper(runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.dumperConfigurationName),dumperPosition);
        this.m_LinearReach = new Robot5100LinearReach(runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.linearReachConfigurationName), linearReachPosition);
        DcMotor CollectorServoDcMotor = runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.collectorServoConfigurationName);
        RobotEncoderMotor CollectorServoEncoderMotor = new RobotEncoderMotor(CollectorServoDcMotor,Robot5100Settings.collectorServoCountsPerRev,Robot5100Settings.collectorServoRevPerSec,Robot5100Settings.collectorServoTimeControl,Robot5100Settings.collectorServoTimeControlPercent);
        this.m_CollectorServo = new RobotEncoderServo(CollectorServoEncoderMotor,Robot5100Settings.collectorServoInitialPos,Robot5100Settings.collectorServoBiggestPos,Robot5100Settings.collectorServoSmallestPos,false);
        DcMotor CollectorSweeperMotor = runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.collectorSweeperConfigurationName);
        CollectorSweeperMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.m_CollectorSweeper = new RobotNoEncoderMotor(CollectorSweeperMotor,Robot5100Settings.collectorSweeperCountsPerRev,Robot5100Settings.collectorSweeperRevPerSec);
        this.m_DeclarationServo = runningOpMode.hardwareMap.servo.get(Robot5100Settings.declarationServoConfigurationName);
        RobotDebugger.setTelemetry(runningOpMode.telemetry);
        RobotDebugger.setDebugOn(true);
        if(readSetting){
            this.readSavedPosition(initialX,initialY,initialRotation,rackAndPinionPosition,dumperPosition,linearReachPosition,collectorServoPos);
        }
    }

    public RobotEncoderServo getCollectorServo(){
        return this.m_CollectorServo;
    }

    public RobotNoEncoderMotor getCollectorSweeper(){
        return this.m_CollectorSweeper;
    }

    public void startSuckingMinerals(){
        this.m_CollectorSweeper.moveWithFixedSpeed(Robot5100Settings.collectorSweeperSpeed);
    }

    public void startVomitingMinerals(){
        this.m_CollectorSweeper.moveWithFixedSpeed(-Robot5100Settings.collectorSweeperSpeed);
    }

    public void closeMouth(){
        this.m_CollectorSweeper.stopRunning_getMovedRev();
    }

    public Robot5100LinearReach getLinearReach(){
        return this.m_LinearReach;
    }

    public GyroWrapper getGyro() {
        return this.m_Gyro;
    }

    public Robot5100Dumper getDumper(){
        return this.m_Dumper;
    }

    public Robot5100RackAndPinion getRackAndPinion(){
        return this.m_RackAndPinion;
    }

    public void openRackAndPinionToHook(){
        this.m_RackAndPinion.setPosition(Robot5100Settings.rackAndPinionHookPos);
    }
    public void closeRackAndPinion(){
        this.m_RackAndPinion.setPosition(this.m_RackAndPinion.getSmallestPos());
    }
    public void openRackAndPinion(){
        this.m_RackAndPinion.setPosition(this.m_RackAndPinion.getBiggestPos());
    }

    public void openLinearReach(){
        this.m_LinearReach.setPosition(this.m_LinearReach.getBiggestPos());
    }

    public void closeLinearReach(){
        this.m_LinearReach.setPosition(this.m_LinearReach.getSmallestPos());
    }

    public void openCollectorServo(){
        this.m_CollectorServo.setPosition(this.m_CollectorServo.getBiggestPos(),Robot5100Settings.collectorServoSpeed);
    }

    public void closeCollectorServo(){
        this.m_CollectorServo.setPosition(this.m_CollectorServo.getSmallestPos(),Robot5100Settings.collectorServoSpeed);
    }

    public void raiseDumper(){
        this.m_Dumper.setPosition(this.m_Dumper.getBiggestPos());
    }

    public void descendDumper(){
        this.m_Dumper.setPosition(this.m_Dumper.getSmallestPos());
    }

    public Servo getDeclarationServo(){
        return this.m_DeclarationServo;
    }

    public void openDeclarationServo(){
        this.m_DeclarationServo.setPosition(1.0);
    }

    public void closeDeclarationServo(){
        this.m_DeclarationServo.setPosition(0.0);
    }

    public Robot5100MotionSystem getMotionSystem(){
        return this.m_MotionSystem;
    }

    public RobotPositionTracker getPositionTracker(){
        return this.m_PositionTracker;
    }

    public FTC2018GameSpecificFunctions getGameSpecificFunction(){
        return this.m_2018Specific;
    }
    public void readSavedPosition(double defaultX, double defaultY, double defaultRotations, double defaultRackAndPinionPos, double defaultDumperPos, double defaultLinearReachPos, double defaultCollectorServoPos){
        RobotSetting.settingFile = Robot5100Settings.posSaveFile;
        double X = RobotSetting.getSetting("RobotX",new Double(defaultX));
        double Y = RobotSetting.getSetting("RobotY",new Double(defaultY));
        double Rotation = RobotSetting.getSetting("Rotation", new Double(defaultRotations));
        double rackAndPinionPos = RobotSetting.getSetting("rackPosition", new Double(defaultRackAndPinionPos));
        double dumperPos = RobotSetting.getSetting("dumperPosition",new Double(defaultDumperPos));
        double linearReachPos = RobotSetting.getSetting("linearReachPosition", new Double(defaultLinearReachPos));
        double collectorServoPos = RobotSetting.getSetting("collectorServoPosition", new Double(defaultCollectorServoPos));
        this.m_PositionTracker.setCurrentPosX(X);
        this.m_PositionTracker.setCurrentPosY(Y);
        this.m_PositionTracker.setRobotRotation(Rotation);
        this.m_Gyro.adjustCurrentAngle((float) Rotation);
        this.m_RackAndPinion.adjustPosition(rackAndPinionPos);
        this.m_Dumper.adjustPosition(dumperPos);
        this.m_LinearReach.adjustPosition(linearReachPos);
        this.m_CollectorServo.adjustPosition(collectorServoPos);
    }

    public void savePosition(){
        RobotSetting.settingFile = Robot5100Settings.posSaveFile;
        RobotSetting.saveSetting("RobotX",this.m_PositionTracker.getCurrentPosX());
        RobotSetting.saveSetting("RobotY",this.m_PositionTracker.getCurrentPosY());
        RobotSetting.saveSetting("Rotation",this.m_PositionTracker.getRobotRotation());
        RobotSetting.saveSetting("rackPosition",this.m_RackAndPinion.getPosition());
        RobotSetting.saveSetting("dumperPosition",this.m_Dumper.getPosition());
        RobotSetting.saveSetting("linearReachPosition",this.m_LinearReach.getPosition());
        RobotSetting.saveSetting("collectorServoPosition",this.m_CollectorServo.getPosition());
    }

    @Override
    public void doLoop() {
        this.m_MotionSystem.doLoop();
        this.m_Gyro.doLoop();
        this.m_RackAndPinion.doLoop();
        this.m_Dumper.doLoop();
        this.m_LinearReach.doLoop();
        this.m_CollectorServo.doLoop();
        this.m_CollectorSweeper.doLoop();

        RobotDebugger.addDebug("RackAndPinionPos","" + this.m_RackAndPinion.getPosition());
        RobotDebugger.addDebug("DumperPos","" + this.m_Dumper.getPosition());
        RobotDebugger.addDebug("CollectorServoPos",""+this.m_CollectorServo.getPosition());
        RobotDebugger.addDebug("LinearApproachPos",""+this.m_LinearReach.getPosition());
        RobotDebugger.addDebug("GyroMeasuredAngle","" + this.m_Gyro.getCurrentAngle());
        RobotDebugger.addDebug("GyroX", "" + this.m_Gyro.getGyro().getRawX());
        RobotDebugger.addDebug("GyroY","" + this.m_Gyro.getGyro().getRawY());
        RobotDebugger.addDebug("GyroZ","" + this.m_Gyro.getGyro().getRawZ());
        RobotDebugger.addDebug("X","" + this.getPositionTracker().getCurrentPosX());
        RobotDebugger.addDebug("Y","" + this.getPositionTracker().getCurrentPosY());
        RobotDebugger.addDebug("Rotation","" + this.getPositionTracker().getRobotRotation());
        RobotDebugger.doLoop();
    }

    @Override
    public boolean isBusy() {
        return (this.m_MotionSystem.isBusy() || this.m_RackAndPinion.isBusy() || this.m_Dumper.isBusy() || this.m_LinearReach.isBusy() || this.m_CollectorServo.isBusy());
    }

    @Override
    public void waitUntilFinish() {
        while(this.isBusy()){
            this.doLoop();
        }
    }

    public boolean calibratePositionUsingVuforiaMarks(){
        FTC2018GameSpecificFunctions.NavigationResult m_Result = this.getGameSpecificFunction().navigateVuforiaMarks(this.getPositionTracker().getFieldSize());
        if(m_Result.getResultType() == FTC2018GameSpecificFunctions.NavigationResultType.Unknown){
            return false;
        }
        this.getPositionTracker().setCurrentPosX(m_Result.getX());
        this.getPositionTracker().setCurrentPosY(m_Result.getY());
        this.getPositionTracker().setRobotRotation(m_Result.getRotation());
        return true;
    }
}
