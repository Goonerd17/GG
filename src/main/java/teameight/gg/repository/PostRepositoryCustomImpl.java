package teameight.gg.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import teameight.gg.dto.PostResponseDto;
import teameight.gg.dto.PostSearchCondition;
import teameight.gg.entity.Post;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.*;
import static teameight.gg.entity.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom{

    private final JPAQueryFactory query;

    @Override
    public Slice<PostResponseDto> serachPostBySlice(PostSearchCondition condition, Pageable pageable) {
        List<Post> result = query
                .selectFrom(post)
                .where(usernameEq(condition.getUsername()), titleEq(condition.getTitle()))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<PostResponseDto> content = new ArrayList<>();
        for (Post post : result) {
            content.add(new PostResponseDto(post));
        }
        return checkEndPage(pageable, content);
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return hasText(usernameCond) ? post.username.eq(usernameCond) : null;
    }

    private BooleanExpression titleEq(String titleCond) {
        return hasText(titleCond) ? post.title.eq(titleCond) : null;
    }

    private static SliceImpl<PostResponseDto> checkEndPage(Pageable pageable, List<PostResponseDto> content) {
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }
}
