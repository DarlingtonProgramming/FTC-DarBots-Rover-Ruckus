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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.RobotMineralColorSensor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotEventLoopManager;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotSetting;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.BNO055IMUGyro;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RevColorSensor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotColorSensorImpl;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotOnPhoneCamera;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;
import org.firstinspires.ftc.teamcode.RobotControllers.DarbotsPrivateInfo.PrivateSettings;

import java.util.ResourceBundle;

public class Robot5100Core implements RobotNonBlockingDevice,RobotEventLoopable {
    private Robot5100MotionSystem m_MotionSystem;
    private RobotPositionTracker m_PosTracker;
    private RobotServoUsingMotor m_LinearActuator;
    private FTC2018GameSpecificFunctions m_GameSpecificFunction;
    private FTC2018GameSpecificFunctions.GoldPosType m_GoldPos = FTC2018GameSpecificFunctions.GoldPosType.Unknown;
    private RobotServoUsingMotor m_Arm;
    private RobotServoUsingMotor m_ArmReach;
    private CRServo m_IntakeServo;

    public Robot5100Core(boolean readSetting, boolean loadGameSpecific, @NonNull OpMode ControllingOpMode, double initialX, double initialY, double initialRotation, double linearActuatorPos, double armMotorPos, double armReachPos){
        this.m_PosTracker = new RobotPositionTracker(Robot5100Setting.FIELDTOTALX,Robot5100Setting.FIELDTOTALY,initialX,initialY,initialRotation,Robot5100Setting.LEFTFRONTWHEEL_POSITION,Robot5100Setting.RIGHTFRONTWHEEL_POSITION,Robot5100Setting.LEFTBACKWHEEL_POSITION,Robot5100Setting.RIGHTBACKWHEEL_POSITION);
        this.m_MotionSystem = new Robot5100MotionSystem(ControllingOpMode,this.m_PosTracker);
        DcMotor LinearActuatorDc = ControllingOpMode.hardwareMap.dcMotor.get(Robot5100Setting.LINEARACTUATOR_CONFIGURATIONNAME);
        RobotMotor LinearActuatorMotor = new RobotMotor(LinearActuatorDc,Robot5100Setting.LINEARACTUATOR_COUNTSPERREV,Robot5100Setting.LINEARACTUATOR_REVPERSEC,Robot5100Setting.LINEARACTUATOR_TIMECONTROL,Robot5100Setting.LINEARACTUATOR_TIMECONTROLEXCESSPCT);
        this.m_LinearActuator = new RobotServoUsingMotor(LinearActuatorMotor,linearActuatorPos,Robot5100Setting.LINEARACTUATOR_BIGGESTPOS,Robot5100Setting.LINEARACTUATOR_SMALLESTPOS);
        DcMotor ArmMotorDC = ControllingOpMode.hardwareMap.dcMotor.get(Robot5100Setting.ARMMOTOR_CONFIGURATIONNAME);
        RobotMotor ArmMotor = new RobotMotor(ArmMotorDC,Robot5100Setting.ARMMOTOR_COUNTSPERREV,Robot5100Setting.ARMMOTOR_REVPERSEC,Robot5100Setting.ARMMOTOR_TIMECONTROL,Robot5100Setting.ARMMOTOR_TIMECONTROLEXCESSPCT);
        this.m_Arm = new RobotServoUsingMotor(ArmMotor,armMotorPos,Robot5100Setting.ARMMOTOR_BIGGESTPOS,Robot5100Setting.ARMMOTOR_SMALLESTPOS);
        DcMotor ArmReachMotorDC = ControllingOpMode.hardwareMap.dcMotor.get(Robot5100Setting.ARMREACHMOTOR_CONFIGURATIONNAME);
        ArmReachMotorDC.setDirection(DcMotorSimple.Direction.REVERSE);
        RobotMotor ArmReachMotor = new RobotMotor(ArmReachMotorDC,Robot5100Setting.ARMREACHMOTOR_COUNTSPERREV,Robot5100Setting.ARMREACHMOTOR_REVPERSEC,Robot5100Setting.ARMREACHMOTOR_TIMECONTROL,Robot5100Setting.ARMREACHMOTOR_TIMECONTROLEXCESSPCT);
        this.m_ArmReach = new RobotServoUsingMotor(ArmReachMotor,armReachPos,Robot5100Setting.ARMREACHMOTOR_BIGGESTPOS,Robot5100Setting.ARMREACHMOTOR_SMALLESTPOS);
        this.m_IntakeServo = ControllingOpMode.hardwareMap.crservo.get(Robot5100Setting.INTAKESERVO_CONFIGURATIONNAME);
        this.m_IntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
        if(loadGameSpecific) {
            RobotOnPhoneCamera PhoneCamera = new RobotOnPhoneCamera(VuforiaLocalizer.CameraDirection.BACK, PrivateSettings.VUFORIALICENSE);
            this.m_GameSpecificFunction = new FTC2018GameSpecificFunctions(ControllingOpMode, PhoneCamera, Robot5100Setting.TFOL_SHOWPREVIEWONRC);
        }else {
            this.m_GameSpecificFunction = null;
        }
        RobotDebugger.setTelemetry(ControllingOpMode.telemetry);
        RobotDebugger.setDebugOn(true);
        if(readSetting){
            this.read(initialX,initialY,initialRotation,linearActuatorPos,armMotorPos,armReachPos);
        }
    }
    public FTC2018GameSpecificFunctions.GoldPosType getLastDetectedGoldPos(){
        return this.m_GoldPos;
    }
    public void setLastDetectedGoldPos(FTC2018GameSpecificFunctions.GoldPosType lastGoldPos){
        this.m_GoldPos = lastGoldPos;
    }
    public void tryDetectGoldPos(){
        if(this.m_GameSpecificFunction != null) {
            FTC2018GameSpecificFunctions.GoldPosType tempPos = this.m_GameSpecificFunction.detectAutonomousGoldMineralPosHorizontal(this.m_GameSpecificFunction.detectAllBlocksInCamera());
            if (tempPos != FTC2018GameSpecificFunctions.GoldPosType.Unknown) {
                this.m_GoldPos = tempPos;
            }
        }
    }
    public Robot5100MotionSystem getMotionSystem(){
        return this.m_MotionSystem;
    }
    public RobotPositionTracker getPositionTracker(){
        return this.m_PosTracker;
    }
    public RobotServoUsingMotor getLinearActuator(){
        return this.m_LinearActuator;
    }
    public RobotServoUsingMotor getArm(){
        return this.m_Arm;
    }
    public RobotServoUsingMotor getArmReach(){
        return this.m_ArmReach;
    }
    public void startSuckingMineral(){
        this.m_IntakeServo.setPower(1.0);
    }
    public void startVomitingMineral(){
        this.m_IntakeServo.setPower(-1.0);
    }
    public void stopSuckingMineral(){
        this.m_IntakeServo.setPower(0);
    }
    public void save(){
        RobotSetting.settingFile = Robot5100Setting.SETTINGFILENAME;
        RobotSetting.saveSetting("X",new Double(this.m_PosTracker.getCurrentPosX()));
        RobotSetting.saveSetting("Y",new Double(this.m_PosTracker.getCurrentPosY()));
        RobotSetting.saveSetting("Rotation",new Double(this.m_PosTracker.getRobotRotation()));
        RobotSetting.saveSetting("LinearActuatorPos",new Double(this.m_LinearActuator.getCurrentPosition()));
        RobotSetting.saveSetting("ArmMotorPos",new Double(this.m_Arm.getCurrentPosition()));
        RobotSetting.saveSetting("ArmReachMotorPos",new Double(this.m_ArmReach.getCurrentPosition()));
    }
    public void read(double defaultX, double defaultY, double defaultRotation, double defaultLinearActuatorPos, double defaultArmMotorPos, double defaultArmReachPos){
        RobotSetting.settingFile = Robot5100Setting.SETTINGFILENAME;
        double X = RobotSetting.getSetting("X",new Double(defaultX));
        double Y = RobotSetting.getSetting("Y",new Double(defaultY));
        double Rotation = RobotSetting.getSetting("Rotation",new Double(defaultRotation));
        double LinearActuatorPos = RobotSetting.getSetting("LinearActuatorPos",new Double(defaultLinearActuatorPos));
        double ArmMotorPos = RobotSetting.getSetting("ArmMotorPos",new Double(defaultArmMotorPos));
        double ArmReachMotorPos = RobotSetting.getSetting("ArmReachMotorPos",new Double(this.m_ArmReach.getCurrentPosition()));
        this.m_PosTracker.setCurrentPosX(X);
        this.m_PosTracker.setCurrentPosY(Y);
        this.m_PosTracker.setRobotRotation(Rotation);
        this.m_LinearActuator.adjustCurrentPosition(LinearActuatorPos);
        this.m_Arm.adjustCurrentPosition(ArmMotorPos);
        this.m_ArmReach.adjustCurrentPosition(ArmReachMotorPos);
    }
    public FTC2018GameSpecificFunctions getGameSpecificFunction(){return this.m_GameSpecificFunction;}
    @Override
    public void doLoop() {
        this.m_MotionSystem.doLoop();
        this.m_LinearActuator.doLoop();
        this.m_Arm.doLoop();
        this.m_ArmReach.doLoop();
        RobotDebugger.addDebug("LinearActuatorPos","" + this.getLinearActuator().getCurrentPosition() + " (" + this.getLinearActuator().getCurrentPercent() + "%)");
        RobotDebugger.addDebug("ArmMotorPos","" + this.getArm().getCurrentPosition() + " (" + this.getArm().getCurrentPercent() + "%)");
        RobotDebugger.addDebug("ArmReachMotorPos","" + this.getArmReach().getCurrentPosition() + " (" + this.getArmReach().getCurrentPercent() + "%)");
        RobotDebugger.addDebug("X","" + this.getPositionTracker().getCurrentPosX());
        RobotDebugger.addDebug("Y","" + this.getPositionTracker().getCurrentPosY());
        RobotDebugger.addDebug("Rotation","" + this.getPositionTracker().getRobotRotation());
        RobotDebugger.addDebug("LastGoldPos",this.getLastDetectedGoldPos().name());
        RobotDebugger.doLoop();
    }

    public void setLinearActuatorToHook(double Speed){
        this.m_LinearActuator.setTargetPercent(Robot5100Setting.LINEARACTUATOR_HOOKPCT,Speed);
    }

    @Override
    public boolean isBusy() {
        return this.m_LinearActuator.isBusy() || this.m_Arm.isBusy() || this.m_ArmReach.isBusy();
    }

    @Override
    public void waitUntilFinish() {
        while(this.isBusy()){
            this.doLoop();
        }
    }
}
