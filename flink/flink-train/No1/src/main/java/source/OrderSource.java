package source;

import domain.OrderDetail;
import iterator.OrderIterator;
import org.apache.flink.streaming.api.functions.source.FromIteratorFunction;
import java.io.Serializable;
import java.util.Iterator;

/**
 * @className: OrderSource
 * @description: 订单源 每2s下发一条，共20条记录(存在订单转单，数据重复等情况，需要在统计时考虑到)
 * 注意：可以选择订单数据源是bounded或unbounded。为统计结果好判断对错，先以bounded的结果为准。
 * 转单定义：一个订单先指派给一个司机，之后指派给另一个司机。一个订单某一个时刻只对应一个有效司机。
 * OrderDetail对象是一条订单记录
 * @author: zhangzhen
 * @date: 2023/1/9 17:41
 **/
public class OrderSource extends FromIteratorFunction<OrderDetail> {

    private static final long serialVersionUID = 1L;

    public OrderSource() {
        super(new OrderSource.RateLimitedIterator<>(OrderIterator.bounded()));

    }

    @Override
    public void run(SourceContext<OrderDetail> ctx) throws Exception {
        super.run(ctx);
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    private static class RateLimitedIterator<T> implements Iterator<T>, Serializable {
        private static final long serialVersionUID = 1L;

        private final Iterator<T> inner;

        public RateLimitedIterator(Iterator<T> inner) {
            this.inner = inner;
        }

        @Override
        public boolean hasNext() {
            return inner.hasNext();
        }

        @Override
        public T next() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            T next = inner.next();
//            System.out.println(next.toString());
            return next;
        }
    }

}
