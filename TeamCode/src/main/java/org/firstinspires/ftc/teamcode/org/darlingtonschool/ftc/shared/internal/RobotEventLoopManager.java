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
