package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotWebcamCamera;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;
import org.firstinspires.ftc.teamcode.RobotControllers.DarbotsPrivateInfo.PrivateSettings;

public class Robot5100Core implements RobotNonBlockingDevice,RobotEventLoopable {
    private Robot5100MotionSystem m_MotionSystem;
    private RobotPositionTracker m_PosTracker;
    private RobotServoUsingMotor m_LinearActuator;
    private FTC2018GameSpecificFunctions m_GameSpecificFunction;
    public Robot5100Core(@NonNull OpMode ControllingOpMode, double initialX, double initialY, double initialRotation, double linearActuatorPos){
        this.m_PosTracker = new RobotPositionTracker(Robot5100Setting.FIELDTOTALX,Robot5100Setting.FIELDTOTALY,initialX,initialY,initialRotation,Robot5100Setting.LEFTFRONTWHEEL_POSITION,Robot5100Setting.RIGHTFRONTWHEEL_POSITION,Robot5100Setting.LEFTBACKWHEEL_POSITION,Robot5100Setting.RIGHTBACKWHEEL_POSITION);
        this.m_MotionSystem = new Robot5100MotionSystem(ControllingOpMode,this.m_PosTracker);
        DcMotor LinearActuatorDc = ControllingOpMode.hardwareMap.dcMotor.get(Robot5100Setting.LINEARACTUATOR_CONFIGURATIONNAME);
        RobotMotor LinearActuatorMotor = new RobotMotor(LinearActuatorDc,Robot5100Setting.LINEARACTUATOR_COUNTSPERREV,Robot5100Setting.LINEARACTUATOR_REVPERSEC,Robot5100Setting.LINEARACTUATOR_TIMECONTROL,Robot5100Setting.LINEARACTUATOR_TIMECONTROLEXCESSPCT);
        this.m_LinearActuator = new RobotServoUsingMotor(LinearActuatorMotor,linearActuatorPos,Robot5100Setting.LINEARACTUATOR_BIGGESTPOS,Robot5100Setting.LINEARACTUATOR_SMALLESTPOS);
        RobotWebcamCamera WebCam = new RobotWebcamCamera(ControllingOpMode,Robot5100Setting.TFOL_RECOGNITIONCAMERA_CONFIGURATIONNAME,PrivateSettings.VUFORIALICENSE);
        this.m_GameSpecificFunction = new FTC2018GameSpecificFunctions(ControllingOpMode,WebCam,Robot5100Setting.TFOL_SHOWPREVIEWONRC);
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

    @Override
    public void doLoop() {
        this.m_MotionSystem.doLoop();
        this.m_LinearActuator.doLoop();
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
