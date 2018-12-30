package org.firstinspires.ftc.teamcode.Aldrin;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotAldrin {
    //motors that control the left and right side of the drivetrian
    public DcMotor left;
    public DcMotor right;

    //motor that controls the impeller of the robot
    public DcMotor impeller;

    //encoder marks
    private int encoderL;
    private int encoderR;

    public void init(HardwareMap map){
        right = map.dcMotor.get("right");
        left = map.dcMotor.get("left");
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        impeller = map.dcMotor.get("impeller");

        setEncoders(true);
        markEncoders();
    }

    /**
     * Sets the power of the motors to 0, thus stopping the motion of the robot
     */
    public void stop(){
        left.setPower(0);
        right.setPower(0);
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

    public void updateTelemetry(Telemetry t)
    {
        t.addData("encoders", "L: " + (left.getMode() == DcMotor.RunMode.RUN_USING_ENCODER ? getLeftDisplacement() : "off") + "; R: " + (right.getMode() == DcMotor.RunMode.RUN_USING_ENCODER ? getRightDisplacement() : "off"));
    }
}
