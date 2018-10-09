package com.ncu.springboot.websocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ServerManager {
    private  static final Logger LOG= LoggerFactory.getLogger(ServerManager.class);
    private static Collection<BitCoinWebSocketServer> servers = Collections.synchronizedCollection(new ArrayList<BitCoinWebSocketServer>());

    public static void broadCast(String msg){
        for (BitCoinWebSocketServer bitCoinServer : servers) {
            try {
                bitCoinServer.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getTotal(){
        return servers.size();
    }
    public static void add(BitCoinWebSocketServer server){
        servers.add(server);
        LOG.info("有新连接加入！ 当前总连接数是：{}",servers.size());
    }
    public static void remove(BitCoinWebSocketServer server){
        servers.remove(server);
        LOG.info("有连接退出！ 当前总连接数是：{}",servers.size());
    }

}
