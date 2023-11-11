package modernfarmer.server.farmuscommunity.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
public class AllUserDto {

    private Long Id;
    private String nickName;
    private String imageUrl;



}
