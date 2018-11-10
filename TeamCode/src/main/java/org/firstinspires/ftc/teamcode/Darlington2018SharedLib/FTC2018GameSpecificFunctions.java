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
    private String VUFORIA_KEY = " -- YOUR NEW VUFORIA KEY GOES HERE  --- ";
    private VuforiaLocalizer m_vuforia = null;
    private TFObjectDetector m_tfod = null;
    private OpMode m_opMode;
    private VuforiaLocalizer.CameraDirection m_CameraDirection;
    private double[] m_phoneLocation; //size: 3 in X,Y,Z
    private double[] m_phoneRotation; //size: 3 in X,Y,Z
    private List<VuforiaTrackable> allTrackables;
    public FTC2018GameSpecificFunctions(@NonNull OpMode controllerOp, VuforiaLocalizer.CameraDirection CameraDirection, double[] phone3DLocationInRobotAxis, double[] phoneRotationInRobotAxis, String VUFORIAKEY){
        this.m_opMode = controllerOp;
        this.m_CameraDirection = CameraDirection;
        this.m_phoneLocation = FTC2018GameFieldConvert.convertVuforiaRobotFromRobotAxis(phone3DLocationInRobotAxis);

        //Convert unit of phoneLocation from cm to mm.
        this.m_phoneLocation[0] *= 10.0;
        this.m_phoneLocation[1] *= 10.0;
        this.m_phoneLocation[2] *= 10.0;
        //Finish converting

        this.m_phoneRotation = FTC2018GameFieldConvert.convert3DVuforiaRobotRotationFrom3DRobotRotation(phoneRotationInRobotAxis);
        this.VUFORIA_KEY = VUFORIAKEY;
        initVoforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        }
        initNav();
    }
    public VuforiaLocalizer.CameraDirection getCameraDirection(){
        return this.m_CameraDirection;
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
            m_Results[i] = new MineralInformation(recogType,recog.getLeft(),recog.getTop(),recog.getWidth(),recog.getHeight());
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
    public NavigationResult navigateVuforiaMarks(@NonNull double[] FieldAxisSize){
        NavigationResultType VisibleSigns = NavigationResultType.Unknown;
        OpenGLMatrix lastLocation = null;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                switch(trackable.getName()){
                    case "Blue-Rover":
                        VisibleSigns = NavigationResultType.BlueRover;
                        break;
                    case "Red-Footprint":
                        VisibleSigns = NavigationResultType.RedFootprint;
                        break;
                    case "Front-Craters":
                        VisibleSigns = NavigationResultType.FrontCraters;
                        break;
                    default: //case "Back-Space":
                        VisibleSigns = NavigationResultType.BackSpace;
                        break;
                }

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                    VisibleSigns = NavigationResultType.Unknown;
                    break;
                }
            }
        }

        // Provide feedback as to where the robot is located (if we know).
        if (VisibleSigns != NavigationResultType.Unknown) {
            // express position (translation) of robot in inches.
            VectorF translation = lastLocation.getTranslation();

            double[] m_VuforiaFieldPos = {translation.get(0) / 10.0,translation.get(1) / 10.0, translation.get(2) / 10.0}; //The unit for vuforia detection is mm. Convert to cm.
            //translation.get(2) yields the height of the robot.
            // express the rotation of the robot in degrees.
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            double[] m_VuforiaFieldRotation = {rotation.firstAngle,rotation.secondAngle,rotation.thirdAngle};
            double[] m_FieldAxisPos = FTC2018GameFieldConvert.convertFieldAxisFromVuforiaField(m_VuforiaFieldPos,FieldAxisSize);
            double[] m_FieldRotation = FTC2018GameFieldConvert.convert3DFieldRotationFrom3DVuforiaFieldRotation(m_VuforiaFieldRotation);
            NavigationResult m_Result = new NavigationResult(VisibleSigns,(float) m_FieldAxisPos[0],(float) m_FieldAxisPos[1], (float) m_FieldRotation[2]);
            return m_Result;
        }
        else {
            NavigationResult m_Result = new NavigationResult();
            return m_Result;
        }
    }
    protected void initTfod() {
        /* If you want to activate the object detecting camera contents to display on the ftc RC app, use this following code
        int tfodMonitorViewId = m_opMode.hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", m_opMode.hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        */
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters();
        this.m_tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, m_vuforia);
        this.m_tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        this.m_tfod.activate();
    }
    protected void initVoforia(){
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = this.VUFORIA_KEY;
        parameters.cameraDirection = this.m_CameraDirection;

        //  Instantiate the Vuforia engine
        m_vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
    protected void initNav(){
        float mmPerInch        = 25.4f;
        float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
        float mmTargetHeight   = (6) * mmPerInch;          // the height of the center of the target image above the floor

        VuforiaTrackables targetsRoverRuckus = this.m_vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsRoverRuckus);

        /**
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the {@link OpenGLMatrix} class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * This Rover Ruckus sample places a specific target in the middle of each perimeter wall.
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         *  coordinate system (the center of the field), facing up.
         */

        /**
         * To place the BlueRover target in the middle of the blue perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Then, we translate it along the Y axis to the blue perimeter wall.
         */
        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);

        /**
         * To place the RedFootprint target in the middle of the red perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 180 around the field's Z axis so the image is flat against the red perimeter wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative Y axis to the red perimeter wall.
         */
        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);

        /**
         * To place the FrontCraters target in the middle of the front perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 90 around the field's Z axis so the image is flat against the front wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative X axis to the front perimeter wall.
         */
        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);

        /**
         * To place the BackSpace target in the middle of the back perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it -90 around the field's Z axis so the image is flat against the back wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the X axis to the back perimeter wall.
         */
        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);

        /**
         * Create a transformation matrix describing where the phone is on the robot.
         *
         * The coordinate frame for the robot looks the same as the field.
         * The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
         * Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
         *
         * The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
         * pointing to the LEFT side of the Robot.  It's very important when you test this code that the top of the
         * camera is pointing to the left side of the  robot.  The rotation angles don't work if you flip the phone.
         *
         * If using the rear (High Res) camera:
         * We need to rotate the camera around it's long axis to bring the rear camera forward.
         * This requires a negative 90 degree rotation on the Y axis
         *
         * If using the Front (Low Res) camera
         * We need to rotate the camera around it's long axis to bring the FRONT camera forward.
         * This requires a Positive 90 degree rotation on the Y axis
         *
         * Next, translate the camera lens to where it is on the robot.
         * In this example, it is centered (left to right), but 110 mm forward of the middle of the robot, and 200 mm above ground level.
         */

        final int CAMERA_FORWARD_DISPLACEMENT  = 110;   // eg: Camera is 110 mm in front of robot center
        final int CAMERA_VERTICAL_DISPLACEMENT = 200;   // eg: Camera is 200 mm above ground
        final int CAMERA_LEFT_DISPLACEMENT     = 0;     // eg: Camera is ON the robot's center line

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        (float) this.m_phoneRotation[1], (float) this.m_phoneRotation[2], (float) this.m_phoneRotation[0]));

        /**  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : allTrackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, this.m_CameraDirection);
        }

        /** Start tracking the data sets we care about. */
        targetsRoverRuckus.activate();
    }
}
