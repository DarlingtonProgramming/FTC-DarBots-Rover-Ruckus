package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.org.json.simple.JSONObject;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.org.json.simple.parser.JSONParser;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.org.json.simple.parser.ParseException;

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
