/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionCorrectionSystem;
 */

package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotPositionDistanceSortedSensor;

public class RobotPositionCorrectionSystem {
    private static final double CorrectionRangeAcceptanceUnit = 10.0; //In CM
    private RobotSensorWrapper<DistanceSensor> m_LeftDistanceSensor, m_RightDistanceSensor, m_FrontDistanceSensor, m_BackDistanceSensor;
    private double m_HowManyCMPerAxisUnit = 1.0;
    private double m_CurrentRotation = 0;

    protected static double calculateRotation(double Angle){ // -180 <= Angle < 180
        while(Angle >= 180){
            Angle -= 360;
        }
        while(Angle < -180){
            Angle += 360;
        }
        return Angle;
    }

    public RobotSensorWrapper<DistanceSensor> getBackDistanceSensor() {
        return this.m_BackDistanceSensor;
    }

    public void setBackDistanceSensor(RobotSensorWrapper<DistanceSensor> BackDistanceSensor) {
        this.m_BackDistanceSensor = BackDistanceSensor;
    }

    public RobotSensorWrapper<DistanceSensor> getFrontDistanceSensor() {
        return this.m_FrontDistanceSensor;
    }

    public void setFrontDistanceSensor(RobotSensorWrapper<DistanceSensor> FrontDistanceSensor) {
        this.m_FrontDistanceSensor = FrontDistanceSensor;
    }

    public RobotSensorWrapper<DistanceSensor> getLeftDistanceSensor() {
        return this.m_LeftDistanceSensor;
    }

    public void setLeftDistanceSensor(RobotSensorWrapper<DistanceSensor> LeftDistanceSensor) {
        this.m_LeftDistanceSensor = LeftDistanceSensor;
    }

    public RobotSensorWrapper<DistanceSensor> getRightDistanceSensor() {
        return this.m_RightDistanceSensor;
    }

    public void setRightDistanceSensor(RobotSensorWrapper<DistanceSensor> RightDistanceSensor) {
        this.m_RightDistanceSensor = RightDistanceSensor;
    }

    public double getCMPerAxisUnit() {
        return this.m_HowManyCMPerAxisUnit;
    }

    public void setCMPerAxisUnit(double CMPerAxisUnit) {
        this.m_HowManyCMPerAxisUnit = CMPerAxisUnit;
    }

    public double getCurrentRotation(){
        return this.m_CurrentRotation;
    }

    public void setCurrentRotation(double CurrentRotation) {
        this.m_CurrentRotation = this.calculateRotation(CurrentRotation);
    }

