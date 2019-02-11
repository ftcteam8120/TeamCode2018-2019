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
        testMotor.setPower(gamepad1.a ? .5 : (gamepad1.b ? -.5 : 0));
        telemetry.addData("count", testMotor.getCurrentPosition());
    }
}
