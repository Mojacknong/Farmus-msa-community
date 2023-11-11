package modernfarmer.server.farmuscommunity.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Builder
public class PostingCommentResponseDto {

    ArrayList<PostingCommentDto> postingCommentList;
}