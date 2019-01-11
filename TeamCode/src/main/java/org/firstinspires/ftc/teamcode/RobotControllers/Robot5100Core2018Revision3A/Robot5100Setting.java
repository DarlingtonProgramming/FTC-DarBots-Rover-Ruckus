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

package org.firstinspires.ftc.teamcode.RobotControllers.Robot5100Core2018Revision3A;

public class Robot5100Setting {
    public static final String SETTINGFILENAME = "5100saved.json";

    public static final boolean TFOL_SHOWPREVIEWONRC = false;

    public static final double FIELDTOTALX = 365.76;
    public static final double FIELDTOTALY = 365.76;

    public static final double TELEOP_GAMEPADTRIGGERVALUE = 0.1;
    public static final double TELEOP_BIGGESTDRIVINGSPEED = 0.5;
    public static final double TELEOP_LINEARACTUATORSPEED = 0.8;
    public static final double TELEOP_ARMSPEED = 0.3;
    public static final double TELEOP_ARMREACHSPEED = 0.5;
    public static final double AUTONOMOUS_BIGGESTDRIVINGSPEED = 0.5;
    public static final double AUTONOMOUS_LINEARACTUATORSPEED = 1.0;
    public static final double AUTONOMOUS_ARMSPEED = 0.5;
    public static final double AUTONOMOUS_ARMREACHSPEED = 0.5;

    public static final boolean MOTIONSYSTEM_TIMECONTROL = false;
    public static final double MOTIONSYSTEM_TIMECONTROLPCT = 30;
    public static final String LEFTFRONTWHEEL_CONFIGURATIONNAME = "motor0";
    public static final double LEFTFRONTWHEEL_COUNTSPERREV = 1120;
    public static final double LEFTFRONTWHEEL_REVPERSEC = 2.5;
    public static final double LEFTFRONTWHEEL_INSTALLEDANGLE = -45;
    public static final double LEFTFRONTWHEEL_RADIUS = 5;
    public static final double[] LEFTFRONTWHEEL_POSITION = {-16.0,16.0};
    public static final String RIGHTFRONTWHEEL_CONFIGURATIONNAME = "motor1";
    public static final double RIGHTFRONTWHEEL_COUNTSPERREV = 1120;
    public static final double RIGHTFRONTWHEEL_REVPERSEC = 2.5;
    public static final double RIGHTFRONTWHEEL_INSTALLEDANGLE = 45;
    public static final double RIGHTFRONTWHEEL_RADIUS = 5;
    public static final double[] RIGHTFRONTWHEEL_POSITION = {16.0,16.0};
    public static final String LEFTBACKWHEEL_CONFIGURATIONNAME = "motor3";
    public static final double LEFTBACKWHEEL_COUNTSPERREV = 1120;
    public static final double LEFTBACKWHEEL_REVPERSEC = 2.5;
    public static final double LEFTBACKWHEEL_INSTALLEDANGLE = -135;
    public static final double LEFTBACKWHEEL_RADIUS = 5;
    public static final double[] LEFTBACKWHEEL_POSITION = {-16.0,-16.0};
    public static final String RIGHTBACKWHEEL_CONFIGURATIONNAME = "motor2";
    public static final double RIGHTBACKWHEEL_COUNTSPERREV = 1120;
    public static final double RIGHTBACKWHEEL_REVPERSEC = 2.5;
    public static final double RIGHTBACKWHEEL_INSTALLEDANGLE = 135;
    public static final double RIGHTBACKWHEEL_RADIUS = 5;
    public static final double[] RIGHTBACKWHEEL_POSITION = {16.0,-16.0};

    public static final String LINEARACTUATOR_CONFIGURATIONNAME = "linearActuator";
    public static final double LINEARACTUATOR_COUNTSPERREV = 1120;
    public static final double LINEARACTUATOR_REVPERSEC = 2.67;
    public static final double LINEARACTUATOR_BIGGESTPOS = 1000;
    public static final double LINEARACTUATOR_HOOKPCT = 18.8;
    public static final double LINEARACTUATOR_SMALLESTPOS = -1000;
    public static final boolean LINEARACTUATOR_TIMECONTROL = true;
    public static final double LINEARACTUATOR_TIMECONTROLEXCESSPCT = 200;

    public static final String ARMMOTOR_CONFIGURATIONNAME = "armMotor";
    public static final double ARMMOTOR_COUNTSPERREV = 1680;
    public static final double ARMMOTOR_REVPERSEC = 1.75;
    public static final double ARMMOTOR_BIGGESTPOS = 1000;
    public static final double ARMMOTOR_SMALLESTPOS = -1000;
    public static final boolean ARMMOTOR_TIMECONTROL = true;
    public static final double ARMMOTOR_TIMECONTROLEXCESSPCT = 200;

    public static final String ARMREACHMOTOR_CONFIGURATIONNAME = "armReachMotor";
    public static final double ARMREACHMOTOR_COUNTSPERREV = 28;
    public static final double ARMREACHMOTOR_REVPERSEC = 19.167;
    public static final double ARMREACHMOTOR_BIGGESTPOS = 1000;
    public static final double ARMREACHMOTOR_SMALLESTPOS = 0;
    public static final boolean ARMREACHMOTOR_TIMECONTROL = true;
    public static final double ARMREACHMOTOR_TIMECONTROLEXCESSPCT = 200;

    public static final String INTAKESERVO_CONFIGURATIONNAME = "intakeServo";
}
