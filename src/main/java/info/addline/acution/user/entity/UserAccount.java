package info.addline.acution.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import cool.graph.cuid.Cuid;

import java.time.LocalDateTime;

/**
 * 사용자의 외부 계정 연동 정보를 나타내는 JPA 엔티티 클래스입니다.
 *
 * <p>이 엔티티는 소셜 로그인 제공자(Google, Facebook, GitHub 등)와의
 * 연동 정보를 관리하며, 하나의 사용자가 여러 외부 계정을 연결할 수 있도록 합니다.</p>
 *
 * <p>주요 특징:</p>
 * <ul>
 *   <li>User 엔티티와 N:1 관계 (한 사용자가 여러 외부 계정 연동 가능)</li>
 *   <li>OAuth 제공자별 고유 식별자 저장</li>
 *   <li>액세스 토큰 및 리프레시 토큰 관리</li>
 *   <li>계정 연동 상태 관리</li>
 *   <li>생성/수정 시간 자동 관리</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see User
 */
@Entity
@Table(name = "user_accounts",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"provider", "provider_account_id"})
       })
public class UserAccount {

    /**
     * 계정 연동의 고유 식별자입니다.
     * CUID를 사용하여 자동으로 생성되는 기본 키입니다.
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 25)
    private String id;

    /**
     * 계정이 연결된 사용자입니다.
     * User 엔티티와 N:1 관계를 맺습니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    /**
     * OAuth 제공자 유형입니다.
     * (예: GOOGLE, FACEBOOK, GITHUB, KAKAO, NAVER 등)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 50)
    private OAuthProvider provider;

    /**
     * 제공자에서의 계정 고유 식별자입니다.
     * 각 OAuth 제공자에서 제공하는 사용자의 고유 ID입니다.
     */
    @Column(name = "provider_account_id", nullable = false, length = 255)
    @NotBlank(message = "제공자 계정 ID는 필수입니다")
    @Size(max = 255, message = "제공자 계정 ID는 255자를 초과할 수 없습니다")
    private String providerAccountId;

    /**
     * 제공자에서 제공하는 사용자 이메일입니다.
     */
    @Column(name = "provider_email", length = 255)
    @Size(max = 255, message = "제공자 이메일은 255자를 초과할 수 없습니다")
    private String providerEmail;

    /**
     * 제공자에서 제공하는 사용자 이름입니다.
     */
    @Column(name = "provider_name", length = 100)
    @Size(max = 100, message = "제공자 이름은 100자를 초과할 수 없습니다")
    private String providerName;

    /**
     * 제공자에서 제공하는 사용자 프로필 이미지 URL입니다.
     */
    @Column(name = "provider_picture", length = 500)
    @Size(max = 500, message = "제공자 프로필 이미지 URL은 500자를 초과할 수 없습니다")
    private String providerPicture;

    /**
     * OAuth 액세스 토큰입니다.
     * 제한된 시간 동안 API 호출에 사용됩니다.
     */
    @Column(name = "access_token", length = 1000)
    private String accessToken;

    /**
     * OAuth 리프레시 토큰입니다.
     * 액세스 토큰을 갱신하는데 사용됩니다.
     */
    @Column(name = "refresh_token", length = 1000)
    private String refreshToken;

    /**
     * 액세스 토큰의 만료 시간입니다.
     */
    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;

    /**
     * 계정 연동 상태입니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;

    /**
     * 계정 연동 생성 시간입니다.
     * Hibernate에 의해 자동으로 설정되며, 수정 불가능합니다.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 계정 연동 정보 최종 수정 시간입니다.
     * Hibernate에 의해 자동으로 업데이트됩니다.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * OAuth 제공자 유형을 나타내는 열거형입니다.
     */
    public enum OAuthProvider {
        GOOGLE, FACEBOOK, GITHUB, KAKAO, NAVER, APPLE, TWITTER, DISCORD
    }

    /**
     * 계정 연동 상태를 나타내는 열거형입니다.
     */
    public enum AccountStatus {
        ACTIVE,    // 활성 상태
        INACTIVE,  // 비활성 상태
        REVOKED    // 연동 해제됨
    }

    /**
     * 기본 생성자입니다.
     * JPA에서 요구하는 기본 생성자입니다.
     */
    public UserAccount() {
        this.id = Cuid.createCuid();
    }

