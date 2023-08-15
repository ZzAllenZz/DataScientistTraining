package overtime;

import domain.CityAgg;
import domain.OrderDetail;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import sink.OrderSink;
import source.OrderSource;

import java.util.Random;

/**
 * @className: OvertimeCountJob
 * @description: 每10s统计一次全天(全量)的单量完成情况(包括 城市和司机的接单量和超时单量)
 * @author: zhangzhen
 * @date: 2023/1/25 08:28
 * 1. 完成OvertimeCounter类的编码，达到效果：每10s统计一次全天(全量)的单量完成情况(包括 城市和司机的接单量和超时单量)
 * 2. 分别开启env.enableCheckpointing(1000L)和dirtyData注释，理解flink checkpoint保证的有状态计算和重启机制。
 * 超时单判定标准：当前时间>eta时间，认为该订单超时。
 **/
public class OvertimeCountJob {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.setInteger(RestOptions.PORT,8888);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);

        //完成任务1时 先忽略
/*        env.enableCheckpointing(100L);
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3,5000));*/

        DataStream<OrderDetail> orders = env
                .addSource(new OrderSource())
                .name("orders");

        //完成任务1时 先忽略
/*        DataStream<OrderDetail> dirtyData = orders.map(new MapFunction<OrderDetail, OrderDetail>() {
            @Override
            public OrderDetail map(OrderDetail orderDetail) throws Exception {

                boolean randomBool = new Random().nextBoolean();
                if(randomBool && orderDetail.getOrderId()%2==0){
                    throw new Exception("dirty data occurs");
                }
                return orderDetail;
            }
        });
        dirtyData.print();*/

        DataStream<CityAgg> counter = orders
                .keyBy(OrderDetail::getCityId)
                .process(new OvertimeCounter())
                .name("fraud-detector");

        counter
                .addSink(new OrderSink())
                .name("sink-log");

        env.execute("OvertimeCountJob");
    }

}
