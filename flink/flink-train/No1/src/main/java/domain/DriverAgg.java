package domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @className: DriverAgg
 * @description:
 * @author: zhangzhen
 * @date: 2023/1/25 08:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverAgg {
    long driverId;
    String driverName;
    long orderCnt;
    long overtimeCnt;

    public DriverAgg(long driverId, String driverName) {
        this.driverId = driverId;
        this.driverName = driverName;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\t骑手:"+driverName+"(id="+driverId+")\n");
        sb.append("\t\t总单量:"+orderCnt+"\n");
        sb.append("\t\t超时单量:"+overtimeCnt+"\n");
        return sb.toString();
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
}
