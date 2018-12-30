package org.firstinspires.ftc.teamcode.Armstrong;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class RobotArmstrong {
    //motors that control the left and right side of the drivetrian
    public DcMotor left;
    public DcMotor right;
    //motor that controls the hanging mechanism of the robot
    public DcMotor hanger;
    //motor that controls the elbow of the robot
    public DcMotor elbow;
    //motor that controls the arm of the robot
    public DcMotor arm;

    //motor that controls the impeller of the robot
    public DcMotor impeller;

    //color sensors
    public ColorSensor colorL;
    public ColorSensor colorR;

    //encoder marks
    private int encoderL;
    private int encoderR;


    //touch sensors that limit the elevator motion
    public TouchSensor lowerTouch;
    public TouchSensor upperTouch;

    public void init(HardwareMap map){
        right = map.dcMotor.get("right");
        left = map.dcMotor.get("left");
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        hanger = map.dcMotor.get("hanger");
        hanger.setDirection(DcMotorSimple.Direction.REVERSE);
        elbow = map.dcMotor.get("elbow");
        arm = map.dcMotor.get("arm");
        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        impeller = map.dcMotor.get("impeller");

        lowerTouch = map.touchSensor.get("lowerTouch");
        upperTouch = map.touchSensor.get("upperTouch");

        colorL = map.colorSensor.get("colorL");
        colorR = map.colorSensor.get("colorR");

        setEncoders(true);
        markEncoders();
    }

    /**
     * Sets the power of the motors to 0, thus stopping the motion of the robot
     */
    public void stop(){
        left.setPower(0);
        right.setPower(0);
        hanger.setPower(0);
        arm.setPower(0);
    }

    public void setEncoders(boolean enabled)
    {
        right.setMode(enabled ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setMode(enabled ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void markEncoders()
    {
        encoderL = left.getCurrentPosition();
        encoderR = right.getCurrentPosition();
    }

    public int getLeftDisplacement()
    {
        return Math.abs(left.getCurrentPosition() - encoderL);
    }

    public int getRightDisplacement()
    {
        return Math.abs(right.getCurrentPosition() - encoderR);
    }
}
