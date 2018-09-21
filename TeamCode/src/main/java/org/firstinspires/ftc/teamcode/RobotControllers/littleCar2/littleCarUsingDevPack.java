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

package org.firstinspires.ftc.teamcode.RobotControllers.littleCar2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotDebugger;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;

@TeleOp (name = "littleCar2.0", group = "David Cao")
@Disabled
public class littleCarUsingDevPack extends LinearOpMode {
    RobotPositionTracker m_Position;
    littleCarMotionSystem m_MotionSystem;
    DcMotor m_LeftMotor, m_RightMotor;

    public void initialize(){
        //fieldTotal: 12 feet * 12 feet
        //robotTotal: 38 cm * 30 cm
        m_LeftMotor = hardwareMap.dcMotor.get("leftMotor");
        m_RightMotor = hardwareMap.dcMotor.get("rightMotor");
        this.m_RightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.m_LeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        //this line needs to be fixed before running the code.
        //m_Position = new RobotPositionTracker(365.76,365.76,38,30,182.88,182.88,0);
        m_MotionSystem = new littleCarMotionSystem(this.m_Position,this.m_LeftMotor,this.m_RightMotor);
    }

    @Override
    public void runOpMode(){
        telemetry.setAutoClear(false);
        RobotDebugger.setTelemetry(this.telemetry);
        RobotDebugger.setDebug(true);
        this.initialize();
        waitForStart();

        while(this.opModeIsActive()){
            this.m_MotionSystem.driveForward(20);
            this.m_MotionSystem.doLoop();
            RobotDebugger.doLoop();
        }
    }
}
