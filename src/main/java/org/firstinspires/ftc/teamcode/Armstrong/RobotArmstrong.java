package org.firstinspires.ftc.teamcode.Armstrong;

import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotArmstrong {
    //Declares the motor that controls the left side of the drivetrian
    public DcMotor left;
    //Declares the motor that controls the right side of the drivetrian
    public DcMotor right;
    //Declares the motor that controls the hanging mechanism of the robot
    public DcMotor hanger;
    //Declares the motor that controls the arm of the robot
    public DcMotor armRotate;

    public void init(HardwareMap map){
        right = map.dcMotor.get("right");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        left = map.dcMotor.get("left");
        hanger = map.dcMotor.get("hanger");
        armRotate = map.dcMotor.get("armRotate");
    }
    /*
    Sets the power of the motors to 0, thus stopping the motion of the robot
     */
    public void stop(){
        left.setPower(0);
        right.setPower(0);
        hanger.setPower(0);
        armRotate.setPower(0);
    }
}
