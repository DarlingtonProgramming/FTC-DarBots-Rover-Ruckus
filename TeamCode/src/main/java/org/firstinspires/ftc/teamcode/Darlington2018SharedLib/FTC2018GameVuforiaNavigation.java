package org.firstinspires.ftc.teamcode.Darlington2018SharedLib;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.Robot2DPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.XYPlaneCalculations;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot3DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Runtime.RobotGlobal;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotOnPhoneCamera;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotWebcamCamera;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotCamera;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotNonBlockingDevice;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

public class FTC2018GameVuforiaNavigation {

    public static class Vuforia2DPosIndicator{
        protected double m_X;
        protected double m_Y;
        protected double m_RotationZ;
        public Vuforia2DPosIndicator(double X, double Y, double ZRotation){
            this.m_X = X;
            this.m_Y = Y;
            this.m_RotationZ = XYPlaneCalculations.normalizeDeg(ZRotation);
        }
        public Vuforia2DPosIndicator(Vuforia2DPosIndicator Pos2D){
            this.m_X = Pos2D.m_X;
            this.m_Y = Pos2D.m_Y;
            this.m_RotationZ = Pos2D.m_RotationZ;
        }
        public double getX(){
            return this.m_X;
        }
        public void setX(double X){
            this.m_X = X;
        }
        public double getY(){
            return this.m_Y;
        }
        public void setY(double Y){
            this.m_Y = Y;
        }
        public double getDistanceToOrigin(){
            return (Math.sqrt(Math.pow(this.getX(),2) + Math.pow(this.getY(),2)));
        }
        public double getRotationZ(){
            return this.m_RotationZ;
        }
        public void setRotationZ(double RotationZ){
            this.m_RotationZ = XYPlaneCalculations.normalizeDeg(RotationZ);
        }
    }
    public static class Vuforia3DPosIndicator extends Robot3DPositionIndicator {

        public Vuforia3DPosIndicator(double X, double Y, double Z, double RotationX, double RotationY, double RotationZ) {
            super(X, Y, Z, RotationX, RotationY, RotationZ);
        }

        public Vuforia3DPosIndicator(Robot2DPositionIndicator Pos2D, double Y, double RotationX, double RotationZ) {
            super(Pos2D, Y, RotationX, RotationZ);
        }

        public Vuforia3DPosIndicator(Robot3DPositionIndicator Pos3D) {
            super(Pos3D);
        }

        public Vuforia3DPosIndicator(Vuforia2DPosIndicator PosVuforia2D, double Z, double RotationX, double RotationY){
            super(PosVuforia2D.getX(),PosVuforia2D.getY(),Z,RotationX,RotationY,PosVuforia2D.getRotationZ());
        }

        public Vuforia2DPosIndicator toVuforia2DPosition(){
            return new Vuforia2DPosIndicator(this.getX(),this.getY(),this.getRotationZ());
        }
    }
    public static class Vuforia2DRobotAxisIndicator extends Vuforia2DPosIndicator{

        public Vuforia2DRobotAxisIndicator(double X, double Y, double ZRotation) {
            super(X, Y, ZRotation);
        }

        public Vuforia2DRobotAxisIndicator(Vuforia2DPosIndicator Pos2D) {
            super(Pos2D);
        }

        public Vuforia2DRobotAxisIndicator(Vuforia2DRobotAxisIndicator Pos2D) {
            super(Pos2D);
        }

        public Vuforia2DRobotAxisIndicator(Robot2DPositionTracker.Robot2DPositionRobotAxisIndicator RobotAxis2D){
            super(RobotAxis2D.getZ(),-RobotAxis2D.getX(),RobotAxis2D.getRotationY());
        }

        public Robot2DPositionTracker.Robot2DPositionRobotAxisIndicator toRobotAxis2D(){
            return new Robot2DPositionTracker.Robot2DPositionRobotAxisIndicator(-this.getY(),this.getX(),this.getRotationZ());
        }
    }
    public static class Vuforia2DFieldAxisIndicator extends Vuforia2DPosIndicator{

        public Vuforia2DFieldAxisIndicator(double X, double Y, double ZRotation) {
            super(X, Y, ZRotation);
        }

        public Vuforia2DFieldAxisIndicator(Vuforia2DPosIndicator Pos2D) {
            super(Pos2D);
        }

        public Vuforia2DFieldAxisIndicator(Vuforia2DFieldAxisIndicator Pos2D) {
            super(Pos2D);
        }

        public Vuforia2DFieldAxisIndicator(Robot2DPositionTracker.Robot2DPositionFieldAxisIndicator Pos2D){
            super(Pos2D.getX(),Pos2D.getZ(),Pos2D.getRotationY());
        }

        public Robot2DPositionTracker.Robot2DPositionFieldAxisIndicator toFieldAxis(Robot2DPositionTracker PosTracker){
            return PosTracker.new Robot2DPositionFieldAxisIndicator(this.getX(),this.getY(),this.getRotationZ());
        }
    }
    public static class Vuforia3DRobotAxisIndicator extends Vuforia3DPosIndicator {

        public Vuforia3DRobotAxisIndicator(double X, double Y, double Z, double RotationX, double RotationY, double RotationZ) {
            super(X, Y, Z, RotationX, RotationY, RotationZ);
        }

        public Vuforia3DRobotAxisIndicator(Robot2DPositionIndicator Pos2D, double Y, double RotationX, double RotationZ) {
            super(Pos2D, Y, RotationX, RotationZ);
        }

        public Vuforia3DRobotAxisIndicator(Robot3DPositionIndicator Pos3D) {
            super(Pos3D);
        }