    public RobotPositionCorrectionSystem(RobotSensorWrapper<DistanceSensor> leftDistanceSensor, RobotSensorWrapper<DistanceSensor> rightDistanceSensor, RobotSensorWrapper<DistanceSensor> frontDistanceSensor, RobotSensorWrapper<DistanceSensor> backDistanceSensor, double CMPerAxisUnit, double angleRotation){
        this.m_LeftDistanceSensor = leftDistanceSensor;
        this.m_RightDistanceSensor = rightDistanceSensor;
        this.m_FrontDistanceSensor = frontDistanceSensor;
        this.m_BackDistanceSensor = backDistanceSensor;
        this.m_HowManyCMPerAxisUnit = CMPerAxisUnit;
        this.m_CurrentRotation = this.calculateRotation(angleRotation);
    }
    public double[] detectPosition(double[] fieldSize) throws RuntimeException{
        double rLeftDistance = this.m_LeftDistanceSensor.getSensor().getDistance(DistanceUnit.CM);
        double rRightDistance = this.m_RightDistanceSensor.getSensor().getDistance(DistanceUnit.CM);
        double rFrontDistance = this.m_FrontDistanceSensor.getSensor().getDistance(DistanceUnit.CM);
        double rBackDistance = this.m_BackDistanceSensor.getSensor().getDistance(DistanceUnit.CM);

        double AFactorToH = 0;
        double leftPos = 0, rightPos = 0, topPos = 0, botPos = 0;

        if(this.getCurrentRotation() > -45 && this.getCurrentRotation() <= 45){
            AFactorToH = Math.cos(Math.toRadians(this.getCurrentRotation()));
            leftPos = rLeftDistance;
            rightPos = rRightDistance;
            topPos = rFrontDistance;
            botPos = rBackDistance;
        }else if(this.getCurrentRotation() > -135 && this.getCurrentRotation() <= -45) {
            AFactorToH = Math.cos(Math.toRadians(90 + this.getCurrentRotation()));
            leftPos = rFrontDistance;
            rightPos = rBackDistance;
            topPos = rRightDistance;
            botPos = rLeftDistance;
        }else if(this.getCurrentRotation() > 45 && this.getCurrentRotation() <= 135){
            AFactorToH = Math.cos(Math.toRadians(90 - this.getCurrentRotation()));
            leftPos = rBackDistance;
            rightPos = rFrontDistance;
            topPos = rLeftDistance;
            botPos = rRightDistance;
        }else if(this.getCurrentRotation() >= -180 && this.getCurrentRotation() <= -135){
            AFactorToH = Math.cos(Math.toRadians(180 + this.getCurrentRotation()));
            topPos = rBackDistance;
            botPos = rFrontDistance;
            leftPos = rRightDistance;
            rightPos = rLeftDistance;
        }else if(this.getCurrentRotation() > 135 && this.getCurrentRotation() < 180){
            AFactorToH = Math.cos(Math.toRadians(180-this.getCurrentRotation()));
            topPos = rBackDistance;
            botPos = rFrontDistance;
            leftPos = rRightDistance;
            rightPos = rLeftDistance;
        }
        boolean leftAvailable = true, rightAvailable = true, topAvailable = true, botAvailable = true;
        if(topPos == DistanceSensor.distanceOutOfRange)
            topAvailable = false;
        if(botPos == DistanceSensor.distanceOutOfRange)
            botAvailable = false;
        if(leftPos == DistanceSensor.distanceOutOfRange)
            leftAvailable = false;
        if(rightPos == DistanceSensor.distanceOutOfRange)
            rightAvailable = false;
        leftPos *= AFactorToH ;
        rightPos *= AFactorToH;
        topPos *= AFactorToH;
        botPos *= AFactorToH;

        RobotPositionDistanceSortedSensor leftSensor = null, rightSensor = null, topSensor = null, botSensor = null;
        if(this.getCurrentRotation() > -45 && this.getCurrentRotation() <= 45){
            leftSensor = new RobotPositionDistanceSortedSensor(leftPos,this.m_LeftDistanceSensor.getPos());
            rightSensor = new RobotPositionDistanceSortedSensor(rightPos,this.m_RightDistanceSensor.getPos());
            topSensor = new RobotPositionDistanceSortedSensor(topPos,this.m_FrontDistanceSensor.getPos());
            botSensor = new RobotPositionDistanceSortedSensor(botPos,this.m_BackDistanceSensor.getPos());
        }else if(this.getCurrentRotation() > -135 && this.getCurrentRotation() <= -45) {
            leftSensor = new RobotPositionDistanceSortedSensor(leftPos,this.m_FrontDistanceSensor.getPos());
            rightSensor = new RobotPositionDistanceSortedSensor(rightPos,this.m_BackDistanceSensor.getPos());
            topSensor = new RobotPositionDistanceSortedSensor(topPos,this.m_RightDistanceSensor.getPos());
            botSensor = new RobotPositionDistanceSortedSensor(botPos,this.m_LeftDistanceSensor.getPos());
        }else if(this.getCurrentRotation() > 45 && this.getCurrentRotation() <= 135){
            leftSensor = new RobotPositionDistanceSortedSensor(leftPos,this.m_BackDistanceSensor.getPos());
            rightSensor = new RobotPositionDistanceSortedSensor(rightPos,this.m_FrontDistanceSensor.getPos());
            topSensor = new RobotPositionDistanceSortedSensor(topPos,this.m_LeftDistanceSensor.getPos());
            botSensor = new RobotPositionDistanceSortedSensor(botPos,this.m_RightDistanceSensor.getPos());
        }else if(this.getCurrentRotation() >= -180 && this.getCurrentRotation() <= -135){
            leftSensor = new RobotPositionDistanceSortedSensor(leftPos,this.m_RightDistanceSensor.getPos());
            rightSensor = new RobotPositionDistanceSortedSensor(rightPos,this.m_LeftDistanceSensor.getPos());
            topSensor = new RobotPositionDistanceSortedSensor(topPos,this.m_BackDistanceSensor.getPos());
            botSensor = new RobotPositionDistanceSortedSensor(botPos,this.m_FrontDistanceSensor.getPos());
        }else if(this.getCurrentRotation() > 135 && this.getCurrentRotation() < 180){
            leftSensor = new RobotPositionDistanceSortedSensor(leftPos,this.m_RightDistanceSensor.getPos());
            rightSensor = new RobotPositionDistanceSortedSensor(rightPos,this.m_LeftDistanceSensor.getPos());
            topSensor = new RobotPositionDistanceSortedSensor(topPos,this.m_BackDistanceSensor.getPos());
            botSensor = new RobotPositionDistanceSortedSensor(botPos,this.m_FrontDistanceSensor.getPos());
        }

        if((!topAvailable) && (!botAvailable)){
            throw new RuntimeException("Y Axis Sensor Not Available");
        }else if((!leftAvailable) && (!rightAvailable)){
            throw new RuntimeException("X Axis Sensor Not Available");
        }
        double robotX = 0, robotY = 0;
        double[] sensorOffset;
        if(topAvailable){
            sensorOffset = topSensor.getFieldOffset(this.getCurrentRotation());
            robotY = fieldSize[1] - topSensor.getDistanceMeasured() - sensorOffset[1];
        }else if(botAvailable){
            sensorOffset = botSensor.getFieldOffset(this.getCurrentRotation());
            robotY = botSensor.getDistanceMeasured() - sensorOffset[1];
        }
        if(leftAvailable){
            sensorOffset = leftSensor.getFieldOffset(this.getCurrentRotation());
            robotX = leftSensor.getDistanceMeasured() - sensorOffset[0];
        }else if(rightAvailable){
            sensorOffset = rightSensor.getFieldOffset(this.getCurrentRotation());
            robotX = fieldSize[0] - rightSensor.getDistanceMeasured() - sensorOffset[0];
        }
        double[] mResult = {robotX, robotY};
        return mResult;
    }
}
