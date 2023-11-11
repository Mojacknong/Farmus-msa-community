package modernfarmer.server.farmuscommunity.community.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PostingCommentDto {

    private Integer userId;

    private Long postingId;

    private int commentCount;

    private String created_at;

    private String commentContents;

    private String nickName;

    private String userImageUrl;
}
