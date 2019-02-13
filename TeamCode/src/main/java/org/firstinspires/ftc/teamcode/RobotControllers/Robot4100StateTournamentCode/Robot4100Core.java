package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameSpecificFunctions;
import org.firstinspires.ftc.teamcode.Darlington2018SharedLib.FTC2018GameVuforiaNavigation;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.Robot2DPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ChassisControllers.OmniWheel4SideDiamondShaped;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotion;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorController;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorWithEncoder;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorWithoutEncoder;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotOnPhoneCamera;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor_WithLimitSwitches;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotWebcamCamera;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotCore;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;
import org.firstinspires.ftc.teamcode.RobotControllers.DarbotsPrivateInfo.PrivateSettings;


public class Robot4100Core extends RobotCore {
    private OmniWheel4SideDiamondShaped m_MotionSystem;
    private RobotServoUsingMotor m_LinearActuator;
    private RobotServoUsingMotor m_DrawerSlide;
    private RobotServoUsingMotor m_DumperSlide;
    private RobotMotorWithoutEncoder m_CollectorSweep;
    private Servo m_CollectorSetOut;
    private Servo m_DumperServo;
    private FTC2018GameVuforiaNavigation m_VuforiaNav;
    private FTC2018GameSpecificFunctions m_MineralDetection;

