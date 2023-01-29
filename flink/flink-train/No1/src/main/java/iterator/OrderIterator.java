package iterator;

import domain.OrderDetail;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @className: OrderIterator
 * @description:
 * @author: zhangzhen
 * @date: 2023/1/25 07:33
 **/
public class OrderIterator implements Iterator<OrderDetail>, Serializable {
    private static final long serialVersionUID = 1L;

    private static final Timestamp INITIAL_TIMESTAMP = new Timestamp(System.currentTimeMillis());

    private static final Integer SIX_MINUTES = 6 * 60 * 1000;

    private static final Integer ONE_MINUTE = 60 * 1000;

    private final boolean bounded;

    private int index = 0;

    private long timestamp;

    public OrderIterator(boolean bounded) {
        this.bounded = bounded;
        this.timestamp = INITIAL_TIMESTAMP.getTime();
    }

    public static OrderIterator bounded(){
        return new OrderIterator(true);
    }
    public static OrderIterator unbounded(){
        return new OrderIterator(false);
    }

    @Override
    public boolean hasNext() {
        if (index < data.size()) {
            return true;
        } else if (!bounded) {
            index = 0;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public OrderDetail next() {
        OrderDetail order = data.get(index++);
        order.setUtime(timestamp);
        boolean negSign = new Random().nextBoolean();
        long randomMin = new Random().nextInt(ONE_MINUTE);
        if(negSign) {
            randomMin = -randomMin;
        }
        //eta时间基于ctime生成，上下浮动1分钟。
        order.setEta(timestamp+randomMin);
        return order;
    }
    private static List<OrderDetail> data =
            Arrays.asList(
                    new OrderDetail(1L,1L,"北京",1L,"curry"),
                    new OrderDetail(2L,2L,"上海",2L,"james"),
                    new OrderDetail(3L,2L,"上海",3L,"durant"),
                    new OrderDetail(4L,2L,"上海",3L,"durant"),
                    new OrderDetail(5L,1L,"北京",1L,"curry"),
                    new OrderDetail(6L,1L,"北京",4L,"thompson"),
                    new OrderDetail(7L,1L,"北京",5L,"harden"),
                    //转单 orderId=1  driverId:1->driverId:4
                    new OrderDetail(1L,1L,"北京",4L,"thompson"),
                    //重复数据
                    new OrderDetail(2L,2L,"上海",2L,"james"),
                    //转单 driverId:3->driverId:2
                    new OrderDetail(3L,2L,"上海",2L,"james"),
                    new OrderDetail(8L,1L,"北京",1L,"curry"),
                    new OrderDetail(9L,1L,"北京",1L,"curry"),
                    new OrderDetail(10L,1L,"北京",1L,"curry"),
                    new OrderDetail(10L,1L,"北京",1L,"curry"),
                    new OrderDetail(10L,1L,"北京",1L,"curry"),
                    new OrderDetail(10L,1L,"北京",1L,"curry"),
                    new OrderDetail(10L,1L,"北京",1L,"curry"),
                    new OrderDetail(10L,1L,"北京",1L,"curry"),
                    new OrderDetail(10L,1L,"北京",1L,"curry"),
                    new OrderDetail(10L,1L,"北京",1L,"curry")
            );
            //终态：curry=4,thompson=2,thompson=1,james=2,durant=1

}
