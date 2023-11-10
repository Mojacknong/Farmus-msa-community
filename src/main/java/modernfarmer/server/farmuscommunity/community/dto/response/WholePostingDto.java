package modernfarmer.server.farmuscommunity.community.dto.response;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class WholePostingDto {

        private Integer userId;

        private String title;

        private String contents;

        private List<String> postingImage;

        private Long postingId;

        private List<String> tagName;

        private String created_at;

        private int commentCount;

        private String nickName;

        private String userImageUrl;





}