    public Robot4100Core(@NonNull OpMode ControllingOpMode, Robot2DPositionIndicator currentPosition, boolean readSavedValues, boolean initVuforiaNav, boolean initTFOD){
        super(ControllingOpMode,Robot4100Setting.SettingFileName);

        Robot2DPositionTracker PosTracker;

        if(currentPosition == null){
            PosTracker = null;
        }else {
            PosTracker = new Robot2DPositionTracker(currentPosition, Robot4100Setting.EXTREMEPOINTS, Robot4100Setting.Field_MinPoint, Robot4100Setting.Field_MaxPoint);
        }

        RobotMotion FLMotion = new RobotMotion(new RobotMotorController(new RobotMotorWithEncoder(ControllingOpMode.hardwareMap.dcMotor.get(Robot4100Setting.LEFTFRONTWHEEL_CONFIGURATIONNAME),Robot4100Setting.LEFTFRONTWHEEL_MOTORTYPE),Robot4100Setting.MOTIONSYSTEM_TIMECONTROLENABLED,Robot4100Setting.MOTIONSYSTEM_TIMECONTROLFACTOR),Robot4100Setting.LEFTFRONTWHEEL_MOTORWHEEL);
        RobotMotion FRMotion = new RobotMotion(new RobotMotorController(new RobotMotorWithEncoder(ControllingOpMode.hardwareMap.dcMotor.get(Robot4100Setting.RIGHTFRONTWHEEL_CONFIGURATIONNAME),Robot4100Setting.RIGHTFRONTWHEEL_MOTORTYPE),Robot4100Setting.MOTIONSYSTEM_TIMECONTROLENABLED,Robot4100Setting.MOTIONSYSTEM_TIMECONTROLFACTOR),Robot4100Setting.RIGHTFRONTWHEEL_MOTORWHEEL);
        RobotMotion BLMotion = new RobotMotion(new RobotMotorController(new RobotMotorWithEncoder(ControllingOpMode.hardwareMap.dcMotor.get(Robot4100Setting.LEFTBACKWHEEL_CONFIGURATIONNAME),Robot4100Setting.LEFTBACKWHEEL_MOTORTYPE),Robot4100Setting.MOTIONSYSTEM_TIMECONTROLENABLED,Robot4100Setting.MOTIONSYSTEM_TIMECONTROLFACTOR),Robot4100Setting.LEFTBACKWHEEL_MOTORWHEEL);
        RobotMotion BRMotion = new RobotMotion(new RobotMotorController(new RobotMotorWithEncoder(ControllingOpMode.hardwareMap.dcMotor.get(Robot4100Setting.RIGHTBACKWHEEL_CONFIGURATIONNAME),Robot4100Setting.RIGHTBACKWHEEL_MOTORTYPE),Robot4100Setting.MOTIONSYSTEM_TIMECONTROLENABLED,Robot4100Setting.MOTIONSYSTEM_TIMECONTROLFACTOR),Robot4100Setting.RIGHTBACKWHEEL_MOTORWHEEL);
        this.m_MotionSystem = new OmniWheel4SideDiamondShaped(FLMotion,FRMotion,BLMotion,BRMotion,PosTracker);

        RobotMotorWithEncoder LinearActuatorMotor = new RobotMotorWithEncoder(ControllingOpMode.hardwareMap.dcMotor.get(Robot4100Setting.LINEARACTUATOR_CONFIGURATIONNAME),Robot4100Setting.LINEARACTUATOR_MOTORTYPE);
        this.m_LinearActuator = new RobotServoUsingMotor(new RobotMotorController(LinearActuatorMotor,Robot4100Setting.LINEARACTUATOR_TIMEOUTCONTROL,Robot4100Setting.LINEARACTUATOR_TIMEOUTFACTOR),0,Robot4100Setting.LINEARACTUATOR_MAX,Robot4100Setting.LINEARACTUATOR_MIN);

        RobotMotorWithEncoder DrawerSlideMotor = new RobotMotorWithEncoder(ControllingOpMode.hardwareMap.dcMotor.get(Robot4100Setting.DRAWERSLIDEAPPROACH_CONFIGURATIONNAME),Robot4100Setting.DRAWERSLIDEAPPROACH_MOTORTYPE);
        DrawerSlideMotor.setDirectionReversed(true);
        TouchSensor drawerSlideMin = ControllingOpMode.hardwareMap.touchSensor.get(Robot4100Setting.DRAWERSLIDE_MINTOUCHSENSOR_CONFIGURATIONNAME);
        TouchSensor drawerSlideMax = ControllingOpMode.hardwareMap.touchSensor.get(Robot4100Setting.DRAWESLIDE_MAXTOUCHSENSOR_CONFIGURATIONNAME);
        this.m_DrawerSlide = new RobotServoUsingMotor_WithLimitSwitches(new RobotMotorController(DrawerSlideMotor,Robot4100Setting.DRAWERSLIDE_TIMEOUTCONTROL,Robot4100Setting.DRAWERSLIDE_TIMEOUTFACTOR),drawerSlideMin,drawerSlideMax,0,Robot4100Setting.DRAWERSLIDEAPPROACH_MAX,Robot4100Setting.DRAWERSLIDEAPPROACH_MIN);
        //this.m_DrawerSlide = new RobotServoUsingMotor(new RobotMotorController(DrawerSlideMotor,Robot4100Setting.DRAWERSLIDE_TIMEOUTCONTROL,Robot4100Setting.DRAWERSLIDE_TIMEOUTFACTOR),0,Robot4100Setting.DRAWERSLIDEAPPROACH_MAX,Robot4100Setting.DRAWERSLIDEAPPROACH_MIN);

        RobotMotorWithEncoder DumperMotor = new RobotMotorWithEncoder(ControllingOpMode.hardwareMap.dcMotor.get(Robot4100Setting.DUMPERSLIDE_CONFIGURATIONNAME),Robot4100Setting.DUMPERSLIDE_MOTORTYPE);
        TouchSensor dumperSlideMax = ControllingOpMode.hardwareMap.touchSensor.get(Robot4100Setting.DUMPERSLIDE_MAXTOUCHSENSOR_CONFIGURATIONNAME);
        this.m_DumperSlide = new RobotServoUsingMotor_WithLimitSwitches(new RobotMotorController(DumperMotor,Robot4100Setting.DUMPERSLIDE_TIMEOUTCONTROL,Robot4100Setting.DUMPERSLIDE_TIMEOUTFACTOR),null,dumperSlideMax,0,Robot4100Setting.DUMPERSLIDE_MAX,Robot4100Setting.DUMPERSLIDE_MIN);
        //this.m_DumperSlide = new RobotServoUsingMotor(new RobotMotorController(DumperMotor,Robot4100Setting.DUMPERSLIDE_TIMEOUTCONTROL,Robot4100Setting.DUMPERSLIDE_TIMEOUTFACTOR),0,Robot4100Setting.DUMPERSLIDE_MAX,Robot4100Setting.DUMPERSLIDE_MIN);

        this.m_CollectorSweep = new RobotMotorWithoutEncoder(ControllingOpMode.hardwareMap.dcMotor.get(Robot4100Setting.COLLECTOR_CONFIGURATIONNAME),Robot4100Setting.COLLECTOR_MOTORTYPE);
        this.m_CollectorSweep.setDirectionReversed(true);

        this.m_CollectorSetOut = ControllingOpMode.hardwareMap.servo.get(Robot4100Setting.COLLECTOROUTSERVO_CONFIGURATIONNAME);

        this.m_DumperServo = ControllingOpMode.hardwareMap.servo.get(Robot4100Setting.DUMPERSERVO_CONFIGURATIONNAME);

        if(initVuforiaNav) {
            RobotOnPhoneCamera phoneCamera = new RobotOnPhoneCamera(ControllingOpMode, Robot4100Setting.VUFORIANAV_ShowPreviewScreen, VuforiaLocalizer.CameraDirection.FRONT, PrivateSettings.VUFORIALICENSE);
            this.m_VuforiaNav = new FTC2018GameVuforiaNavigation(phoneCamera, Robot4100Setting.VUFORIANAV_PHONEPOSITION);
        }else{
            this.m_VuforiaNav = null;
        }

        if(initTFOD){
            RobotWebcamCamera webcamCamera = new RobotWebcamCamera(ControllingOpMode, false, Robot4100Setting.TFOL_CAMERACONFIGURTIONNAME,PrivateSettings.VUFORIALICENSE);
            this.m_MineralDetection = new FTC2018GameSpecificFunctions(ControllingOpMode,webcamCamera,Robot4100Setting.TFOL_ShowPreviewScreen);
        }

        this.getDebugger().addDebuggerCallable(new RobotDebugger.ObjectDebuggerWrapper<>("PositionTracker",new Object(){
            @Override
            public String toString(){
                if(Robot4100Core.this.m_MotionSystem.getPositionTracker() == null)
                    return "Null";

                Robot2DPositionIndicator Position = Robot4100Core.this.m_MotionSystem.getPositionTracker().getPosition();

                if (Position != null)
                    return "(X: " + Position.getX() + ", Z: " + Position.getZ() + ") - Rotation: " + Position.getRotationY() + " deg";
                else
                    return "Null";
            }
        }));
        this.getDebugger().addDebuggerCallable(new RobotDebugger.ObjectDebuggerWrapper<>("LinearActuator",new Object(){
            @Override
            public String toString(){
                return "" + Robot4100Core.this.m_LinearActuator.getCurrentPosition() + "(" + Robot4100Core.this.m_LinearActuator.getCurrentPercent() + "%)";
            }
        }));
        this.getDebugger().addDebuggerCallable(new RobotDebugger.ObjectDebuggerWrapper<>("DrawerSlide",new Object(){
            @Override
            public String toString(){
                return "" + Robot4100Core.this.m_DrawerSlide.getCurrentPosition() + "(" + Robot4100Core.this.m_DrawerSlide.getCurrentPercent() + "%)";
            }
        }));
        this.getDebugger().addDebuggerCallable(new RobotDebugger.ObjectDebuggerWrapper<>("DumperSlide",new Object(){
            @Override
            public String toString(){
                return "" + Robot4100Core.this.m_DumperSlide.getCurrentPosition() + "(" + Robot4100Core.this.m_DumperSlide.getCurrentPercent() + "%)";
            }
        }));

        if(readSavedValues){
            this.readSetting();
        }
    }

