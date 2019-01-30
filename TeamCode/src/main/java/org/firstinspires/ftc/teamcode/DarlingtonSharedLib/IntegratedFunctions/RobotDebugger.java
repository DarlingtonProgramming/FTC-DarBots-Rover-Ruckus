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

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

import java.util.ArrayList;

public class RobotDebugger implements RobotNonBlockingDevice {
    public static class RobotDebuggerInformation{
        public RobotDebuggerInformation(String Title, String Description){
            this.title = Title;
            this.description = Description;
        }
        public String title;
        public String description;
    }
    public static interface RobotDebuggerCallable{
        RobotDebuggerInformation updateDebugger();
    }
    public static class ObjectDebuggerWrapper<E> implements  RobotDebuggerCallable{
        protected E m_Object;
        protected String m_Title;
        public E getObject(){
            return this.m_Object;
        }
        public void setObject(E Object){
            this.m_Object = Object;
        }
        public String getTitle(){
            return this.m_Title;
        }
        public void setTitle(String Title){
            this.m_Title = Title;
        }

        public ObjectDebuggerWrapper(String Title, E Object){
            this.m_Title = Title;
            this.m_Object = Object;
        }
        @Override
        public RobotDebuggerInformation updateDebugger() {
            return new RobotDebuggerInformation(this.m_Title,this.m_Object.toString());
        }
    }
    public static class ProcedureDebuggerWrapper<E> implements RobotDebuggerCallable{
        protected Func<E> m_Procedure;
        protected String m_Title;
        public ProcedureDebuggerWrapper(String Title, Func<E> Procedure){
            this.m_Procedure = Procedure;
            this.m_Title = Title;
        }
        public Func<E> getProcedure(){
            return this.m_Procedure;
        }
        public void setProcedure(Func<E> Procedure){
            this.m_Procedure = Procedure;
        }
        public String getTitle(){
            return this.m_Title;
        }
        public void setTitle(String Title){
            this.m_Title = Title;
        }
        @Override
        public RobotDebuggerInformation updateDebugger() {
            return new RobotDebuggerInformation(this.m_Title,this.m_Procedure.value().toString());
        }
    }
    protected Telemetry m_Telemetry = null;
    protected ArrayList<RobotDebuggerCallable> m_Callables = null;

    public RobotDebugger(Telemetry telemetry){
        this.m_Callables = new ArrayList<RobotDebuggerCallable>();
        this.m_Telemetry = telemetry;
    }

    public Telemetry getTelemetry(){
        return this.m_Telemetry;
    }

    public void setTelemetry(Telemetry telemetry){
        this.m_Telemetry = telemetry;

    }

    public ArrayList<RobotDebuggerCallable> getDebuggerCallableList(){
        return this.m_Callables;
    }

    public void setDebuggerCallbleList(ArrayList<RobotDebuggerCallable> callables){
        if(callables != null){
            this.m_Callables = callables;
        }else{
            this.m_Callables = new ArrayList<RobotDebuggerCallable>();
        }
    }

    public void addDebuggerCallable(RobotDebuggerCallable callable){
        if(callable != null) {
            this.m_Callables.add(callable);
        }
    }

    public boolean deleteDebuggerCallable(RobotDebuggerCallable callable){
        int indexCB = this.m_Callables.indexOf(callable);
        if(indexCB == -1){
            return false;
        }else{
            this.m_Callables.remove(indexCB);
            return true;
        }
    }

    public void addDebug(RobotDebuggerInformation information){
        if(this.m_Telemetry != null){
            this.m_Telemetry.addData(information.title,information.description);
        }
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void waitUntilFinish() {
        return;
    }

    @Override
    public void updateStatus() {
        if(this.m_Callables != null){
            for(RobotDebuggerCallable i : this.m_Callables){
                this.addDebug(i.updateDebugger());
            }
        }
        if(this.m_Telemetry != null) {
            this.m_Telemetry.update();
        }
    }
}
