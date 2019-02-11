package org.firstinspires.ftc.teamcode.Armstrong.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Armstrong.RobotArmstrong;

@Disabled
@TeleOp(name = "ArmstrongDrive", group = "Armstrong")
public class ArmstrongDrive extends OpMode {

    private RobotArmstrong robot;

    //position of the left gamepad1 stick
    double lVal;
    //position of the right gamepad1 stick
    double rVal;

    //position of the left gamepad2 stick
    double armVal;

    //impeller state variable
    int succ;

    /*
    Establishes the robot and initializes the robot using the hardwareMap
     */
    public void init() {

        robot = new RobotArmstrong();
        robot.init(hardwareMap);
    }

    /*
    Continuously updates the status of the Driver and CoDriver using the gamepads
     */
    @Override
    public void loop() {
        robot.setEncoders(true);
        updateDriver(gamepad1);
        updateCoDriver(gamepad2);
        updateAuto();
        printStats();
    }

    final static double maxPower = 0.8;

    private void updateDriver(Gamepad gamepad) {
        //Check drive train controls and updates accordingly
        lVal = -gamepad.left_stick_y;
        rVal = -gamepad.right_stick_y;

        //Sets the power and moves the robot according to the current stick positions / bumpers
        if(gamepad.left_bumper)
        {
            robot.right.setPower(.3);
            robot.left.setPower(-.3);
        }
        else if(gamepad.right_bumper)
        {
            robot.right.setPower(-.3);
            robot.left.setPower(.3);
        }
        else
        {
            robot.left.setPower(lVal * maxPower);
            robot.right.setPower(rVal * maxPower);
        }

        double hangerPower = gamepad.right_trigger - gamepad.left_trigger;
        if(hangerPower > 0  && !robot.upperTouch.isPressed())
            robot.hanger.setPower(hangerPower);
        else if(hangerPower < 0 && !robot.lowerTouch.isPressed())
            robot.hanger.setPower(hangerPower);
        else
            robot.hanger.setPower(0);

        if(gamepad.x)
            robot.markEncoders();

    }

    private void updateCoDriver(Gamepad gamepad) {
        robot.elbow.setPower(gamepad.left_stick_y * -0.5);
        robot.arm.setPower(gamepad.right_trigger - gamepad.left_trigger);

        if(gamepad.a) succ = 1;
        else if(gamepad.b) succ = -1;
        else if(gamepad.y) succ = 0;
        robot.impeller.setPower(succ * .75);
    }

    private void updateAuto() {}

    private void printStats()
    {
        if(robot != null)
        {
            telemetry.addData("touch", (robot.upperTouch.isPressed() ? "U: pressed" : "U: unpressed") + (robot.lowerTouch.isPressed() ? "; L: pressed" : "; L: unpressed"));
            telemetry.addData("encoders", (robot.left.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) ? "left: " + robot.getLeftDisplacement() + "; right: " + robot.getRightDisplacement() : "off");
            Orientation orientation = robot.imu.getAngularOrientation();
            telemetry.addData("imu", "1: " + orientation.firstAngle + "; 2: " + orientation.secondAngle + "; 3: " + orientation.thirdAngle);
        }
    }

}
