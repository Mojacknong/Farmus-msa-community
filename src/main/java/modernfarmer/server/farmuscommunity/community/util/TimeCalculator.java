package modernfarmer.server.farmuscommunity.community.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class TimeCalculator {

    public String formatCreatedAt(LocalDateTime createdAt) {
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");

        return createdAt.atZone(zoneId).format(formatter);
    }
}
