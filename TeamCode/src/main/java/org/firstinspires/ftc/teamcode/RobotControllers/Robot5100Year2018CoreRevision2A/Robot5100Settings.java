package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Year2018CoreRevision2A;

public class Robot5100Settings {
    public static final String posSaveFile = "5100PositionFile.json";

    public static final double gamepadTriggerValue = 0.2;

    public static final boolean motionTimeControl = true;
    public static final double motionTimeControlPct = 20;

    public static final String frontMotorConfigurationName = "frontMotor";
    public static final double[] frontWheelPos = {0,0};
    public static final double frontMotorCountsPerRev = 1120;
    public static final double frontMotorRevPerSec = 2.5;
    public static final double frontWheelRadius = 5;
    public static final double frontWheelAngle = 90;

    public static final String leftBackMotorConfigurationName = "leftBackMotor";
    public static final double[] leftBackWheelPos = {0,0};
    public static final double leftBackMotorCountsPerRev = 1120;
    public static final double leftBackMotorRevPerSec = 2.5;
    public static final double leftBackWheelRadius = 5;
    public static final double leftBackWheelAngle = -45;

    public static final String rightBackMotorConfigurationName = "rightBackMotor";
    public static final double[] rightBackWheelPos = {0,0};
    public static final double rightBackMotorCountsPerRev = 1120;
    public static final double rightBackMotorRevPerSec = 2.5;
    public static final double rightBackWheelRadius = 5;
    public static final double rightBackWheelAngle = -135;

    public static final double[] leftFrontExtremePos = {0,0};
    public static final double[] rightFrontExtremePos = {0,0};
    public static final double[] leftBackExtremePos = {0,0};
    public static final double[] rightBackExtremePos = {0,0};

    public static final String gyroConfigurationName = "imu";
    public static final boolean gyroReversed = false;
}
