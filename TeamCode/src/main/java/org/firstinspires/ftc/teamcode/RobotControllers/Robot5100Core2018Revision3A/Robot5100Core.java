package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.RobotMineralColorSensor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotSetting;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RevColorSensor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotColorSensorImpl;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotOnPhoneCamera;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;
import org.firstinspires.ftc.teamcode.RobotControllers.DarbotsPrivateInfo.PrivateSettings;

public class Robot5100Core implements RobotNonBlockingDevice,RobotEventLoopable {
    private Robot5100MotionSystem m_MotionSystem;
    private RobotPositionTracker m_PosTracker;
    private RobotServoUsingMotor m_LinearActuator;
    private FTC2018GameSpecificFunctions m_GameSpecificFunction;
    private RobotMineralColorSensor m_ColorSensor;

    public Robot5100Core(boolean readSetting,@NonNull OpMode ControllingOpMode, double initialX, double initialY, double initialRotation, double linearActuatorPos){
        this.m_PosTracker = new RobotPositionTracker(Robot5100Setting.FIELDTOTALX,Robot5100Setting.FIELDTOTALY,initialX,initialY,initialRotation,Robot5100Setting.LEFTFRONTWHEEL_POSITION,Robot5100Setting.RIGHTFRONTWHEEL_POSITION,Robot5100Setting.LEFTBACKWHEEL_POSITION,Robot5100Setting.RIGHTBACKWHEEL_POSITION);
        this.m_MotionSystem = new Robot5100MotionSystem(ControllingOpMode,this.m_PosTracker);
        DcMotor LinearActuatorDc = ControllingOpMode.hardwareMap.dcMotor.get(Robot5100Setting.LINEARACTUATOR_CONFIGURATIONNAME);
        RobotMotor LinearActuatorMotor = new RobotMotor(LinearActuatorDc,Robot5100Setting.LINEARACTUATOR_COUNTSPERREV,Robot5100Setting.LINEARACTUATOR_REVPERSEC,Robot5100Setting.LINEARACTUATOR_TIMECONTROL,Robot5100Setting.LINEARACTUATOR_TIMECONTROLEXCESSPCT);
        this.m_LinearActuator = new RobotServoUsingMotor(LinearActuatorMotor,linearActuatorPos,Robot5100Setting.LINEARACTUATOR_BIGGESTPOS,Robot5100Setting.LINEARACTUATOR_SMALLESTPOS);
        RobotOnPhoneCamera PhoneCamera = new RobotOnPhoneCamera(VuforiaLocalizer.CameraDirection.BACK,PrivateSettings.VUFORIALICENSE);
        this.m_GameSpecificFunction = new FTC2018GameSpecificFunctions(ControllingOpMode,PhoneCamera,Robot5100Setting.TFOL_SHOWPREVIEWONRC);
        this.m_ColorSensor = new RobotMineralColorSensor(new RevColorSensor(ControllingOpMode.hardwareMap.colorSensor.get(Robot5100Setting.COLORSENSOR_CONFIGURATIONNAME),ControllingOpMode.hardwareMap.get(DistanceSensor.class,Robot5100Setting.COLORSENSOR_CONFIGURATIONNAME)));
        RobotDebugger.setTelemetry(ControllingOpMode.telemetry);
        RobotDebugger.setDebugOn(true);
        if(readSetting){
            this.read(initialX,initialY,initialRotation,linearActuatorPos);
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
    public void save(){
        RobotSetting.settingFile = Robot5100Setting.SETTINGFILENAME;
        RobotSetting.saveSetting("X",new Double(this.m_PosTracker.getCurrentPosX()));
        RobotSetting.saveSetting("Y",new Double(this.m_PosTracker.getCurrentPosY()));
        RobotSetting.saveSetting("Rotation",new Double(this.m_PosTracker.getRobotRotation()));
        RobotSetting.saveSetting("LinearActuatorPos",new Double(this.m_LinearActuator.getCurrentPosition()));
    }
    public void read(double defaultX, double defaultY, double defaultRotation, double defaultLinearActuatorPos){
        RobotSetting.settingFile = Robot5100Setting.SETTINGFILENAME;
        double X = RobotSetting.getSetting("X",new Double(defaultX));
        double Y = RobotSetting.getSetting("Y",new Double(defaultY));
        double Rotation = RobotSetting.getSetting("Rotation",new Double(defaultRotation));
        double LinearActuatorPos = RobotSetting.getSetting("LinearActuatorPos",new Double(defaultLinearActuatorPos));
        this.m_PosTracker.setCurrentPosX(X);
        this.m_PosTracker.setCurrentPosY(Y);
        this.m_PosTracker.setRobotRotation(Rotation);
        this.m_LinearActuator.adjustCurrentPosition(LinearActuatorPos);
    }
    public FTC2018GameSpecificFunctions getGameSpecificFunction(){return this.m_GameSpecificFunction;}
    public RobotMineralColorSensor getColorSensor(){
        return this.m_ColorSensor;
    }
    @Override
    public void doLoop() {
        this.m_MotionSystem.doLoop();
        this.m_LinearActuator.doLoop();
        RobotDebugger.addDebug("LinearActuatorPos","" + this.getLinearActuator().getCurrentPosition());
        RobotDebugger.addDebug("X","" + this.getPositionTracker().getCurrentPosX());
        RobotDebugger.addDebug("Y","" + this.getPositionTracker().getCurrentPosY());
        RobotDebugger.addDebug("Rotation","" + this.getPositionTracker().getRobotRotation());
        RobotDebugger.doLoop();
    }

    public void setLinearActuatorToHook(double Speed){
        this.m_LinearActuator.setTargetPosition(Robot5100Setting.LINEARACTUATOR_HOOKPOS,Speed);
    }

    @Override
    public boolean isBusy() {
        return this.m_LinearActuator.isBusy();
    }

    @Override
    public void waitUntilFinish() {
        while(this.isBusy()){
            this.doLoop();
        }
    }
}
