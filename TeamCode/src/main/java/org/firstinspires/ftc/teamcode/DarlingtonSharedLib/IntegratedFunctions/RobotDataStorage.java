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

import android.provider.ContactsContract;

import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ExtLibs.json.JSONObject;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ExtLibs.json.parser.JSONParser;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ExtLibs.json.parser.ParseException;

import java.io.File;

public class RobotDataStorage {
    protected String settingFile = "DarBots.json";
    private JSONObject m_Settings = null;

    public RobotDataStorage(String settingFileName){
        this.settingFile = settingFileName;
        this.readSettings();
    }

    public RobotDataStorage(RobotDataStorage DataStorage){
        this.settingFile = DataStorage.settingFile;
        this.m_Settings = DataStorage.m_Settings;
    }

    public String getSettingFileName(){
        return this.settingFile;
    }

    public void setSettingFileName(String Filename){
        this.settingFile = Filename;
        if(m_Settings != null || m_Settings.size() > 0){
            this.saveSettingsToFile();
        }else {
            this.readSettings();
        }
    }

    protected void readSettings(){
        JSONParser m_Parser = new JSONParser();
        String filename = this.settingFile;
        File settingFile = AppUtil.getInstance().getSettingsFile(filename);
        String FileContent = ReadWriteFile.readFile(settingFile);
        if(FileContent.isEmpty()){
            m_Settings = new JSONObject();
        }
        try{
            JSONObject m_Object = (JSONObject) m_Parser.parse(FileContent);
            m_Settings = m_Object;
        }catch(ParseException e){
            m_Settings = new JSONObject();
        }catch(Exception e){
            m_Settings = new JSONObject();
        }
    }
    public void saveSettingsToFile(){
        String fileName = this.settingFile;
        File file = AppUtil.getInstance().getSettingsFile(fileName);
        ReadWriteFile.writeFile(file, m_Settings.toJSONString());
    }

    public JSONObject getSettings(){
        return this.m_Settings;
    }

    public void setSettings(JSONObject settings){
        this.m_Settings = settings;
    }


    public <T> T getSetting(String Key, T defaultValue){
        JSONObject tempSettings = this.getSettings();
        if(tempSettings.containsKey(Key)){
            return (T) tempSettings.get(Key);
        }else{
            return defaultValue;
        }
    }
    public <T> void saveSetting(String Key, T Data){
        JSONObject tempSettings = this.getSettings();
        tempSettings.put(Key,Data);
        this.setSettings(tempSettings);
    }
}
