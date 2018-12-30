package org.firstinspires.ftc.teamcode.Armstrong.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Armstrong.RobotArmstrong;

@Autonomous(name = "AutoStutter", group = "Armstrong")
public class AutoStutter extends OpMode {

    RobotArmstrong robot;

    @Override
    public void init() {
        robot = new RobotArmstrong();
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        robot.left.setPower(.5);
        robot.right.setPower(.5);
    }
}
