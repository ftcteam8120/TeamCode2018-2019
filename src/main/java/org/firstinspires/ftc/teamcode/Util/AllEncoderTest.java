package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "CollinsEncoderTest", group = "Collins")
public class AllEncoderTest extends OpMode {

    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;

    ElapsedTime timer;

    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("front_left");
        fr = hardwareMap.dcMotor.get("front_right");
        bl = hardwareMap.dcMotor.get("back_left");
        br = hardwareMap.dcMotor.get("back_right");

        timer = new ElapsedTime();
    }

    @Override
    public void loop() {
        if(timer.seconds() < 3)
        {
            br.setPower(0);
            fl.setPower(1);
        }
        else if(timer.seconds() < 6)
        {
            fl.setPower(0);
            fr.setPower(1);
        }
        else if(timer.seconds() < 9)
        {
            fr.setPower(0);
            bl.setPower(1);
        }
        else if(timer.seconds() < 12)
        {
            bl.setPower(0);
            br.setPower(1);
        }
        else
            timer.reset();

        telemetry.addData("front_left", ""+fl.getCurrentPosition());
        telemetry.addData("front_right", ""+fr.getCurrentPosition());
        telemetry.addData("back_left", ""+bl.getCurrentPosition());
        telemetry.addData("back_right", ""+br.getCurrentPosition());
    }
}
