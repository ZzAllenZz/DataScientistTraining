package sink;

import domain.CityAgg;
import domain.DriverAgg;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * @className: OrderSink
 * @description:
 * @author: zhangzhen
 * @date: 2023/1/25 08:30
 **/
@PublicEvolving
@SuppressWarnings("unused")
@Slf4j
public class OrderSink implements SinkFunction<CityAgg> {

    private static final long serialVersionUID = 1L;

    @Override
    public void invoke(CityAgg value, SinkFunction.Context context) { log.info(value.toString()); }

}
