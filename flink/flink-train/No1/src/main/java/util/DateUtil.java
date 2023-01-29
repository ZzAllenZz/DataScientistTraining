package util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * @className: DateUtil
 * @description:
 * @author: zhangzhen
 * @date: 2023/1/28 16:28
 **/
public class DateUtil {

    private static final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMdd");
    private static final FastDateFormat dateHourFormat = FastDateFormat.getInstance("yyyyMMddHH");
    private static final FastDateFormat fullTimeFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    private static final FastDateFormat fullTimeFormatInMillis = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");

    public static String fromTimestamp(Long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return fullTimeFormat.format(calendar.getTime());
    }
}