    public void readSetting(){
        Robot2DPositionIndicator RobotPosition = this.m_MotionSystem.getPositionTracker() == null ? null : this.m_MotionSystem.getPositionTracker().getPosition();
        Double LinearActuatorPos = this.m_LinearActuator.getCurrentPosition();
        Double DrawerSlidePos = this.m_DrawerSlide.getCurrentPosition();
        Double DumperSlidePos = this.m_DumperSlide.getCurrentPosition();
        RobotPosition = this.getDataStorage().getSetting("RobotPosition",RobotPosition);
        LinearActuatorPos = this.getDataStorage().getSetting("LinearActuatorPos",LinearActuatorPos);
        DrawerSlidePos = this.getDataStorage().getSetting("DrawerSlidePos",DrawerSlidePos);
        DumperSlidePos = this.getDataStorage().getSetting("DumperSlidePos",DumperSlidePos);
        if(RobotPosition != null){
            this.m_MotionSystem.getPositionTracker().setPosition(RobotPosition);
        }
        this.m_LinearActuator.adjustCurrentPosition(LinearActuatorPos);
        this.m_DrawerSlide.adjustCurrentPosition(DrawerSlidePos);
        this.m_DumperSlide.adjustCurrentPosition(DumperSlidePos);
    }
    public void save(){
        Robot2DPositionIndicator RobotPosition = this.m_MotionSystem.getPositionTracker() == null ? null : this.m_MotionSystem.getPositionTracker().getPosition();
        Double LinearActuatorPos = this.m_LinearActuator.getCurrentPosition();
        Double DrawerSlidePos = this.m_DrawerSlide.getCurrentPosition();
        Double DumperPos = this.m_DumperSlide.getCurrentPosition();
        this.getDataStorage().saveSetting("RobotPosition",RobotPosition);
        this.getDataStorage().saveSetting("LinearActuatorPos",LinearActuatorPos);
        this.getDataStorage().saveSetting("DrawerSlidePos",DrawerSlidePos);
        this.getDataStorage().saveSetting("DumperSlidePos",DumperPos);
        this.getDataStorage().saveSettingsToFile();
    }

