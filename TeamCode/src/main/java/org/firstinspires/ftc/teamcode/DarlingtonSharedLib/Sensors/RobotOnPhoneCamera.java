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

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotCamera;
import org.firstinspires.ftc.teamcode.RobotControllers.DarbotsPrivateInfo.PrivateSettings;

public class RobotOnPhoneCamera implements RobotCamera {
    private VuforiaLocalizer.CameraDirection m_CameraDirection;
    private VuforiaLocalizer m_Vuforia;
    private String m_VuforiaKey;
    public RobotOnPhoneCamera(VuforiaLocalizer.CameraDirection CameraDirection,String VuforiaKey){
        this.m_CameraDirection = CameraDirection;
        this.m_VuforiaKey = VuforiaKey;
        this.createVuforia();
    }
    public VuforiaLocalizer.CameraDirection getCameraDirection(){
        return this.m_CameraDirection;
    }
    @Override
    public VuforiaLocalizer getVuforia() {
        return this.m_Vuforia;
    }
    protected void createVuforia(){
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = this.m_VuforiaKey;
        parameters.cameraDirection = this.m_CameraDirection;

        //  Instantiate the Vuforia engine
        this.m_Vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
}
