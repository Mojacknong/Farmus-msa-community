package modernfarmer.server.farmuscommunity.user;


import modernfarmer.server.farmuscommunity.user.dto.AllUserResponseDto;
import modernfarmer.server.farmuscommunity.user.dto.BaseResponseDto;
import modernfarmer.server.farmuscommunity.user.dto.SpecificUserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "user-service")
public interface UserServiceFeignClient {


    @GetMapping(value = "/api/user/all-user", consumes = "application/json")
    BaseResponseDto allUser();



    @GetMapping(value = "/api/user/specific-user")
    BaseResponseDto specificUser(@RequestParam("userId") Long userId);



}
