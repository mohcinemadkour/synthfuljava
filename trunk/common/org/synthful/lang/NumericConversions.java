/*
 * Offered under Apache Public Licence 2.0 blessedgeek [@] synthful.org
 * www.synthful.org 2008
 */
package org.synthful.lang;

/**
 * @author Blessed Geek
 */
public class NumericConversions
{
    public static final StringBuffer fromBinaryArrayToStringBuffer(
        boolean[] binArray)
    {
        if (binArray == null) return new StringBuffer();

        StringBuffer binbuf = new StringBuffer(binArray.length);
        for (int i = 0; i < binArray.length; i++)
            binbuf.append(binArray[i] ? '1' : '0');

        return binbuf;
    }

    public static final String fromBinaryArrayToString(
        boolean[] binArray)
    {
        if (binArray == null) return Empty.String;
        return fromBinaryArrayToStringBuffer(binArray).toString();
    }

    public static final long fromBinaryArrayToLong(
        boolean[] binArray, int count)
    {
        long value = 0;
        if (binArray == null) return value;

        long shifter = 1;
        if (binArray[0]) value = 1;

        for (char i = 1; i < binArray.length && i < count; i++)
        {
            shifter <<= 1;
            if (binArray[i]) value += shifter;
        }

        return value;
    }

    public static final int fromBinaryArrayToInt(
        boolean[] binArray)
    {
        return (int) fromBinaryArrayToLong(binArray, 31);
    }

    public static final long fromBinaryArrayToLong(
        boolean[] binArray)
    {
        return fromBinaryArrayToLong(binArray, 63);
    }

    public static final long fromByteArrayToLong(
        byte[] bytes, int count)
    {
        if (bytes == null) return 0;

        long value = bytes[0], shifter = 0;

        for (char i = 1; i < bytes.length && i < count; i++)
        {
            shifter = bytes[i];
            value += (shifter << 8 * i);
        }

        return value;
    }

    public static final int fromByteArrayToInt(
        byte[] bytes)
    {
        return (int) fromByteArrayToLong(bytes, 4);
    }

    public static final long fromByteArrayToLong(
        byte[] bytes)
    {
        return fromByteArrayToLong(bytes, 8);
    }

    public static final long fromCharArrayToLong(
        char[] bytes, int count)
    {
        if (bytes == null) return 0;

        long value = bytes[0], shifter = 0;

        for (char i = 1; i < bytes.length && i < count; i++)
        {
            shifter = bytes[i];
            value += (shifter << 8 * i);
        }

        return value;
    }
    
    public static final long fromCharArrayToLong(
        char[] bytes)
    {
        return fromCharArrayToLong(bytes, 8);
    }
    
    public static final int fromCharArrayToInt(
        char[] bytes)
    {
        return (int)fromCharArrayToLong(bytes, 4);
    }
    
}
