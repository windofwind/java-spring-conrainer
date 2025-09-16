package info.addline.acution.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * 사용자 정보 수정 요청을 위한 DTO 클래스입니다.
 *
 * <p>기존 사용자의 정보를 수정할 때 사용되며,
 * 모든 필드가 선택적입니다. null이 아닌 값만 업데이트됩니다.</p>
 *
 * <p>주요 특징:</p>
 * <ul>
 *   <li>모든 필드는 선택적 (제공된 값만 업데이트)</li>
 *   <li>이메일 변경 시 중복 검사 수행</li>
 *   <li>새 비밀번호 제공 시 암호화 처리</li>
 *   <li>Bean Validation을 통한 입력 데이터 검증</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see UserResponseDto
 * @see info.addline.acution.user.entity.User
 */
@Schema(description = "사용자 정보 수정 요청 DTO")
public class UserUpdateDto {

    /**
     * 수정할 이메일 주소입니다.
     * null이 아닌 경우에만 업데이트되며, 중복 검사가 수행됩니다.
     */
    @Schema(description = "이메일", example = "newemail@example.com")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    /**
     * 수정할 전체 이름입니다.
     * null이 아닌 경우에만 업데이트됩니다.
     */
    @Schema(description = "전체 이름", example = "John Doe Updated")
    private String fullName;

    /**
     * 수정할 전화번호입니다.
     * null이 아닌 경우에만 업데이트됩니다.
     */
    @Schema(description = "전화번호", example = "010-9876-5432")
    private String phoneNumber;

    /**
     * 새로 설정할 비밀번호입니다.
     * null이 아닌 경우에만 업데이트되며, 암호화되어 저장됩니다.
     */
    @Schema(description = "새 비밀번호", example = "newpassword123")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String newPassword;

    /**
     * 기본 생성자입니다.
     * JSON 직렬화/역직렬화를 위해 필요합니다.
     */
    public UserUpdateDto() {}

    /**
     * 수정할 이메일 주소를 반환합니다.
     *
     * @return 이메일 주소 (null 가능)
     */
    public String getEmail() {
        return email;
    }

    /**
     * 수정할 이메일 주소를 설정합니다.
     *
     * @param email 설정할 이메일 주소
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 수정할 전체 이름을 반환합니다.
     *
     * @return 전체 이름 (null 가능)
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 수정할 전체 이름을 설정합니다.
     *
     * @param fullName 설정할 전체 이름
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 수정할 전화번호를 반환합니다.
     *
     * @return 전화번호 (null 가능)
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 수정할 전화번호를 설정합니다.
     *
     * @param phoneNumber 설정할 전화번호
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 새로 설정할 비밀번호를 반환합니다.
     *
     * @return 새 비밀번호 (평문, null 가능)
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * 새로 설정할 비밀번호를 설정합니다.
     *
     * @param newPassword 설정할 새 비밀번호 (평문)
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
