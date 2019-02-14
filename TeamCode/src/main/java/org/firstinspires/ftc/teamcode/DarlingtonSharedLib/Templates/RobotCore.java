package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.NonBlockingTimer;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotAudioPlayer;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDataStorage;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Runtime.RobotGlobal;
import org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode.Robot4100Setting;

public abstract class RobotCore implements RobotNonBlockingDevice {
    private RobotDataStorage m_DataStorage;
    private RobotDebugger m_Debugger;
    private OpMode m_ControllingOpMode;
    private RobotAudioPlayer m_AudioPlayer;
    private NonBlockingTimer m_Timer;
    public RobotCore(@NonNull OpMode ControllingOpMode, @NonNull String SettingFileName){
        this.m_ControllingOpMode = ControllingOpMode;
        this.m_Debugger = new RobotDebugger(ControllingOpMode.telemetry);
        this.m_DataStorage = new RobotDataStorage(SettingFileName);
        this.m_AudioPlayer = new RobotAudioPlayer(ControllingOpMode);
        this.m_Timer = new NonBlockingTimer();
        RobotGlobal.setRunningRobotCore(this);
    }
    public RobotDataStorage getDataStorage(){
        return this.m_DataStorage;
    }
    public RobotDebugger getDebugger(){
        return this.m_Debugger;
    }
    public RobotAudioPlayer getAudioPlayer(){return this.m_AudioPlayer;}
    @Override
    public void updateStatus(){
        this.m_Debugger.updateStatus();
        this.m_Timer.updateStatus();
    }

    public NonBlockingTimer getTimer(){
        return this.m_Timer;
    }

    public abstract RobotMotionSystem getMotionSystem();

    public OpMode getControllingOpMode(){
        return this.m_ControllingOpMode;
    }
}
