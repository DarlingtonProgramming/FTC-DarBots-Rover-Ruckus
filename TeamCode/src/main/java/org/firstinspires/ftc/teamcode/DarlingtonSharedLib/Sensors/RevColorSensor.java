package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotDistanceSensor;

public class RevColorSensor extends RobotColorSensorImpl implements RobotDistanceSensor {
    private DistanceSensor m_DistanceSensor;
    private double m_LastCMMeasured = DistanceSensor.distanceOutOfRange;

    public RevColorSensor(ColorSensor colorSensor, DistanceSensor distanceSensor){
        super(colorSensor);
        this.m_DistanceSensor = distanceSensor;
    }
    @Override
    public double getDistance(DistanceUnit unit) throws RuntimeException {
        if(this.m_LastCMMeasured == DistanceSensor.distanceOutOfRange){
            throw new RuntimeException("Distance Out Of Range");
        }else{
            return unit.fromCm(m_LastCMMeasured);
        }
    }

    @Override
    public DistanceSensor getDistanceSensor() {
        return this.m_DistanceSensor;
    }

    @Override
    public void setDistanceSensor(DistanceSensor distanceSensor) {
        this.m_DistanceSensor = distanceSensor;
    }

    @Override
    public void updateData(){
        super.updateData();
        this.m_LastCMMeasured = this.m_DistanceSensor.getDistance(DistanceUnit.CM);
    }
}
