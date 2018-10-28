package org.firstinspires.ftc.teamcode.Armstrong.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Armstrong.RobotArmstrong;

@TeleOp(name = "ArmstrongDrive", group = "Armstrong")
public class ArmstrongDrive extends OpMode {

    RobotArmstrong robot;

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
        updateDriver(gamepad1);
        updateCoDriver(gamepad2);
    }

    final double maxPower = 0.8;

    //Variable for the position of the left gamepad stick
    double lVal;
    //Variable for the position of the right gamepad stick
    double rVal;

    public void updateDriver(Gamepad gamepad) {
        //Check drive train controls and updates accordingly
        lVal = gamepad.left_stick_y;
        rVal = gamepad.right_stick_y;

        //Sets the power and moves the robot according to the current stick positions
        robot.left.setPower(lVal * maxPower);
        robot.right.setPower(rVal * maxPower);

        //Moves the hanging mechanism up and dowm depending on the trigger positions
        if(gamepad.right_trigger > 0 && gamepad.left_trigger == 0)
            robot.hanger.setPower(gamepad.right_trigger);
        else if(gamepad.right_trigger == 0 && gamepad.left_trigger > 0)
            robot.hanger.setPower(-gamepad.left_trigger);
        else
            robot.hanger.setPower(0);

    }

    /*
    Extends and Retracts arm on the robot depending on the stick placement using the second controller
     */
    public void updateCoDriver(Gamepad gamepad) {
        //Checks gamepad controls and updates accordingly
        lVal = gamepad.left_stick_y;

        //Moves robot arm according to the left sticks position
        robot.armRotate.setPower(lVal * maxPower);
    }

}
