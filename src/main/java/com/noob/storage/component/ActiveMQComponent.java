package com.noob.storage.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * 发送异步消息组件<br>
 * 不建议传输数据量过大<br>
 */
public class ActiveMQComponent {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JmsTemplate jmsTemplate;
    private Destination destination;

    /**
     * 发送MQ消息<br>
     */
    public void sendMessage(final String key, final Object value) {

        this.jmsTemplate.send(destination, new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                // 发送的消息都是MapMessage类型的
                MapMessage message = session.createMapMessage();
                String json = serialization(value);
                message.setStringProperty(key, json);
                logger.info("MQ send Message: key=" + key + ",value=" + json);
                return message;
            }
        });
    }

    /**
     * 发送text类型的MQ消息，监听端获取消息示例：
     * <pre>String value = ((TextMessage)message).getText();</pre>
     */
    public void sendTextMessage(final String value) {

        this.jmsTemplate.send(destination, new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage();
                message.setText(value);
                return message;
            }
        });
    }

    /**
     * 获取MQ消息,键对应的值<br>
     * 接收 key-value 的 value, 现在只处理MapMessage类型<br>
     */
    public <T> T receive(Message message, final String key, final Class<T> c) {

        if (message instanceof MapMessage) {
            MapMessage map = (MapMessage) message;
            try {
                String json = map.getStringProperty(key);
                logger.info("MQ receive Message: key=" + key + ",value=" + json);
                return deserialization(json, c);
            } catch (JMSException e) {
                logger.error("MapMessage map getStringProperty error key=" + key, "errMsg=" + e.getMessage());
                return null;
            }
        } else {
            logger.error("the mq message type must be mdpmessage, message=" + serialization(message));
            return null;
        }

    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    private String serialization(Object object) {
        if (object == null) {
            return null;
        }
        return JSONObject.toJSONString(object);

    }

    private <T> T deserialization(String json, Class<T> c) {
        if (json == null) {
            return null;
        }
        return JSON.parseObject(json, c);

    }

}
