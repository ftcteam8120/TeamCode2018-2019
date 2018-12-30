package org.firstinspires.ftc.teamcode.Armstrong.Autonomous;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Armstrong.RobotArmstrong;
import org.firstinspires.ftc.teamcode.Util.RobotState;

import static org.firstinspires.ftc.teamcode.Util.RobotState.*;

public class AutoHandler {

    RobotArmstrong robot;
    ElapsedTime timer;
    RobotState state;
    boolean blue; // are we on blue side?
    boolean facingCrater; // are we facing the crater?

    public AutoHandler(RobotArmstrong r, ElapsedTime et, boolean b, boolean c)
    {
        robot = r;
        timer = et;
        blue = b;
        facingCrater = c;
        toState(START);
    }

    /**
     * goes to a specific state, marks the encoders, and resets the clock
     * @param next the next state
     */
    private void toState(RobotState next) {
        robot.stop();
        robot.markEncoders();
        this.state = next;
        timer.reset();
    }

    /**
     * prints debugging information to the phone screen
     * @param telemetry the telemetry output
     */
    public void printStats(Telemetry telemetry) {
        if(robot != null) {
            telemetry.addData("state", this.state.name() + ", " + this.timer.milliseconds());
        }
    }

    int turnTicks; // how many ticks it actually took to turn off of the hook, used to undo the

    public void tick()
    {
        switch (state) {
            case START:
                toState(RobotState.LOWER);
                break;
            case LOWER:
                // lower until the limit is reached
                this.robot.hanger.setPower(.6);
                if (robot.upperTouch.isPressed())
                    toState(UNHOOK_1);
                break;
            case UNHOOK_1:
                // turn off of the hook
                robot.left.setPower(.3);
                robot.right.setPower(-.3);
                if(robot.getRightDisplacement() > 100)
                {
                    turnTicks = robot.getRightDisplacement();
                    toState(UNHOOK_2);
                }
                break;
            case UNHOOK_2:
                // move forward a bit to line up with the other half of the crater
                robot.left.setPower(.3);
                robot.right.setPower(.3);
                if(robot.getRightDisplacement() > 1400)
                    toState(UNHOOK_3);
                break;
            case UNHOOK_3:
                // undo the last turn to keep the robot straight
                robot.left.setPower(-.3);
                robot.right.setPower(.3);
                if(robot.getRightDisplacement() > turnTicks * 2)
                    toState(facingCrater ? DRIVE_AWAY : HALT);
                break;
            case DRIVE_AWAY:
                // drive ur heart out
                robot.right.setPower(.6);
                robot.left.setPower(.6);
                robot.hanger.setPower(robot.lowerTouch.isPressed() ? 0 : -.6);
                if(timer.milliseconds() > 2000)
                    toState(RETRACT);
                break;
            case RETRACT:
                // finish pulling the hanger back down
                robot.hanger.setPower(-.6);
                if(robot.lowerTouch.isPressed())
                    toState(HALT);
                break;
            case HALT:
            default:
                this.robot.stop();

        }
    }
}
