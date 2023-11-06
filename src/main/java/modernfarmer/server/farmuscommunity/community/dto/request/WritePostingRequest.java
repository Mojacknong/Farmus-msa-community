package modernfarmer.server.farmuscommunity.community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WritePostingRequest {

    private String title;

    private String contents;

    private ArrayList<String> tags;
}