    /**
     * 필수 정보로 계정 연동을 생성하는 생성자입니다.
     *
     * @param user 연동될 사용자
     * @param provider OAuth 제공자
     * @param providerAccountId 제공자에서의 계정 ID
     */
    public UserAccount(User user, OAuthProvider provider, String providerAccountId) {
        this();
        this.user = user;
        this.provider = provider;
        this.providerAccountId = providerAccountId;
    }

    // Getters and Setters

    /**
     * 계정 연동 ID를 반환합니다.
     *
     * @return 계정 연동의 고유 식별자(CUID)
     */
    public String getId() {
        return id;
    }

    /**
     * 계정 연동 ID를 설정합니다.
     *
     * @param id 설정할 계정 연동 CUID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 연동된 사용자를 반환합니다.
     *
     * @return 계정이 연동된 사용자
     */
    public User getUser() {
        return user;
    }

    /**
     * 연동된 사용자를 설정합니다.
     *
     * @param user 설정할 사용자
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * OAuth 제공자를 반환합니다.
     *
     * @return OAuth 제공자 유형
     */
    public OAuthProvider getProvider() {
        return provider;
    }

    /**
     * OAuth 제공자를 설정합니다.
     *
     * @param provider 설정할 OAuth 제공자
     */
    public void setProvider(OAuthProvider provider) {
        this.provider = provider;
    }

    /**
     * 제공자 계정 ID를 반환합니다.
     *
     * @return 제공자에서의 계정 고유 식별자
     */
    public String getProviderAccountId() {
        return providerAccountId;
    }

    /**
     * 제공자 계정 ID를 설정합니다.
     *
     * @param providerAccountId 설정할 제공자 계정 ID
     */
    public void setProviderAccountId(String providerAccountId) {
        this.providerAccountId = providerAccountId;
    }

    /**
     * 제공자 이메일을 반환합니다.
     *
     * @return 제공자에서 제공하는 이메일
     */
    public String getProviderEmail() {
        return providerEmail;
    }

    /**
     * 제공자 이메일을 설정합니다.
     *
     * @param providerEmail 설정할 제공자 이메일
     */
    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    /**
     * 제공자 이름을 반환합니다.
     *
     * @return 제공자에서 제공하는 사용자 이름
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * 제공자 이름을 설정합니다.
     *
     * @param providerName 설정할 제공자 이름
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    /**
     * 제공자 프로필 이미지를 반환합니다.
     *
     * @return 제공자에서 제공하는 프로필 이미지 URL
     */
    public String getProviderPicture() {
        return providerPicture;
    }

    /**
     * 제공자 프로필 이미지를 설정합니다.
     *
     * @param providerPicture 설정할 프로필 이미지 URL
     */
    public void setProviderPicture(String providerPicture) {
        this.providerPicture = providerPicture;
    }

    /**
     * 액세스 토큰을 반환합니다.
     *
     * @return OAuth 액세스 토큰
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 액세스 토큰을 설정합니다.
     *
     * @param accessToken 설정할 액세스 토큰
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 리프레시 토큰을 반환합니다.
     *
     * @return OAuth 리프레시 토큰
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * 리프레시 토큰을 설정합니다.
     *
     * @param refreshToken 설정할 리프레시 토큰
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * 토큰 만료 시간을 반환합니다.
     *
     * @return 액세스 토큰 만료 시간
     */
    public LocalDateTime getTokenExpiresAt() {
        return tokenExpiresAt;
    }

    /**
     * 토큰 만료 시간을 설정합니다.
     *
     * @param tokenExpiresAt 설정할 토큰 만료 시간
     */
    public void setTokenExpiresAt(LocalDateTime tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }

    /**
     * 계정 연동 상태를 반환합니다.
     *
     * @return 현재 계정 연동 상태
     */
    public AccountStatus getStatus() {
        return status;
    }

    /**
     * 계정 연동 상태를 설정합니다.
     *
     * @param status 설정할 계정 연동 상태
     */
    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    /**
     * 생성 시간을 반환합니다.
     *
     * @return 계정 연동 생성 시간
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 생성 시간을 설정합니다.
     *
     * @param createdAt 설정할 생성 시간
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 최종 수정 시간을 반환합니다.
     *
     * @return 계정 연동 최종 수정 시간
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
}
