package org.firstinspires.ftc.teamcode.Util;

import android.graphics.Camera;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldDetector;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Collins.RobotCollins;

@Autonomous(name = "SamplingTest")
public class SamplingTest extends OpMode {

    GoldDetector detector;

    @Override
    public void init() {
        // initialize, configure, & enable DogeCV sampling order detector
        detector = new GoldDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        detector.useDefaults();
        detector.downscale = 0.4; // How much to downscale the input frames
        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.001;
        detector.ratioScorer.weight = 15;
        detector.ratioScorer.perfectRatio = 1.0;
    }

    @Override
    public void start()
    {
        detector.enable();
    }

    @Override
    public void loop() {
        telemetry.addData("gold sample", (detector.isFound() ? ("found @ " + detector.getScreenPosition()) : "not found"));
    }

    @Override
    public void stop()
    {
        detector.disable();
    }
}
