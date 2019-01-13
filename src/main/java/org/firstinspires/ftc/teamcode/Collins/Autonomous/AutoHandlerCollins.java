package org.firstinspires.ftc.teamcode.Collins.Autonomous;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Collins.RobotCollins;
import org.firstinspires.ftc.teamcode.Util.AutoHandler;
import org.firstinspires.ftc.teamcode.Util.Robot;

import static org.firstinspires.ftc.teamcode.Util.RobotState.*;

public class AutoHandlerCollins extends AutoHandler {

    /**
     * Constructor
     * @param r the robot
     * @param et the timer
     * @param b blue side?
     * @param c crater side?
     */
    public AutoHandlerCollins(Robot r, ElapsedTime et, boolean b, boolean c) {
        super(r, et, b, c);
    }

    /**
     * Telemetry call
     * @param telemetry the telemetry output
     */
    @Override
    public void printStats(Telemetry telemetry) {
        super.printStats(telemetry);
        telemetry.addData("sampling", ((RobotCollins) r).detector.getLastOrder());
    }

    /**
     * State Machine
     */
    @Override
    public void tick() {

        RobotCollins robot = (RobotCollins) r;

        switch(state)
        {
            case START:
                if(robot.imu.isGyroCalibrated())
                    toState(HALT);
                break;
            case HALT:
            default:
                robot.stop();
        }
    }
}
