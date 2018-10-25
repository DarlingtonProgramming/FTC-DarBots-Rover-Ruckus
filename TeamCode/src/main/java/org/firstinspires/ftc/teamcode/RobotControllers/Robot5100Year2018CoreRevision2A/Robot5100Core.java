package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.GyroWrapper;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotSetting;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

public class Robot5100Core implements RobotNonBlockingDevice, RobotEventLoopable {
    private RobotPositionTracker m_PositionTracker;
    private Robot5100MotionSystem m_MotionSystem;
    private GyroWrapper m_Gyro;

    public Robot5100Core(OpMode runningOpMode, double initialX, double initialY, double initialRotation, boolean readSetting){
        m_Gyro = new GyroWrapper(runningOpMode,Robot5100Settings.gyroConfigurationName,Robot5100Settings.gyroReversed,(float) initialRotation);
        this.m_PositionTracker = new RobotPositionTracker(365.76,365.76,initialX,initialY,initialRotation,Robot5100Settings.leftFrontExtremePos,Robot5100Settings.rightFrontExtremePos,Robot5100Settings.leftBackExtremePos,Robot5100Settings.rightBackExtremePos);
        if(readSetting){
            this.readSavedPosition(initialX,initialY,initialRotation);
        }
        this.m_MotionSystem = new Robot5100MotionSystem(runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.frontMotorConfigurationName), runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.leftBackMotorConfigurationName), runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.rightBackMotorConfigurationName),this.m_PositionTracker);
        RobotDebugger.setTelemetry(runningOpMode.telemetry);
        RobotDebugger.setDebugOn(true);
    }

    public GyroWrapper getGyro() {
        return this.m_Gyro;
    }

    public Robot5100MotionSystem getMotionSystem(){
        return this.m_MotionSystem;
    }

    public RobotPositionTracker getPositionTracker(){
        return this.m_PositionTracker;
    }

    public void readSavedPosition(double defaultX, double defaultY, double defaultRotations){
        RobotSetting.settingFile = Robot5100Settings.posSaveFile;
        double X = RobotSetting.getSetting("RobotX",new Double(defaultX));
        double Y = RobotSetting.getSetting("RobotY",new Double(defaultY));
        double Rotation = RobotSetting.getSetting("Rotation", new Double(defaultRotations));
        this.m_PositionTracker.setCurrentPosX(X);
        this.m_PositionTracker.setCurrentPosY(Y);
        this.m_PositionTracker.setRobotRotation(Rotation);
        this.m_Gyro.adjustCurrentAngle((float) Rotation);
    }

    public void savePosition(){
        RobotSetting.settingFile = Robot5100Settings.posSaveFile;
        RobotSetting.saveSetting("RobotX",this.m_PositionTracker.getCurrentPosX());
        RobotSetting.saveSetting("RobotY",this.m_PositionTracker.getCurrentPosY());
        RobotSetting.saveSetting("Rotation",this.m_PositionTracker.getRobotRotation());
    }

    @Override
    public void doLoop() {
        this.m_MotionSystem.doLoop();
        this.m_Gyro.doLoop();

        RobotDebugger.addDebug("GyroMeasuredAngle","" + this.m_Gyro.getCurrentAngle());
        RobotDebugger.addDebug("GyroX", "" + this.m_Gyro.getGyro().getRawX());
        RobotDebugger.addDebug("GyroY","" + this.m_Gyro.getGyro().getRawY());
        RobotDebugger.addDebug("GyroZ","" + this.m_Gyro.getGyro().getRawZ());
        RobotDebugger.doLoop();
    }

    @Override
    public boolean isBusy() {
        return this.m_MotionSystem.isBusy();
    }

    @Override
    public void waitUntilFinish() {
        while(this.isBusy()){
            this.doLoop();
        }
    }
}
