package org.firstinspires.ftc.teamcode.Armstrong.Autonomous;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Armstrong.RobotArmstrong;
import org.firstinspires.ftc.teamcode.Util.AutoHandler;
import org.firstinspires.ftc.teamcode.Util.Robot;
import org.firstinspires.ftc.teamcode.Util.RobotState;

import static org.firstinspires.ftc.teamcode.Util.RobotState.ALIGN_0;
import static org.firstinspires.ftc.teamcode.Util.RobotState.ALIGN_1;
import static org.firstinspires.ftc.teamcode.Util.RobotState.BACK_UP_1;
import static org.firstinspires.ftc.teamcode.Util.RobotState.BACK_UP_2;
import static org.firstinspires.ftc.teamcode.Util.RobotState.DRIVE_AWAY;
import static org.firstinspires.ftc.teamcode.Util.RobotState.EXPEL;
import static org.firstinspires.ftc.teamcode.Util.RobotState.EXTEND;
import static org.firstinspires.ftc.teamcode.Util.RobotState.HALT;
import static org.firstinspires.ftc.teamcode.Util.RobotState.RAISE;
import static org.firstinspires.ftc.teamcode.Util.RobotState.RETRACT;
import static org.firstinspires.ftc.teamcode.Util.RobotState.ROTATE;
import static org.firstinspires.ftc.teamcode.Util.RobotState.TOKYO_DRIFT;
import static org.firstinspires.ftc.teamcode.Util.RobotState.UNHOOK_0;
import static org.firstinspires.ftc.teamcode.Util.RobotState.UNHOOK_1;
import static org.firstinspires.ftc.teamcode.Util.RobotState.UNHOOK_2;
import static org.firstinspires.ftc.teamcode.Util.RobotState.UNHOOK_3;

public class AutoHandlerArmstrong extends AutoHandler {

    /**
     * Constructor
     * @param r the robot
     * @param et the timer
     * @param b blue side?
     * @param c crater side?
     */
    public AutoHandlerArmstrong(Robot r, ElapsedTime et, boolean b, boolean c) {
        super(r, et, b, c);
    }

    /**
     * Telemetry call
     * @param telemetry the telemetry output
     */
    @Override
    public void printStats(Telemetry telemetry) {
        super.printStats(telemetry);
    }

    /**
     * State machine
     */
    @Override
    public void tick() {

        RobotArmstrong robot = (RobotArmstrong) r;

        switch (state) {
            case START:
                if(robot.imu.isGyroCalibrated())
                    toState(RobotState.LOWER);
                break;
            case LOWER:
                // lower until the limit is reached
                robot.hanger.setPower(.6);
                if (robot.upperTouch.isPressed())
                    toState(UNHOOK_0);
                break;
            case UNHOOK_0:
                robot.left.setPower(.3);
                robot.right.setPower(.3);
                if(timer.milliseconds() >= 500)
                    toState(UNHOOK_1);
                break;
            case UNHOOK_1:
                // turn off of the hook
                robot.left.setPower(.3);
                robot.right.setPower(-.3);
                if(robot.imu.getAngularOrientation().firstAngle <= -18)
                    toState(UNHOOK_2);
                break;
            case UNHOOK_2:
                // move forward a bit to line up with the other half of the crater
                robot.left.setPower(.3);
                robot.right.setPower(.3);
                robot.hanger.setPower(robot.lowerTouch.isPressed() ? 0 : -.6);
                if(robot.getLeftDisplacement() > 400)
                    toState(UNHOOK_3);
                break;
            case UNHOOK_3:
                robot.left.setPower(-.3);
                robot.right.setPower(.3);
                robot.hanger.setPower(robot.lowerTouch.isPressed() ? 0 : -.6);
                if(robot.imu.getAngularOrientation().firstAngle >= 18)
                    toState(ALIGN_0);
                break;
            case ALIGN_0:
                robot.left.setPower(.3);
                robot.right.setPower(.3);
                if(robot.getLeftDisplacement() > 200)
                    toState(ALIGN_1);
                break;
            case ALIGN_1:
                robot.left.setPower(.3);
                robot.right.setPower(-.3);
                if(robot.imu.getAngularOrientation().firstAngle <= 10)
                    toState(DRIVE_AWAY);
                break;
            case DRIVE_AWAY:
                // drive ur heart out
                robot.right.setPower(1);
                robot.left.setPower(1);
                robot.hanger.setPower(robot.lowerTouch.isPressed() ? 0 : -.6);
                if(timer.milliseconds() > (facingCrater ? 700 : 1400))
                    toState(RETRACT);
                break;
            case RETRACT:
                // finish pulling the hanger back down
                robot.right.setPower(0);
                robot.left.setPower(0);
                robot.hanger.setPower(-.6);
                if(robot.lowerTouch.isPressed())
                    toState(facingCrater ? RAISE : BACK_UP_1);
                break;
            case BACK_UP_1:
                robot.left.setPower(-.5);
                robot.right.setPower(-.5);
                if(timer.milliseconds() >= 250)
                    toState(EXPEL);
                break;
            case EXPEL:
                robot.impeller.setPower(.75);
                if(timer.milliseconds() > 3000)
                    toState(BACK_UP_2);
                break;
            case BACK_UP_2:
                robot.left.setPower(-.5);
                robot.right.setPower(-.5);
                if(timer.milliseconds() >= 350)
                    toState(ROTATE);
                break;
            case ROTATE:
                robot.left.setPower(-.3);
                robot.right.setPower(.3);
                if(robot.imu.getAngularOrientation().firstAngle >= 114)
                    toState(TOKYO_DRIFT);
                break;
            case TOKYO_DRIFT:
                robot.right.setPower(1);
                robot.left.setPower(1);
                if(timer.milliseconds() >= 1450)
                    toState(RAISE);
                break;
            case RAISE:
                robot.elbow.setPower(.5);
                if(timer.milliseconds() >= 1000)
                    toState(EXTEND);
                break;
            case EXTEND:
                robot.arm.setPower(1);
                if(timer.milliseconds() >= 4500)
                    toState(HALT);
                break;
            case HALT:
            default:
                robot.stop();
        }
    }
}
