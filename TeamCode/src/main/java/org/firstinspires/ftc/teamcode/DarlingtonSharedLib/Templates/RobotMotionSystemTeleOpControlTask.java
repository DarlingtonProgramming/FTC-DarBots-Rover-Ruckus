package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

public abstract class RobotMotionSystemTeleOpControlTask extends RobotMotionSystemTask {
    private double m_DriveXSpeed;
    private double m_DriveZSpeed;
    private double m_DriveRotationSpeed;

    public RobotMotionSystemTeleOpControlTask(){
        this.m_DriveXSpeed = 0;
        this.m_DriveZSpeed = 0;
        this.m_DriveRotationSpeed = 0;
    }

    public RobotMotionSystemTeleOpControlTask(RobotMotionSystemTeleOpControlTask Task){
        super(Task);
        this.m_DriveXSpeed = Task.m_DriveXSpeed;
        this.m_DriveZSpeed = Task.m_DriveZSpeed;
        this.m_DriveRotationSpeed = Task.m_DriveRotationSpeed;
    }

    public double getDriveXSpeed(){
        return this.m_DriveXSpeed;
    }

    public void setDriveXSpeed(double XSpeed){
        this.m_DriveXSpeed = XSpeed;
    }

    public double getDriveZSpeed(){
        return this.m_DriveZSpeed;
    }

    public void setDriveZSpeed(double ZSpeed){
        this.m_DriveZSpeed = ZSpeed;
    }

    public double getDriveRotationSpeed(){
        return this.m_DriveRotationSpeed;
    }

    public void setDriveRotationSpeed(double RotationSpeed){
        this.m_DriveRotationSpeed = RotationSpeed;
    }

    protected abstract void __updateDriveSpeedAndPositionTracker();
    protected abstract void __startDrive();

    @Override
    protected void __startTask() {
        this.__startDrive();
        this.__updateDriveSpeedAndPositionTracker();
    }

    @Override
    public void updateStatus() {
        if(this.isBusy()) {
            this.__updateDriveSpeedAndPositionTracker();
        }
    }
}
