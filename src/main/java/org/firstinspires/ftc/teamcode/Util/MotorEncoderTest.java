package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "MotorEncoderTest")
public class MotorEncoderTest extends OpMode {

    DcMotor testMotor;

    @Override
    public void init() {
        testMotor = hardwareMap.dcMotor.get("test");
        testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        testMotor.setPower(1);
        telemetry.addData("count", testMotor.getCurrentPosition());
    }
}
