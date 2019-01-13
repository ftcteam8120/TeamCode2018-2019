package org.firstinspires.ftc.teamcode.Rockwell;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Rockwell.RobotRockwellA;

@TeleOp(name = "Rockwell A Drive", group = "Rockwell")
public class RockwellADrive extends OpMode
{
    RobotRockwellA robot;

    @Override
    public void init() {
        robot = new RobotRockwellA();
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        robot.left.setPower(gamepad1.left_stick_y);
        robot.right.setPower(gamepad1.right_stick_y);

        robot.arm.setPower(-gamepad2.right_stick_y);

        if(gamepad2.a) robot.close();
        else if(gamepad2.b) robot.open();
    }
}