        public Vuforia3DRobotAxisIndicator(Vuforia2DPosIndicator PosVuforia2D, double Z, double RotationX, double RotationY) {
            super(PosVuforia2D, Z, RotationX, RotationY);
        }

        public Vuforia3DRobotAxisIndicator(Vuforia2DRobotAxisIndicator PosVuforiaRobot2D, double Z, double RotationX, double RotationY){
            super(PosVuforiaRobot2D,Z,RotationX,RotationY);
        }

        public Vuforia2DRobotAxisIndicator getVuforia2DRobotAxisIndicator(){
            return new Vuforia2DRobotAxisIndicator(this.getX(),this.getY(),this.getRotationZ());
        }
    }
    public static class Vuforia3DFieldAxisIndicator extends Vuforia3DPosIndicator{

        public Vuforia3DFieldAxisIndicator(double X, double Y, double Z, double RotationX, double RotationY, double RotationZ) {
            super(X, Y, Z, RotationX, RotationY, RotationZ);
        }

        public Vuforia3DFieldAxisIndicator(Robot2DPositionIndicator Pos2D, double Y, double RotationX, double RotationZ) {
            super(Pos2D, Y, RotationX, RotationZ);
        }

        public Vuforia3DFieldAxisIndicator(Robot3DPositionIndicator Pos3D) {
            super(Pos3D);
        }

        public Vuforia3DFieldAxisIndicator(Vuforia2DPosIndicator PosVuforia2D, double Z, double RotationX, double RotationY) {
            super(PosVuforia2D, Z, RotationX, RotationY);
        }

        public Vuforia3DFieldAxisIndicator(Vuforia2DFieldAxisIndicator PosVuforia2D, double Z, double RotationX, double RotationY){
            super(PosVuforia2D, Z, RotationX, RotationY);
        }

        public Vuforia2DFieldAxisIndicator getVuforia2DFieldAxisIndicator(){
            return new Vuforia2DFieldAxisIndicator(this.getX(),this.getY(),this.getRotationZ());
        }
    }
    protected RobotCamera m_Camera;
    protected Vuforia3DRobotAxisIndicator m_CameraPosition;
    private List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
    private VuforiaTrackables targetsRoverRuckus;

    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
    private static final float mmTargetHeight   = (6) * mmPerInch;          // the height of the center of the target image above the floor

    public FTC2018GameVuforiaNavigation(RobotCamera Camera, Vuforia3DRobotAxisIndicator CameraPositionInCM){
        this.m_Camera = Camera;
        this.m_CameraPosition = CameraPositionInCM;
        initializeTrackables();
    }

    public Vuforia3DRobotAxisIndicator getCameraPosition(){
        return this.m_CameraPosition;
    }

    public void setCameraPosition(Vuforia3DRobotAxisIndicator CameraPositionInCM){
        this.m_CameraPosition = CameraPositionInCM;
    }

    public RobotCamera getCamera(){
        return this.m_Camera;
    }
    private void initializeTrackables(){
        VuforiaTrackables targetsRoverRuckus = this.m_Camera.getVuforia().loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

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

        updatePhoneLocation();

        targetsRoverRuckus.activate();
    }
    private void updatePhoneLocation(){
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

        float CAMERA_FORWARD_DISPLACEMENT  = (float) this.getCameraPosition().getX() * 10.0f;   // eg: Camera is 110 mm in front of robot center
        float CAMERA_VERTICAL_DISPLACEMENT = (float) this.getCameraPosition().getZ() * 10.0f;   // eg: Camera is 200 mm above ground
        float CAMERA_LEFT_DISPLACEMENT     = (float) this.getCameraPosition().getY() * 10.0f;     // eg: Camera is ON the robot's center line

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        (float) this.getCameraPosition().getRotationY(), (float) this.getCameraPosition().getRotationZ(), (float) this.getCameraPosition().getRotationX()));

        if(this.m_Camera instanceof RobotOnPhoneCamera) {
            VuforiaLocalizer.CameraDirection mCameraDirection = ((RobotOnPhoneCamera) this.m_Camera).getCameraDirection();
            for (VuforiaTrackable trackable : allTrackables) {
                ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, mCameraDirection);
            }
        }else if(this.m_Camera instanceof RobotWebcamCamera){
            for (VuforiaTrackable trackable : allTrackables) {
                ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(((RobotWebcamCamera) m_Camera).getWebcam(),phoneLocationOnRobot);
            }
        }
    }

    public Vuforia3DFieldAxisIndicator getRobotPosition(){
        boolean targetVisible;
        // check all the trackable target to see which one (if any) is visible.
        targetVisible = false;
        OpenGLMatrix lastLocation = null;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                RobotGlobal.getRunningRobotCore().getDebugger().addDebug(new RobotDebugger.RobotDebuggerInformation("Navigation Target", trackable.getName()));
                targetVisible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                break;
            }
        }

        // Provide feedback as to where the robot is located (if we know).
        if (targetVisible && lastLocation != null) {
            // express position (translation) of robot in inches.
            VectorF translation = lastLocation.getTranslation();
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            double X = translation.get(0) / 10.0;
            double Y = translation.get(1) / 10.0;
            double Z = translation.get(2) / 10.0;
            double RotX = rotation.firstAngle;
            double RotY = rotation.secondAngle;
            double RotZ = rotation.thirdAngle;
            return new Vuforia3DFieldAxisIndicator(X,Y,Z,RotX,RotY,RotZ);
        }
        else {
            return null;
        }
    }
}

