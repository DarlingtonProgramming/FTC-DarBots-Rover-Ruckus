package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

public abstract class RobotMotorType {
    public abstract String getMotorName();
    public abstract double getCountsPerRev();
    public abstract double getRevPerSec();
    @Override
    public String toString(){
        return this.getMotorName();
    }
}