package org.firstinspires.ftc.teamcode.Armstrong;

import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotArmstrong {
    //Declares the motors that control the left and right side of the drivetrian
    public DcMotor left;
    public DcMotor right;
    //Declares the motor that controls the hanging mechanism of the robot
    public DcMotor hanger;
    //Declares the motor that controls the arm of the robot
    public DcMotor arm;

    //Declares the touch sensors that limit the elevator motion
    public TouchSensor lowerTouch;
    public TouchSensor upperTouch;

    public void init(HardwareMap map){
        right = map.dcMotor.get("right");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        left = map.dcMotor.get("left");
        hanger = map.dcMotor.get("hanger");
        hanger.setDirection(DcMotorSimple.Direction.REVERSE);
        arm = map.dcMotor.get("arm");

        lowerTouch = map.touchSensor.get("lowerTouch");
        upperTouch = map.touchSensor.get("upperTouch");
    }

    /*
    Sets the power of the motors to 0, thus stopping the motion of the robot
     */
    public void stop(){
        left.setPower(0);
        right.setPower(0);
        hanger.setPower(0);
        arm.setPower(0);
    }
}
