package com.hk.commons.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.nio.conn.NHttpClientConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 该类摘自 alibaba oss sdk 类，主要作用是 启动一个守护进程 死循环 {@link #run()} 对 {@link #connectionManagers} 中过期的链接资源释放。
 *
 * @author kevin
 * @date 2019-8-7 13:47
 */
@Slf4j
public class ClientIdleConnectionReaper extends Thread {

    private static final int REAP_INTERVAL_MILLISECONDS = 5 * 1000;

    private static ClientIdleConnectionReaper instance;

    private static long idleConnectionTime = 60 * 1000;

    private volatile boolean shuttingDown;

    private static final ArrayList<HttpClientConnectionManager> connectionManagers = new ArrayList<>();

    private static final ArrayList<NHttpClientConnectionManager> nConnectionManager = new ArrayList<>();

    private ClientIdleConnectionReaper() {
        super("http_client_idle_connection_reaper");
        setDaemon(true);
    }

    public static synchronized boolean registerNConnectionManager(NHttpClientConnectionManager connectionManager) {
        if (instance == null) {
            instance = new ClientIdleConnectionReaper();
            instance.start();
        }
        return nConnectionManager.add(connectionManager);
    }

    public static synchronized boolean removeNConnectionManager(NHttpClientConnectionManager connectionManager) {
        boolean b = nConnectionManager.remove(connectionManager);
        shutdownIfEmptyTask();
        return b;
    }

    public static synchronized boolean registerConnectionManager(HttpClientConnectionManager connectionManager) {
        if (instance == null) {
            instance = new ClientIdleConnectionReaper();
            instance.start();
        }
        return connectionManagers.add(connectionManager);
    }

    public static synchronized boolean removeConnectionManager(HttpClientConnectionManager connectionManager) {
        var b = connectionManagers.remove(connectionManager);
        shutdownIfEmptyTask();
        return b;
    }

    private void markShuttingDown() {
        shuttingDown = true;
    }

    public static synchronized boolean shutdownIfEmptyTask() {
        if (connectionManagers.isEmpty() && nConnectionManager.isEmpty()) {
            if (instance != null) {
                instance.markShuttingDown();
                instance.interrupt();
                connectionManagers.clear();
                nConnectionManager.clear();
                instance = null;
                return true;
            }
        }
        return false;
    }

    public static synchronized void setIdleConnectionTime(long idleTime) {
        idleConnectionTime = idleTime;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        while (true) {
            if (shuttingDown) {
                log.debug("Shutting down reaper thread.");
                return;
            }
            try {
                Thread.sleep(REAP_INTERVAL_MILLISECONDS);
            } catch (InterruptedException e) {
                // ignore
            }
            try {
                List<HttpClientConnectionManager> connectionManagers;
                List<NHttpClientConnectionManager> nConnectionManagers;
                synchronized (ClientIdleConnectionReaper.class) {
                    connectionManagers = (List<HttpClientConnectionManager>) ClientIdleConnectionReaper.connectionManagers.clone();
                    nConnectionManagers = (List<NHttpClientConnectionManager>) ClientIdleConnectionReaper.nConnectionManager.clone();
                }
                for (HttpClientConnectionManager connectionManager : connectionManagers) {
                    try {
                        connectionManager.closeExpiredConnections();
                        connectionManager.closeIdleConnections(idleConnectionTime, TimeUnit.MILLISECONDS);
                    } catch (Exception ex) {
                        log.warn("Unable to close idle connections", ex);
                    }
                }
                for (NHttpClientConnectionManager connectionManager : nConnectionManagers) {
                    try {
                        connectionManager.closeExpiredConnections();
                        connectionManager.closeIdleConnections(idleConnectionTime, TimeUnit.MILLISECONDS);
                    } catch (Exception ex) {
                        log.warn("Unable to close idle connections", ex);
                    }
                }
            } catch (Throwable t) {
                log.debug("Reaper thread: ", t);
            }
        }
    }
}
