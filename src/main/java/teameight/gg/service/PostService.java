package teameight.gg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teameight.gg.dto.PostRequestDto;
import teameight.gg.dto.PostResponseDto;
import teameight.gg.dto.PostSearchCondition;
import teameight.gg.entity.Like;
import teameight.gg.entity.Post;
import teameight.gg.entity.User;
import teameight.gg.repository.LikeRepository;
import teameight.gg.repository.PostRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Post post = postRepository.save(new Post(postRequestDto, user));
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getSinglePost(Long postID) {
        Post post = findPost(postID);
        return new PostResponseDto(post);
    }

    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        Post post = confirmPost(postId, user);
        post.update(postRequestDto);
        return new PostResponseDto(post);
    }

    public String deletePost(Long postId, User user) {
        Post post = confirmPost(postId, user);
        postRepository.delete(post);
        return "삭제완료";
    }

    @Transactional(readOnly = true)
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(()->
                new IllegalArgumentException("해당 게시글은 존재하지 않습니다"));
    }

    private Post confirmPost(Long postId, User user) {
        Post post = findPost(postId);
        if (!(user.getId() == post.getUser().getId()))
            throw new IllegalArgumentException("해당 게시글 작성자만 수정,삭제할 수 있습니다");
        return post;
    }

    @Transactional(readOnly = true)
    public Slice<PostResponseDto> searchPost(PostSearchCondition condition, Pageable pageable) {
        return postRepository.serachPostBySlice(condition, pageable);
    }

    public PostResponseDto updateLike(Long postId, User user) {
        Post post = findPost(postId);

        if (!isLikedPost(post, user)) {
            post.increaseLike();
            createLike(post, user);
            return new PostResponseDto(post);
        }

        post.decreaseLike();
        removeLike(post, user);
        return new PostResponseDto(post);
    }

    public boolean isLikedPost(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user).isPresent();
    }

    public void createLike(Post post, User user) {
        Like like = new Like(post, user);
        likeRepository.save(like);
    }

    public void removeLike(Post post, User user) {
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow();
        likeRepository.delete(like);
    }
}

