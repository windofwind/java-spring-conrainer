package info.addline.acution.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 사용자 회원가입 요청을 위한 DTO 클래스입니다.
 *
 * <p>새로운 사용자가 시스템에 가입할 때 필요한 정보를 담고 있으며,
 * Bean Validation을 통해 입력 데이터의 유효성을 검증합니다.</p>
 *
 * <p>주요 특징:</p>
 * <ul>
 *   <li>사용자명, 이메일, 비밀번호는 필수 입력</li>
 *   <li>전체 이름과 전화번호는 선택 입력</li>
 *   <li>Swagger 문서화를 위한 스키마 설정</li>
 *   <li>서버 측 유효성 검증</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see info.addline.acution.user.entity.User
 * @see UserResponseDto
 */
@Schema(description = "사용자 회원가입 요청 DTO")
public class UserRegistrationDto {

    /**
     * 사용자명입니다.
     * 로그인 시 사용되며, 시스템 내에서 고유해야 합니다.
     */
    @Schema(description = "사용자명", example = "johndoe", required = true)
    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 3, max = 20, message = "사용자명은 3-20자 사이여야 합니다")
    private String username;

    /**
     * 이메일 주소입니다.
     * 로그인 및 연락처로 사용되며, 시스템 내에서 고유해야 합니다.
     */
    @Schema(description = "이메일", example = "john@example.com", required = true)
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    /**
     * 비밀번호입니다.
     * 평문으로 입력되며, 서버에서 암호화되어 저장됩니다.
     */
    @Schema(description = "비밀번호", example = "password123", required = true)
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 255, message = "비밀번호는 8-255자 사이여야 합니다")
    private String password;

    /**
     * 사용자의 전체 이름입니다.
     * 선택적 필드로, 프로필 표시에 사용됩니다.
     */
    @Schema(description = "전체 이름", example = "John Doe")
    private String fullName;

    /**
     * 사용자의 전화번호입니다.
     * 선택적 필드로, 연락처 정보로 사용됩니다.
     */
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    /**
     * 기본 생성자입니다.
     * JSON 직렬화/역직렬화를 위해 필요합니다.
     */
    public UserRegistrationDto() {}

    /**
     * 필수 필드를 설정하는 생성자입니다.
     *
     * @param username 사용자명
     * @param email 이메일 주소
     * @param password 비밀번호 (평문)
     */
    public UserRegistrationDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * 사용자명을 반환합니다.
     *
     * @return 사용자명
     */
    public String getUsername() {
        return username;
    }

    /**
     * 사용자명을 설정합니다.
     *
     * @param username 설정할 사용자명
     */
    public void setUsername(String username) {
        this.username = username;
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
     * 비밀번호를 반환합니다.
     *
     * @return 비밀번호 (평문)
     */
    public String getPassword() {
        return password;
    }

    /**
     * 비밀번호를 설정합니다.
     *
     * @param password 설정할 비밀번호 (평문)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 전체 이름을 반환합니다.
     *
     * @return 전체 이름
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 전체 이름을 설정합니다.
     *
     * @param fullName 설정할 전체 이름
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 전화번호를 반환합니다.
     *
     * @return 전화번호
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 전화번호를 설정합니다.
     *
     * @param phoneNumber 설정할 전화번호
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
