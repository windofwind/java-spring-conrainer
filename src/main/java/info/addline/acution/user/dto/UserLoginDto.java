package info.addline.acution.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 사용자 로그인 요청을 위한 DTO 클래스입니다.
 *
 * <p>사용자가 시스템에 로그인할 때 필요한 인증 정보를 담고 있으며,
 * 이메일 형식의 아이디로 로그인을 지원합니다.</p>
 *
 * <p>주요 특징:</p>
 * <ul>
 *   <li>이메일 형식의 아이디로 로그인</li>
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
     * 이메일 주소입니다.
     * 로그인 시 사용되는 이메일 형식의 아이디입니다.
     */
    @Schema(description = "이메일", example = "john@example.com", required = true)
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    /**
     * 로그인에 사용되는 비밀번호입니다.
     * 평문으로 입력되며, 서버에서 암호화된 비밀번호와 비교됩니다.
     */
    @Schema(description = "비밀번호", example = "password123", required = true)
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 255, message = "비밀번호는 8-255자 사이여야 합니다")
    private String password;

    /**
     * 기본 생성자입니다.
     * JSON 직렬화/역직렬화를 위해 필요합니다.
     */
    public UserLoginDto() {}

    /**
     * 로그인 정보를 설정하는 생성자입니다.
     *
     * @param email 이메일 주소
     * @param password 비밀번호 (평문)
     */
    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * 이메일 주소를 반환합니다.
     *
     * @return 이메일 주소
     */
    public String getEmail() {
        return email;
    }

    /**
     * 이메일 주소를 설정합니다.
     *
     * @param email 설정할 이메일 주소
     */
    public void setEmail(String email) {
        this.email = email;
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
