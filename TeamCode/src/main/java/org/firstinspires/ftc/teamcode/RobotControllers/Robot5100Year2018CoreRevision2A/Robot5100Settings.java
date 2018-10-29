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

public class Robot5100Settings {
    public static final String posSaveFile = "5100PositionFile.json";

    public static final double TeleOP_GamepadTriggerValue = 0.1;
    public static final double TeleOP_BiggestSpeed = 0.2;
    public static final double Autonomous_BiggestSpeed = 0.2;

    public static final boolean motionTimeControl = true;
    public static final double motionTimeControlPct = 20;
    public static final double backMotorFactor = 2 / 3;

    public static final String frontMotorConfigurationName = "frontMotor";
    public static final double[] frontWheelPos = {0,11.4+3.567};
    public static final double frontMotorCountsPerRev = 1120;
    public static final double frontMotorRevPerSec = 2.5;
    public static final double frontWheelRadius = 5;
    public static final double frontWheelAngle = 90;

    public static final String leftBackMotorConfigurationName = "leftBackMotor";
    public static final double[] leftBackWheelPos = {-14.4-1.2,-14.4-1.2};
    public static final double leftBackMotorCountsPerRev = 1120;
    public static final double leftBackMotorRevPerSec = 2.5;
    public static final double leftBackWheelRadius = 5;
    public static final double leftBackWheelAngle = -45;

    public static final String rightBackMotorConfigurationName = "rightBackMotor";
    public static final double[] rightBackWheelPos = {14.4+1.2,-14.4-1.2};
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

    public static final String rackAndPinionConfigurationName = "rackMotor";
    public static final double rackAndPinionMotorCountsPerRev = 1680;
    public static final double rackAndPinionRevPerSec = 1.75;
    public static final boolean rackAndPinionTimeControl = true;
    public static final double rackAndPinionTimeControlPercent = 20;
    public static final double rackAndPinionBiggestPos = 1000;
    public static final double rackAndPinionSmallestPos = -1000;
    public static final double rackAndPinionSpeed = 1.0;
    public static final double rackAndPinionHookPos = 2.0;

}
