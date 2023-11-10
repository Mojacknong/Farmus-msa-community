package modernfarmer.server.farmuscommunity.user;


import modernfarmer.server.farmuscommunity.user.dto.BaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "user-service", url = "http://3.38.2.59:8081")
public interface UserServiceFeignClient {


    @GetMapping(value = "/api/user/all-user", consumes = "application/json")
    BaseResponseDto allUser();



    @GetMapping(value = "/api/user/specific-user")
    BaseResponseDto specificUser(@RequestParam("userId") Long userId);



}
