package info.addline.acution.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import cool.graph.cuid.Cuid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserAccount {

    /**
     * 계정 연동의 고유 식별자입니다.
     * CUID를 사용하여 자동으로 생성되는 기본 키입니다.
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 25)
    @Builder.Default
    private String id = Cuid.createCuid();

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
    @Builder.Default
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

    public UserAccount(User user, OAuthProvider provider, String providerAccountId) {
        this.id = Cuid.createCuid();
        this.user = user;
        this.provider = provider;
        this.providerAccountId = providerAccountId;
    }
}