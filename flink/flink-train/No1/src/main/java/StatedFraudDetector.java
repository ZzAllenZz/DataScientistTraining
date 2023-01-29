import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.walkthrough.common.entity.Transaction;

/**
 * @className: StatedFraudDetector
 * @description:
 * @author: zhangzhen
 * @date: 2023/1/12 17:29
 **/
public class StatedFraudDetector extends KeyedProcessFunction<Long, Transaction, Alert> {

    private static final long serialVersionUID = 1L;
    private static final double SMALL_AMOUNT = 1.00;
    private static final double LARGE_AMOUNT = 500.00;
    private static final long ONE_MINUTE = 60 * 1000;
    private transient ValueState<Boolean> flagState;

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor valueStateDescriptor = new ValueStateDescriptor<>("flag", Types.BOOLEAN);
        flagState = getRuntimeContext().getState(valueStateDescriptor);
    }

    @Override
    public void processElement(Transaction value, Context ctx, Collector<Alert> out) throws Exception {
        //获取当前状态
        Boolean currentState = flagState.value();

        if (null!=currentState){
            if(value.getAmount()>LARGE_AMOUNT){
                Alert alert = new Alert();
                alert.setId(value.getAccountId());
                out.collect(alert);
            }
            flagState.clear();

        }
        if(value.getAmount()<SMALL_AMOUNT){
            flagState.update(Boolean.TRUE);
        }
    }
    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<Alert> out) throws Exception {
        super.onTimer(timestamp, ctx, out);
    }
}
