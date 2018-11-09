package com.flyer.job.common;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;

/**
 * 唯一号生成工具
 */
public class UID {

    /**
     * seq:自增序列.
     */
    private static int seq = 0;
    /**
     * ROTATION:数值循环范围.
     */
    private static final int ROTATION = 99999;
    /**
     * _genmachine:(机器码+进程号).
     */
    private static int _genmachine;

    /**
     * unix timestamp起始位置
     * //2018/1/1 00:00:00
     */
    private static int timeStampStart = 1514736000;

    static {
        //为了增大空间，这里特殊处理下，把起始时间从1970-1-1调整为2017-10-1年，理论上，可以满足29年内不会超long的最大值。
        _genmachine = getMachineId() | getProcessId();
    }

    private UID() {
    }

    /**
     * 生成规则：unix时间戳 + 5位唯一号(机器码+进程号) + 自增序列(0~99999)
     * 形如：938111 55283 00097
     * 即：在同一个jvm中，能保证1s内，最多生成10w个id不撞码
     *
     * @return
     */
    public static synchronized Long next() {
        StringBuilder buf = new StringBuilder();
        if (seq > ROTATION) {
            seq = 0;
        }
        buf.append((int) (System.currentTimeMillis() / 1000L) - timeStampStart);
        buf.append(_genmachine);
        buf.append(String.format("%05d", seq++));
        return Long.parseLong(buf.toString());
    }

    /**
     * 机器码(网卡信息)
     *
     * @return
     */
    private static int getMachineId() {
        int machineId;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();

                if (ni.getName() != null && ni.getName().trim() != "") {
                    sb.append(ni.getName());
                }
                if (ni.getHardwareAddress() != null) {
                    sb.append(Arrays.toString(ni.getHardwareAddress()));
                }
                sb.append(ni.getMTU());
                Iterator<InterfaceAddress> it = ni.getInterfaceAddresses().iterator();
                while (it.hasNext()) {
                    InterfaceAddress ia = it.next();
                    if (ia.getAddress() != null) {
                        sb.append(ia.getAddress());
                    }
                }
            }
            machineId = sb.toString().hashCode() << 16;
        } catch (Throwable e) {
            // exception sometimes happens with IBM JVM, use random
            machineId = (new Random().nextInt()) << 16;
        }
        return Integer.toHexString(machineId).hashCode() & 0xFFFF;
    }

    /**
     * 进程号(5位)
     *
     * @return
     */
    private static int getProcessId() {
        int processPiece;
        int processId = new Random().nextInt();
        try {
            processId =
                java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
        } catch (Throwable t) {
        }
        ClassLoader loader = UID.class.getClassLoader();
        int loaderId = loader != null ? System.identityHashCode(loader) : 0;
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(processId));
        sb.append(Integer.toHexString(loaderId));
        processPiece = sb.toString().hashCode() & 0xFFFF;
        return processPiece;
    }

}
