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
package org.firstinspires.ftc.teamcode.DarlingtonSharedLib.Calculations;


public class XYPlaneCalculations {
    public static double[] rotatePointAroundFixedPoint(double[] point, double[] fixedPoint, double counterClockwiseAng) {
        double relativeY = point[1] - fixedPoint[1], relativeX = point[0] - fixedPoint[0];
        double deltaAng = Math.toRadians(counterClockwiseAng);
        double newX = relativeX * Math.cos(deltaAng) - relativeY * Math.sin(deltaAng);
        double newY = relativeX * Math.sin(deltaAng) + relativeY * Math.cos(deltaAng);
        double[] result = {newX + fixedPoint[0], newY + fixedPoint[1]};
        return result;
    }
    public static double chooseAngleFromRange(double[] angleList, double angleSmallestRange, double angleBiggestRange) {
        for(int i=0;i<angleList.length;i++) {
            if(angleList[i] >= angleSmallestRange && angleList[i] <= angleBiggestRange) {
                return angleList[i];
            }
        }
        return angleList[0];
    }
    public static double normalizeRad(double Rad) {
        double tempRad = Rad;
        while(tempRad >= Math.PI) {
            tempRad -= Math.PI * 2;
        }
        while(tempRad < -Math.PI) {
            tempRad += Math.PI * 2;
        }
        return tempRad;
    }
    public static double normalizeDeg(double Deg) {
        double tempDeg = Deg;
        while(tempDeg >= 180) {
            tempDeg -= 180 * 2;
        }
        while(tempDeg < -180) {
            tempDeg += 180 * 2;
        }
        return tempDeg;
    }
}
