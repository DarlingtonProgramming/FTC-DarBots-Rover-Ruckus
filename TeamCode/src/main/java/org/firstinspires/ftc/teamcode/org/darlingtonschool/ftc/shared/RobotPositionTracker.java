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

/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program.
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;
 * Additional Information:
 * I recommend you to use the X,Y values in CM.
 */

package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.RobotEventLoopable;
import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.internal.XYPlaneCalculations;
public class RobotPositionTracker {
    private double m_TotalX = 0;
    private double m_TotalY = 0;
    private double m_CurrentX = 0;
    private double m_CurrentY = 0;
    private double m_Rotation = 0; //-180 to 180
    private boolean m_RecommendPositionFix = false;
    private double[] m_robotAxisLeftFrontExtreme;
    private double[] m_robotAxisRightFrontExtreme;
    private double[] m_robotAxisLeftBackExtreme;
    private double[] m_robotAxisRightBackExtreme;

    protected static double calculateRotation(double Degree) { // -180 <= return val < 180
        double mDeg = Degree;
        while(mDeg < -180) {
            mDeg+=360;
        }
        while(mDeg >= 180) {
            mDeg-=360;
        }
        return mDeg;
    }
    public RobotPositionTracker(double fieldTotalX, double fieldTotalY, double initialX, double initialY, double initialRotation, double[] leftFrontExtremePoint, double[] rightFrontExtremePoint, double[] leftBackExtremePoint, double[] rightBackExtremePoint){
        this.m_TotalX = fieldTotalX;
        this.m_TotalY = fieldTotalY;
        this.m_CurrentX = initialX;
        this.m_CurrentY = initialY;
        this.m_Rotation = this.calculateRotation(initialRotation);
        this.m_robotAxisLeftBackExtreme = leftBackExtremePoint;
        this.m_robotAxisLeftFrontExtreme = leftFrontExtremePoint;
        this.m_robotAxisRightBackExtreme = rightBackExtremePoint;
        this.m_robotAxisRightFrontExtreme = rightFrontExtremePoint;
        this.m_RecommendPositionFix = false;
    }
    public double[] robotAxisFromFieldAxis(double[] FieldAxis){
        double fieldRelativeX = FieldAxis[0] - this.getCurrentPosX();
        double fieldRelativeY = FieldAxis[1] - this.getCurrentPosY();
        double[] fieldRelativePoint = {fieldRelativeX,fieldRelativeY};
        double[] origin = {0,0};
        double[] mResult = XYPlaneCalculations.rotatePointAroundFixedPoint(fieldRelativePoint, origin, -this.getRobotRotation());
        return mResult;
    }
    public double[] fieldAxisFromRobotAxis(double[] robotAxis){
        if(robotAxis[0] == 0 && robotAxis[1] == 0){
            return this.getCurrentPos();
        }
        double[] origin = {0,0};
        double[] relativeRobotRotationFixedPoint = XYPlaneCalculations.rotatePointAroundFixedPoint(robotAxis, origin, this.getRobotRotation());
        double[] result = {relativeRobotRotationFixedPoint[0] + this.getCurrentPosX(), relativeRobotRotationFixedPoint[1] + this.getCurrentPosY()};
        return result;
    }
    public double[][] getRobotExtremePoints_robotAxis(){

        double[][] RobotExtremes = new double[4][2];
        RobotExtremes[0] = this.m_robotAxisLeftFrontExtreme;
        RobotExtremes[1] = this.m_robotAxisRightFrontExtreme;
        RobotExtremes[2] = this.m_robotAxisLeftBackExtreme;
        RobotExtremes[3] = this.m_robotAxisRightBackExtreme;
        return RobotExtremes;
    }
    public double[][] getRobotExtremePoints_fieldAxis(){
        double[][] RobotExtremes = this.getRobotExtremePoints_robotAxis();
        RobotExtremes[0] = this.fieldAxisFromRobotAxis(RobotExtremes[0]);
        RobotExtremes[1] = this.fieldAxisFromRobotAxis(RobotExtremes[1]);
        RobotExtremes[2] = this.fieldAxisFromRobotAxis(RobotExtremes[2]);
        RobotExtremes[3] = this.fieldAxisFromRobotAxis(RobotExtremes[3]);

        return RobotExtremes;
    }
    protected void fixBouncingBox(){
        double[][] RobotExtremes = new double[4][2];
        for(int i=0; i<4; i++){
            RobotExtremes = this.getRobotExtremePoints_fieldAxis();
            double[] EachExtreme = RobotExtremes[i];
            double Offset = 0;
            if(EachExtreme[0] < 0 || EachExtreme[0] > this.getFieldTotalX() || EachExtreme[1] < 0 || EachExtreme[1] > this.getFieldTotalY()) {
                this.m_RecommendPositionFix = true;
            }
            if(EachExtreme[0] < 0) {
                Offset = -EachExtreme[0];
                this.setCurrentPosX(this.getCurrentPosX() + Offset);
            }else if(EachExtreme[0] > this.getFieldTotalX()){
                Offset = EachExtreme[0] - this.getFieldTotalX();
                this.setCurrentPosX(this.getCurrentPosX() - Offset);
            }else if(EachExtreme[1] < 0){
                Offset = -EachExtreme[1];
                this.setCurrentPosY(this.getCurrentPosY() + Offset);
            }else if(EachExtreme[1] > this.getFieldTotalY()){
                Offset = EachExtreme[1] - this.getFieldTotalY();
                this.setCurrentPosY(this.getCurrentPosY() - Offset);
            }
        }
    }
    public boolean needToFixPosition(){
        return this.m_RecommendPositionFix;
    }
    public void fixPositionFinished(){
        this.m_RecommendPositionFix = false;
    }
    public double getFieldTotalX(){
        return this.m_TotalX;
    }
    public void setFieldTotalX(double newTotalX){
        this.m_TotalX = newTotalX;
    }
    public double getFieldTotalY(){
        return this.m_TotalY;
    }
    public void setFieldTotalY(double newTotalY){
        this.m_TotalY = newTotalY;
    }
    public double[] getFieldSize(){
        double[] mResult = new double[2];
        mResult[0] = this.getFieldTotalX();
        mResult[1] = this.getFieldTotalY();
        return mResult;
    }
    public void setFieldSize(double[] newDimension){
        this.setFieldTotalX(newDimension[0]);
        this.setFieldTotalY(newDimension[1]);
    }
    public double getCurrentPosX(){
        return this.m_CurrentX;
    }
    public void setCurrentPosX(double newPosX){
        this.m_CurrentX = newPosX;
    }
    public double getCurrentPosY(){
        return this.m_CurrentY;
    }
    public void setCurrentPosY(double newPosY){
        this.m_CurrentY = newPosY;
    }
    public double[] getCurrentPos(){
        double[] mResult = new double[2];
        mResult[0] = this.getCurrentPosX();
        mResult[1] = this.getCurrentPosY();
        return mResult;
    }
    public void setCurrentPos(double[] newPos){
        this.setCurrentPosX(newPos[0]);
        this.setCurrentPosY(newPos[1]);
    }
    public double getRobotRotation() {
        return this.m_Rotation;
    }
    public void setRobotRotation(double newRotation){
        this.m_Rotation = this.calculateRotation(newRotation);
    }
    public double[] getRobotLeftFrontExtreme(){
        return this.m_robotAxisLeftFrontExtreme;
    }
    public void setRobotLeftFrontExtreme(double[] ExtremePoint){
        this.m_robotAxisLeftFrontExtreme = ExtremePoint;
    }
    public double[] getRobotRightFrontExtreme(){
        return this.m_robotAxisRightFrontExtreme;
    }
    public void setRobotRightFrontExtreme(double[] ExtremePoint){
        this.m_robotAxisRightFrontExtreme = ExtremePoint;
    }
    public double[] getRobotLeftBackExtreme(){
        return this.m_robotAxisLeftBackExtreme;
    }
    public void setRobotLeftBackExtreme(double[] ExtremePoint){
        this.m_robotAxisLeftBackExtreme = ExtremePoint;
    }
    public double[] getRobotRightBackExtreme(){
        return this.m_robotAxisRightBackExtreme;
    }
    public void setRobotRightBackExtreme(double[] ExtremePoint){
        this.m_robotAxisRightFrontExtreme = ExtremePoint;
    }
    public void moveThroughRobotAngle(double moveAngleInDegree, double Distance){
        double actualMovingAngle = this.calculateRotation(moveAngleInDegree);
        double movingToRelativePoint[] = new double[2];
        movingToRelativePoint[0] = Math.sin(Math.toRadians(actualMovingAngle)) * Distance;
        movingToRelativePoint[1] = Math.cos(Math.toRadians(actualMovingAngle)) * Distance;
        this.moveThroughRobotAxis(movingToRelativePoint);
    }

