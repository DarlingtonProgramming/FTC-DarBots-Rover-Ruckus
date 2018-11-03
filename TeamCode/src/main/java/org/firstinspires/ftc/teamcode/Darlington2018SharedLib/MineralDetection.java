package org.firstinspires.ftc.teamcode.Darlington2018SharedLib;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class MineralDetection {
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
    public class MineralInformation{
        private MineralType m_MineralType;
        private float m_Left;
        private float m_Top;
        private float m_Width;
        private float m_Height;
        public MineralInformation(){
            this.m_MineralType = MineralType.Unknown;
            this.m_Left = 0;
            this.m_Top = 0;
            this.m_Width = 0;
            this.m_Height = 0;
        }
        public MineralInformation(MineralType Type, float Left, float Top, float Width, float Height){
            this.m_MineralType = Type;
            this.m_Left = Left;
            this.m_Top = Top;
            this.m_Width = Width;
            this.m_Height = Height;
        }
        public MineralType getMineralType(){
            return this.m_MineralType;
        }
        public void setMineralType(MineralType Type){
            this.m_MineralType = Type;
        }
        public float getLeft(){
            return this.m_Left;
        }
        public void setLeft(float Left){
            this.m_Left = Left;
        }
        public float getTop(){
            return this.m_Top;
        }
        public void setTop(float Top){
            this.m_Top = Top;
        }
        public float getWidth(){
            return this.m_Width;
        }
        public void setWidth(float Width){
            this.m_Width = Width;
        }
        public float getHeight(){
            return this.m_Height;
        }
        public void setHeight(float Height){
            this.m_Height = Height;
        }
    }
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private String VUFORIA_KEY = " -- YOUR NEW VUFORIA KEY GOES HERE  --- ";
    private VuforiaLocalizer m_vuforia = null;
    private TFObjectDetector m_tfod = null;
    private OpMode m_opMode = null;
    public MineralDetection(@NonNull OpMode controllerOp, String VUFORIAKEY){
        this.m_opMode = controllerOp;
        this.VUFORIA_KEY = VUFORIAKEY;
        initVoforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        }
    }
    public boolean isDetectionCompatible(){
        return (this.m_tfod != null);
    }
    public MineralInformation[] detectAllBlocksInCamera(){
        if(!this.isDetectionCompatible()){
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
            m_Results[i] = new MineralInformation(recogType,recog.getLeft(),recog.getTop(),recog.getWidth(),recog.getHeight());
        }
        return m_Results;
    }
    /*
    returns -1 if on the left, 0 if in the center, 1 if on the right.
     */
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
    protected void initTfod() {
        int tfodMonitorViewId = m_opMode.hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", m_opMode.hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        this.m_tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, m_vuforia);
        this.m_tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    protected void initVoforia(){
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = this.VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        m_vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
}
