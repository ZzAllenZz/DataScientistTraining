package overtime;

import domain.CityAgg;
import domain.DriverAgg;
import domain.OrderDetail;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import util.DateUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @className: OvertimeCounter
 * @description:
 * @author: zhangzhen
 * @date: 2023/1/25 08:28
 **/
public class OvertimeCounter extends KeyedProcessFunction<Long, OrderDetail, CityAgg>  {

    //触发频率 10s
    public static int interval = 10*1000;

    //key:orderId value:OrderDetail对象
    private MapState<Long,OrderDetail> detailState;


    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        detailState = getRuntimeContext().getMapState(new MapStateDescriptor<Long, OrderDetail>("detailState",Long.class,OrderDetail.class));
    }

    @Override
    public void processElement(OrderDetail value, Context ctx, Collector<CityAgg> out) throws Exception {
        //注册计时器，确保下一个interval触发
        ctx.timerService().registerProcessingTimeTimer(System.currentTimeMillis() / interval * interval + interval);
        //upsert
        detailState.put(value.getOrderId(),value);
//        System.out.println(detailState);
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<CityAgg> out) throws Exception {
        //注册计时器，确保下一个interval触发
        ctx.timerService()
                .registerProcessingTimeTimer(timestamp + interval);
        System.out.println("触发时间:"+ DateUtil.fromTimestamp(timestamp));

        Iterator<Map.Entry<Long, OrderDetail>> iterator = detailState.iterator();
        long currentProcessingTime = ctx.timerService().currentProcessingTime();
        CityAgg cityAgg = new CityAgg();
//        Map<Long, DriverAgg> driverMap = new HashMap<>();

        Iterator<Map.Entry<Long, OrderDetail>> entries = detailState.iterator();
        List<OrderDetail> orderDetailList = new ArrayList<>();
        while (entries.hasNext()){
            orderDetailList.add(entries.next().getValue());
        }
        Map<Long, List<OrderDetail>> groupByDriver = orderDetailList.stream().collect(Collectors.groupingBy(OrderDetail::getDriverId));
        for (Map.Entry<Long, List<OrderDetail>> entry : groupByDriver.entrySet()) {
            List<OrderDetail> orderDetails = entry.getValue();
            Long driverId = entry.getKey();
            String driverName = orderDetails.get(0).getDriverName();
            DriverAgg driverAgg = new DriverAgg(driverId,driverName);
            orderDetails.forEach(o->{
                driverAgg.add1Order();
                if(currentProcessingTime>o.getEta()){
                    driverAgg.add1Overtime();
                }
            });
            cityAgg.addDriver(driverAgg);
        }


        while (iterator.hasNext()){
            Map.Entry<Long, OrderDetail> orderDetailEntry = iterator.next();
            OrderDetail orderDetail = orderDetailEntry.getValue();
            cityAgg.process(orderDetail,currentProcessingTime);
        }
        out.collect(cityAgg);
//        System.out.println("cityAgg is "+cityAgg);
//        out.collect(new CityAgg());

    }
}
