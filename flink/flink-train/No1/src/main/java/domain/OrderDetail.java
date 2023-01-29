package domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.DateUtil;

/**
 * @className: OrderDetail
 * @description:
 * @author: zhangzhen
 * @date: 2023/1/25 07:28
 **/
@Data
@AllArgsConstructor
public class OrderDetail {
    /**
     * 订单ID
     * */
    Long orderId;
    /**
     * 城市ID
     * */
    Long cityId;
    /**
     * 城市名称
     * */
    String cityName;
    /**
     * 司机ID
     * */
    Long driverId;
    /**
     * 司机名称
     * */
    String driverName;
    /**
    * 订单更新时间
    * */
    Long utime;
    /**
     * 订单预计到达时间(expected to arrival)
     * */
    Long eta;

    public OrderDetail(Long orderId, Long cityId, String cityName, Long driverId, String driverName) {
        this.orderId = orderId;
        this.cityId = cityId;
        this.cityName = cityName;
        this.driverId = driverId;
        this.driverName = driverName;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId=" + orderId +
                ", cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                ", driverId=" + driverId +
                ", driverName='" + driverName + '\'' +
                ", ctime=" + DateUtil.fromTimestamp(utime) +
                ", eta=" + DateUtil.fromTimestamp(eta) +
                '}';
    }
}
