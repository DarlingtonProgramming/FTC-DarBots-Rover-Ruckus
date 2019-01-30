package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDataStorage;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode.Robot4100Setting;

public abstract class RobotCore implements RobotNonBlockingDevice {
    private RobotDataStorage m_DataStorage;
    private RobotDebugger m_Debugger;
    private OpMode m_ControllingOpMode;
    public RobotCore(@NonNull OpMode ControllingOpMode, @NonNull String SettingFileName){
        this.m_ControllingOpMode = ControllingOpMode;
        this.m_Debugger = new RobotDebugger(ControllingOpMode.telemetry);
        this.m_DataStorage = new RobotDataStorage(SettingFileName);
    }
    public RobotDataStorage getDataStorage(){
        return this.m_DataStorage;
    }
    public RobotDebugger getDebugger(){
        return this.m_Debugger;
    }

    @Override
    public void updateStatus(){
        this.m_Debugger.updateStatus();
    }

    public abstract RobotMotionSystem getMotionSystem();
}
