package org.firstinspires.ftc.teamcode.Armstrong.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Armstrong.RobotArmstrong;

@Autonomous(name = "AutoBlueCrater", group = "Armstrong")
public class AutoBlueCrater extends OpMode {

    private AutoHandler handler;

    /*
     * Instantiates the robot and initializes the robot using the hardwareMap
     */
    public void init() {
        RobotArmstrong robot = new RobotArmstrong();
        robot.init(hardwareMap);
        handler = new AutoHandler(robot, new ElapsedTime(), true, true);
    }

    /*
     * Continuously updates the robot
     */
    @Override
    public void loop() {
       handler.tick();
       handler.printStats(telemetry);
    }
}
