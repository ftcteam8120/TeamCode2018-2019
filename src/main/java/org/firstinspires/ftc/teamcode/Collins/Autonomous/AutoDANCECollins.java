package org.firstinspires.ftc.teamcode.Collins.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Collins.RobotCollins;
import org.firstinspires.ftc.teamcode.Util.Utilities;

// dance program for... 'outreach'
@Autonomous(name = "AutoDANCECollins", group = "Collins")
public class AutoDANCECollins extends OpMode {

    private static final double SPEED = 0.5;
    private static final int INTERVAL = 1000;

    RobotCollins robot;
    ElapsedTime timer;

    @Override
    public void init() {
        timer = new ElapsedTime();
        robot = new RobotCollins();
        robot.init(hardwareMap);
    }

    @Override
    public void stop() {
        timer.reset();
        state = 0;
    }

    int state;

    @Override
    public void loop() {

        switch (state)
        {
            case 0:
                robot.drive(Utilities.FORWARD, SPEED);
                break;
            case 1:
                robot.drive(Utilities.BACKWARD, SPEED);
                break;
            case 2:
                robot.drive(Utilities.BACKWARD, SPEED);
                break;
            case 3:
                robot.drive(Utilities.FORWARD, SPEED);
                break;
            case 4:
                robot.drive(Utilities.RIGHT, SPEED);
                break;
            case 5:
                robot.drive(Utilities.LEFT, SPEED);
                break;
            case 6:
                robot.drive(Utilities.LEFT, SPEED);
                break;
            case 7:
                robot.drive(Utilities.RIGHT, SPEED);
                break;
            case 8:
                robot.drive(Utilities.ROTATE_CCW, SPEED);
                break;
        }

        if(timer.milliseconds() > INTERVAL)
        {
            state++;
            if(state > 8)
                state = 0;
            timer.reset();
        }
    }
}
