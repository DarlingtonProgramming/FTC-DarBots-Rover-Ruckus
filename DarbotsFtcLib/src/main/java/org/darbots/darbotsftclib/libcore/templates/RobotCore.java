package org.darbots.darbotsftclib.libcore.templates;

import org.darbots.darbotsftclib.libcore.integratedfunctions.RobotLogger;
import org.darbots.darbotsftclib.libcore.templates.RobotNonBlockingDevice;

public abstract class RobotCore implements RobotNonBlockingDevice {
    private RobotLogger m_Logger;
    public RobotCore(String logFileName){
        m_Logger = new RobotLogger(logFileName);
    }
    public abstract void stop();
    public RobotLogger getLogger(){
        return this.m_Logger;
    }
    @Override
    public void waitUntilFinish(){
        while(this.isBusy()){
            this.updateStatus();
        }
    }
}