    public void moveThroughRobotAxis(double[] axisValues){
        double movingToRelativePoint[] = {axisValues[0], axisValues[1]};
        this.setCurrentPos(this.fieldAxisFromRobotAxis(movingToRelativePoint));
        this.fixBouncingBox();
    }

    public void moveThroughFieldAngle(double moveAngleInDegree, double Distance){
        double actualMovingAngle = this.calculateRotation(moveAngleInDegree);
        double movingToAbsolutePoint[] = new double[2];
        movingToAbsolutePoint[0] = Math.sin(Math.toRadians(actualMovingAngle)) * Distance;
        movingToAbsolutePoint[1] = Math.cos(Math.toRadians(actualMovingAngle)) * Distance;
        this.moveThroughFieldAxis(movingToAbsolutePoint);
    }
    public void moveThroughFieldAxis(double[] axisValues){
        double movingToAbsolutePoint[] = this.getCurrentPos();
        movingToAbsolutePoint[0] += axisValues[0];
        movingToAbsolutePoint[1] += axisValues[1];
        this.setCurrentPos(movingToAbsolutePoint);
        this.fixBouncingBox();
    }
    public void rotateAroundRobotRelativePoint(double[] point, double angleInDegree){
        this.rotateAroundFieldAbsolutePoint(this.fieldAxisFromRobotAxis(point),angleInDegree);
    }
    public void rotateAroundFieldAbsolutePoint(double[] point, double angleInDegree){
        if(point[0] == this.getCurrentPosX() && point[1] == this.getCurrentPosY()){
            this.setRobotRotation(this.getRobotRotation() + angleInDegree);
            return;
        }
        double[] newRobotPosition = XYPlaneCalculations.rotatePointAroundFixedPoint(point, this.getCurrentPos(), angleInDegree);
        this.setCurrentPos(newRobotPosition);
        this.setRobotRotation(this.getRobotRotation() + angleInDegree);
        this.fixBouncingBox();
    }
    public void moveWithRobotFixedPoint(double[] fixedPoint, double powerRadius, double Distance){
        double moveAngleRad = Distance / powerRadius;
        double moveAngleDeg = Math.toDegrees(moveAngleRad);
        this.rotateAroundRobotRelativePoint(fixedPoint,moveAngleDeg);
    }
    public void moveWithFieldFixedPoint(double[] fixedPoint, double powerRadius, double Distance){
        this.moveWithRobotFixedPoint(this.robotAxisFromFieldAxis(fixedPoint),powerRadius,Distance);
    }
    public void moveWithRobotFixedPointAndPowerPoint(double[] fixedPoint, double[] powerPoint, double Distance){
        double powerDistance = Math.sqrt(Math.pow((powerPoint[1]-fixedPoint[1]),2) + Math.pow((powerPoint[0]-fixedPoint[0]),2));
        this.moveWithRobotFixedPoint(fixedPoint,powerDistance,Distance);
    }
    public void moveWithFieldFixedPointAndPowerPoint(double[] fixedPoint, double[] powerPoint, double Distance){
        double powerDistance = Math.sqrt(Math.pow((powerPoint[1]-fixedPoint[1]),2) + Math.pow((powerPoint[0]-fixedPoint[0]),2));
        this.moveWithFieldFixedPoint(fixedPoint,powerDistance,Distance);
    }
    public double calculateAngleDeltaMovingToRobotPoint(double[] robotFixPoint){
        if(robotFixPoint[1] == 0){
            if(robotFixPoint[0] == 0){
                return 0;
            }else if(robotFixPoint[0] > 0){
                return 90;
            }else{
                return -90;
            }
        }
        double angleRadian = Math.atan(robotFixPoint[0] / robotFixPoint[1]);
        return Math.toDegrees(angleRadian);
    }
    public double calculateAngleDeltaMovingToFieldPoint(double[] fieldFixPoint){
        return this.calculateAngleDeltaMovingToRobotPoint(this.robotAxisFromFieldAxis(fieldFixPoint));
    }
    public double calculateDistanceToRobotPoint(double[] robotFixPoint){
        return Math.sqrt(Math.pow(robotFixPoint[0],2)+Math.pow(robotFixPoint[1],2));
    }
    public double calculateDistanceToFixedPoint(double[] fieldFixPoint){
        return this.calculateDistanceToRobotPoint(this.robotAxisFromFieldAxis(fieldFixPoint));
    }
    public double calculateDistanceToRotateAroundRobotPoint(double[] robotFixPoint, double[] powerPoint, double DeltaAngle){
        DeltaAngle = this.calculateRotation(DeltaAngle);
        double circleRadius = Math.sqrt(Math.pow((robotFixPoint[0] - powerPoint[0]),2) + Math.pow((robotFixPoint[1] - powerPoint[1]),2));
        double circlePerimeter = 2 * Math.PI * circleRadius;
        return DeltaAngle / 360 * circlePerimeter;
    }
    public double calculateDistanceToRotateAroundFieldPoint(double[] fieldFixPoint, double[] powerPoint, double DeltaAngle){
        return this.calculateDistanceToRotateAroundRobotPoint(this.robotAxisFromFieldAxis(fieldFixPoint),this.robotAxisFromFieldAxis(powerPoint),DeltaAngle);
    }
}

