package org.firstinspires.ftc.teamcode.RobotControllers.Robot4100StateTournamentCode;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations.Robot2DPositionTracker;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.ChassisControllers.OmniWheel4SideDiamondShaped;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.RobotDebugger;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotion;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorController;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorWithEncoder;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotMotorWithoutEncoder;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Sensors.RobotServoUsingMotor;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotCore;
import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Templates.RobotMotionSystem;

import java.util.ResourceBundle;

public class Robot4100Core extends RobotCore {
    private OmniWheel4SideDiamondShaped m_MotionSystem;
    private RobotServoUsingMotor m_LinearActuator;
    private RobotServoUsingMotor m_DrawerSlide;
    private RobotServoUsingMotor m_Dumper;
    private RobotMotorWithoutEncoder m_CollectorSweep;
    private Servo m_CollectorSetOut;
    private Servo m_DeclarationServo;

    public Robot4100Core(@NonNull OpMode ControllingOpMode, Robot2DPositionIndicator currentPosition, boolean readSavedValues){
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
        this.m_DrawerSlide = new RobotServoUsingMotor(new RobotMotorController(DrawerSlideMotor,Robot4100Setting.DRAWERSLIDE_TIMEOUTCONTROL,Robot4100Setting.DRAWERSLIDE_TIMEOUTFACTOR),0,Robot4100Setting.DRAWERSLIDEAPPROACH_MAX,Robot4100Setting.DRAWERSLIDEAPPROACH_MIN);

        RobotMotorWithEncoder DumperMotor = new RobotMotorWithEncoder(ControllingOpMode.hardwareMap.dcMotor.get(Robot4100Setting.DUMP_CONFIGURATIONNAME),Robot4100Setting.DUMP_MOTORTYPE);
        this.m_Dumper = new RobotServoUsingMotor(new RobotMotorController(DumperMotor,Robot4100Setting.DUMP_TIMEOUTCONTROL,Robot4100Setting.DUMP_TIMEOUTFACTOR),0,Robot4100Setting.DUMP_MAX,Robot4100Setting.DUMP_MIN);

        this.m_CollectorSweep = new RobotMotorWithoutEncoder(ControllingOpMode.hardwareMap.dcMotor.get(Robot4100Setting.COLLECTOR_CONFIGURATIONNAME),Robot4100Setting.COLLECTOR_MOTORTYPE);
        //this.m_CollectorSweep.setDirectionReversed(true);

        this.m_CollectorSetOut = ControllingOpMode.hardwareMap.servo.get(Robot4100Setting.COLLECTOROUTSERVO_CONFIGURATIONNAME);

        this.m_DeclarationServo = ControllingOpMode.hardwareMap.servo.get(Robot4100Setting.TEAMMARKER_CONFIGURATIONNAME);

        this.getDebugger().addDebuggerCallable(new RobotDebugger.ObjectDebuggerWrapper<>("PositionTracker",new Object(){
            @Override
            public String toString(){
                Robot2DPositionIndicator Position = Robot4100Core.this.m_MotionSystem.getPositionTracker().getPosition();
                return "(X: " + Position.getX() + ", Z: " + Position.getZ() + ") - Rotation: " + Position.getRotationY() + " deg";
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
        this.getDebugger().addDebuggerCallable(new RobotDebugger.ObjectDebuggerWrapper<>("Dumper",new Object(){
            @Override
            public String toString(){
                return "" + Robot4100Core.this.m_Dumper.getCurrentPosition() + "(" + Robot4100Core.this.m_Dumper.getCurrentPercent() + "%)";
            }
        }));
    }

    public void readSetting(){
        Robot2DPositionIndicator RobotPosition = this.m_MotionSystem.getPositionTracker().getPosition();
        Double LinearActuatorPos = this.m_LinearActuator.getCurrentPosition();
        Double DrawerSlidePos = this.m_DrawerSlide.getCurrentPosition();
        Double DumperPos = this.m_Dumper.getCurrentPosition();
        RobotPosition = this.getDataStorage().getSetting("RobotPosition",RobotPosition);
        LinearActuatorPos = this.getDataStorage().getSetting("LinearActuatorPos",LinearActuatorPos);
        DrawerSlidePos = this.getDataStorage().getSetting("DrawerSlidePos",DrawerSlidePos);
        DumperPos = this.getDataStorage().getSetting("DumperPos",DumperPos);
    }
    public void save(){
        Robot2DPositionIndicator RobotPosition = this.m_MotionSystem.getPositionTracker().getPosition();
        Double LinearActuatorPos = this.m_LinearActuator.getCurrentPosition();
        Double DrawerSlidePos = this.m_DrawerSlide.getCurrentPosition();
        Double DumperPos = this.m_Dumper.getCurrentPosition();
        this.getDataStorage().saveSetting("RobotPosition",RobotPosition);
        this.getDataStorage().saveSetting("LinearActuatorPos",LinearActuatorPos);
        this.getDataStorage().saveSetting("DrawerSlidePos",DrawerSlidePos);
        this.getDataStorage().saveSetting("DumperPos",DumperPos);
        this.getDataStorage().saveSettingsToFile();
    }

    @Override
    public boolean isBusy() {
        return (
                this.m_LinearActuator.isBusy()
                || this.m_DrawerSlide.isBusy()
                || this.m_Dumper.isBusy()
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
        this.m_Dumper.updateStatus();
        this.m_LinearActuator.updateStatus();
        this.m_CollectorSweep.updateStatus();
        this.m_DrawerSlide.updateStatus();
    }

    @Override
    public RobotMotionSystem getMotionSystem() {
        return this.m_MotionSystem;
    }

    public RobotServoUsingMotor getLinearActuator(){
        return this.m_LinearActuator;
    }

    public RobotServoUsingMotor getDrawerSlide(){
        return this.m_DrawerSlide;
    }

    public RobotServoUsingMotor getDumper(){
        return this.m_Dumper;
    }

    public Servo getCollectorSetOutServo(){
        return this.m_CollectorSetOut;
    }

    public Servo getTeamMarkerServo(){
        return this.m_DeclarationServo;
    }

    public RobotMotorWithoutEncoder getCollectorSweeper(){
        return this.m_CollectorSweep;
    }
}
