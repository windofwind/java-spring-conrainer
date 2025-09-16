package info.addline.acution.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 사용자 로그인 요청을 위한 DTO 클래스입니다.
 *
 * <p>사용자가 시스템에 로그인할 때 필요한 인증 정보를 담고 있으며,
 * 사용자명 또는 이메일 두 가지 방식으로 로그인을 지원합니다.</p>
 *
 * <p>주요 특징:</p>
 * <ul>
 *   <li>사용자명 또는 이메일 중 하나로 로그인 가능</li>
 *   <li>비밀번호 필드 포함</li>
 *   <li>Bean Validation을 통한 입력 데이터 검증</li>
 *   <li>Swagger 문서화 지원</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see UserResponseDto
 */
@Schema(description = "사용자 로그인 요청 DTO")
public class UserLoginDto {

    /**
     * 로그인 식별자입니다.
     * 사용자명 또는 이메일 주소를 둘 다 지원합니다.
     */
    @Schema(description = "사용자명 또는 이메일", example = "johndoe", required = true)
    @NotBlank(message = "사용자명 또는 이메일은 필수입니다")
    private String usernameOrEmail;

    /**
     * 로그인에 사용되는 비밀번호입니다.
     * 평문으로 입력되며, 서버에서 암호화된 비밀번호와 비교됩니다.
     */
    @Schema(description = "비밀번호", example = "password123", required = true)
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    /**
     * 기본 생성자입니다.
     * JSON 직렬화/역직렬화를 위해 필요합니다.
     */
    public UserLoginDto() {}

    /**
     * 로그인 정보를 설정하는 생성자입니다.
     *
     * @param usernameOrEmail 사용자명 또는 이메일
     * @param password 비밀번호 (평문)
     */
    public UserLoginDto(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    /**
     * 로그인 식별자를 반환합니다.
     *
     * @return 사용자명 또는 이메일
     */
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    /**
     * 로그인 식별자를 설정합니다.
     *
     * @param usernameOrEmail 설정할 사용자명 또는 이메일
     */
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    /**
     * 로그인 비밀번호를 반환합니다.
     *
     * @return 비밀번호 (평문)
     */
    public String getPassword() {
        return password;
    }

    /**
     * 로그인 비밀번호를 설정합니다.
     *
     * @param password 설정할 비밀번호 (평문)
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
