package modernfarmer.server.farmuscommunity.community.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/community/comment")
public class CommentController {

    @GetMapping()
    public String test(){
        return "HI";
    }
}
