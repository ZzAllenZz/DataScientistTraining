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

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<CityAgg> out) throws Exception {
        super.onTimer(timestamp, ctx, out);
    }

    @Override
    public void processElement(OrderDetail value, Context ctx, Collector<CityAgg> out) throws Exception {

    }
}
