package teameight.gg.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import teameight.gg.domain.post.dto.PostRequestDto;
import teameight.gg.domain.post.dto.PostResponseDto;
import teameight.gg.domain.post.dto.PostSearchCondition;
import teameight.gg.domain.post.entity.Post;
import teameight.gg.domain.user.entity.User;
import teameight.gg.domain.post.repository.PostRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;

    public Slice<PostResponseDto> searchPost(PostSearchCondition condition, Pageable pageable) {
        return postRepository.serachPostBySlice(condition, pageable);
    }

    @Transactional
    public String createPost(PostRequestDto postRequestDto,MultipartFile image, User user) {
        String imageUrl = s3Service.upload(image, "GG");
        postRepository.save(new Post(postRequestDto, imageUrl, user));
        return "게시글 작성 성공";
    }

    public PostResponseDto getSinglePost(Long postId) {
        Post post = postRepository.findDetailPost(postId).orElseThrow(()->
                new IllegalArgumentException("해당 게시글은 존재하지 않습니다"));
        return new PostResponseDto(post);
    }

    @Transactional
    public String updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        Post post = confirmPost(postId, user);
        post.update(postRequestDto);
        return "게시글 수정 성공";
    }

    @Transactional
    public String deletePost(Long postId, User user) {
        Post post = confirmPost(postId, user);
        postRepository.delete(post);
        return "게시글 삭제 성공";
    }

    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(()->
                new IllegalArgumentException("해당 게시글은 존재하지 않습니다"));
    }

    public Post confirmPost(Long postId, User user) {
        Post post = findPost(postId);
        if (!(user.getId() == post.getUser().getId()))
            throw new IllegalArgumentException("해당 게시글 작성자만 수정,삭제할 수 있습니다");
        return post;
    }
}

