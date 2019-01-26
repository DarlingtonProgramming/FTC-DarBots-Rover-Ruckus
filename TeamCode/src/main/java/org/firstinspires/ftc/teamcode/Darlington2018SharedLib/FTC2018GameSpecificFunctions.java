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

package org.firstinspires.ftc.teamcode.Darlington2018SharedLib;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotCamera;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

public class FTC2018GameSpecificFunctions {
    public enum MineralType{
        Silver,
        Gold,
        Unknown
    }
    public enum GoldPosType{
        Left,
        Center,
        Right,
        Unknown
    }
    public static class MineralInformation{
        private MineralType m_MineralType;
        private float m_Left;
        private float m_Top;
        private float m_Width;
        private float m_Height;
        private int m_ImageHeight;
        private int m_ImageWidth;
        public MineralInformation(){
            this.m_MineralType = MineralType.Unknown;
            this.m_Left = 0;
            this.m_Top = 0;
            this.m_Width = 0;
            this.m_Height = 0;
            this.m_ImageWidth = 0;
            this.m_ImageHeight = 0;
        }
        public MineralInformation(MineralType Type, float Left, float Top, float Width, float Height, int ImageWidth, int ImageHeight){
            this.m_MineralType = Type;
            this.m_Left = Left;
            this.m_Top = Top;
            this.m_Width = Width;
            this.m_Height = Height;
            this.m_ImageWidth = ImageWidth;
            this.m_ImageHeight = ImageHeight;
        }
        public MineralType getMineralType(){
            return this.m_MineralType;
        }
        public float getLeft(){
            return this.m_Left;
        }
        public float getTop(){
            return this.m_Top;
        }
        public float getWidth(){
            return this.m_Width;
        }
        public float getHeight(){
            return this.m_Height;
        }
        public int getImageWidth(){
            return this.m_ImageWidth;
        }
        public int getImageHeight(){
            return this.m_ImageHeight;
        }
    }
    public enum NavigationResultType{
        BlueRover,
        RedFootprint,
        FrontCraters,
        BackSpace,
        Unknown
    }
    public static class NavigationResult{
        private NavigationResultType m_ResultType;
        private float m_X;
        private float m_Y;
        private float m_Rotation;
        public NavigationResult(){
            this.m_ResultType = NavigationResultType.Unknown;
            this.m_X = 0f;
            this.m_Y = 0f;
            this.m_Rotation = 0f;
        }
        public NavigationResult(NavigationResultType ResultType, float X, float Y, float Rotation){
            this.m_ResultType = ResultType;
            this.m_X = X;
            this.m_Y = Y;
            this.m_Rotation = Rotation;
        }
        public NavigationResultType getResultType(){
            return this.m_ResultType;
        }
        public float getX(){
            return this.m_X;
        }
        public float getY(){
            return this.m_Y;
        }
        public float getRotation(){
            return this.m_Rotation;
        }
    }
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private RobotCamera m_Camera = null;
    private TFObjectDetector m_tfod = null;
    private OpMode m_opMode;
    private boolean m_Preview;
    public FTC2018GameSpecificFunctions(@NonNull OpMode controllerOp, RobotCamera CameraToUse, boolean CameraPreview){
        this.m_opMode = controllerOp;
        this.m_Preview = CameraPreview;
        this.m_Camera = CameraToUse;
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        }
    }
    public boolean isPreviewingDetectionResult(){
        return this.m_Preview;
    }
    public RobotCamera getCamera(){
        return this.m_Camera;
    }
    public boolean isMineralDetectionCompatible(){
        return (this.m_tfod != null);
    }
    public MineralInformation[] detectAllBlocksInCamera(){
        if(!this.isMineralDetectionCompatible()){
            return null;
        }
        List<Recognition> updatedRecognitions = this.m_tfod.getUpdatedRecognitions();
        if(updatedRecognitions == null){
            return null; //0 recognitions
        }
        int numDetected = updatedRecognitions.size();
        MineralInformation[] m_Results = new MineralInformation[numDetected];
        for(int i = 0; i<numDetected; i++){
            Recognition recog = updatedRecognitions.get(i);
            MineralType recogType = recog.getLabel().equals(LABEL_GOLD_MINERAL) ? MineralType.Gold : MineralType.Silver;
            m_Results[i] = new MineralInformation(recogType,recog.getLeft(),recog.getTop(),recog.getWidth(),recog.getHeight(),recog.getImageWidth(),recog.getImageHeight());
        }
        return m_Results;
    }
    public static MineralInformation firstMineralInCamera(MineralInformation[] Blocks){
        if(Blocks != null){
            return Blocks[0];
        }else{
            return new MineralInformation();
        }
    }
    public static MineralInformation leftMineralInCameraX(MineralInformation[] Blocks){
        if(Blocks != null && Blocks.length > 0){
            MineralInformation lowestXMineral = Blocks[0];
            if(Blocks.length <= 1){
                return Blocks[0];
            }
            for(int i = 1; i < Blocks.length; i++){
                if(Blocks[i].getLeft() < lowestXMineral.getLeft()){
                    lowestXMineral = Blocks[i];
                }
            }
            return lowestXMineral;
        }else{
            return new MineralInformation();
        }
    }
    public static MineralInformation rightMineralInCameraX(MineralInformation[] Blocks){
        if(Blocks != null && Blocks.length > 0){
            MineralInformation highestXMineral = Blocks[0];
            if(Blocks.length <= 1){
                return Blocks[0];
            }
            for(int i = 1; i < Blocks.length; i++){
                if(Blocks[i].getLeft() > highestXMineral.getLeft()){
                    highestXMineral = Blocks[i];
                }
            }
            return highestXMineral;
        }else{
            return new MineralInformation();
        }
    }
    public static MineralInformation MineralClosestToX(MineralInformation[] Blocks, float X){
        if(Blocks != null && Blocks.length > 0){
            MineralInformation closestXMineral = Blocks[0];
            float closestXMineralMidX = Blocks[0].getLeft() + Blocks[0].getWidth() / 2.0f;
            float closestXMineralDiff = Math.abs(closestXMineralMidX - X);
            float currentMidX = 0.0f;
            if(Blocks.length <= 1){
                return Blocks[0];
            }
            for(int i = 1; i < Blocks.length; i++){
                currentMidX = Blocks[i].getLeft() + Blocks[i].getWidth() / 2.0f;
                if(Math.abs(currentMidX - X) < closestXMineralDiff){
                    closestXMineral = Blocks[i];
                    closestXMineralMidX = currentMidX;
                    closestXMineralDiff = Math.abs(closestXMineralMidX - X);
                }
            }
            return closestXMineral;
        }else{
            return new MineralInformation();
        }
    }
    public static MineralInformation MineralInCenterX(MineralInformation[] Blocks){
        if(Blocks != null && Blocks.length > 0){
            return MineralClosestToX(Blocks,Blocks[0].getImageWidth() / 2.0f);
        }else{
            return new MineralInformation();
        }
    }
    public static MineralInformation leftMineralInCameraY(MineralInformation[] Blocks){
        if(Blocks != null && Blocks.length > 0){
            MineralInformation lowestYMineral = Blocks[0];
            if(Blocks.length <= 1){
                return Blocks[0];
            }
            for(int i = 1; i < Blocks.length; i++){
                if(Blocks[i].getTop() < lowestYMineral.getTop()){
                    lowestYMineral = Blocks[i];
                }
            }
            return lowestYMineral;
        }else{
            return new MineralInformation();
        }
    }
    public static MineralInformation rightMineralInCameraY(MineralInformation[] Blocks){
        if(Blocks != null && Blocks.length > 0){
            MineralInformation highestYMineral = Blocks[0];
            if(Blocks.length <= 1){
                return Blocks[0];
            }
            for(int i = 1; i < Blocks.length; i++){
                if(Blocks[i].getTop() > highestYMineral.getTop()){
                    highestYMineral = Blocks[i];
                }
            }
            return highestYMineral;
        }else{
            return new MineralInformation();
        }
    }
    public static MineralInformation MineralClosestToY(MineralInformation[] Blocks, float Y){
        if(Blocks != null && Blocks.length > 0){
            MineralInformation closestYMineral = Blocks[0];
            float closestYMineralMidY = Blocks[0].getTop() + Blocks[0].getHeight() / 2.0f;
            float closestYMineralDiff = Math.abs(closestYMineralMidY - Y);
            float currentMidY = 0.0f;
            float currentMidYDiff = 0.0f;
            if(Blocks.length <= 1){
                return Blocks[0];
            }
            for(int i = 1; i < Blocks.length; i++){
                currentMidY = Blocks[i].getTop() + Blocks[i].getHeight() / 2.0f;
                currentMidYDiff = Math.abs(currentMidY - Y);
                if(currentMidYDiff < closestYMineralDiff){
                    closestYMineral = Blocks[i];
                    closestYMineralMidY = currentMidY;
                    closestYMineralDiff = currentMidYDiff;
                }
            }
            return closestYMineral;
        }else{
            return new MineralInformation();
        }
    }
    public static MineralInformation MineralInCenterY(MineralInformation[] Blocks){
        if(Blocks != null && Blocks.length > 0){
            return MineralClosestToY(Blocks,Blocks[0].getImageHeight() / 2.0f);
        }else{
            return new MineralInformation();
        }
    }
    public static GoldPosType detectAutonomousGoldMineralPos(MineralInformation[] MineralsDetected){
        if(MineralsDetected == null){
            return GoldPosType.Unknown;
        }else if(MineralsDetected.length != 3){
            return GoldPosType.Unknown;
        }
        float goldMineralX = -1;
        float silverMineral1X = -1;
        float silverMineral2X = -1;
        for(MineralInformation Mineral : MineralsDetected){
            if(Mineral.getMineralType() == MineralType.Gold){
                goldMineralX = Mineral.getLeft();
            }else if(Mineral.getMineralType() == MineralType.Silver){
                if(silverMineral1X == -1){
                    silverMineral1X = Mineral.getLeft();
                }else{
                    silverMineral2X = Mineral.getLeft();
                }
            }
        }
        if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) { //must be one gold, 2 silver
            if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X){
                return GoldPosType.Left;
            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                return GoldPosType.Right;
            } else {
                return GoldPosType.Center;
            }
        }else{
            return GoldPosType.Unknown;
        }
    }
    public static GoldPosType detectAutonomousGoldMineralPosHorizontal(MineralInformation[] MineralsDetected){
        if(MineralsDetected == null){
            return GoldPosType.Unknown;
        }
        MineralInformation[] minerals = new MineralInformation[MineralsDetected.length];
        for(int i=0;i<MineralsDetected.length;i++){
            minerals[i] = new MineralInformation(MineralsDetected[i].getMineralType(),MineralsDetected[i].getTop(),MineralsDetected[i].getLeft(),MineralsDetected[i].getWidth(),MineralsDetected[i].getHeight(),MineralsDetected[i].getImageWidth(),MineralsDetected[i].getImageHeight());
            //Reverse Left and Top
        }
        return detectAutonomousGoldMineralPos(minerals);
    }
    protected void initTfod() {
        TFObjectDetector.Parameters tfodParameters;
        if(this.m_Preview) {
            int tfodMonitorViewId = m_opMode.hardwareMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", m_opMode.hardwareMap.appContext.getPackageName());
            tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        }else {
            tfodParameters = new TFObjectDetector.Parameters();
        }
        this.m_tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, this.m_Camera.getVuforia());
        this.m_tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        this.m_tfod.activate();
    }
}
