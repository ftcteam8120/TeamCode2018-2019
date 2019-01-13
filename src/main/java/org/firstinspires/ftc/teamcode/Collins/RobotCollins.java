package org.firstinspires.ftc.teamcode.Collins;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.Util.Utilities;
import org.firstinspires.ftc.teamcode.Util.Robot;

public class RobotCollins implements Robot {

    // Drive motors
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    // Elevator components
    public DcMotor hanger;
    public TouchSensor lowerTouch;
    public TouchSensor upperTouch;

    // Arm components
    public DcMotor elbow;
    public DcMotor arm;
    public DcMotor impeller;

    // IMU
    public BNO055IMU imu;

    // DogeCV detector
    public SamplingOrderDetector detector;

    @Override
    public void init(HardwareMap map) {

        // Initialize & configure drive motors
        frontLeft = map.dcMotor.get("front_left");
        frontRight = map.dcMotor.get("front_right");
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft = map.dcMotor.get("back_left");
        backRight = map.dcMotor.get("back_right");
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        setEncoders(true);
        markEncoders();

        // Initialize & configure elevator components
        hanger = map.dcMotor.get("hanger");
        hanger.setDirection(DcMotorSimple.Direction.REVERSE);
        lowerTouch = map.touchSensor.get("lower_touch");
        upperTouch = map.touchSensor.get("upper_touch");

        // Initialize & configure arm components
        elbow = map.dcMotor.get("elbow");
        arm = map.dcMotor.get("arm");
        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        impeller = map.dcMotor.get("impeller");

        // Initialize & configure IMU
        imu = map.get(BNO055IMU.class, "imu");
        imu.initialize(Utilities.getGyroParams());

        // initialize & configure DogeCV sampling order detector
        detector = new SamplingOrderDetector();
        detector.init(map.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();
        detector.downscale = 0.4; // How much to downscale the input frames
        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.001;
        detector.ratioScorer.weight = 15;
        detector.ratioScorer.perfectRatio = 1.0;
    }

    /**
     * Stop the robot
     */
    @Override
    public void stop() {
        drive(Utilities.STOPPED, 0f);
    }

    /**
     * Enable/disable encoders
     * @param enabled
     */
    @Override
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

    private int flet;
    private int fret;
    private int blet;
    private int bret;

    /**
     * Zero the software encoder positions
     */
    @Override
    public void markEncoders() {
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
        backRight.setPower(direction[2]*speed);
    }
}
