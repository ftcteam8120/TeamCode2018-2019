package org.firstinspires.ftc.teamcode.Armstrong.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Armstrong.RobotArmstrong;
import org.firstinspires.ftc.teamcode.Util.AutoHandler;

@Disabled
@Autonomous(name = "AutoCraterArmstrong", group = "Armstrong")
public class AutoCraterArmstrong extends OpMode {

    private AutoHandler handler;

    /*
     * Instantiates and initializes the robot using the hardwareMap
     */
    public void init() {
        RobotArmstrong robot = new RobotArmstrong();
        robot.init(hardwareMap);
        handler = new AutoHandlerArmstrong(robot, new ElapsedTime(), true, true);
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
