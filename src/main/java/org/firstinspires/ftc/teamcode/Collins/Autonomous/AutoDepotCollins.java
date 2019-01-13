package org.firstinspires.ftc.teamcode.Collins.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Collins.RobotCollins;
import org.firstinspires.ftc.teamcode.Util.AutoHandler;

@Autonomous(name = "AutoDepotCollins", group = "Collins")
public class AutoDepotCollins extends OpMode {

    private AutoHandler handler;
    private RobotCollins robot;

    /**
     * Initializes the robot and the handler
     */
    @Override
    public void init() {
        robot = new RobotCollins();
        robot.init(hardwareMap);
        handler = new AutoHandlerCollins(robot, new ElapsedTime(), false, false);
    }

    /**
     * Enables DogeCV at the start of the op mode
     */
    @Override
    public void start()
    {
        robot.detector.enable();
    }

    /**
     * Continuously asks the autonomous handler to execute & print stats
     */
    @Override
    public void loop() {
        handler.tick();
        handler.printStats(telemetry);
    }

    /**
     * Disables DogeCV after the op mode is over
     */
    @Override
    public void stop() {
        robot.detector.disable();
    }
}
