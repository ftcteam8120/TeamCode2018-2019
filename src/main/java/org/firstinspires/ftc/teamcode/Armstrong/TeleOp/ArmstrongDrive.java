package org.firstinspires.ftc.teamcode.Armstrong.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Armstrong.RobotArmstrong;

@TeleOp(name = "ArmstrongDrive", group = "Armstrong")
public class ArmstrongDrive extends OpMode {

    private RobotArmstrong robot;

    //position of the left gamepad1 stick
    double lVal;
    //position of the right gamepad1 stick
    double rVal;
    //position of the left gamepad2 stick
    double armVal;

    //elevator state variables
    boolean goingUp;
    boolean goingDown;

    /*
    Establishes the robot and initializes the robot using the hardwareMap
     */
    public void init() {

        goingDown = false;
        goingUp = false;

        robot = new RobotArmstrong();
        robot.init(hardwareMap);
    }

    /*
    Continuously updates the status of the Driver and CoDriver using the gamepads
     */
    @Override
    public void loop() {
        updateDriver(gamepad1);
        updateCoDriver(gamepad2);
        updateAuto();
        printStats();
    }

    final static double maxPower = 0.8;
    final static double elevatorSpeed = 0.6;

    private void updateDriver(Gamepad gamepad) {
        //Check drive train controls and updates accordingly
        lVal = gamepad.left_stick_y;
        rVal = gamepad.right_stick_y;

        //Sets the power and moves the robot according to the current stick positions
        robot.left.setPower(lVal * maxPower);
        robot.right.setPower(rVal * maxPower);

        //Moves the hanging mechanism up and dowm depending on the trigger positions
        if(gamepad.right_trigger > 0 && gamepad.left_trigger == 0)
        {
            goingUp = false;
            goingDown = true;
        }
        else if(gamepad.right_trigger == 0 && gamepad.left_trigger > 0)
        {
            goingUp = true;
            goingDown = false;
        }

    }

    private void updateCoDriver(Gamepad gamepad) {
        //Checks gamepad controls and updates accordingly
        armVal = gamepad.left_stick_y;

        //Moves robot arm according to the left sticks position
        robot.arm.setPower(armVal * 0.5);
    }

    private void updateAuto() {
        if(goingUp)
        {
            if(robot.upperTouch.isPressed())
            {
                goingUp = false;
                goingDown = false;
                robot.hanger.setPower(0);
            }
            else
                robot.hanger.setPower(elevatorSpeed);
        }
        else if(goingDown)
        {
            if(robot.lowerTouch.isPressed())
            {
                goingUp = false;
                goingDown = false;
                robot.hanger.setPower(0);
            }
            else
                robot.hanger.setPower(-elevatorSpeed);
        }
        else
            robot.hanger.setPower(0);
    }

    public void printStats()
    {
        if(robot != null)
        {
            telemetry.addData("upper", robot.upperTouch.isPressed() ? "pressed" : "unpressed");
            telemetry.addData("lower", robot.lowerTouch.isPressed() ? "pressed" : "unpressed");
        }
    }

}
