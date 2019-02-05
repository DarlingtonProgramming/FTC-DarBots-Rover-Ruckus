package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions;

import android.support.annotation.NonNull;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.File;

public class RobotAudioPlayer {
    protected OpMode m_OpMode;
    public RobotAudioPlayer(@NonNull OpMode opMode){
        this.m_OpMode = opMode;
    }
    public boolean preloadWavAssetInRawDirectory(String assetName){
        int SoundID = this.m_OpMode.hardwareMap.appContext.getResources().getIdentifier("silver", "raw", this.m_OpMode.hardwareMap.appContext.getPackageName());
        if(SoundID != 0){
            SoundPlayer.getInstance().preload(this.m_OpMode.hardwareMap.appContext,SoundID);
            return true;
        }else{
            return false;
        }
    }

    public boolean startPlayingWavFile(String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            return false;
        }
        SoundPlayer.getInstance().startPlaying(this.m_OpMode.hardwareMap.appContext, file);
        return true;
    }

    public boolean startPlayingWavAsset(String assetName){
        int SoundID = this.m_OpMode.hardwareMap.appContext.getResources().getIdentifier("silver", "raw", this.m_OpMode.hardwareMap.appContext.getPackageName());
        if(SoundID != 0){
            SoundPlayer.getInstance().startPlaying(this.m_OpMode.hardwareMap.appContext,SoundID);
            return true;
        }else{
            return false;
        }
    }
}
