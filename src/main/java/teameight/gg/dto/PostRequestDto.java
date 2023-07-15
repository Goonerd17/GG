package teameight.gg.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String title;
    private String description;
    private String like;
    private String dislike;
}
