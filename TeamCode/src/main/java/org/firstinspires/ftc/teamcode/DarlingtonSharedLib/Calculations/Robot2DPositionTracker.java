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
 * Additional Information:
 * I recommend you to use the X,Y values in CM.
 */

package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DarlingtonSharedLib.IntegratedFunctions.Robot2DPositionIndicator;

public class Robot2DPositionTracker {
    public class Robot2DPositionFieldAxisIndicator extends Robot2DPositionIndicator {
        public Robot2DPositionFieldAxisIndicator(double X, double Z, double YRotation) {
            super(X, Z, YRotation);
        }

        public Robot2DPositionFieldAxisIndicator(@NonNull Robot2DPositionFieldAxisIndicator indicator) {
            super(indicator);
        }

        public Robot2DPositionFieldAxisIndicator(@NonNull Robot2DPositionRobotAxisIndicator robotAxisIndicator) {
            super(0, 0, 0);
            double[] robotAxisPoint = {robotAxisIndicator.getX(), robotAxisIndicator.getZ()};
            double[] pointOfRotation = {0, 0};
            double rotationAng = Robot2DPositionTracker.this.getPosition().getRotationY();
            double[] rotatedAxis = XYPlaneCalculations.rotatePointAroundFixedPoint(robotAxisPoint, pointOfRotation, rotationAng);
            double rotatedAng = robotAxisIndicator.getRotationY() + Robot2DPositionTracker.this.getPosition().getRotationY();
            this.m_X = rotatedAxis[0];
            this.m_Z = rotatedAxis[1];
            this.m_RotationY = rotatedAng;
        }

        public Robot2DPositionRobotAxisIndicator toRobotAxis() {
            double fieldRelativeX = this.getX() - Robot2DPositionTracker.this.getPosition().getX(), fieldRelativeZ = this.getZ() - Robot2DPositionTracker.this.getPosition().getZ();
            double[] fieldRelativePoint = {fieldRelativeX, fieldRelativeZ};
            double[] pointOfRotation = {0, 0};
            double rotationAng = -Robot2DPositionTracker.this.getPosition().getRotationY(); //Here the axis plane is rotating into the robot axis plane (going through robot's Y rotation), but the point remain fixed.
            double[] rotatedAxis = XYPlaneCalculations.rotatePointAroundFixedPoint(fieldRelativePoint, pointOfRotation, rotationAng);
            double rotatedAng = this.getRotationY() - Robot2DPositionTracker.this.getPosition().getRotationY();
            return new Robot2DPositionRobotAxisIndicator(rotatedAxis[0], rotatedAxis[1], rotatedAng);
        }
    }

    public static class Robot2DPositionRobotAxisIndicator extends Robot2DPositionIndicator {
        public Robot2DPositionRobotAxisIndicator(double X, double Z, double YRotation) {
            super(X, Z, YRotation);
        }

        public Robot2DPositionFieldAxisIndicator toFieldAxis(@NonNull Robot2DPositionTracker positionTracker) {
            return positionTracker.new Robot2DPositionFieldAxisIndicator(this);
        }
    }

    private Robot2DPositionIndicator m_2DPos;
    private Robot2DPositionRobotAxisIndicator[] m_ExtremePoints;
    private Robot2DPositionIndicator m_FieldMinPoint, m_FieldMaxPoint;
    private boolean m_NeedPositionFix = false;

    public Robot2DPositionTracker(@NonNull Robot2DPositionIndicator initialPosition, @NonNull Robot2DPositionRobotAxisIndicator[] robotExtremePoints, @NonNull Robot2DPositionIndicator fieldMin, @NonNull Robot2DPositionIndicator fieldMax) {
        this.m_2DPos = initialPosition;
        this.m_FieldMinPoint = fieldMin;
        this.m_FieldMaxPoint = fieldMax;
        this.m_NeedPositionFix = false;
        this.setExtremePoints(robotExtremePoints);
    }

