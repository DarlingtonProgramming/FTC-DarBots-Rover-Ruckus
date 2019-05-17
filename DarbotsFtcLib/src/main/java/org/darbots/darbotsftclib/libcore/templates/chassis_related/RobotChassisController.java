package org.darbots.darbotsftclib.libcore.templates.chassis_related;

import org.darbots.darbotsftclib.libcore.templates.RobotNonBlockingDevice;

public abstract class RobotChassisController implements RobotNonBlockingDevice {

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
