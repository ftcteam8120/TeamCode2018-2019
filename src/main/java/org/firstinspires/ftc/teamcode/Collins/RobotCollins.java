package org.firstinspires.ftc.teamcode.Collins;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldDetector;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.Util.Utilities;
import org.firstinspires.ftc.teamcode.Util.Robot;

public class RobotCollins implements Robot {

    // Drive train
    public DriveTrain driveTrain;

    // Elevator components
    public DcMotor hanger;
    public TouchSensor lowerTouch;
    public TouchSensor upperTouch;

    // Arm components
    public DcMotor flipper;
    public DcMotor lifter;
    public DcMotor extender;
    public TouchSensor extendTouch;

    // Impeller Servo
    public CRServo impeller;

    // IMU
    public BNO055IMU imu;

    private double lastAngle;

    // DogeCV detector
    public GoldDetector detector;

    @Override
    public void init(HardwareMap map) {

        // Initialize & configure drive train
        driveTrain = new DriveTrain();
        driveTrain.init(map);

        setEncoders(false);

        // Initialize & configure hanger components
        hanger = map.dcMotor.get("hanger");
        hanger.setDirection(DcMotorSimple.Direction.REVERSE);
        lowerTouch = map.touchSensor.get("lower_touch");
        upperTouch = map.touchSensor.get("upper_touch");

        impeller = map.crservo.get("impeller");
        //knocker = map.crservo.get("knocker");

        // Initialize & configure mineral components
        flipper = map.dcMotor.get("flipper");
        lifter = map.dcMotor.get("lifter");
        extender = map.dcMotor.get("extender");
        extendTouch = map.touchSensor.get("extend_touch");

        // Initialize & configure IMU
        imu = map.get(BNO055IMU.class, "imu");
        imu.initialize(Utilities.getGyroParams());
        lastAngle = imu.getAngularOrientation().firstAngle;

        markEncoders();

        // initialize & configure DogeCV sampling order detector
        detector = new GoldDetector();
        detector.init(map.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();
        detector.downscale = 0.4; // How much to downscale the input frames
        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.001;
        detector.ratioScorer.weight = 15;
        detector.ratioScorer.perfectRatio = 1.0;

        // this is important
        stop();
    }

    /**
     * Stop the robot
     */

    @Override
    public void stop() {
        driveTrain.drive(Utilities.STOPPED, 0f);
        hanger.setPower(0);
        impeller.setPower(0);
        flipper.setPower(0);
        extender.setPower(0);
        lifter.setPower(0);
    }

    /**
     * Enable/disable encoders
     * @param enabled
     */
    @Override
    public void setEncoders(boolean enabled)
    {
        driveTrain.setEncoders(enabled);
    }

    /**
     * Passthrough to drive train drive function
     * @param dir preset direction array
     * @param pow power scale
     */
    public void drive(double[] dir, double pow)
    {
        driveTrain.drive(dir, pow);
    }

    /**
     * Passthrough to drive train displacement function
     */
    public int getDisplacement()
    {
        return driveTrain.getDisplacement();
    }

    /**
     * Zero the software encoder positions
     */
    @Override
    public void markEncoders() {
        driveTrain.markEncoders();
        lastAngle = imu.getAngularOrientation().firstAngle;
    }

    public double getAngDisplacement() {
        return Math.abs(imu.getAngularOrientation().firstAngle - lastAngle);
    }
}
