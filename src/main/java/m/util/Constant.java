package m.util;

public class Constant {

    /**
     * hardware parameters
     */
    /**
     * total hosts
     */
    public static final int HOSTS = 2;
    /**
     * total core of single host
     */
    public static final int HOST_PES = 32;
    /**
     * total memory of single host
     */
    public static final long HOST_RAM = 131072; //in Megabytes
    /**
     * total internet bandwidth of single host
     */
    public static final long HOST_BW = 10240; //in Megabits/s
    /**
     * total storage of single host
     */
    public static final long HOST_STORAGE = 10240000; //in Megabytes
    /**
     * schedul interval
     */
    public static final int SCHEDULING_INTERVAL = 10;
    /**
     * Defines the power a Host uses, even if it's idle (in Watts).
     */
    public static final double HOST_STATIC_POWER = 35;

    /**
     * The max power a Host uses (in Watts).
     */
    public static final double HOST_MAX_POWER = 50;

    /**
     * total cores of single vm
     */
    public static final int VM_PES = 2;
    /**
     * total memory of single vm
     */
    public static final int VM_RAM = 2048;
    /**
     * total internet bandwidth of single vm
     */
    public static final int VM_BW = 1000;
    /**
     * total storage of single vm
     */
    public static final int VM_SIZE = 10000;
    /**
     * total cores of single cloudlet needed
     */
    public static final int CLOUDLET_PES = 2;
    /**
     * Each cloudlet execution time
     */
    public static final int CLOUDLET_LENGTH = 10000;

    public static final int VM_MIPS = 1000;

}