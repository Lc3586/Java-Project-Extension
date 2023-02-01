package project.extension.hub;

import org.atmosphere.cpr.AtmosphereResource;
import org.slf4j.Logger;
import project.extension.task.TaskQueueHandler;
import project.extension.tuple.Tuple2;
import project.extension.tuple.Tuple4;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 基础集线器处理类
 *
 * @author LCTR
 * @date 2022-10-26
 */
public abstract class HubHandler {
    public HubHandler(String name,
                      Logger logger) {
        this(name,
             -1,
             -1,
             logger);
    }

    public HubHandler(String name,
                      int handlerThreadPoolSize,
                      int senderThreadPoolSize,
                      Logger logger) {
        this.messageHandler = new MessageHandler(name,
                                                 handlerThreadPoolSize,
                                                 logger);
        messageHandler.start(true);
        this.messageSender = new MessageSender(name,
                                               senderThreadPoolSize,
                                               logger);
        messageSender.start(true);
    }

    /**
     * 客户端集合
     * <p>key：uuid，value：客户端</p>
     */
    private final ConcurrentMap<String, AtmosphereResource> clientMap = new ConcurrentHashMap<>();

    /**
     * 客户端分组集合
     * <p>key：分组，value：客户端uuid集合</p>
     */
    private final ConcurrentMap<String, List<String>> groupMap = new ConcurrentHashMap<>();

    /**
     * 消息处理类
     */
    private final MessageHandler messageHandler;

    /**
     * 消息发送类
     */
    private final MessageSender messageSender;

    /**
     * 添加客户端
     *
     * @param client 客户端
     */
    public void add(AtmosphereResource client) {
        clientMap.put(client.uuid(),
                      client);
    }

    /**
     * 添加客户端
     * <p>同时加入分组</p>
     *
     * @param client 客户端
     * @param group  分组
     */
    public void add(AtmosphereResource client,
                    String group) {
        add(client);

        join(client.uuid(),
             group);
    }

    /**
     * 移除客户端
     * <p>同时退出全部分组</p>
     *
     * @param uuid 客户端标识
     */
    public void remove(String uuid) {
        clientMap.remove(uuid);

        quitAll(uuid);
    }

    /**
     * 加入分组
     *
     * @param uuid  客户端标识
     * @param group 分组
     */
    public void join(String uuid,
                     String group) {
        if (!groupMap.containsKey(group))
            groupMap.put(group,
                         new ArrayList<>());

        if (!groupMap.get(group)
                     .contains(uuid))
            groupMap.get(group)
                    .add(uuid);
    }

    /**
     * 退出分组
     *
     * @param uuid  客户端标识
     * @param group 分组
     */
    public void quit(String uuid,
                     String group) {
        if (!groupMap.containsKey(group))
            return;
        groupMap.get(group)
                .remove(uuid);
    }

    /**
     * 退出全部分组
     *
     * @param uuid 客户端标识
     */
    public void quitAll(String uuid) {
        for (String group : groupMap.keySet()) {
            groupMap.get(group)
                    .remove(uuid);
        }
    }

    /**
     * 获取全部客户端
     *
     * @return 客户端集合
     */
    public List<AtmosphereResource> getAllClients() {
        return new ArrayList<>(clientMap.values());
    }

    /**
     * 获取全部客户端
     *
     * @param group 分组
     * @return 客户端集合
     */
    public List<AtmosphereResource> getClients(String group) {
        if (!groupMap.containsKey(group))
            return new ArrayList<>();
        return groupMap.get(group)
                       .stream()
                       .map(clientMap::get)
                       .collect(Collectors.toList());
    }

    /**
     * 获取客户端
     *
     * @param uuid 标识
     * @return 客户端
     */
    public AtmosphereResource getClient(String uuid) {
        return clientMap.get(uuid);
    }

    /**
     * 处理消息
     *
     * @param data 数据
     * @return 应当消息，为null或空时不发送消息
     */
    protected abstract Tuple2<MessageType, Object> handlerMessage(AtmosphereResource client,
                                                                  Object data);

    /**
     * 处理消息
     *
     * @param uuid 客户端标识
     * @param data 数据
     */
    public void handlerMessage(String uuid,
                               Object data) {
        messageHandler.add(uuid,
                           data);
    }

    /**
     * 发送消息
     *
     * @param uuid 客户端标识
     * @param data 数据
     */
    public void send(String uuid,
                     String data) {
        messageSender.send(uuid,
                           MessageType.STRING,
                           data);
    }

    /**
     * 发送消息
     *
     * @param uuid 客户端标识
     * @param data 数据
     */
    public void send(String uuid,
                     byte[] data) {
        messageSender.send(uuid,
                           MessageType.BUFFER,
                           data);
    }

    /**
     * 广播
     *
     * @param data 数据
     */
    public void broadcast(String data) {
        messageSender.broadcast(
                MessageType.STRING,
                data);
    }

    /**
     * 广播
     *
     * @param data 数据
     */
    public void broadcast(byte[] data) {
        messageSender.broadcast(
                MessageType.BUFFER,
                data);
    }

    /**
     * 分组广播
     *
     * @param group 分组
     * @param data  数据
     */
    public void broadcast(String group,
                          String data) {
        messageSender.broadcastGroup(group,
                                     MessageType.STRING,
                                     data);
    }

