package modernfarmer.server.farmuscommunity.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import modernfarmer.server.farmuscommunity.community.entity.ReportTag;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
public class ReportTagResponse {

    List<ReportTag> reportTags;
}
