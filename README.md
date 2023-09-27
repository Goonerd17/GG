# 게임 소개 및 리뷰 사이트
<img src='./src/main/resources/images/GGmain.png' width="100%">  

## 목차
[프로젝트 개요](#1-프로젝트-개요)  

[프로젝트 소개](#2-프로젝트-소개)  

[기술스택](#3-기술스택)  

[기술적 의사결정](#4-기술적-의사결정)  

[트러블슈팅 및 고민 점](#5-트러블-슈팅-및-고민-점)  

* * *

### 1 프로젝트 개요

- 팀원 : 유시환, 김광균, 이성목
- 기간 : 2022년 07월 16일 ~ 07월 21일, 5일 간 진행
- 이미지 업로드, 검색이 가능한 게임 소개 및 리뷰 사이트 프로젝트입니다.

* * *  

### 2 프로젝트 소개

<details>
<summary> 담당 역할 </summary>

|역할|담당자|기능|
|:--|:--|:--|
|회원가입, 로그인페이지|유시환,김광균|회원가입, 로그인|
|메인페이지|유시환|게시글 조회|
|검색페이지|유시환|작성내용, 작성자 검색|
|상세페이지|유시환,이성목,김광균|게시글 작성,수정,삭제 댓글 작성,수정,삭제|
|배포|유시환|서버 배|
</details>

<details>
<summary> 회원가입, 로그인 페이지 </summary>  
  <img src='./src/main/resources/images/GGsignuplogin.png' width="70%">  
  - 회원가입, 로그인 
</details>

<details>
<summary> 메인페이지 </summary>  
  <img src='./src/main/resources/images/GGmain.png' width="70%">
  - 게시물 조회, 검색    
</details>

<details>
<summary> 검색페이지 </summary>  
   <img src='./src/main/resources/images/GGsearchtitle.png' width="70%"> 
  - 제목 검색  

   <img src='./src/main/resources/images/GGsearchwriter.png' width="70%">
  - 작성자 점

<details>
<summary>1. CORS </summary>  
  
  - 프론트엔드와 통신과정에서 CORS 에러가 반복적으로 발생했습니다.
    
  - 해결과정에서 별도의 class 생성 또는 Spring Security의 수동 빈 등록의 방법이 있었는데, 최대한 Spring Security를 활용해보고자 하는 생각에서 후자를 이용해 요청 리소스의 정보를 작성하고 이를 허용하도록 설정했습니다.

  - 그럼에도 CORS가 해결되지 않았는데 이는 preflight 요청 메서드를 명시하지 않았기 때문에 발생하는 에러였고, 이를 위해  OPTIONS 메서드 허용과 exposedHeader를 설정하여 프론트엔드와 원활한 통신이 가능했습니다.
    
```java
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT","OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.addExposedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
```
</details>  

<details>
<summary>2. Response 타입 변경과 정적 메서드 사용 </summary>  
  
  - 기존의 DTO를 사용한 Response는 에러 발생 시, 메세지를 확인하기도 어렵고, 단순 데이터만 있었기에 프론트엔드 측에서 불편함을 이야기했습니다.

  - 따라서 '요청 성공 여부, Dto, 에러 여부 + 에러 메세지' 데이를 프론트엔드에게 제공하고자 했습니다.
    
  - Generic을 이용하여 응답에 담길 성공 여부와 데이터, 에러를 종합하는 ApiResponse<T>를 생성했고, ApiResponse에 실제 값들을 담을 수 있는 ResponseUtil 클래스와 정적 메서드를 작성했습니다.
    
```java
@Getter
@NoArgsConstructor
public class ApiResponse<T>{

    private boolean success;
    private T info;
    private ErrorResponse error;

    public ApiResponse(boolean success, T info, ErrorResponse error) {
        this.success = success;
        this.info = info;
        this.error = error;
    }
}

@Getter
@NoArgsConstructor
public class ResponseUtils {

    public static <T> ApiResponse<T> ok(T response) {
        return new ApiResponse<>(true, response, null);
    }

    public static ApiResponse<?> okWithMessage(SuccessCodeEnum successCodeEnum) {
        return new ApiResponse<>(true, successCodeEnum.getMessage(), null);
    }
    public static ApiResponse<?> error(String message, int status) {
        return new ApiResponse<>(false, null, new ErrorResponse(message, status));
    }

    public static ApiResponse<?> customError(ErrorCodeEnum errorCodeEnum) {
        return new ApiResponse<>(false, null, new ErrorResponse(errorCodeEnum));
    }
}
```

  - 정적 메서드를 이용하였기에, ResponseUtil 클래스의 객체를 직접 생성하지 않아도, 해당 클래스의 메서드를 직접 호출할 수 있고, 입력 값을 기반으로 결과를 반환하는 기능을 가지기 때문에 상태를 유지할 필요가 없게 설정하였습니다. 또한 멤버변수를 설정하지 않았기 때문에 변수가 공유되어 값이 의도치 않게 변경되는 일도 방지했습니다.
  - 메서드의 이름을 통해 그 기능을 명확하게 알 수 있고, 필요하다면 static import를 통해 코드를 보다 간결하게 유지할 수 있다는 점, 인스턴스의 생성이 불필요하므로 객체 관리, 메모리 사용이 줄어들어 사용에 있어 부담이 줄어드는 이점 등을 고려하여 ResponseUtil 클래스의 메서드들을 정적 메서드로 작성했습니다.
</details>  

<details>
<summary>3. 동적 쿼리 </summary>  
  
  - 프론트엔드와 통신과정에서 CORS 에러가 반복적으로 발생했습니다.
    
  - 해결과정에서 별도의 class 생성 또는 Spring Security의 수동 빈 등록의 방법이 있었는데, 최대한 Spring Security를 활용해보고자 하는 생각에서 후자를 이용해 요청 리소스의 정보를 작성하고 이를 허용하도록 설정했습니다.

  - 그럼에도 CORS가 해결되지 않았는데 이는 preflight 요청 메서드를 명시하지 않았기 때문에 발생하는 에러였고, 이를 위해  OPTIONS 메서드 허용과 exposedHeader를 설정하여 프론트엔드와 원활한 통신이 가능했습니다.
    
```java
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT","OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.addExposedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
```
</details>  


### 1-3) 동적 쿼리

- 검색은 기본적으로 게시글의 제목과 작성자를 바탕으로 기능하는 방식을 구상하였습니다. 어떤 값에 null이 들어와도 다른 기준을 바탕으로, 혹은 두 값 모두 null이라면 모든 게시글을 반환하는 로직이 필요했습니다.
- JPQL 이나 네이티브쿼리를 작성하기에는 작성 과정에 있어 많이 헷갈리고, 컴파일 시점에서 에러를 잡을 수 없다는 큰 단점이 있어 Querydsl을 선택했습니다.
- 검색 조건의 변경을 염두에 두고 요구사항 변경에도 조건 간의 조합으로 유연성의 이점을 봉줄 수 있는 BooleanExpression 타입을 기반으로 검색기능을 구현했습니다.
- 추가적으로 검색과정에서 Post 엔티티를 찾고 반복문을 사용하여 Dto에 데이터를 담아줬는데 @QueryProjections를 사용하여 직접 select절에서 해당 값들을 Dto에 담아주었습니다. 서비스의 특성을 고려했을 때, 게시글 조회 상황에서 댓글까지 노출할 필요가 없다고 생각했기 때문에 comment를 제외한 다른 값들을 담는 개별적인 Dto를 사용하는 게 좋겠다고 생각을 했습니다. 이를 바탕으로 @QueryProjections를 사용하게 되었습다.

### 1-4) 예외 처리

- 코드의 일관성과 예외메세지를 일괄적으로 처리할 수 있도록 재구성하고자 했습니다.
- Enum을 통해 해당 예외 메세지들을 한 곳에서 관리하고 IllegalargumentException을 상속받는 InvalidConditionException을 생성하였습니다.
- 다만 모든 예외를 커스텀하여 처리하는 것보다 해당 예외의 원인이 이미 자바에서 제공하는 예외로 설명이 가능하다면, 이를 활용하는 것이 더 효율적이라는 걸 느낄 수 있었습니다.
- 구체적인 설명이 필요하거나, 서비스에서 중요하게 다루어져야 하는 예외라면 이를 커스텀예외로 처리하는 방식을 선택하고자 했고, 해당 프로젝트의 규모는 작다고 판단했기에 크게 InvalidCondtionException과 UploadException로 나누고 예외처리를 진행하였습니다.

### 1-5) 이미지 데이터 핸들링을 위한 S3와 multipart 사용

- 처음에는 단순히 이미지URL을 직접 String으로 받아 DB에 저장하려고 했습니다. 하지만 이미지 URL이 너무 긴 경우 DB 저장에서 오류가 발생하고, 만약 해당 URL에서 이미지의 변경,손상이 일어난다면 게시판의 이미지도 같은 상태로 변경될 수 있습니다. 이러한 점에서 유저에게 큰 불편을 초래할 수 있다고 판단하였기에 파일 자체를 저장할 수 있는 방법을 찾아야했습니다.
- 단건 이미지 파일 업로드로 프로젝트를 진행하겠다고 프론트엔드에서 이야기했기 때문에 파일과 JSON을 동시에 받는 방식을 찾게 되었고, @RequestPart를 이용하여 이미지 파일 데이터를 받았습니다.
- S3에 저장까지는 가능했으나 수정,삭제 부분이 생각보다 까다로웠지만, 리팩토링을 통하여 확장자 추출, 고유 파일명 생성, 객체 키 추출 등을 진행하였습니다.
- 이미지 수정의 경우, 새롭게 전송된 이미지를 S3에 저장한 후 DB에 저장되어 있는 기존 이미지의 URL에서 객체 키를 추출한 뒤 S3에 해당 객체를 삭제하는 방식으로 진행하여 기능을 구현하였습니다.
  

### 1-6) n + 1 문제와 카르테시안 곱

- Post 엔티티의 List로 존재하는 Comment 엔티티를 조회할 때 n + 1 문제가 발생할 것이 예상되었으므로 이 부분에 대해서 left join fetch를 사용했습니다.
- 실제 쿼리를 날려보니 comment의 username을 조회하기 위해 user들에 대해서도 쿼리를 날리게 되어 comment를 작성한 user가 5명일 경우 6번의 쿼리가 발생하는 문제가 발생하였습니다.
- 프로젝트 막바지에 해당 문제를 확인해서 이에 대해 미리 포착하지 못한 불찰이 아쉬웠지만, 최대한 주어진 시간 내에 해당 문제를 해결할 수 있는 방법을 찾고자 했습니다.
- "left join fetch p.commentList cl join fetch cl.user" 쿼리로 기존 문제를 해결할 수 있었지만, 이러한 방식은 실제 row는 10개에 불과한 데이터가 반복적인 join fetch로 인해 row가 뻥튀기 되고 20,30, 혹은 그 이상의 반복되는 row를 가져올 수 있는 카르테시안 곱 문제가 발생할 수 있다고 생각했습니다.
- 따라서 컬렉션의 요소들을 서브쿼리로 이용하여 가져오는 방법인 @Fetch를 이용하여 post에 대한 쿼리 한 번, comment에 대한 쿼리 한 번(서브쿼리 이용)을 이용해 데이터를 가져올 수 있었습니다.
- 쿼리 자체의 수는 늘어났지만 과도한 데이터 부풀림 현상을 방지할 수 있기 때문에 기존 해결 방법보다 더 합리적이라고 생각했습니다.


### 1-7) 협업

- 처음 프론트엔드와의 협업은 생각보다 쉽지 않았습니다. 각 분야에 대해서 깊이있는 이해가 있지 않는 이상 빈번하고 생산성있는 의사소통이 프로젝트를 진행하는 데 중요했습니다.
- 또한 분명하고 이해하기 쉬운 API 명세는 꼭 필요하며 명세 작성 과정은 팀원들이 모두 공유하고 참여해야한다는 것도 알 수 있었습니다.
