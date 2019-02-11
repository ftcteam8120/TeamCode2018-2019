package org.firstinspires.ftc.teamcode.Collins;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveTrain {

    // Drive motors
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    public void init(HardwareMap map)
    {
        // Initialize & configure drive motors
        frontLeft = map.dcMotor.get("front_left");
        frontRight = map.dcMotor.get("front_right");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft = map.dcMotor.get("back_left");
        backRight = map.dcMotor.get("back_right");
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /**
     * Enable/disable encoders
     * @param enabled
     */
    public void setEncoders(boolean enabled) {
        if(enabled)
        {
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else
        {
            frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    // Marked encoder positions
    private int flet;
    private int fret;
    private int blet;
    private int bret;

    /**
     * Mark encoder positions
     */
    public void markEncoders()
    {
        flet = frontLeft.getCurrentPosition();
        fret = frontRight.getCurrentPosition();
        blet = backLeft.getCurrentPosition();
        bret = backRight.getCurrentPosition();
    }

    /**
     * Get average encoder tick difference for the entire robot
     * @return average motor displacement
     */
    public int getDisplacement() {
        int fldiff = frontLeft.getCurrentPosition() - flet;
        int frdiff = frontRight.getCurrentPosition() - fret;
        int bldiff = backLeft.getCurrentPosition() - blet;
        int brdiff = backRight.getCurrentPosition() - bret;

        return (Math.abs(fldiff) + Math.abs(frdiff) + Math.abs(bldiff) + Math.abs(brdiff)) / 4;
    }

    /**
     * Set motor powers according to preset direction at a certain speed
     * @param direction preset direction (array of motor powers defined in Utilities class)
     * @param speed factor that multiplies into motor powers to scale robot speed
     */
    public void drive(double[] direction, double speed) {
        frontLeft.setPower(direction[0]*speed);
        frontRight.setPower(direction[1]*speed);
        backLeft.setPower(direction[2]*speed);
        backRight.setPower(direction[3]*speed);
    }
}
