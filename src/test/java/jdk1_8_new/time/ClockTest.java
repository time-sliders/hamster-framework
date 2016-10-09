package jdk1_8_new.time;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class ClockTest {

    public static void main(String[] args) {

        //-----时间的一些API-----
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.toString());

        // 日期的格式化
        // 不同于原来的java.text.DateFormat 这里的 DateTimeFormatter 是线程安全的
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
        System.out.println(now.format(formatter));

        // 日期解析
        now = LocalDateTime.parse("2016-09-10 11:10:20,111", formatter);
        System.out.println(now);

        // 日期时间设置
        System.out.println(now.withHour(3));
        System.out.println(now.withDayOfMonth(1));
        System.out.println(now.with(ChronoField.YEAR,1990));
        // 日期时间的运算
        System.out.println(now.plusDays(1));
        System.out.println(now.plusDays(-1));


        //-----时区的一些API-----
        System.out.println(ZoneId.getAvailableZoneIds());

        // 创建带有时区的时间
        ZonedDateTime zonedLocalDateTime = ZonedDateTime.now();
        System.out.println("zonedLocalDateTime:" + zonedLocalDateTime);
        ZonedDateTime zonedRemoteDateTime;
        // 当前时区的时间 偏移到 另外一个时区之后的时间
        zonedRemoteDateTime = zonedLocalDateTime.withZoneSameInstant(ZoneId.of("Asia/Aden"));// ZoneId.of("+08:00")
        System.out.println("zonedRemoteDateTime:" + zonedRemoteDateTime);
        // 设置时区
        zonedRemoteDateTime = zonedLocalDateTime.withZoneSameLocal(ZoneId.of("Asia/Aden"));
        System.out.println("zonedRemoteDateTime:" + zonedRemoteDateTime);

        //------时间与时区的转换------
        zonedLocalDateTime = now.atZone(ZoneId.systemDefault());
        now = zonedLocalDateTime.toLocalDateTime();

        //Clock 时区API
        Clock clock = Clock.systemDefaultZone();
        System.out.println(clock);
        Clock c2 = clock.withZone(ZoneId.of("Africa/Nairobi"));
        System.out.println(c2);
    }

}
