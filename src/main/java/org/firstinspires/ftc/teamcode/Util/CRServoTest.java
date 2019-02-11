package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;


// OpMode to test (and, more importantly, calibrate) continuous rotation servos
@TeleOp(name = "CRServoTest")
public class CRServoTest extends OpMode {

    CRServo servo;
    int state;
    boolean lastPressed;

    @Override
    public void init() {
        // Initialize servo
        servo = hardwareMap.crservo.get("test");
        // Start stopped
        state = 0;
        lastPressed = false;
    }

    @Override
    public void loop() {

        // Check for Y button toggle
        if(gamepad1.y)
            lastPressed = true;
        else
        {
            if(lastPressed)
            {
                // Increment state, wrap-around if needed
                state++;
                if(state > 2)
                    state = 0;
                lastPressed = false;
            }
        }

        // Different states for stopped, forward and backward
        switch (state)
        {
            case 0:
                servo.setPower(0);
                telemetry.addData("dir", "STOPPED (0)");
                break;
            case 1:
                servo.setPower(1);
                telemetry.addData("dir", "FORWARD (+)");
                break;
            case 2:
                servo.setPower(-1);
                telemetry.addData("dir", "BACKWARD (-)");
                break;
        }
    }
}
