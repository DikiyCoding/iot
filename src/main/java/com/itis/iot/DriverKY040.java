package com.itis.iot;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public class DriverKY040 {

    private final GpioPinDigitalInput ky040encoderSwitch;
    private final GpioPinDigitalInput ky040encoderClock;
    private final GpioPinDigitalInput ky040encoderDirection;

    private static final ConcurrentHashMap<String, DriverKY040> map = new ConcurrentHashMap<>();

    synchronized public static DriverKY040 getInstance(@NotNull Pin pinSwitch,
                                                       @NotNull Pin pinClock,
                                                       @NotNull Pin pinDirection,
                                                       @NotNull GpioController controller,
                                                       @NotNull GpioPinListenerDigital listener) {
        String key = pinSwitch.getName() + "_" + pinClock.getName() + "_" + pinDirection.getName();
        DriverKY040 ky040 = map.get(key);
        if (ky040 == null) {
            ky040 = new DriverKY040(pinSwitch, pinClock, pinDirection, controller, listener);
            map.put(key, ky040);
        }
        return ky040;
    }

    private DriverKY040(Pin pinSwitch, Pin pinClock, Pin pinDirection,
                        GpioController controller, GpioPinListenerDigital listener) {
        ky040encoderSwitch = controller.provisionDigitalInputPin(pinSwitch,
                "KY-040-switch", PinPullResistance.PULL_DOWN);
        ky040encoderSwitch.setShutdownOptions(true);
        ky040encoderSwitch.addListener(listener);

        ky040encoderClock = controller.provisionDigitalInputPin(pinClock,
                "KY-040-clock", PinPullResistance.PULL_DOWN);
        ky040encoderClock.setShutdownOptions(true);
        ky040encoderClock.addListener(listener);

        ky040encoderDirection = controller.provisionDigitalInputPin(pinDirection,
                "KY-009-direction", PinPullResistance.PULL_DOWN);
        ky040encoderDirection.setShutdownOptions(true);
        ky040encoderDirection.addListener(listener);
    }
}
