package org.firstinspires.ftc.teamcode.Collins.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Collins.RobotCollins;
import org.firstinspires.ftc.teamcode.Util.Utilities;

@TeleOp(name = "CollinsDrive", group = "Collins")
public class CollinsDrive extends OpMode {

    private RobotCollins robot;
    private double speed;
    private ElapsedTime timer;

    @Override
    public void init() {
        robot = new RobotCollins();
        robot.init(hardwareMap);
        speed = 1;
        timer = new ElapsedTime();
    }

    @Override
    public void start() {
        timer.reset();
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
        // Reset encoder readings
        if(gamepad1.x)
            robot.markEncoders();

        // Set drive speed
        speed = .5;
        if(gamepad1.left_bumper)
            speed -= .25;
        if(gamepad1.right_bumper)
            speed += .25;

        // Move hanger up/down with touch sensor limits
        double hangerPower = gamepad1.right_trigger - gamepad1.left_trigger;
        if(hangerPower > 0  && !robot.upperTouch.isPressed())
            robot.hanger.setPower(hangerPower);
        else if(hangerPower < 0 && !robot.lowerTouch.isPressed())
            robot.hanger.setPower(hangerPower);
        else
            robot.hanger.setPower(0);

        // Check for rotation first, then calculate movement direction
        if(gamepad1.right_stick_x < -.1) robot.drive(Utilities.ROTATE_CCW, speed);
        else if(gamepad1.right_stick_x > .1) robot.drive(Utilities.ROTATE_CW, speed);
        else if(gamepad1.dpad_up) robot.drive(Utilities.FORWARD, .5 * speed);
        else if(gamepad1.dpad_right) robot.drive(Utilities.RIGHT, .5 * speed);
        else if(gamepad1.dpad_down) robot.drive(Utilities.BACKWARD, .5 * speed);
        else if(gamepad1.dpad_left) robot.drive(Utilities.LEFT, .5 * speed);
        else robot.drive(Utilities.calculateDirection(gamepad1.left_stick_x, -gamepad1.left_stick_y), speed);
    }

    double succ = 0;
    /**
     * Process gamepad2 (codriver) controls
     */
    private void updateCodriver()
    {
        // Arm controls
        /* OLD
        robot.elbow.setPower(gamepad2.left_stick_y * -1);
        robot.arm.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
        */
        robot.extender.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
        robot.flipper.setPower(gamepad2.left_stick_y * -0.5);
        robot.lifter.setPower(gamepad2.right_stick_y * 0.5);

        // Update the state of the impeller
        if(gamepad2.a) succ = 1;
        else if(gamepad2.b) succ = -1;
        else if(gamepad2.y) succ = 0;
        robot.impeller.setPower(succ);
    }

    /**
     * Run autonomous processes
     * - print telemetry data
     */
    private void updateAuto()
    {
        //telemetry.addData("time", timer.milliseconds() + " ms since start");
        telemetry.addData("angle", robot.getAngDisplacement());
        telemetry.addData("encoders", "" + robot.driveTrain.getDisplacement());
        telemetry.addData("touch", (robot.upperTouch.isPressed() ? "U:pressed;" : "U:unpressed;") + (robot.lowerTouch.isPressed() ? "L:pressed;" : "L:unpressed;"));
    }
}
