package m;

import ch.qos.logback.classic.Level;
import m.algorithmTool.reinforcementLearning.QTable;
import m.migrationAlgorithm.AntColonyOptimization;
import m.migrationAlgorithm.Migration;
import m.po.ProcessResult;
import m.util.Constant;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.util.Log;
import m.po.EnvironmentInfo;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author zhengxiangyu
 */
public class agent {


    /**
     * get the implement of environment
     */
    private static environment environment = new environment();

    /**
     * get the simulation
     */
    private static final CloudSim simulation = environment.getSimulation();

    private static Constant constant = new Constant();

    private static boolean allHostsHaveNoVms = true;

//    private static Migration migrate = new ReinforcementLearning();
//    private static Migration migrate = new GreedyMinMaxHostUtilization();
    private static Migration migrate = new AntColonyOptimization();


    private static ConcurrentLinkedQueue<EnvironmentInfo> queue = environment.getQueue();

    /**
     * main function create two threads
     * the first illustrate that strat the simulation - produce infomation of hosts and vms.
     * the second one simulate the m.migrationAlgorithm.agent, witch get information of hosts and vms from queue.
     *
     * @param args
     */
    public static void main(String[] args) {
        Log.setLevel(Level.DEBUG);
        ExecutorService ex = Executors.newFixedThreadPool(3);

        agent agent = new agent();

        // create a thread to start the m.migrationAlgorithm.envirnment
        try {
            ex.submit(new Runnable() {
                @Override
                public void run() {
                    agent.startSimulation();
                }
            });
            waitSomeMillis((long) constant.SIMULATION_RUNNING_INTERVAL * 1000);
            // create a thread to get cpu and ram from HOST and VM
            ex.submit(new Runnable() {
                @Override
                public void run() {
                    agent.getInfo();
                }
            });

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }


    /**
     * start simulation and get the information from the environment each second then put the information into the queue.
     */
    public void startSimulation() {

            environment.start();
            QTable.iniQtable();
            while (simulation.isRunning()) {
                try {
                simulation.runFor(constant.SIMULATION_RUNNING_INTERVAL);
                waitSomeMillis((long) constant.SIMULATION_RUNNING_INTERVAL * 1000);
                } catch (Exception e) {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" +
                            "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    e.printStackTrace();
                }
            }

    }


    /**
     * get the information from the queue then sent the assigment of create vms and vm migration to the environment
     */
    public void getInfo() {
        while (simulation.isRunning()) {
            if (!queue.isEmpty()) {
                EnvironmentInfo info = queue.poll();
                try {
                    if (allHostsHaveNoVms) {
                        checkAllHosts(info.getDatacenter().getHostList());
                        if (allHostsHaveNoVms) {
                            continue;
                        }
                    }

                    processInfo(info);
                } catch (Exception e) {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" +
                            "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    e.printStackTrace();
                }

            }
            waitSomeMillis(1000);
        }

    }

    private void processInfo(EnvironmentInfo info) {
        ProcessResult pr = migrate.processMigration(info);
        migrateVms(pr.getVmToHostMap());
    }


    private void migrateVms(Map<Vm, Host> vmHostMap) {
        if (vmHostMap != null && vmHostMap.size() > 0) {
            for (Map.Entry<Vm, Host> e : vmHostMap.entrySet()) {
                Vm vm = e.getKey();
                Host host = e.getValue();
                if (vm == null || host == null) {
                    continue;
                }
                environment.getDatacenter().requestVmMigration(vm, host);
            }
        }
    }

    private void checkAllHosts(List<Host> hostList) {
        for (Host host : hostList) {
            if (host.getVmList() != null && host.getVmList().size() > 0) {
                allHostsHaveNoVms = false;
            }
        }

    }

    private static void waitSomeMillis(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
