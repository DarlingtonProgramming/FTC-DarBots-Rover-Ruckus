package org.firstinspires.ftc.teamcode.RobotControllers.Util;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.Robot2DPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotCore;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystemTeleOpControlTask;

import java.text.FieldPosition;

public abstract class ChassisFrictionCalibration extends LinearOpMode {

    protected String debugMsg = "";

    public abstract RobotCore getRobotCore();

    protected void startChassisTest(){
        getRobotCore().getDebugger().addDebuggerCallable(new RobotDebugger.ObjectDebuggerWrapper<>("frictionTester",new Object(){
            @Override
            public String toString(){
                return debugMsg;
            }
        }));

        getRobotCore().getMotionSystem().setRotationalMotionFrictionFactor(1);
        getRobotCore().getMotionSystem().setLinearMotionFrictionFactor(1);
        debugMsg = "press gamepad1-X to begin";
        getRobotCore().updateStatus();
        waitForKey();
        debugMsg = "ready to begin X distance test? press X";
        getRobotCore().updateStatus();
        waitForKey();
        debugMsg = "The robot will now start moving to the right, linearFriction = actual distance in CM / 100";
        getRobotCore().updateStatus();
        waitForKey();
        getRobotCore().getMotionSystem().replaceTask(getRobotCore().getMotionSystem().getFixedXDistanceTask(100,0.1));
        getRobotCore().getMotionSystem().waitUntilFinish();
        debugMsg = "linear friction test finished, press X to continue";
        getRobotCore().updateStatus();
        waitForKey();
        debugMsg = "now we will begin rotation friction test. Press X to continue";
        getRobotCore().updateStatus();
        waitForKey();
        debugMsg = "rotational friction = actual deg / 180. Press X to continue";
        getRobotCore().updateStatus();
        waitForKey();
        getRobotCore().getMotionSystem().replaceTask(getRobotCore().getMotionSystem().getFixedTurnTask(180,0.1));
        getRobotCore().getMotionSystem().waitUntilFinish();
        debugMsg = "rotational friction test finished, press X to continue";
        getRobotCore().updateStatus();
        waitForKey();
    }

    private void waitForKey(){
        while((!this.gamepad1.x) && this.opModeIsActive()){
            idle();
        }
    }

}
