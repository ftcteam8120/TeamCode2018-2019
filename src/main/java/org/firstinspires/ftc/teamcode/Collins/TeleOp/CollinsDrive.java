package org.firstinspires.ftc.teamcode.Collins.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Collins.RobotCollins;
import org.firstinspires.ftc.teamcode.Util.Utilities;

@TeleOp(name = "CollinsDrive", group = "Collins")
public class CollinsDrive extends OpMode {

    private RobotCollins robot;
    private double speed;

    @Override
    public void init() {
        robot = new RobotCollins();
        robot.init(hardwareMap);
        speed = 1;
    }

    @Override
    public void loop() {
        // Routines separated into separate functions
        updateDriver();
        updateCodriver();
        updateAuto();
    }

    /**
     * Process gamepad1 (driver) controls
     */
    private void updateDriver()
    {
        // Check for rotation first, then calculate movement direction
        if(gamepad1.right_stick_x < -.1) robot.drive(Utilities.ROTATE_CCW, speed);
        else if(gamepad1.right_stick_x > .1) robot.drive(Utilities.ROTATE_CW, speed);
        else if(gamepad1.dpad_up) robot.drive(Utilities.FORWARD, .5);
        else if(gamepad1.dpad_right) robot.drive(Utilities.RIGHT, .5);
        else if(gamepad1.dpad_down) robot.drive(Utilities.BACKWARD, .5);
        else if(gamepad1.dpad_left) robot.drive(Utilities.LEFT, .5);
        else robot.drive(Utilities.calculateDirection(gamepad1.left_stick_x, gamepad1.left_stick_y), speed);
    }

    /**
     * Process gamepad2 (codriver) controls
     */
    private void updateCodriver()
    {
        // Move the arm
        robot.elbow.setPower(gamepad2.left_stick_y * -0.5);
        robot.arm.setPower(gamepad2.right_trigger - gamepad2.left_trigger);

        // Update the state of the impeller
        double succ = 0;
        if(gamepad2.a) succ = 1;
        else if(gamepad2.b) succ = -1;
        else if(gamepad2.y) succ = 0;
        robot.impeller.setPower(succ * .75);
    }

    /**
     * Run autonomous processes
     */
    private void updateAuto()
    {
        telemetry.addData("encoders", "" + robot.getDisplacement());
    }
}
