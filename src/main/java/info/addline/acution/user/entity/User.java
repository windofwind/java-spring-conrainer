package info.addline.acution.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import cool.graph.cuid.Cuid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 통합 사용자 정보를 나타내는 JPA 엔티티 클래스입니다.
 *
 * <p>이 엔티티는 통합 로그인을 지원하는 사용자의 핵심 정보를 관리하며,
 * 다양한 소셜 로그인 제공자와 연동할 수 있도록 설계되었습니다.</p>
 *
 * <p>주요 특징:</p>
 * <ul>
 *   <li>CUID를 사용한 안전한 사용자 식별자</li>
 *   <li>여러 소셜 계정과 연동 가능 (UserAccount 엔티티와 관계)</li>
 *   <li>프로필 정보는 별도 엔티티로 분리</li>
 *   <li>사용자 상태 관리 (활성, 비활성, 삭제)</li>
 *   <li>생성/수정 시간 자동 관리</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see UserStatus
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * 사용자의 고유 식별자입니다.
     * CUID를 사용하여 자동으로 생성되는 기본 키입니다.
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 25)
    private String id;

    /**
     * 주 이메일 주소입니다.
     * 통합 계정의 주 연락처로 사용되며, 시스템에서 유일해야 합니다.
     */
    @Column(name = "primary_email", unique = true, nullable = false)
    @NotBlank(message = "주 이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String primaryEmail;

    /**
     * 이메일 인증 여부입니다.
     */
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    /**
     * 사용자의 현재 상태입니다.
     * 기본값은 ACTIVE이며, 논리 삭제 시 DELETED로 변경됩니다.
     * 가능한 값: ACTIVE, INACTIVE, SUSPENDED, DELETED
     */
    @Column(nullable = false)
    private String status = "ACTIVE";

    /**
     * 연결된 소셜 계정들입니다.
     * 하나의 사용자가 여러 소셜 계정을 연동할 수 있습니다.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserAccount> accounts = new ArrayList<>();

    /**
     * 사용자 프로필 정보입니다.
     * 프로필 정보는 별도 엔티티로 관리됩니다.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Profile profile;

    /**
     * 사용자 계정 생성 시간입니다.
     * Hibernate에 의해 자동으로 설정되며, 수정 불가능합니다.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 사용자 정보 최종 수정 시간입니다.
     * Hibernate에 의해 자동으로 업데이트됩니다.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    /**
     * 기본 생성자입니다.
     * JPA에서 요구하는 기본 생성자입니다.
     */
    public User() {
        this.id = Cuid.createCuid();
    }

    /**
     * 주 이메일로 사용자를 생성하는 생성자입니다.
     *
     * @param primaryEmail 주 이메일 주소
     */
    public User(String primaryEmail) {
        this();
        this.primaryEmail = primaryEmail;
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
     * 연결된 계정 목록을 반환합니다.
     *
     * @return 연결된 소셜 계정 목록
     */
    public List<UserAccount> getAccounts() {
        return accounts;
    }

    /**
     * 연결된 계정 목록을 설정합니다.
     *
     * @param accounts 설정할 계정 목록
     */
    public void setAccounts(List<UserAccount> accounts) {
        this.accounts = accounts;
    }

    /**
     * 프로필 정보를 반환합니다.
     *
     * @return 사용자 프로필
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * 프로필 정보를 설정합니다.
     *
     * @param profile 설정할 프로필
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * 사용자 상태를 반환합니다.
     *
     * @return 현재 사용자 상태
     */
    public String getStatus() {
        return status;
    }

    /**
     * 사용자 상태를 설정합니다.
     *
     * @param status 설정할 사용자 상태
     */
    public void setStatus(String status) {
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
     * 일반적으로 Hibernate에 의해 자동으로 설정되므로 직접 호출할 필요가 없습니다.
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
     * 일반적으로 Hibernate에 의해 자동으로 업데이트되므로 직접 호출할 필요가 없습니다.
     *
     * @param updatedAt 설정할 수정 시간
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
