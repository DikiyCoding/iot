package com.itis.iot;

import com.pi4j.io.gpio.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public class DriverKY009 {

    private final GpioPinDigitalOutput ky009ledRed;
    private final GpioPinDigitalOutput ky009ledGreen;
    private final GpioPinDigitalOutput ky009ledBlue;

    private static final ConcurrentHashMap<String, DriverKY009> map = new ConcurrentHashMap<>();

    synchronized public static DriverKY009 getInstance(Pin pinRed, Pin pinGreen, Pin pinBlue,
                                                           @NotNull GpioController controller) {
        String key = pinRed.getName() + "_" + pinGreen.getName() + "_" + pinBlue.getName();
        DriverKY009 ky009 = map.get(key);
        if (ky009 == null) {
            ky009 = new DriverKY009(pinRed, pinGreen, pinBlue, controller);
            map.put(key, ky009);
        }
        return ky009;
    }

    private DriverKY009(Pin pinRed, Pin pinGreen, Pin pinBlue, @NotNull GpioController controller) {
        ky009ledRed = controller.provisionDigitalOutputPin(pinRed, "KY-009-red", PinState.HIGH);
        ky009ledRed.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        ky009ledGreen = controller.provisionDigitalOutputPin(pinGreen, "KY-009-green", PinState.HIGH);
        ky009ledGreen.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        ky009ledBlue = controller.provisionDigitalOutputPin(pinBlue, "KY-009-blue", PinState.HIGH);
        ky009ledBlue.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
    }
}
