package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import com.qualcomm.robotcore.hardware.ColorSensor;

public interface RobotColorSensor {
    ColorSensor getColorSensor();
    void setColorSensor(ColorSensor ColorSensor);
    void updateData();
    int alpha();
    int red();
    int green();
    int blue();
}
