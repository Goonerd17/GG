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
import teameight.gg.global.exception.InvalidConditionException;
import teameight.gg.global.responseDto.ApiResponse;

import static teameight.gg.global.stringCode.ErrorCodeEnum.POST_NOT_EXIST;
import static teameight.gg.global.stringCode.ErrorCodeEnum.USER_NOT_MATCH;
import static teameight.gg.global.stringCode.SuccessCodeEnum.*;
import static teameight.gg.global.utils.ResponseUtils.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;

    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        Slice<PostResponseDto> postSlice = postRepository.serachPostBySlice(condition, pageable);
        return successWithData(postSlice);
    }

    @Transactional
    public ApiResponse<?> createPost(PostRequestDto postRequestDto, MultipartFile image, User user) {
        String imageUrl = s3Service.upload(image, "GG");
        postRepository.save(new Post(postRequestDto, imageUrl, user));
        return success(POST_CREATE_SUCCESS);
    }

    public ApiResponse<?> getSinglePost(Long postId) {
        Post post = postRepository.findDetailPost(postId).orElseThrow(()->
                new InvalidConditionException(POST_NOT_EXIST));
        return successWithData(new PostResponseDto(post));
    }

    @Transactional
    public ApiResponse<?> updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        Post post = confirmPost(postId, user);
        post.update(postRequestDto);
        return success(POST_UPDATE_SUCCESS);
    }

    @Transactional
    public ApiResponse<?> deletePost(Long postId, User user) {
        Post post = confirmPost(postId, user);
        postRepository.delete(post);
        return success(POST_DELETE_SUCCESS);
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(()->
                new InvalidConditionException(POST_NOT_EXIST));
    }

    private Post confirmPost(Long postId, User user) {
        Post post = findPost(postId);
        if (!(user.getId() == post.getUser().getId()))
            throw new InvalidConditionException(USER_NOT_MATCH);
        return post;
    }
}

