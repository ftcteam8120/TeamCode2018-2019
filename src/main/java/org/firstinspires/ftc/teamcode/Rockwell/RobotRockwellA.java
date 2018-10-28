package org.firstinspires.ftc.teamcode.Rockwell;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class RobotRockwellA {

    public DcMotor left;
    public DcMotor right;
    public DcMotor arm;

    public Servo clawL;
    public Servo clawR;

    public void init(HardwareMap hw)
    {
        left = hw.dcMotor.get("left");
        right = hw.dcMotor.get("right");
        arm = hw.dcMotor.get("arm");
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        clawL = hw.servo.get("clawL");
        clawR = hw.servo.get("clawR");

        open();
    }

    public void close()
    {
        clawL.setPosition(0);
        clawR.setPosition(1);
    }

    public void open()
    {
        clawL.setPosition(1);
        clawR.setPosition(0);
    }
}
