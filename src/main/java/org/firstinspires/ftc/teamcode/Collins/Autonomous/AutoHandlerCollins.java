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

    public static final double speedFactor = 2;

    /**
     * Constructor
     * @param r the robot
     * @param et the timer
     * @param b blue side?
     * @param c crater side?
     */
    public AutoHandlerCollins(Robot r, ElapsedTime et, boolean b, boolean c) {
        super(r, et, b, c);
        goldPos = -999;
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
                if(timer.milliseconds() > 3000)
                    toState(WAIT_FOR_IMU);
                break;
            case WAIT_FOR_IMU:
                // Make sure gyro is calibrated before continuing
                if(robot.imu.isGyroCalibrated()) {
                    // Determine gold sample location based on screen position
                    goldLocation = null;
                    if (goldPos < 0)
                        goldLocation = LEFT;
                    else if (goldPos > 300)
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
                // This line is used in the next few states to gradually retract the hanger
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
                    toState(SAMPLE_0);
                robot.drive(goldLocation == RIGHT ? Utilities.RIGHT : Utilities.LEFT, .20 * speedFactor);
                robot.hanger.setPower(robot.lowerTouch.isPressed() ? 0 : -.6);
                if(robot.getDisplacement() >= 1570)
                    toState(SAMPLE_0);
                break;
            case SAMPLE_0:
                // Knock the gold sample off
                robot.drive(Utilities.FORWARD, .3);
                robot.hanger.setPower(robot.lowerTouch.isPressed() ? 0 : -.6);
                if(timer.milliseconds() >= (facingCrater ? 750 : 2750))
                    toState(facingCrater ? SAMPLE_1 : ALIGN_1);
                break;
            case SAMPLE_1:
                // bACK UP
                robot.drive(Utilities.BACKWARD, .3);
                if(timer.milliseconds() >= 750)
                    toState(ROTATE_0);
                break;
            case ROTATE_0:
                // Rotate to face the field perimeter
                robot.drive(Utilities.ROTATE_CCW, robot.getAngDisplacement() >= 60 ? .2 : .3);
                if(robot.getAngDisplacement() >= 90)
                    toState(WALL);
                break;
            case WALL:
                // Reach the field perimeer
                robot.drive(Utilities.FORWARD, .3 * speedFactor);
                int val = 4000;
                if(goldLocation == LEFT) val -= 1600;
                if(goldLocation == RIGHT) val += 1600;
                if(robot.getDisplacement() >= val)
                    toState(ROTATE_1);
                break;
            case ROTATE_1:
                // Rotate once more to face the depot
                robot.drive(Utilities.ROTATE_CCW, .2);
                if(robot.getAngDisplacement() >= 40)
                    toState(DEPOT);
                break;
            case DEPOT:
                // Drive to the depot
                robot.drive(Utilities.FORWARD, .3 * speedFactor);
                if(robot.getDisplacement() >= 3400)
                    toState(PREXPEL);
                break;
            case PRE_TOKYO:
                // Veeery slight rotation to narrow margin of error
                robot.drive(Utilities.ROTATE_CCW, .2);
                if(robot.getAngDisplacement() >= 3)
                    toState(TOKYO_REVERSE);
                break;
            case TOKYO_REVERSE:
                // Back up in an attempt to get in the crater
                robot.drive(Utilities.BACKWARD, .4 * speedFactor);
                if(timer.seconds() >= 5)
                    toState(HALT);
                break;
            case ALIGN_1:  // Line back up with center mineral
                if(goldLocation == null || goldLocation == CENTER)
                    toState(PREXPEL); // ignore if we're already there
                robot.drive(goldLocation == RIGHT ? Utilities.LEFT : Utilities.RIGHT, .20 * speedFactor);
                if(robot.getDisplacement() >= 1570)
                    toState(PREXPEL);
                break;
            case PREXPEL:
                robot.flipper.setPower(-1);
                if(timer.milliseconds() >= 400)
                    toState(facingCrater ? PRE_TOKYO : SQUARE_0);
                break;
            case SQUARE_0: // Rotate to be roughly parallel to the wall
                robot.drive(Utilities.ROTATE_CCW, robot.getAngDisplacement() >= 90 ? .2 : .3);
                if(robot.getAngDisplacement() >= 130)
                    toState(SQUARE_1);
                break;
            case SQUARE_1: // SLAM!
                robot.drive(Utilities.RIGHT, .75 * speedFactor);
                if(timer.milliseconds() >= 1250)
                    toState(SQUARE_2);
                break;
            case SQUARE_2: // Pull away from the wall slowly
                robot.drive(Utilities.LEFT, .15 * speedFactor);
                if(robot.getDisplacement() >= 500)
                    toState(SQUARE_3);
                break;
            case SQUARE_3:
                robot.drive(Utilities.ROTATE_CW, .2);
                if(robot.getAngDisplacement() >= 15)
                    toState(TOKYO_DRIFT);
                break;
            case TOKYO_DRIFT: // Bolt towards the crater
                robot.drive(Utilities.FORWARD, .2 * speedFactor);
                if(robot.getDisplacement() >= 5400)
                    toState(RAISE);
                break;
            case RAISE: // Raise the cannons!!
                robot.flipper.setPower(1);
                if(timer.milliseconds() >= 250)
                    toState(EXTEND);
                break;
            case EXTEND: // Fireeeeeeeee!!!!!
                robot.extender.setPower(1);
                if(timer.milliseconds() >= 2000)
                    toState(HALT);
                break;
            case HALT:
            default:
                robot.stop();
        }
    }
}