    public Robot2DPositionTracker(Robot2DPositionTracker Tracker){
        this.m_2DPos = new Robot2DPositionIndicator(Tracker.m_2DPos);
        this.m_FieldMinPoint = Tracker.m_FieldMinPoint;
        this.m_FieldMaxPoint = Tracker.m_FieldMaxPoint;
        this.m_NeedPositionFix = Tracker.m_NeedPositionFix;
        this.setExtremePoints(Tracker.m_ExtremePoints);
    }

    protected void fixBouncingBox() {
        boolean needFix = false;
        double[][] RobotExtremes = new double[4][2];
        Robot2DPositionFieldAxisIndicator[] robotExtremes_Indicators = this.getRobotExtremePoints_fieldAxis();
        for (int i = 0; i < 4; i++) {
            RobotExtremes[i][0] = robotExtremes_Indicators[i].getX();
            RobotExtremes[i][1] = robotExtremes_Indicators[i].getZ();
        }
        for (int i = 0; i < 4; i++) {
            double[] EachExtreme = RobotExtremes[i];
            double Offset = 0;
            if (EachExtreme[0] < this.getFieldMinPoint().getX() || EachExtreme[0] > this.getFieldMaxPoint().getX() || EachExtreme[1] < this.getFieldMinPoint().getZ() || EachExtreme[1] > this.getFieldMaxPoint().getZ()) {
                this.m_NeedPositionFix = true;
            }
            if (EachExtreme[0] < this.getFieldMinPoint().getX()) {
                Offset = this.getFieldMinPoint().getX() - EachExtreme[0];
                this.offsetPosition(new Robot2DPositionIndicator(Offset, 0, 0));
                needFix = true;
                break;
            } else if (EachExtreme[0] > this.getFieldMaxPoint().getX()) {
                Offset = EachExtreme[0] - this.getFieldMaxPoint().getX();
                this.offsetPosition(new Robot2DPositionIndicator(-Offset, 0, 0));
                needFix = true;
                break;
            } else if (EachExtreme[1] < this.getFieldMinPoint().getZ()) {
                Offset = this.getFieldMinPoint().getZ() - EachExtreme[1];
                this.offsetPosition(new Robot2DPositionIndicator(0, Offset, 0));
            } else if (EachExtreme[1] > this.getFieldMaxPoint().getZ()) {
                Offset = EachExtreme[1] - this.getFieldMaxPoint().getZ();
                this.offsetPosition(new Robot2DPositionIndicator(0, -Offset, 0));
            }
        }
        if (needFix) {
            //Because offsetPosition() always calls fixBouncingBox(), we can choose to not call it here
            //this.fixBouncingBox();
        }
    }

    public boolean isNeedPositionFix() {
        return this.m_NeedPositionFix;
    }

    public void finishPositionFix() {
        this.m_NeedPositionFix = false;
    }

    public Robot2DPositionIndicator getFieldMinPoint() {
        return this.m_FieldMinPoint;
    }

    public void setFieldMinPoint(@NonNull Robot2DPositionIndicator minPoint) {
        this.m_FieldMinPoint = minPoint;
        this.fixBouncingBox();
    }

    public Robot2DPositionIndicator getFieldMaxPoint() {
        return this.m_FieldMaxPoint;
    }

    public void setFieldMaxPoint(Robot2DPositionIndicator maxPoint) {
        this.m_FieldMaxPoint = maxPoint;
        this.fixBouncingBox();
    }

    public Robot2DPositionRobotAxisIndicator[] getRobotExtremePoints() {
        return this.m_ExtremePoints;
    }

    public Robot2DPositionFieldAxisIndicator[] getRobotExtremePoints_fieldAxis() {
        Robot2DPositionFieldAxisIndicator[] result = new Robot2DPositionFieldAxisIndicator[4];
        for (int i = 0; i < 4; i++) {
            result[i] = this.m_ExtremePoints[i].toFieldAxis(this);
        }
        return result;
    }

    public void setExtremePoints(@NonNull Robot2DPositionRobotAxisIndicator[] extremes) {
        if (extremes.length != 4) {
            throw new RuntimeException("Length of RobotExtremePoints is not 4");
        }
        this.m_ExtremePoints = extremes;
        this.fixBouncingBox();
    }

    public Robot2DPositionIndicator getPosition() {
        return this.m_2DPos;
    }

