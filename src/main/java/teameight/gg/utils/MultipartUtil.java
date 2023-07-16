package teameight.gg.utils;

import org.springframework.util.StringUtils;

import java.util.UUID;

public class MultipartUtil {

    private static final String BASE_DIR = "image";

    /**
     * 로컬에서 사용자 홈 디렉토리 경로 반환
     */
    public static String getLocalHomeDirectory() {
        return System.getProperty("user.home");
    }

    /**
     * 새로운 파일 고유 아이디 생성
     * 기존 파일 이름 중복가능성이 있기 때문에 대체
     */
    public static String createFileId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Multipart 의 Content-Type 값에서 . 이후의 확장자만 가져오기
     */
    public static String getFormat(String contentType) {
        if (StringUtils.hasText(contentType)) {
            return contentType.substring(contentType.lastIndexOf(".") + 1);
        }
        return null;
    }

    /**
     * 파일 전체 경로 생성
     */
    public static String createPath(String fileId, String format) {
        return String.format("%s/%s.%s", BASE_DIR, fileId, format);
    }
}
