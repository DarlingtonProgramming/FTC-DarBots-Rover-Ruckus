/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopManager;
 */

package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal;

import java.util.ArrayList;

public class RobotEventLoopManager implements RobotEventLoopable {
    private ArrayList<RobotEventLoopable> m_Loopables;
    public RobotEventLoopManager(){
        this.m_Loopables = new ArrayList<RobotEventLoopable>();
    }
    public void add(RobotEventLoopable loopableObject){
        this.m_Loopables.add(loopableObject);
    }
    public void remove(RobotEventLoopable loopableObject){
        if(this.m_Loopables.contains(loopableObject)) {
            this.m_Loopables.remove(loopableObject);
        }
    }
    @Override
    public void doLoop(){
        for(RobotEventLoopable myLoopable : this.m_Loopables){
            myLoopable.doLoop();
        }
    }
}