    public void setPosition(@NonNull Robot2DPositionIndicator newPosition) {
        this.m_2DPos = newPosition;
        this.fixBouncingBox();
    }

    public void offsetPosition(@NonNull Robot2DPositionIndicator offsetPosition) {
        this.m_2DPos.setX(this.m_2DPos.getX() + offsetPosition.getX());
        this.m_2DPos.setZ(this.m_2DPos.getZ() + offsetPosition.getZ());
        this.m_2DPos.setRotationY(this.m_2DPos.getRotationY() + offsetPosition.getRotationY());
        this.fixBouncingBox();
    }

    public void drive_MoveThroughFieldAngle(double angleInDeg, double distance) {
        double angleInRad = Math.toRadians(angleInDeg);
        double xMoved = Math.cos(angleInRad) * distance, yMoved = Math.sin(angleInRad) * distance;
        this.offsetPosition(new Robot2DPositionIndicator(xMoved, yMoved, 0));
    }

    public void drive_MoveThroughRobotAngle(double angleInDeg, double distance) {
        Robot2DPositionRobotAxisIndicator tempRobotIndicator = new Robot2DPositionRobotAxisIndicator(0, 0, angleInDeg);
        Robot2DPositionFieldAxisIndicator tempFieldIndicator = tempRobotIndicator.toFieldAxis(this);
        this.drive_MoveThroughFieldAngle(tempFieldIndicator.getRotationY(), distance);
    }

    public void drive_MoveThroughRobotAxisOffset(@NonNull Robot2DPositionRobotAxisIndicator robotAxisValues) {
        this.offsetPosition(robotAxisValues.toFieldAxis(this));
    }

    public void drive_RotateAroundFieldPoint(@NonNull Robot2DPositionFieldAxisIndicator fieldPointAndRotation) {
        if (fieldPointAndRotation.getX() == this.getPosition().getX() && fieldPointAndRotation.getZ() == this.getPosition().getZ()) {
            this.offsetPosition(new Robot2DPositionIndicator(0, 0, fieldPointAndRotation.getRotationY()));
            return;
        } else {
            double[] point = {fieldPointAndRotation.getX(), fieldPointAndRotation.getZ()};
            double[] currentPos = {this.getPosition().getX(), this.getPosition().getZ()};
            double[] newRobotPosition = XYPlaneCalculations.rotatePointAroundFixedPoint(point, currentPos, fieldPointAndRotation.getRotationY());
            this.setPosition(new Robot2DPositionIndicator(newRobotPosition[0], newRobotPosition[1], this.getPosition().getRotationY() + fieldPointAndRotation.getRotationY()));
        }
    }

    public void drive_RotateAroundRobotAxisPoint(@NonNull Robot2DPositionRobotAxisIndicator robotPointAndRotation) {
        Robot2DPositionFieldAxisIndicator tempFieldPointAndRotation = robotPointAndRotation.toFieldAxis(this);
        tempFieldPointAndRotation.setRotationY(robotPointAndRotation.getRotationY());
        this.drive_RotateAroundFieldPoint(tempFieldPointAndRotation);
    }

    public void drive_RotateAroundFieldPointWithRadiusAndPowerPoint(@NonNull Robot2DPositionIndicator fieldPoint, double Radius, double DistanceCounterClockwise) {
        double moveAngleRad = DistanceCounterClockwise / Radius;
        double moveAngleDeg = Math.toDegrees(moveAngleRad);
        this.drive_RotateAroundFieldPoint(new Robot2DPositionFieldAxisIndicator(fieldPoint.getX(), fieldPoint.getZ(), moveAngleDeg));
    }
    public void drive_RotateAroundRobotPointWithRadiusAndPowerPoint(@NonNull Robot2DPositionIndicator robotAxisPoint, double Radius, double DistanceCounterClockwise){
        double moveAngleRad = DistanceCounterClockwise / Radius;
        double moveAngleDeg = Math.toDegrees(moveAngleRad);
        this.drive_RotateAroundRobotAxisPoint(new Robot2DPositionRobotAxisIndicator(robotAxisPoint.getX(), robotAxisPoint.getZ(), moveAngleDeg));
    }
}

