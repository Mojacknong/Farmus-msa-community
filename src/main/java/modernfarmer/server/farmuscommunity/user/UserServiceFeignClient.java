package modernfarmer.server.farmuscommunity.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import modernfarmer.server.farmuscommunity.user.dto.AllUserResponseDto;
import modernfarmer.server.farmuscommunity.user.dto.BaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient("user-service")
public interface UserServiceFeignClient {


    @GetMapping(value = "/api/user/all-user", consumes = "application/json")
    BaseResponseDto allUser();

}
