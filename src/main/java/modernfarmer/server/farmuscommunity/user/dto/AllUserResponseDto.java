package modernfarmer.server.farmuscommunity.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
public class AllUserResponseDto {
    List<AllUserDto> allUserDtoList;
}
