package modernfarmer.server.farmuscommunity.community.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import modernfarmer.server.farmuscommunity.community.entity.PostingImage;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SpecificPostingDto {

    private Integer userId;

    private String title;

    private String contents;

    private Set<PostingImage> postingImage;

    private Long postingId;

    private String tag;

    private String created_at;

    private int commentCount;

    private String nickName;

    private String userImageUrl;

}
