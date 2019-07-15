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

package org.darbots.darbotsftclib.libcore.templates.motion_related;

import android.support.annotation.NonNull;

import org.darbots.darbotsftclib.libcore.calculations.dimentionalcalculation.Robot2DPositionTracker;
import org.darbots.darbotsftclib.libcore.templates.RobotNonBlockingDevice;

import java.util.ArrayList;

public abstract class RobotMotionSystem implements RobotNonBlockingDevice {
    public static final double SlowDownFactor = 1.0;
    public static final double SlowDownTime = 0.1;
    public static final double SlowDownSmallest = 10;
    protected ArrayList<RobotMotionSystemTask> m_TaskLists;
    protected Robot2DPositionTracker m_PosTracker;
    protected double m_LinearMotionFrictionFactor;
    protected double m_RotationalMotionFrictionFactor;
    public RobotMotionSystem(Robot2DPositionTracker PositionTracker){
        this.m_TaskLists = new ArrayList<>();
        this.m_PosTracker = PositionTracker;
        this.m_LinearMotionFrictionFactor = 1;
        this.m_RotationalMotionFrictionFactor = 1;
    }
    public RobotMotionSystem(RobotMotionSystem MotionSystem){
        this.m_TaskLists = new ArrayList<>();
        this.m_PosTracker = MotionSystem.m_PosTracker;
        this.m_LinearMotionFrictionFactor = MotionSystem.m_LinearMotionFrictionFactor;
        this.m_RotationalMotionFrictionFactor = MotionSystem.m_RotationalMotionFrictionFactor;
    }

    public double getLinearMotionFrictionFactor(){
        return this.m_LinearMotionFrictionFactor;
    }

    public void setLinearMotionFrictionFactor(double Factor){
        this.m_LinearMotionFrictionFactor = Factor;
    }

    public double getRotationalMotionFrictionFactor(){
        return this.m_RotationalMotionFrictionFactor;
    }

    public void setRotationalMotionFrictionFactor(double Factor){
        this.m_RotationalMotionFrictionFactor = Factor;
    }

    public Robot2DPositionTracker getPositionTracker(){
        return this.m_PosTracker;
    }
    public void setPositionTracker(Robot2DPositionTracker PositionTracker){
        this.m_PosTracker = PositionTracker;
    }
    public void addTask(@NonNull RobotMotionSystemTask Task){
        this.m_TaskLists.add(Task);
        this.scheduleTasks();
    }

    public void replaceTask(@NonNull RobotMotionSystemTask Task){
        if(!this.m_TaskLists.isEmpty()){
             if(this.m_TaskLists.get(0).isBusy())
                 this.m_TaskLists.get(0).stopTask();
        }
        this.m_TaskLists.clear();
        this.m_TaskLists.add(Task);
        this.scheduleTasks();
    }

    public void deleteCurrentTask(){
        if(this.m_TaskLists.isEmpty()){
            this.__stopMotion();
            return;
        }
        this.m_TaskLists.get(0).stopTask();
        this.m_TaskLists.remove(0);
        this.scheduleTasks();
    }

    public ArrayList<RobotMotionSystemTask> getTaskLists(){
        return this.m_TaskLists;
    }

    public RobotMotionSystemTask getCurrentTask(){
        return this.m_TaskLists.isEmpty() ? null : this.m_TaskLists.get(0);
    }

    public void deleteAllTasks(){
        this.__stopMotion();
        if(this.m_TaskLists.isEmpty()){
            return;
        }
        RobotMotionSystemTask currentTask = this.m_TaskLists.get(0);
        this.m_TaskLists.get(0).stopTask();
        this.m_TaskLists.clear();
    }

    public void __checkTasks(){
        if(this.m_TaskLists.isEmpty()){
            this.__stopMotion();
            return;
        }else if(!this.m_TaskLists.get(0).isBusy()){
            this.deleteCurrentTask();
        }
    }

    protected abstract void __stopMotion();

    protected void scheduleTasks(){
        if(!this.m_TaskLists.isEmpty()){
            if(!this.m_TaskLists.get(0).isBusy()) {
                this.m_TaskLists.get(0).setMotionSystem(this);
                this.m_TaskLists.get(0).startTask();
            }
        }else if(this.m_TaskLists.isEmpty()){
            this.__stopMotion();
        }
    }

    @Override
    public boolean isBusy(){
        return (!this.m_TaskLists.isEmpty());
    }

    @Override
    public void updateStatus(){
        if((!this.m_TaskLists.isEmpty())){
            if(this.m_TaskLists.get(0).isBusy())
                this.m_TaskLists.get(0).updateStatus();
        }
    }

    @Override
    public void waitUntilFinish(){
        while(this.isBusy()){
            this.updateStatus();
        }
    }

    public abstract RobotMotionSystemFixedXDistanceTask getFixedXDistanceTask(double XDistance, double Speed);
    public abstract RobotMotionSystemFixedZDistanceTask getFixedZDistanceTask(double ZDistance, double Speed);
    public abstract RobotMotionSystemFixedTurnTask getFixedTurnTask(double Deg, double Speed);
    public abstract RobotMotionSystemTeleOpControlTask getTeleOpTask();
}
