package com.itis.iot;

public class Constants {

    public static final byte I2C_ADDRESS_23 = 0x23;
    public static final byte I2C_ADDRESS_5C = 0x5c;

    public static final byte OPECODE_POWER_DOWN = 0x00;
    public static final byte OPECODE_POWER_ON = 0x01;
    public static final byte OPECODE_RESET = 0x07;

    public static final byte OPECODE_CONTINUOUSLY_H_RESOLUTION_MODE = 0x10;
    public static final byte OPECODE_CONTINUOUSLY_H_RESOLUTION_MODE2 = 0x11;
    private static final byte OPECODE_CONTINUOUSLY_L_RESOLUTION_MODE = 0x13;

    public static final byte OPECODE_ONE_TIME_H_RESOLUTION_MODE = 0x20;
    public static final byte OPECODE_ONE_TIME_H_RESOLUTION_MODE2 = 0x21;
    public static final byte OPECODE_ONE_TIME_L_RESOLUTION_MODE = 0x23;

    public static final int H_RESOLUTION_MODE_MEASUREMENT_TIME_MILLIS = 120;
    public static final int H_RESOLUTION_MODE2_MEASUREMENT_TIME_MILLIS = 120;
    public static final int L_RESOLUTION_MODE_MEASUREMENT_TIME_MILLIS = 16;

    public static final int SENSOR_DATA_LENGTH = 2;
}