    @Override
    public boolean isBusy() {
        return (
                this.m_LinearActuator.isBusy()
                || this.m_DrawerSlide.isBusy()
                || this.m_DumperSlide.isBusy()
                );
    }

    @Override
    public void waitUntilFinish() {
        while(this.isBusy()){
            this.updateStatus();
        }
    }

    @Override
    public void updateStatus(){
        super.updateStatus();
        this.m_MotionSystem.updateStatus();
        this.m_DumperSlide.updateStatus();
        this.m_LinearActuator.updateStatus();
        this.m_CollectorSweep.updateStatus();
        this.m_DrawerSlide.updateStatus();
        this.calibratePosition();
    }

    public void calibratePosition(){
        if(this.m_VuforiaNav != null && this.m_MotionSystem.getPositionTracker() != null){
            FTC2018GameVuforiaNavigation.Vuforia3DFieldAxisIndicator Robot3DVuforiaPos = this.m_VuforiaNav.getRobotPosition();
            Robot2DPositionTracker.Robot2DPositionFieldAxisIndicator Robot2DFieldPos = Robot3DVuforiaPos.getVuforia2DFieldAxisIndicator().toFieldAxis(this.m_MotionSystem.getPositionTracker());
            this.m_MotionSystem.getPositionTracker().setPosition(Robot2DFieldPos);
            this.m_MotionSystem.getPositionTracker().finishPositionFix();
        }
    }

    @Override
    public RobotMotionSystem getMotionSystem() {
        return this.m_MotionSystem;
    }

    public RobotServoUsingMotor getLinearActuator(){
        return this.m_LinearActuator;
    }

    public FTC2018GameSpecificFunctions get2018MineralDetection(){
        return this.m_MineralDetection;
    }

    public RobotServoUsingMotor getDrawerSlide(){
        return this.m_DrawerSlide;
    }

    public RobotServoUsingMotor getDumperSlide(){
        return this.m_DumperSlide;
    }

    public Servo getCollectorSetOutServo(){
        return this.m_CollectorSetOut;
    }

    public Servo getDumperServo(){
        return this.m_DumperServo;
    }

    public RobotMotorWithoutEncoder getCollectorSweeper(){
        return this.m_CollectorSweep;
    }
}
