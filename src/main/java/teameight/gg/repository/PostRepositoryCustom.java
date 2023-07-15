package teameight.gg.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import teameight.gg.dto.PostResponseDto;
import teameight.gg.dto.PostSearchCondition;

public interface PostRepositoryCustom {

    Slice<PostResponseDto> serachPostBySlice(PostSearchCondition condition, Pageable pageable);

}
