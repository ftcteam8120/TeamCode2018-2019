package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.hardware.bosch.BNO055IMU;

public class Utilities {

    /**
     * Generates and returns default parameters for REV IMU
     */
    public static BNO055IMU.Parameters getGyroParams()
    {
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.mode = BNO055IMU.SensorMode.IMU;
        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        return params;
    }

    // Preset Mecanum drive directions {FL, FR, BL, BR}
    public static double[] STOPPED = {0, 0, 0, 0};
    public static double[] FORWARD = {1, 1, 1, 1};
    public static double[] BACKWARD = {-1, -1, -1, -1};
    public static double[] LEFT = {-1, 1, 1, -1};
    public static double[] RIGHT = {1, -1, -1, 1};
    public static double[] FORWARD_LEFT = {0, 1, 1, 0};
    public static double[] FORWARD_RIGHT = {1, 0, 0, 1};
    public static double[] BACKWARD_LEFT = {-1, 0, 0, -1};
    public static double[] BACKWARD_RIGHT = {0, -1, -1, 0};
    public static double[] PIVOT_RIGHT = {1, .5, 1, .5};
    public static double[] PIVOT_LEFT = {.5, 1, .5, 1};
    public static double[] ROTATE_CCW = {-1, 1, -1, 1};
    public static double[] ROTATE_CW = {1, -1, 1, -1};

    public static final double SERVO_STOP = -0.05;
    public static final double SERVO_B = -0.15;
    public static final double SERVO_F = .05;

    /**
     * Calculate the direction of the robot based on the position of the stick.
     * @param stickX x-axis offset of the stick from -1 to 1
     * @param stickY y-axis offset of the stick from -1 to 1
     * @return preset direction (array of motor powers)
     */
    public static double[] calculateDirection(double stickX, double stickY)
    {
        // Check zero cases
        if(stickX == 0 && stickY == 0)
            return STOPPED;
        else if(stickX == 0)
            return (stickY > 0) ? FORWARD : BACKWARD;
        else if(stickY == 0)
            return (stickX > 0) ? RIGHT : LEFT;
        else // Calculate angle and ultimately direction
        {
            double angle = Math.toDegrees(Math.atan2(stickY, stickX)); // -180 to +180
            if(angle < 0) angle += 360; // shift (-180, 0) to (180, 360); now angle must be between 0 and 360

            // Return direction based on angle
            if(angle < 22.5) return RIGHT;
            else if(angle < 67.5) return FORWARD_RIGHT;
            else if(angle < 112.5) return FORWARD;
            else if(angle < 157.5) return FORWARD_LEFT;
            else if(angle < 202.5) return LEFT;
            else if(angle < 247.5) return BACKWARD_LEFT;
            else if(angle < 292.5) return BACKWARD;
            else if(angle < 337.5) return BACKWARD_RIGHT;
            else return RIGHT;
        }
    }
}
