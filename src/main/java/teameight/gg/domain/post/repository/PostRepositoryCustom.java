package teameight.gg.domain.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import teameight.gg.domain.post.dto.PostResponseDto;
import teameight.gg.domain.post.dto.PostSearchCondition;

public interface PostRepositoryCustom {

    Slice<PostResponseDto> serachPostBySlice(PostSearchCondition condition, Pageable pageable);
}
