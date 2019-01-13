package org.firstinspires.ftc.teamcode.Armstrong;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.Util.Utilities;
import org.firstinspires.ftc.teamcode.Util.Robot;

public class RobotArmstrong implements Robot {

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

    //encoder marks
    private int encoderL;
    private int encoderR;

    //touch sensors that limit the elevator motion
    public TouchSensor lowerTouch;
    public TouchSensor upperTouch;

    //IMU
    public BNO055IMU imu;

    //Dogecv Detector
    public SamplingOrderDetector detector;

    public void init(HardwareMap map){

        // map and configure motors
        right = map.dcMotor.get("right");
        left = map.dcMotor.get("left");
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        hanger = map.dcMotor.get("hanger");
        hanger.setDirection(DcMotorSimple.Direction.REVERSE);
        elbow = map.dcMotor.get("elbow");
        arm = map.dcMotor.get("arm");
        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        impeller = map.dcMotor.get("impeller");

        // map and configure sensors
        lowerTouch = map.touchSensor.get("lowerTouch");
        upperTouch = map.touchSensor.get("upperTouch");
        imu = map.get(BNO055IMU.class, "imu");
        imu.initialize(Utilities.getGyroParams());

        // optionally initialize encoders
        setEncoders(true);
        markEncoders();

        // initialize, configure, and enable sampling order detector
        detector = new SamplingOrderDetector();
        detector.init(map.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();
        detector.downscale = 0.4; // How much to downscale the input frames
        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.001;
        detector.ratioScorer.weight = 15;
        detector.ratioScorer.perfectRatio = 1.0;
        detector.enable();
    }

    /**
     * Sets the power of the motors to 0, thus stopping the motion of the robot
     */
    public void stop(){
        left.setPower(0);
        right.setPower(0);
        hanger.setPower(0);
        arm.setPower(0);
        impeller.setPower(0);
        elbow.setPower(0);
    }

    /**
     * enables or disables encoders
     * @param enabled
     */
    public void setEncoders(boolean enabled)
    {
        right.setMode(enabled ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setMode(enabled ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * zeros the encoder counts
     */
    public void markEncoders()
    {
        encoderL = left.getCurrentPosition();
        encoderR = right.getCurrentPosition();
    }

    /**
     * calculates how far the motor has strayed from the zero position
     * @return displacement from marked location
     */
    public int getLeftDisplacement()
    {
        return Math.abs(left.getCurrentPosition() - encoderL);
    }
    public int getRightDisplacement()
    {
        return Math.abs(right.getCurrentPosition() - encoderR);
    }
}
