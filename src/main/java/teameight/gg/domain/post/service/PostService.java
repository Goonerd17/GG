package teameight.gg.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
        return ok(postRepository.serachPostBySlice(condition, pageable));
    }

    @Transactional
    public ApiResponse<?> createPost(PostRequestDto postRequestDto, MultipartFile image, User user) {
        String imageUrl = s3Service.upload(image);
        postRepository.save(new Post(postRequestDto, imageUrl, user));
        return okWithMessage(POST_CREATE_SUCCESS);
    }

    public ApiResponse<?> getSinglePost(Long postId) {
        Post post = postRepository.findDetailPost(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));
        return ok(new PostResponseDto(post));
    }

    @Transactional
    public ApiResponse<?> updatePost(Long postId, PostRequestDto postRequestDto, MultipartFile image, User user) {
        Post post = confirmPost(postId, user);
        post.update(postRequestDto);
        updatePostImage(image, post);
        return okWithMessage(POST_UPDATE_SUCCESS);
    }

    private void updatePostImage(MultipartFile image, Post post) {
        // 이미지 업로드
        if (image != null && !image.isEmpty()) {
            String existingImageUrl = post.getImage();
            String imageUrl = s3Service.upload(image);
            post.setImage(imageUrl); 
            // 리팩토링 해야됨

            // 새로운 이미지 업로드 후에 기존 이미지 삭제
            if (StringUtils.hasText(existingImageUrl)) {
                s3Service.delete(existingImageUrl);
            }
        }
    }

    @Transactional
    public ApiResponse<?> deletePost(Long postId, User user) {
        Post post = confirmPost(postId, user);
        deleteImage(post);
        postRepository.delete(post);
        return okWithMessage(POST_DELETE_SUCCESS);
    }


    private void deleteImage(Post post) {
        String imageUrl = post.getImage();
        if (StringUtils.hasText(imageUrl)) {
            s3Service.delete(imageUrl);
        }
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));
    }

    private Post confirmPost(Long postId, User user) {
        Post post = findPost(postId);
        if (!user.getId().equals(post.getUser().getId())) {
            throw new InvalidConditionException(USER_NOT_MATCH);
        }
        return post;
    }
}

