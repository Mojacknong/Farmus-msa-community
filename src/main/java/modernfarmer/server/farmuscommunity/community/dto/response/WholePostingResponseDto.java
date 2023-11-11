package modernfarmer.server.farmuscommunity.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Builder
public class WholePostingResponseDto {

    List<WholePostingDto> wholePostingDTOList;
}
