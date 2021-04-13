package com.itis.iot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.itis.iot.Constants.I2C_ADDRESS_23;

public class Controller {

//    private static final Logger LOG;
    private static final Controller controller;

    private final GpioController gpio;
    private final DriverKY009 ky009;
    private final DriverKY040 ky040;
    private final DriverBH1750FVI bh1750fvi;

    static {
        controller = new Controller();
//        LOG = LoggerFactory.getLogger(Controller.class);
    }

    synchronized public static Controller getInstance() {
        return controller;
    }

    private Controller() {
        gpio = GpioFactory.getInstance();
        ky009 = DriverKY009.getInstance(RaspiPin.GPIO_00,
                RaspiPin.GPIO_02, RaspiPin.GPIO_03, gpio);
        ky040 = DriverKY040.getInstance(RaspiPin.GPIO_01,
                RaspiPin.GPIO_04, RaspiPin.GPIO_05, gpio, getInputListener());
        bh1750fvi = DriverBH1750FVI.getInstance(I2CBus.BUS_1, I2C_ADDRESS_23);
        bh1750fvi.open();
    }

    private GpioPinListenerDigital getInputListener() {
        return (GpioPinDigitalStateChangeEvent event) ->
//                LOG.info(String.format("%-15s%-15s", event.getPin(), event.getState()));
        System.out.printf("%-30s%-30s%n", event.getPin(), event.getState());
    }

    public float getIllumination() {
        float value = 0;
        try {
            value = bh1750fvi.getOptical();
        } catch (IOException exception) {
//            LOG.warn("caught - {}", exception.toString());
            System.err.println(exception.toString());
        }
//        LOG.info("optical:" + value);
        System.out.println("optical:" + value);
        return value;
    }

    public void shutdown() {
        try {
            bh1750fvi.close();
        } catch (IOException exception) {
//            LOG.warn("caught - {}", exception.toString());
            System.err.println(exception.toString());
        } finally {
            gpio.shutdown();
        }
    }
}
