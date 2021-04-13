package com.itis.iot;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.itis.iot.Constants.*;

public class DriverBH1750FVI {

//    private final Logger LOG = LoggerFactory.getLogger(DriverBH1750FVI.class);

    private final byte i2cAddress;
    private final I2CBus i2cBus;
    private final String i2cName;
    private final String logPrefix;
    private final I2CDevice i2cDevice;

    private final AtomicInteger useCount = new AtomicInteger(0);

    private static final ConcurrentHashMap<String, DriverBH1750FVI> map = new ConcurrentHashMap<>();

    synchronized public static DriverBH1750FVI getInstance(int i2cBusNumber, byte i2cAddress) {
        String key = i2cBusNumber + ":" + String.format("%x", i2cAddress);
        DriverBH1750FVI bh1750fvi = map.get(key);
        if (bh1750fvi == null) {
            bh1750fvi = new DriverBH1750FVI(i2cBusNumber, i2cAddress);
            map.put(key, bh1750fvi);
        }
        return bh1750fvi;
    }

    private DriverBH1750FVI(int i2cBusNumber, byte i2cAddress) {
        if (i2cBusNumber != I2CBus.BUS_0 && i2cBusNumber != I2CBus.BUS_1)
            throw new IllegalArgumentException("The set " + i2cBusNumber + " is not " +
                    I2CBus.BUS_0 + " or " + I2CBus.BUS_1 + ".");
        if (i2cAddress == I2C_ADDRESS_23 || i2cAddress == I2C_ADDRESS_5C)
            this.i2cAddress = i2cAddress;
        else
            throw new IllegalArgumentException("The set " + String.format("%x", i2cAddress) + " is not " +
                    String.format("%x", I2C_ADDRESS_23) + " or " + String.format("%x", I2C_ADDRESS_5C) + ".");

        i2cName = "I2C_" + i2cBusNumber + "_" + String.format("%x", i2cAddress);
        logPrefix = "[" + i2cName + "] ";

        try {
            this.i2cBus = I2CFactory.getInstance(i2cBusNumber);
            this.i2cDevice = i2cBus.getDevice(i2cAddress);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void dump(byte data, String tag) {
//        if (LOG.isTraceEnabled()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(String.format("%02x", data));
//            LOG.trace(logPrefix + "{}{}", tag, stringBuffer.toString());
//        }
    }

    private void dump(byte[] data, String tag) {
//        if (LOG.isTraceEnabled()) {
            StringBuffer sb = new StringBuffer();
            for (byte data1 : data) {
                sb.append(String.format("%02x ", data1));
            }
//            LOG.trace(logPrefix + "{}{}", tag, sb.toString().trim());
//        }
    }

    private void write(byte out) throws IOException {
        try {
            dump(out, "BH1750FVI sensor command: write: ");
            i2cDevice.write(out);
        } catch (IOException e) {
            String message = logPrefix + "failed to write.";
//            LOG.warn(message);
            throw new IOException(message, e);
        }
    }

    private byte[] read(int length) throws IOException {
        try {
            byte[] in = new byte[length];
            i2cDevice.read(in, 0, length);
            dump(in, "BH1750FVI sensor command: read:  ");
            return in;
        } catch (IOException e) {
            String message = logPrefix + "failed to read.";
//            LOG.warn(message);
            throw new IOException(message, e);
        }
    }

    synchronized public void open() {
        try {
//            LOG.debug(logPrefix + "before - useCount:{}", useCount.get());
//            if (useCount.compareAndSet(0, 1))
//                LOG.info(logPrefix + "opened");
        } finally {
//            LOG.debug(logPrefix + "after - useCount:{}", useCount.get());
        }
    }

    synchronized public void close() throws IOException {
        try {
//            LOG.debug(logPrefix + "before - useCount:{}", useCount.get());
            if (useCount.compareAndSet(1, 0)) {
                i2cBus.close();
//                LOG.info(logPrefix + "closed");
            }
        } finally {
//            LOG.debug(logPrefix + "after - useCount:{}", useCount.get());
        }
    }

    public float getOptical() throws IOException {
        write(OPECODE_ONE_TIME_H_RESOLUTION_MODE);

        try {
            Thread.sleep(H_RESOLUTION_MODE_MEASUREMENT_TIME_MILLIS);
        } catch (InterruptedException e) {

        }

        byte[] data = read(SENSOR_DATA_LENGTH);

        return (float) ((((data[0] & 0xff) << 8) + (data[1] & 0xff)) / 1.2);
    }

    public int getI2cBusNumber() {
        return i2cBus.getBusNumber();
    }

    public byte getI2cAddress() {
        return i2cAddress;
    }

    public String getName() {
        return i2cName;
    }

    public String getLogPrefix() {
        return logPrefix;
    }
}
