package org.firstinspires.ftc.teamcode.Armstrong.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Armstrong.RobotArmstrong;
import org.firstinspires.ftc.teamcode.Armstrong.RobotState;

@Autonomous(name = "BasicAutonomous", group = "Armstrong")
public class BasicAutonomous extends OpMode {

    private RobotState state = RobotState.START;

    private RobotArmstrong robot;

    private final static double elevatorSpeed = 0.6;

    /**
     * Goes to a specific state
     * @param next The next state
     */
    private void toState(RobotState next) {
        this.state = next;
    }

    /*
     * Instantiates the robot and initializes the robot using the hardwareMap
     */
    public void init() {
        robot = new RobotArmstrong();
        robot.init(hardwareMap);
    }

    /*
     * Continuously updates the robot
     */
    @Override
    public void loop() {
        // The main state machine
        switch (this.state) {
            case START: {
                // Stop the robot
                this.robot.stop();
                toState(RobotState.LOWER);
                break;
            }
            case LOWER: {
                // Begin lowering the robot from the lander
                this.robot.hanger.setPower(elevatorSpeed);
                // Wait until the upper touch is pressed
                if (this.robot.upperTouch.isPressed()) toState(RobotState.UNHOOK);
                break;
            }
            default: {
                // Stop the robot
                this.robot.stop();
            }
        }
        printStats();
    }

    private void printStats() {
        if(robot != null) {
            telemetry.addData("upper", robot.upperTouch.isPressed() ? "pressed" : "unpressed");
            telemetry.addData("lower", robot.lowerTouch.isPressed() ? "pressed" : "unpressed");
        }
    }
}
