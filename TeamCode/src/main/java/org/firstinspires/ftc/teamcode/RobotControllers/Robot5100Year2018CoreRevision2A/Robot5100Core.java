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

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.RobotPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.GyroWrapper;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotSetting;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;
import org.firstinspires.ftc.teamcode.RobotControllers.DarbotsPrivateInfo.PrivateSettings;

public class Robot5100Core implements RobotNonBlockingDevice, RobotEventLoopable {
    private RobotPositionTracker m_PositionTracker;
    private Robot5100MotionSystem m_MotionSystem;
    private GyroWrapper m_Gyro;
    private Robot5100RackAndPinion m_RackAndPinion;
    private FTC2018GameSpecificFunctions m_2018Specific;

<<<<<<< HEAD
    public Robot5100Core(OpMode runningOpMode, double initialX, double initialY, double initialRotation, double rackAndPinionPosition, boolean readSetting){
=======
    public Robot5100Core(@NonNull OpMode runningOpMode, double initialX, double initialY, double initialRotation, boolean readSetting){
>>>>>>> 072cd12e399d2ae87f42635e32d0fbda17a2b302
        m_Gyro = new GyroWrapper(runningOpMode,Robot5100Settings.gyroConfigurationName,Robot5100Settings.gyroReversed,(float) initialRotation);
        this.m_PositionTracker = new RobotPositionTracker(365.76,365.76,initialX,initialY,initialRotation,Robot5100Settings.leftFrontExtremePos,Robot5100Settings.rightFrontExtremePos,Robot5100Settings.leftBackExtremePos,Robot5100Settings.rightBackExtremePos);
        this.m_MotionSystem = new Robot5100MotionSystem(runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.frontMotorConfigurationName), runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.leftBackMotorConfigurationName), runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.rightBackMotorConfigurationName),this.m_PositionTracker);
<<<<<<< HEAD
        this.m_RackAndPinion = new Robot5100RackAndPinion(runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.rackAndPinionConfigurationName),rackAndPinionPosition);
=======
        this.m_RackAndPinion = new Robot5100RackAndPinion(runningOpMode.hardwareMap.dcMotor.get(Robot5100Settings.rackAndPinionConfigurationName));
        this.m_2018Specific = new FTC2018GameSpecificFunctions(runningOpMode,VuforiaLocalizer.CameraDirection.BACK,Robot5100Settings.phonePos,Robot5100Settings.phoneRotation,PrivateSettings.VUFORIALICENSE);
>>>>>>> 072cd12e399d2ae87f42635e32d0fbda17a2b302
        RobotDebugger.setTelemetry(runningOpMode.telemetry);
        RobotDebugger.setDebugOn(true);
        if(readSetting){
            this.readSavedPosition(initialX,initialY,initialRotation,rackAndPinionPosition);
        }
    }

    public GyroWrapper getGyro() {
        return this.m_Gyro;
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



    public Robot5100MotionSystem getMotionSystem(){
        return this.m_MotionSystem;
    }

    public RobotPositionTracker getPositionTracker(){
        return this.m_PositionTracker;
    }

<<<<<<< HEAD
    public void readSavedPosition(double defaultX, double defaultY, double defaultRotations, double defaultRackAndPinionPos){
=======
    public FTC2018GameSpecificFunctions getGameSpecificFunction(){
        return this.m_2018Specific;
    }

    public void readSavedPosition(double defaultX, double defaultY, double defaultRotations){
>>>>>>> 072cd12e399d2ae87f42635e32d0fbda17a2b302
        RobotSetting.settingFile = Robot5100Settings.posSaveFile;
        double X = RobotSetting.getSetting("RobotX",new Double(defaultX));
        double Y = RobotSetting.getSetting("RobotY",new Double(defaultY));
        double Rotation = RobotSetting.getSetting("Rotation", new Double(defaultRotations));
        double rackAndPinionPos = RobotSetting.getSetting("rackPosition", new Double(defaultRackAndPinionPos));
        this.m_PositionTracker.setCurrentPosX(X);
        this.m_PositionTracker.setCurrentPosY(Y);
        this.m_PositionTracker.setRobotRotation(Rotation);
        this.m_Gyro.adjustCurrentAngle((float) Rotation);
        this.m_RackAndPinion.adjustPosition(rackAndPinionPos);
    }

    public void savePosition(){
        RobotSetting.settingFile = Robot5100Settings.posSaveFile;
        RobotSetting.saveSetting("RobotX",this.m_PositionTracker.getCurrentPosX());
        RobotSetting.saveSetting("RobotY",this.m_PositionTracker.getCurrentPosY());
        RobotSetting.saveSetting("Rotation",this.m_PositionTracker.getRobotRotation());
        RobotSetting.saveSetting("rackPosition",this.m_RackAndPinion.getPosition());
    }

    @Override
    public void doLoop() {
        this.m_MotionSystem.doLoop();
        this.m_Gyro.doLoop();
        this.m_RackAndPinion.doLoop();

        RobotDebugger.addDebug("RackAndPinionPos","" + this.m_RackAndPinion.getPosition());
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
        return this.m_MotionSystem.isBusy();
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
