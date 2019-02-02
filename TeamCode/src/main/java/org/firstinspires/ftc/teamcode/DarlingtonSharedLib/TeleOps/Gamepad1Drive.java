package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.TeleOps;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotCore;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystemTeleOpControlTask;

public abstract class Gamepad1Drive extends LinearOpMode {
    private RobotCore m_RobotCore;
    public double GamepadTriggerValue;
    public double TeleOpBiggestDrivingSpeed;
    public Gamepad1Drive(double TriggerValue, double BiggestDrivingSpeed){
        this.GamepadTriggerValue = TriggerValue;
        this.TeleOpBiggestDrivingSpeed = Math.abs(BiggestDrivingSpeed);
    }

    public RobotCore getRobotCore(){
        return this.m_RobotCore;
    }

    protected void robotCoreInit(@NonNull RobotCore Core){
        this.m_RobotCore = Core;
        this.m_RobotCore.getMotionSystem().replaceTask(this.m_RobotCore.getMotionSystem().getTeleOpTask());
    }

    protected void driveControl(){
        if(this.m_RobotCore == null){
            throw new RuntimeException("Robot Core Not Set Up");
        }
        double XValue = Math.abs(gamepad1.left_stick_x) >= GamepadTriggerValue ? gamepad1.left_stick_x : 0;
        double ZValue = Math.abs(gamepad1.left_stick_y) >= GamepadTriggerValue ? -gamepad1.left_stick_y : 0;
        double TurnValue = Math.abs(gamepad1.right_stick_x) >= GamepadTriggerValue ? -gamepad1.right_stick_x : 0;
        XValue *= Math.abs(TeleOpBiggestDrivingSpeed);
        ZValue *= Math.abs(TeleOpBiggestDrivingSpeed);
        TurnValue *= Math.abs(TeleOpBiggestDrivingSpeed);
        RobotMotionSystemTeleOpControlTask Task = (RobotMotionSystemTeleOpControlTask) this.m_RobotCore.getMotionSystem().getCurrentTask();
        Task.setDriveXSpeed(XValue);
        Task.setDriveZSpeed(ZValue);
        Task.setDriveRotationSpeed(TurnValue);
        Task.updateStatus();
        this.getRobotCore().getDebugger().addDebug(new RobotDebugger.RobotDebuggerInformation("GamepadVal","X:" + gamepad1.left_stick_x + ", Y:" + gamepad1.left_stick_y + ", R:" + gamepad1.right_stick_x));
    }
}
