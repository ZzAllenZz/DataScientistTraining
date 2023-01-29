package domain;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import util.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @className: CityAgg
 * @description: 城市粒度聚合结果对象
 * @author: zhangzhen
 * @date: 2023/1/28 17:13
 **/
@Data
public class CityAgg {
    long cityId;
    String cityName;
    long orderCnt;
    long overtimeCnt;
    List<DriverAgg> drivers = new ArrayList<>();
    String timeWindow;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n统计时间:"+timeWindow+"\n");
        sb.append("城市:"+cityName+"(id="+cityId+")\n");
        sb.append("总单量:"+orderCnt+"\n");
        sb.append("超时单量:"+overtimeCnt+"\n");
        sb.append("【骑手统计】\n");
        drivers.forEach(d->sb.append(d.toString()));
        sb.append("\n\n");
        return sb.toString();
    }



    public void process(OrderDetail orderDetail, Long timestamp){
        cityId=orderDetail.getCityId();
        cityName=orderDetail.getCityName();
        timeWindow= DateUtil.fromTimestamp(timestamp);
        add1Order();
        if(timestamp>orderDetail.eta){
            add1Overtime();
        }
    }

    public void addDriver(DriverAgg driverAgg){
        drivers.add(driverAgg);
    }

    public void add1Order(){
        addOrder(1L);
    }

    public void addOrder(Long num){
        orderCnt+=num;
    }

    public void add1Overtime(){
        addOvertime(1L);
    }

    public void addOvertime(Long num){
        overtimeCnt+=num;
    }

    public Set<Long> getDriverIds(){
        return drivers.stream().map(d->d.driverId).collect(Collectors.toSet());
    }

    public boolean containsDriver(long driverId){
        return getDriverIds().contains(driverId);
    }

    public DriverAgg getDriverById(long driverId){
        Map<Long, DriverAgg> driverMap = drivers.stream().collect(Collectors.toMap(DriverAgg::getDriverId, Function.identity()));
        return driverMap.get(driverId);
    }
}
