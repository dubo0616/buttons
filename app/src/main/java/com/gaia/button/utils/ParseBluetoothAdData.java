package com.gaia.button.utils;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public final class ParseBluetoothAdData {
    public static final ParseBluetoothAdData INSTANCE;

    @NotNull
    public final ParseBluetoothAdData.AdData parse(@NotNull byte[] bytes) {
        String baseUuid = "%08X-0000-1000-8000-00805F9B34FB";
        ArrayList uuidS = new ArrayList();
        byte[] manufacturerByte = (byte[])null;
        String name = (String)null;
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        label36:
        while(buffer.remaining() > 2) {
            int length = buffer.get() & 255;
            if (length == 0) {
                break;
            }

            int type = buffer.get() & 255;
            --length;
            String var14;
            String var10000;
            Object[] var10001;
            Object[] var17;
            boolean var18;
            switch(type) {
                case 1:
                    buffer.position(buffer.position() + length);
                    break;
                case 2:
                case 3:
                case 20:
                    while(true) {
                        if (length < 2) {
                            continue label36;
                        }

                        var10001 = new Object[1];
                        var10001[0] = buffer.getShort();
                        var17 = var10001;
                        var18 = false;
                        var10000 = String.format(baseUuid, Arrays.copyOf(var17, var17.length));
                        var14 = var10000;
                        uuidS.add(UUID.fromString(var14));
                        length -= 2;
                    }
                case 4:
                case 5:
                    while(true) {
                        if (length < 4) {
                            continue label36;
                        }

                        var10001 = new Object[1];
                        var10001[0] = buffer.getInt();
                        var17 = var10001;
                        var18 = false;
                        var10000 = String.format(baseUuid, Arrays.copyOf(var17, var17.length));
                        var14 = var10000;
                        uuidS.add(UUID.fromString(var14));
                        length -= 4;
                    }
                case 6:
                case 7:
                case 21:
                    while(true) {
                        if (length < 16) {
                            continue label36;
                        }

                        long lsb = buffer.getLong();
                        long msb = buffer.getLong();
                        uuidS.add(new UUID(msb, lsb));
                        length -= 16;
                    }
                case 8:
                case 9:
                    byte[] byteArray = new byte[length];
                    buffer.get(byteArray);
                    boolean var10 = false;
                    name = new String(byteArray, StandardCharsets.UTF_8);
                    break;
                case 255:
                    manufacturerByte = new byte[length];
                    buffer.get(manufacturerByte, 0, length);
                    break;
                default:
                    buffer.position(buffer.position() + length);
            }
        }

        return new ParseBluetoothAdData.AdData(uuidS, manufacturerByte, name);
    }

    private ParseBluetoothAdData() {
    }

    static {
        ParseBluetoothAdData var0 = new ParseBluetoothAdData();
        INSTANCE = var0;
    }

    public static final class AdData {
        @NotNull
        private final ArrayList UUIDs;
        @Nullable
        private final byte[] manufacturerByte;
        @Nullable
        private final String name;

        @NotNull
        public final ArrayList getUUIDs() {
            return this.UUIDs;
        }

        @Nullable
        public final byte[] getManufacturerByte() {
            return this.manufacturerByte;
        }

        @Nullable
        public final String getName() {
            return this.name;
        }

        public AdData(@NotNull ArrayList UUIDs, @Nullable byte[] manufacturerByte, @Nullable String name) {
            super();
            this.UUIDs = UUIDs;
            this.manufacturerByte = manufacturerByte;
            this.name = name;
        }
    }
}
