package cn.edu.xmu.oomall.other.dao.mq;


import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.model.bo.FootPrintBo;
import cn.edu.xmu.oomall.other.model.po.FootPrintPo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

public class FootprintProducer {
    private  static  final Logger logger = LoggerFactory.getLogger(FootprintProducer.class);

    @Resource
    RocketMQTemplate rocketMQTemplate;

    /**
     * 向RocketMQ发送消息
     * @param footprintPo
     */
    private void sendFootprint(FootPrintPo footprintPo)
    {
        String json = JacksonUtil.toJson(footprintPo);
        Message message = MessageBuilder.withPayload(json).build();
        rocketMQTemplate.sendOneWay("footprint-topic", message);
        logger.debug(json);
    }
}
