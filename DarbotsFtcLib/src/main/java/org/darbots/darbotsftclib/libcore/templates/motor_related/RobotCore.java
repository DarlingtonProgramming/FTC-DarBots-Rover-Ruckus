package org.darbots.darbotsftclib.libcore.templates.motor_related;

import org.darbots.darbotsftclib.libcore.integratedfunctions.RobotLogger;
import org.darbots.darbotsftclib.libcore.templates.RobotNonBlockingDevice;

public abstract class RobotCore implements RobotNonBlockingDevice {
    public abstract void stop();
    public abstract RobotLogger getLogger();
    @Override
    public void waitUntilFinish(){
        while(this.isBusy()){
            this.updateStatus();
        }
    }
}
