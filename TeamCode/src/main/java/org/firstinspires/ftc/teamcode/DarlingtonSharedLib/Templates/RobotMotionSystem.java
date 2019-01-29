package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates;

import android.support.annotation.NonNull;

import com.sun.tools.javac.util.Position;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.Robot2DPositionTracker;

import java.util.ArrayDeque;

public abstract class RobotMotionSystem implements RobotNonBlockingDevice {
    protected ArrayDeque<RobotMotionSystemTask> m_TaskLists;
    protected Robot2DPositionTracker m_PosTracker;
    public RobotMotionSystem(Robot2DPositionTracker PositionTracker){
        this.m_TaskLists = new ArrayDeque<RobotMotionSystemTask>();
        this.m_PosTracker = PositionTracker;
    }
    public RobotMotionSystem(RobotMotionSystem MotionSystem){
        this.m_TaskLists = new ArrayDeque<RobotMotionSystemTask>();
        this.m_PosTracker = MotionSystem.m_PosTracker;
    }
    public Robot2DPositionTracker getPositionTracker(){
        return this.m_PosTracker;
    }
    public void setPositionTracker(Robot2DPositionTracker PositionTracker){
        this.m_PosTracker = PositionTracker;
    }
    public void addTask(@NonNull RobotMotionSystemTask Task){
        this.m_TaskLists.addLast(Task);
        this.scheduleTasks();
    }
    public void deleteCurrentTask(){
        if(this.m_TaskLists.isEmpty()){
            return;
        }
        this.m_TaskLists.getFirst().stopTask();
        this.m_TaskLists.removeFirst();
        this.scheduleTasks();
    }

    public ArrayDeque<RobotMotionSystemTask> getTaskLists(){
        return this.m_TaskLists;
    }

    public RobotMotionSystemTask getCurrentTask(){
        return this.m_TaskLists.isEmpty() ? null : this.m_TaskLists.getFirst();
    }

    public void deleteAllTasks(){
        if(this.m_TaskLists.isEmpty()){
            return;
        }
        this.m_TaskLists.getFirst().stopTask();
        this.m_TaskLists.clear();
    }

    protected void checkTasks(){
        if(this.m_TaskLists.isEmpty()){
            return;
        }
        if(!this.m_TaskLists.getFirst().isBusy()){
            this.deleteCurrentTask();
        }
    }

    protected void scheduleTasks(){
        if(!this.m_TaskLists.isEmpty() && !this.m_TaskLists.getFirst().isBusy()) {
            this.m_TaskLists.getFirst().setMotionSystem(this);
            this.m_TaskLists.getFirst().startTask();
        }
    }

    @Override
    public boolean isBusy(){
        return (!this.m_TaskLists.isEmpty());
    }

    @Override
    public void updateStatus(){
        this.checkTasks();
        if(!this.m_TaskLists.isEmpty() && this.m_TaskLists.getFirst().isBusy()){
            this.m_TaskLists.getFirst().updateStatus();
        }
    }

    @Override
    public void waitUntilFinish(){
        while(this.isBusy()){
            this.updateStatus();
        }
    }
}
