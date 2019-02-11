package org.firstinspires.ftc.teamcode.Collins.Autonomous;

import com.disnodeteam.dogecv.detectors.roverrukus.GoldDetector;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Collins.RobotCollins;
import org.firstinspires.ftc.teamcode.Util.AutoHandler;
import org.firstinspires.ftc.teamcode.Util.Robot;
import org.firstinspires.ftc.teamcode.Util.Utilities;

import static org.firstinspires.ftc.teamcode.Util.RobotState.*;
import static com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector.GoldLocation.*;

public class AutoHandlerCollins extends AutoHandler {

    public static final double speedFactor = 1.5;

    /**
     * Constructor
     * @param r the robot
     * @param et the timer
     * @param b blue side?
     * @param c crater side?
     */
    public AutoHandlerCollins(Robot r, ElapsedTime et, boolean b, boolean c) {
        super(r, et, b, c);
        goldPos = -1;
    }

    /**
     * Telemetry call
     * @param telemetry the telemetry output
     */
    @Override
    public void printStats(Telemetry telemetry) {
        super.printStats(telemetry);
        GoldDetector detector = ((RobotCollins) r).detector;
        telemetry.addData("gold pos", "@ " + goldPos);
        if(goldLocation != null)
            telemetry.addData("gold loc", goldLocation.name());
    }

    double goldPos;
    SamplingOrderDetector.GoldLocation goldLocation;

    /**
     * State Machine
     */
    @Override
    public void tick() {

        RobotCollins robot = (RobotCollins) r;

        switch(state)
        {
            case START:
                // Check center and right samples for gold location; give it a few seconds
                if(robot.detector.isFound())
                {
                    // If it's found, record the position on screen and move on
                    goldPos = robot.detector.getScreenPosition().x;
                    toState(WAIT_FOR_IMU);
                }
                else if(timer.milliseconds() > 3000)
                    // Give up
                    toState(WAIT_FOR_IMU);
                break;
            case WAIT_FOR_IMU:
                // Make sure gyro is calibrated before continuing
                if(robot.imu.isGyroCalibrated()) {
                    // Determine gold sample location based on screen position
                    goldLocation = null;
                    if (goldPos < 0)
                        goldLocation = LEFT;
                    else if (goldPos > 380)
                        goldLocation = RIGHT;
                    else
                        goldLocation = CENTER;
                    toState(LOWER);
                }
                break;
            case LOWER:
                // lower until the limit is reached
                robot.hanger.setPower(.6);
                if (robot.upperTouch.isPressed())
                    toState(UNHOOK_1);
                break;
            // Get off the hook!
            case UNHOOK_1:
                robot.drive(Utilities.LEFT, .20 * speedFactor);
                if(robot.getDisplacement() >= 250)
                    toState(UNHOOK_2);
                break;
            case UNHOOK_2:
                robot.drive(Utilities.FORWARD, .20 * speedFactor);
                // This line is used to gradually retarct the hanger
                robot.hanger.setPower(robot.lowerTouch.isPressed() ? 0 : -.6);
                if(robot.getDisplacement() >= 940)
                    toState(UNHOOK_3);
                break;
            case UNHOOK_3:
                // Line back up with the center mineral
                robot.drive(Utilities.RIGHT, .20 * speedFactor);
                robot.hanger.setPower(robot.lowerTouch.isPressed() ? 0 : -.6);
                if(robot.getDisplacement() >= 250)
                    toState(ALIGN_0);
                break;
            case ALIGN_0:
                // Move to the gold sample
                if(goldLocation == null || goldLocation == CENTER)
                    toState(SAMPLE);
                robot.drive(goldLocation == RIGHT ? Utilities.RIGHT : Utilities.LEFT, .20 * speedFactor);
                robot.hanger.setPower(robot.lowerTouch.isPressed() ? 0 : -.6);
                if(robot.getDisplacement() >= 1570)
                    toState(SAMPLE);
                break;
            case SAMPLE:
                // Knock the gold sample off
                robot.drive(Utilities.FORWARD, .20 * speedFactor);
                robot.hanger.setPower(robot.lowerTouch.isPressed() ? 0 : -.6);
                if(robot.getDisplacement() >= 3600)
                    toState(facingCrater ? HALT : ALIGN_1);
                break;
            case ALIGN_1:
                if(goldLocation == null || goldLocation == CENTER)
                    toState(QUOTE_ONE_EIGHTY_ENDQUOTE);
                robot.drive(goldLocation == RIGHT ? Utilities.LEFT : Utilities.RIGHT, .20 * speedFactor);
                if(robot.getDisplacement() >= 1570)
                    toState(QUOTE_ONE_EIGHTY_ENDQUOTE);
                break;
            case QUOTE_ONE_EIGHTY_ENDQUOTE:
                robot.drive(Utilities.ROTATE_CCW, .2 * speedFactor);
                if(robot.getAngDisplacement() >= 135)
                    toState(SQUARE_0);
                break;
            case SQUARE_0:
                robot.drive(Utilities.RIGHT, .3 * speedFactor);
                if(timer.milliseconds() >= 3000)
                    toState(SQUARE_1);
                break;
            case SQUARE_1:
                robot.drive(Utilities.LEFT, .3 * speedFactor);
                if(robot.getDisplacement() >= 600)
                    toState(EXPEL);
                break;
            case EXPEL:
                robot.impeller.setPower(1);
                if(timer.milliseconds() >= 3000)
                    toState(TOKYO_DRIFT);
                break;
            case TOKYO_DRIFT:
                robot.drive(Utilities.FORWARD, .2 * speedFactor);
                if(robot.getDisplacement() >= 5400)
                    toState(RAISE);
                break;
            case HALT:
            default:
                robot.stop();
        }
    }
}
