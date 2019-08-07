package com.hk.commons.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.HttpClientConnectionManager;

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

    private ClientIdleConnectionReaper() {
        super("http_client_idle_connection_reaper");
        setDaemon(true);
    }

    /**
     * 注册 connectionManager ，并启动该守护进程
     *
     * @param connectionManager connectionManager
     */
    public static synchronized boolean registerConnectionManager(HttpClientConnectionManager connectionManager) {
        if (instance == null) {
            instance = new ClientIdleConnectionReaper();
            instance.start();
        }
        return connectionManagers.add(connectionManager);
    }

    /**
     * 删除 connectionManager，当 {@link #connectionManagers} 为空时，调用 {@link #shutdown()} 释放该实例对象
     *
     * @param connectionManager
     */
    public static synchronized boolean removeConnectionManager(HttpClientConnectionManager connectionManager) {
        boolean b = connectionManagers.remove(connectionManager);
        if (connectionManagers.isEmpty())
            shutdown();
        return b;
    }

    private void markShuttingDown() {
        shuttingDown = true;
    }

    /**
     * 停止线程，释放资源
     */
    public static synchronized boolean shutdown() {
        if (instance != null) {
            instance.markShuttingDown();
            instance.interrupt();
            connectionManagers.clear();
            instance = null;
            return true;
        }
        return false;
    }

    public static synchronized int size() {
        return connectionManagers.size();
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
                synchronized (ClientIdleConnectionReaper.class) {
                    connectionManagers = (List<HttpClientConnectionManager>) ClientIdleConnectionReaper.connectionManagers.clone();
                }
                for (HttpClientConnectionManager connectionManager : connectionManagers) {
                    try {
                        // 释放资源
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
