package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.hardware.HardwareMap;

public interface Robot {

    void init(HardwareMap map);

    void stop();
    void setEncoders(boolean enabled);
    void markEncoders();
}
