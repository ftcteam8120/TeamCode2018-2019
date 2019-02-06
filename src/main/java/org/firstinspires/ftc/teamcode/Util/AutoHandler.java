package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.Util.RobotState.*;

public abstract class AutoHandler {

    public Robot r;
    public ElapsedTime timer;
    public RobotState state;
    public boolean blue; // are we on blue side?
    public boolean facingCrater; // are we facing the crater?

    public AutoHandler(Robot r, ElapsedTime et, boolean b, boolean c)
    {
        this.r = r;
        timer = et;
        blue = b;
        facingCrater = c;
        toState(START);
    }

    /**
     * goes to a specific state, marks the encoders, and resets the clock
     * @param next the next state
     */
    public void toState(RobotState next) {
        r.stop();
        r.markEncoders();
        this.state = next;
        timer.reset();
    }

    /**
     * prints debugging information to the phone screen
     * @param telemetry the telemetry output
     */
    public void printStats(Telemetry telemetry)
    {
        telemetry.addData("state", state.name());
    }

    public abstract void tick();
}
