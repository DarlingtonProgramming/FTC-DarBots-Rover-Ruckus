/*
 * Written by David Cao for the year of 2018 - 2019
 * To use this class, add the following code to your program.
 * import org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared.RobotPositionTracker;
 * Additional Information:
 * I recommend you to use the X,Y values in CM.
 */

package org.firstinspires.ftc.teamcode.org.darlingtonschool.ftc.shared;

public class RobotPositionTracker {
    private double totalX = 0;
    private double totalY = 0;
    private double currentX = 0;
    private double currentY = 0;
    private double rotation = 0; //-180 to 180
    private boolean recommendPositionFix = false;
    private double[] m_robotAxisLeftFrontExtreme;
    private double[] m_robotAxisRightFrontExtreme;
    private double[] m_robotAxisLeftBackExtreme;
    private double[] m_robotAxisRightBackExtreme;

    public enum RotationType{
        Clockwise, CounterClockwise
    }

    protected static double calculateRotation(double Degree) { // -180 <= return val < 180
        double newDeg = Degree;
        while (newDeg >= 180) {
            newDeg -= 360;
        }
        while (newDeg < -180) {
            newDeg += 360;
        }
        return newDeg;
    }
    public RobotPositionTracker(double fieldTotalX, double fieldTotalY, double initialX, double initialY, double initialRotation, double[] leftFrontExtremePoint, double[] rightFrontExtremePoint, double[] leftBackExtremePoint, double[] rightBackExtremePoint){
        this.totalX = fieldTotalX;
        this.totalY = fieldTotalY;
        this.currentX = initialX;
        this.currentY = initialY;
        this.rotation = this.calculateRotation(initialRotation);
        this.m_robotAxisLeftBackExtreme = leftBackExtremePoint;
        this.m_robotAxisLeftFrontExtreme = leftFrontExtremePoint;
        this.m_robotAxisRightBackExtreme = rightBackExtremePoint;
        this.m_robotAxisRightFrontExtreme = rightFrontExtremePoint;
        this.recommendPositionFix = false;
    }
    public double[] robotAxisFromFieldAxis(double[] FieldAxis){
        double fieldRelativeX = FieldAxis[0] - this.getCurrentPosX();
        double fieldRelativeY = FieldAxis[1] - this.getCurrentPosY();
        if(fieldRelativeX == 0 && fieldRelativeY == 0){
            double[] mTempResult = {0,0};
            return mTempResult;
        }
        double hField = Math.sqrt(Math.pow(fieldRelativeX,2) + Math.pow(fieldRelativeY,2));
        double theta = 0;
        theta = Math.asin(fieldRelativeX / hField);
        double hRobot = hField;
        double angleInRobotAxis = theta - Math.toRadians(this.getRobotRotation());
        double xRobot = Math.sin(angleInRobotAxis) * hRobot;
        double yRobot = Math.cos(angleInRobotAxis) * hRobot;
        double[] mResult = {xRobot,yRobot};
        return mResult;
    }
    public double[] fieldAxisFromRobotAxis(double[] robotAxis){
        if(robotAxis[0] == 0 && robotAxis[1] == 0){
            return this.getCurrentPos();
        }
        double hRobot = Math.sqrt(Math.pow(robotAxis[0],2) + Math.pow(robotAxis[1],2));
        double angleInRobotAxis = Math.asin(robotAxis[0] / hRobot);
        double theta = angleInRobotAxis + Math.toRadians(this.getRobotRotation());
        double hField= hRobot;
        double xRelativeField = Math.sin(theta) * hField;
        double yRelativeField = Math.cos(theta) * hField;
        double[] mResult = {this.getCurrentPosX() + xRelativeField, this.getCurrentPosY() + yRelativeField};
        return mResult;
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
        double[][] RobotExtremes = this.getRobotExtremePoints_fieldAxis();
        double Offset = 0;
        for(double[] EachExtreme : RobotExtremes){
            Offset = 0;
            if(EachExtreme[0] < 0){
                this.recommendPositionFix = true;
                Offset = 0 - EachExtreme[0];
                for(double[] EachFixExtreme : RobotExtremes) {
                    EachFixExtreme[0] += Offset;
                }
                this.setCurrentPosX(this.getCurrentPosX() + Offset);
            }else if(EachExtreme[0] > this.getFieldTotalX()){
                this.recommendPositionFix = true;
                Offset = EachExtreme[0] - this.getFieldTotalX();
                for(double[] EachFixExtreme : RobotExtremes){
                    EachFixExtreme[0] -= Offset;
                }
                this.setCurrentPosX(this.getCurrentPosX() - Offset);
            }
            if(EachExtreme[1] < 0){
                this.recommendPositionFix = true;
                Offset = 0 - EachExtreme[1];
                for(double[] EachFixExtreme : RobotExtremes){
                    EachFixExtreme[1] += Offset;
                }
                this.setCurrentPosY(this.getCurrentPosY() + Offset);
            }else if(EachExtreme[1] > this.getFieldTotalY()){
                this.recommendPositionFix = true;
                Offset = EachExtreme[1] - this.getFieldTotalY();
                for(double[] EachFixExtreme : RobotExtremes){
                    EachFixExtreme[1] -= Offset;
                }
                this.setCurrentPosY(this.getCurrentPosY() - Offset);
            }
        }
    }
    public boolean needToFixPosition(){
        return this.recommendPositionFix;
    }
    public void fixPositionFinished(){
        this.recommendPositionFix = false;
    }
    public double getFieldTotalX(){
        return this.totalX;
    }
    public void setFieldTotalX(double newTotalX){
        this.totalX = newTotalX;
    }
    public double getFieldTotalY(){
        return this.totalY;
    }
    public void setFieldTotalY(double newTotalY){
        this.totalY = newTotalY;
    }
    public double[] getFieldDimension(){
        double[] mResult = new double[2];
        mResult[0] = this.getFieldTotalX();
        mResult[1] = this.getFieldTotalY();
        return mResult;
    }
    public void setFieldDimension(double[] newDimension){
        this.setFieldTotalX(newDimension[0]);
        this.setFieldTotalY(newDimension[1]);
    }
    public double getCurrentPosX(){
        return this.currentX;
    }
    public void setCurrentPosX(double newPosX){
        this.currentX = newPosX;
    }
    public double getCurrentPosY(){
        return this.currentY;
    }
    public void setCurrentPosY(double newPosY){
        this.currentY = newPosY;
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
        return this.rotation;
    }
    public void setRobotRotation(double newRotation){
        this.rotation = this.calculateRotation(newRotation);
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
        RobotDebugger.addDebug("PositionTracker","moveThroughRobotAngleStart("+ moveAngleInDegree + "," + Distance + ") {" + this.getCurrentPosX() + ", " + this.getCurrentPosY() + "}");
        double actualMovingAngle = this.calculateRotation(moveAngleInDegree);
        double movingToRelativePoint[] = new double[2];
        movingToRelativePoint[0] = Math.sin(Math.toRadians(actualMovingAngle)) * Distance;
        movingToRelativePoint[1] = Math.cos(Math.toRadians(actualMovingAngle)) * Distance;
        double movingToAbsolutePoint[] = this.fieldAxisFromRobotAxis(movingToRelativePoint);
        this.setCurrentPos(movingToAbsolutePoint);
        RobotDebugger.addDebug("PositionTracker","moveThroughRobotAngleEnd {" + this.getCurrentPosX() + ", " + this.getCurrentPosY() + "}");
        this.fixBouncingBox();

    }
    public void moveThroughRobotAxis(double[] axisValues){
        double movingToRelativePoint[] = {axisValues[0], axisValues[1]};
        this.setCurrentPos(this.fieldAxisFromRobotAxis(movingToRelativePoint));
        this.fixBouncingBox();
    }
    public void moveThroughFieldAngle(double moveAngleInDegree, double Distance){
        RobotDebugger.addDebug("PositionTracker","moveThroughFieldAngleStart("+ moveAngleInDegree + "," + Distance + ") {" + this.getCurrentPosX() + ", " + this.getCurrentPosY() + "}");
        double actualMovingAngle = this.calculateRotation(moveAngleInDegree);
        double movingToAbsolutePoint[] = this.getCurrentPos();
        movingToAbsolutePoint[0] += Math.sin(Math.toRadians(actualMovingAngle)) * Distance;
        movingToAbsolutePoint[1] += Math.cos(Math.toRadians(actualMovingAngle)) * Distance;
        this.setCurrentPos(movingToAbsolutePoint);
        RobotDebugger.addDebug("PositionTracker","moveThroughFieldAngleEnd {" + this.getCurrentPosX() + ", " + this.getCurrentPosY() + "}");
        this.fixBouncingBox();
    }
    public void moveThroughFieldAxis(double[] axisValues){
        RobotDebugger.addDebug("PositionTracker","moveThroughFieldAxisStart("+ axisValues[0] + "," + axisValues[1] + ") {" + this.getCurrentPosX() + ", " + this.getCurrentPosY() + "}");
        double movingToAbsolutePoint[] = this.getCurrentPos();
        movingToAbsolutePoint[0] += axisValues[0];
        movingToAbsolutePoint[1] += axisValues[1];
        this.setCurrentPos(movingToAbsolutePoint);
        RobotDebugger.addDebug("PositionTracker","moveThroughFieldAxisEnd {" + this.getCurrentPosX() + ", " + this.getCurrentPosY() + "}");
        this.fixBouncingBox();
    }
    public void rotateAroundRobotRelativePoint(double[] point, double angleInDegree){
        if(point[0] == 0 && point[1] == 0){
            this.setRobotRotation(this.getRobotRotation() + angleInDegree);
            this.fixBouncingBox();
            return;
        }
        RobotDebugger.addDebug("PositionTracker","rotateAroundRobotRelativePointStart({"+ point[0] + "," + point[1] + "}, " + angleInDegree + ") {" + this.getCurrentPosX() + ", " + this.getCurrentPosY() + "}");
        double[] originRelativePoint = {-point[0],-point[1]};
        double h = Math.sqrt(Math.pow(point[0],2) + Math.pow(point[1],2));
        double alpha = Math.asin(-point[0]/h);
        double angle = alpha + Math.toRadians(angleInDegree);
        double[] newRelativePoint = new double[2];
        newRelativePoint[0] = Math.sin(angle) * h;
        newRelativePoint[1] = Math.cos(angle) * h;
        double[] newRobotAxisPoint = new double[2];
        newRobotAxisPoint[0] = newRelativePoint[0] + point[0];
        newRobotAxisPoint[1] = newRelativePoint[1] + point[1];
        this.setCurrentPos(this.fieldAxisFromRobotAxis(newRobotAxisPoint));
        this.setRobotRotation(this.getRobotRotation() + angleInDegree);
        RobotDebugger.addDebug("PositionTracker","rotateAroundRobotRelativePointEnd {" + this.getCurrentPosX() + ", " + this.getCurrentPosY() + "}(" + this.getRobotRotation() + ")");
        this.fixBouncingBox();
    }
    public void rotateAroundFieldAbsolutePoint(double[] point, double angleInDegree){
        this.rotateAroundRobotRelativePoint(this.robotAxisFromFieldAxis(point),angleInDegree);
    }
    public void moveWithRobotFixedPoint(RotationType positiveDistanceRotationType, double[] fixedPoint, double powerRadius, double Distance){
        double moveAngleRad = Distance / powerRadius;
        double moveAngleDeg = Math.toDegrees(moveAngleRad);
        if(positiveDistanceRotationType == RotationType.CounterClockwise){
            moveAngleDeg = -moveAngleDeg;
        }
        this.rotateAroundRobotRelativePoint(fixedPoint,moveAngleDeg);
    }
    public void moveWithFieldFixedPoint(RotationType positiveDistanceRotationType, double[] fixedPoint, double powerRadius, double Distance){
        this.moveWithRobotFixedPoint(positiveDistanceRotationType,this.robotAxisFromFieldAxis(fixedPoint),powerRadius,Distance);
    }
    public void moveWithRobotFixedPointAndPowerPoint(RotationType positiveDistanceRotationType, double[] fixedPoint, double[] powerPoint, double Distance){
        double powerDistance = Math.sqrt(Math.pow((powerPoint[1]-fixedPoint[1]),2) + Math.pow((powerPoint[0]-fixedPoint[0]),2));
        this.moveWithRobotFixedPoint(positiveDistanceRotationType,fixedPoint,powerDistance,Distance);
    }
    public void moveWithFieldFixedPointAndPowerPoint(RotationType positiveDistanceRotationType, double[] fixedPoint, double[] powerPoint, double Distance){
        double powerDistance = Math.sqrt(Math.pow((powerPoint[1]-fixedPoint[1]),2) + Math.pow((powerPoint[0]-fixedPoint[0]),2));
        this.moveWithFieldFixedPoint(positiveDistanceRotationType,fixedPoint,powerDistance,Distance);
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
