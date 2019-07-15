package org.darbots.darbotsftclib.libcore.sensors.servos.motor_powered_servos;

import org.darbots.darbotsftclib.libcore.templates.RobotNonBlockingDevice;

public class RobotServoUsingMotor implements RobotNonBlockingDevice {
    private boolean m_IsBusy;

    public RobotServoUsingMotor(){
        this.m_IsBusy = false;
    }
    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void updateStatus() {

    }

    @Override
    public void waitUntilFinish() {

    }
}
