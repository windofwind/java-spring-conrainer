package info.addline.acution.user.dto;

import info.addline.acution.user.entity.User;
import info.addline.acution.user.entity.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 통합 사용자 정보 응답을 위한 DTO 클래스입니다.
 *
 * <p>이 클래스는 통합 로그인 시스템에서 서버에서 클라이언트로 사용자 정보를 전송할 때 사용되며,
 * 보안상 민감한 정보를 제외한 안전한 사용자 데이터를 담고 있습니다.</p>
 *
 * <p>주요 특징:</p>
 * <ul>
 *   <li>CUID를 사용한 안전한 사용자 식별자</li>
 *   <li>User 엔티티에서 자동 매핑</li>
 *   <li>이메일 인증 상태 정보</li>
 *   <li>연결된 소셜 계정 리스트</li>
 *   <li>Swagger 문서화 지원</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see User
 */
@Schema(description = "사용자 응답 DTO")
public class UserResponseDto {

    /**
     * 사용자의 고유 식별자(UUID)입니다.
     */
    @Schema(description = "사용자 CUID", example = "clhz2x0000000l608j2k0j2k0")
    private String id;

    /**
     * 사용자의 주 이메일 주소입니다.
     */
    @Schema(description = "주 이메일", example = "john@example.com")
    private String primaryEmail;

    /**
     * 이메일 인증 여부입니다.
     */
    @Schema(description = "이메일 인증 여부", example = "true")
    private Boolean emailVerified;

    /**
     * 연결된 소셜 계정 리스트입니다.
     */
    @Schema(description = "연결된 소셜 계정 리스트")
    private List<SocialAccountInfo> connectedAccounts = new ArrayList<>();

    /**
     * 사용자의 현재 상태입니다.
     * ACTIVE, INACTIVE, DELETED 중 하나의 값을 가집니다.
     */
    @Schema(description = "사용자 상태", example = "ACTIVE")
    private User.UserStatus status;

    /**
     * 사용자 계정이 생성된 시간입니다.
     */
    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    /**
     * 사용자 정보가 최종 수정된 시간입니다.
     */
    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    /**
     * 기본 생성자입니다.
     * JSON 직렬화/역직렬화를 위해 필요합니다.
     */
    public UserResponseDto() {}

    /**
     * User 엔티티로부터 UserResponseDto를 생성하는 생성자입니다.
     *
     * <p>보안상 민감한 정보를 제외한 사용자 정보를 복사합니다.</p>
     *
     * @param user 변환할 User 엔티티
     */
    public UserResponseDto(User user) {
        this.id = user.getId();
        this.primaryEmail = user.getPrimaryEmail();
        this.emailVerified = user.getEmailVerified();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();

        // 연결된 소셜 계정 정보 매핑
        if (user.getAccounts() != null) {
            this.connectedAccounts = user.getAccounts().stream()
                    .filter(account -> account.getStatus() == UserAccount.AccountStatus.ACTIVE)
                    .map(SocialAccountInfo::new)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 사용자 ID를 반환합니다.
     *
     * @return 사용자의 고유 식별자(CUID)
     */
    public String getId() {
        return id;
    }

    /**
     * 사용자 ID를 설정합니다.
     *
     * @param id 설정할 사용자 CUID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 주 이메일 주소를 반환합니다.
     *
     * @return 주 이메일 주소
     */
    public String getPrimaryEmail() {
        return primaryEmail;
    }

    /**
     * 주 이메일 주소를 설정합니다.
     *
     * @param primaryEmail 설정할 주 이메일 주소
     */
    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    /**
     * 이메일 인증 상태를 반환합니다.
     *
     * @return 이메일 인증 여부
     */
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    /**
     * 이메일 인증 상태를 설정합니다.
     *
     * @param emailVerified 설정할 이메일 인증 상태
     */
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * 연결된 소셜 계정 리스트를 반환합니다.
     *
     * @return 연결된 소셜 계정 리스트
     */
    public List<SocialAccountInfo> getConnectedAccounts() {
        return connectedAccounts;
    }

    /**
     * 연결된 소셜 계정 리스트를 설정합니다.
     *
     * @param connectedAccounts 설정할 소셜 계정 리스트
     */
    public void setConnectedAccounts(List<SocialAccountInfo> connectedAccounts) {
        this.connectedAccounts = connectedAccounts;
    }

    /**
     * 사용자 상태를 반환합니다.
     *
     * @return 사용자의 현재 상태
     */
    public User.UserStatus getStatus() {
        return status;
    }

    /**
     * 사용자 상태를 설정합니다.
     *
     * @param status 설정할 사용자 상태
     */
    public void setStatus(User.UserStatus status) {
        this.status = status;
    }

    /**
     * 계정 생성 시간을 반환합니다.
     *
     * @return 계정 생성 시간
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 계정 생성 시간을 설정합니다.
     *
     * @param createdAt 설정할 생성 시간
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 최종 수정 시간을 반환합니다.
     *
     * @return 최종 수정 시간
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 최종 수정 시간을 설정합니다.
     *
     * @param updatedAt 설정할 수정 시간
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 소셜 계정 정보를 나타내는 내부 클래스입니다.
     */
    @Schema(description = "소셜 계정 정보")
    public static class SocialAccountInfo {
        @Schema(description = "계정 ID", example = "clhz2x0000000l608j2k0j2k1")
        private String accountId;

        @Schema(description = "OAuth 제공자", example = "GOOGLE")
        private UserAccount.OAuthProvider provider;

        @Schema(description = "제공자 이름", example = "John Doe")
        private String providerName;

        @Schema(description = "제공자 이메일", example = "john@gmail.com")
        private String providerEmail;

        @Schema(description = "연결 상태", example = "ACTIVE")
        private UserAccount.AccountStatus status;

        @Schema(description = "연결 일시")
        private LocalDateTime connectedAt;

        public SocialAccountInfo() {}

        public SocialAccountInfo(UserAccount account) {
            this.accountId = account.getId();
            this.provider = account.getProvider();
            this.providerName = account.getProviderName();
            this.providerEmail = account.getProviderEmail();
            this.status = account.getStatus();
            this.connectedAt = account.getCreatedAt();
        }

        // Getters and Setters
        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public UserAccount.OAuthProvider getProvider() {
            return provider;
        }

        public void setProvider(UserAccount.OAuthProvider provider) {
            this.provider = provider;
        }

        public String getProviderName() {
            return providerName;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }

        public String getProviderEmail() {
            return providerEmail;
        }

        public void setProviderEmail(String providerEmail) {
            this.providerEmail = providerEmail;
        }

        public UserAccount.AccountStatus getStatus() {
            return status;
        }

        public void setStatus(UserAccount.AccountStatus status) {
            this.status = status;
        }

        public LocalDateTime getConnectedAt() {
            return connectedAt;
        }

        public void setConnectedAt(LocalDateTime connectedAt) {
            this.connectedAt = connectedAt;
        }
    }
}
