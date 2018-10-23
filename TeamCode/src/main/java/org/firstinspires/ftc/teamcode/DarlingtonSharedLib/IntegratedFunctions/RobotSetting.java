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

import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ExtLibs.json.JSONObject;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ExtLibs.json.parser.JSONParser;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ExtLibs.json.parser.ParseException;

import java.io.File;

public class RobotSetting {
    public static String settingFile = "DarBots.json";
    public static void saveSettings(JSONObject Settings) {
        String filename = settingFile;
        File file = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(file, Settings.toJSONString());
    }
    public static JSONObject getSettings(){
        JSONParser m_Parser = new JSONParser();
        String filename = settingFile;
        File settingFile = AppUtil.getInstance().getSettingsFile(filename);
        String FileContent = ReadWriteFile.readFile(settingFile);
        if(FileContent.isEmpty()){
            return new JSONObject();
        }
        try{
            JSONObject m_Object = (JSONObject) m_Parser.parse(FileContent);
            return m_Object;
        }catch(ParseException e){
            return new JSONObject();
        }catch(Exception e){
            return new JSONObject();
        }
    }
    public static <T> T getSetting(String Key, T defaultValue){
        JSONObject m_Settings = getSettings();
        if(m_Settings.containsKey(Key)){
            return (T) m_Settings.get(Key);
        }else{
            return defaultValue;
        }
    }
    public static <T> void saveSetting(String Key, T Data){
        JSONObject m_Settings = getSettings();
        m_Settings.put(Key,Data);
        saveSettings(m_Settings);
    }
}