    /**
     * 分组广播
     *
     * @param group 分组
     * @param data  数据
     */
    public void broadcast(String group,
                          byte[] data) {
        messageSender.broadcastGroup(group,
                                     MessageType.BUFFER,
                                     data);
    }

    /**
     * 消息处理类
     */
    private class MessageHandler
            extends TaskQueueHandler {
        public MessageHandler(String name,
                              int threadPoolSize,
                              Logger logger) {
            super(name,
                  threadPoolSize,
                  logger);
        }

        /**
         * 待处理的消息集合
         * <p>key：uuid，value：消息集合</p>
         */
        private final ConcurrentMap<String, List<Object>> messageMap = new ConcurrentHashMap<>();

        /**
         * 新增消息
         *
         * @param uuid 客户端标识
         * @param data 数据
         */
        public void add(String uuid,
                        Object data) {
            if (!messageMap.containsKey(uuid))
                messageMap.put(uuid,
                               new ArrayList<>());
            messageMap.get(uuid)
                      .add(data);
            //追加至队列并处理
            super.addTask(uuid,
                          true);
        }

        /**
         * 处理主任务
         *
         * @param uuid 客户端标识
         */
        @Override
        protected void processingTask(Object uuid) {
            String uuid_resolve = String.valueOf(uuid);

            //异步执行
            super.putConcurrentTask(uuid_resolve,
                                    () -> this.handlerTask(uuid_resolve),
                                    x -> super.removeConcurrentTask(uuid_resolve));
        }

        /**
         * 异步处理子任务
         *
         * @param uuid 客户端标识
         */
        private void handlerTask(String uuid) {
            for (Object data : messageMap.get(uuid)) {
                Tuple2<MessageType, Object> ack = handlerMessage(getClient(uuid),
                                                                 data);
                if (ack != null)
                    messageSender.send(uuid,
                                       ack.a,
                                       ack.b);
            }
            messageMap.remove(uuid);
        }
    }

    /**
     * 消息发送类
     */
    private class MessageSender
            extends TaskQueueHandler {
        public MessageSender(String name,
                             int threadPoolSize,
                             Logger logger) {
            super(name,
                  threadPoolSize,
                  logger);
        }

        /**
         * 待发送的消息集合
         * <p>key：任务标识，value：a：发送类型，b：消息类型，c：uuid，d：消息</p>
         */
        private final ConcurrentMap<String, Tuple4<SendType, MessageType, String, Object>> messageMap = new ConcurrentHashMap<>();

        /**
         * 新增消息
         *
         * @param uuid 客户端标识
         * @param type 类型
         * @param data 数据
         */
        public void send(String uuid,
                         MessageType type,
                         Object data) {
            String key = UUID.randomUUID()
                             .toString();
            messageMap.put(key,
                           new Tuple4<>(SendType.Client,
                                        type,
                                        uuid,
                                        data));

            //追加至队列并处理
            super.addTask(key,
                          true);
        }

        /**
         * 新增广播
         *
         * @param type 类型
         * @param data 数据
         */
        public void broadcast(MessageType type,
                              Object data) {
            String key = UUID.randomUUID()
                             .toString();
            messageMap.put(key,
                           new Tuple4<>(SendType.Broadcast,
                                        type,
                                        "ALL",
                                        data));

            //追加至队列并处理
            super.addTask(key,
                          true);
        }

        /**
         * 新增分组广播
         *
         * @param group 分组
         * @param type  类型
         * @param data  数据
         */
        public void broadcastGroup(String group,
                                   MessageType type,
                                   Object data) {
            String key = UUID.randomUUID()
                             .toString();
            messageMap.put(key,
                           new Tuple4<>(SendType.BroadcastGroup,
                                        type,
                                        group,
                                        data));

            //追加至队列并处理
            super.addTask(key,
                          true);
        }

        /**
         * 处理主任务
         *
         * @param key 任务标识
         */
        @Override
        protected void processingTask(Object key) {
            String key_resolve = String.valueOf(key);
            Tuple4<SendType, MessageType, String, Object> message = messageMap.get(key_resolve);

            //异步执行
            super.putConcurrentTask(message.b,
                                    () -> this.handlerTask(key_resolve),
                                    x -> super.removeConcurrentTask(message.b));
        }

        /**
         * 异步处理子任务
         *
         * @param key 任务标识
         */
        private void handlerTask(String key) {
            Tuple4<SendType, MessageType, String, Object> message = messageMap.get(key);
            switch (message.a) {
                case Client:
                    write(getClient(message.c),
                          message.b,
                          message.d);
                    break;
                case BroadcastGroup:
                    for (AtmosphereResource client : getClients(message.c)) {
                        write(client,
                              message.b,
                              message.d);
                    }
                    break;
                case Broadcast:
                    for (AtmosphereResource client : getAllClients()) {
                        write(client,
                              message.b,
                              message.d);
                    }
                    break;
            }
            messageMap.remove(key);
        }

        /**
         * 写入数据
         *
         * @param client 客户端
         * @param type   类型
         * @param data   数据
         */
        private void write(AtmosphereResource client,
                           MessageType type,
                           Object data) {
            switch (type) {
                case STRING:
                    client.write((String) data);
                    break;
                case BUFFER:
                    client.write((byte[]) data);
                    break;
            }
        }
    }

    /**
     * 发送类型
     */
    private enum SendType {
        /**
         * 单个客户端
         */
        Client,
        /**
         * 分组广播
         */
        BroadcastGroup,
        /**
         * 广播
         */
        Broadcast
    }
}
