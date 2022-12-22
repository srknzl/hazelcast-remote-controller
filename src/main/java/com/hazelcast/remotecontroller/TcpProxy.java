package com.hazelcast.remotecontroller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TcpProxy implements Runnable {
    private final Set<String> blockedAddresses;
    private final ServerSocket socket;
    private final Member member;
    private static Logger LOG = LogManager.getLogger(Main.class);

    private static final int BACKLOG = 256;

    public TcpProxy(Member member) {
        this.member = member;
        this.blockedAddresses = new HashSet<>();
        this.socket = createTcpServerSocket();
    }

    private ServerSocket createTcpServerSocket() {
        try {
            return new ServerSocket(0, BACKLOG);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create the TCP Proxy, Exception: %s", e);
        }
    }

    public void block(String address) {
        blockedAddresses.add(address);
    }

    public void unblock(String address) {
        blockedAddresses.remove(address);
    }

    public void run(){
        try {
            while(true) {
                Socket clientSocket = this.socket.accept();
                InputStream in = clientSocket.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);
            }
        } catch (IOException e) {
            LOG.error(String.format("An exception happened in TcpProxy acceptor thread, %s, stack trace: \n %s", e, Arrays.toString(e.getStackTrace())));
        }

    }

    public String getHost() {
        return socket.getInetAddress().getHostName();
    }

    public int getPort() {
        return socket.getLocalPort();
    }
}
