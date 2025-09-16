package info.addline.acution.repository;

import info.addline.acution.user.entity.User;
import info.addline.acution.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 계정 연동 데이터 접근을 위한 JPA 리포지토리 인터페이스입니다.
 *
 * <p>이 인터페이스는 Spring Data JPA의 JpaRepository를 확장하여
 * UserAccount 엔티티에 대한 기본적인 CRUD 작업과 함께 소셜 로그인 관련 특화된 쿼리 메서드들을 제공합니다.</p>
 *
 * <p>주요 기능:</p>
 * <ul>
 *   <li>기본 CRUD 작업 (JpaRepository 제공)</li>
 *   <li>OAuth 제공자별 계정 조회</li>
 *   <li>사용자별 연동된 계정 목록 조회</li>
 *   <li>계정 연동 상태별 조회</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see UserAccount
 * @see JpaRepository
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    /**
     * OAuth 제공자와 제공자 계정 ID로 계정을 조회합니다.
     *
     * @param provider OAuth 제공자
     * @param providerAccountId 제공자에서의 계정 ID
     * @return 해당 소셜 계정 (Optional)
     */
    Optional<UserAccount> findByProviderAndProviderAccountId(
            UserAccount.OAuthProvider provider,
            String providerAccountId
    );

    /**
     * 사용자와 OAuth 제공자로 계정을 조회합니다.
     *
     * @param user 조회할 사용자
     * @param provider OAuth 제공자
     * @return 해당 사용자의 특정 소셜 계정 (Optional)
     */
    Optional<UserAccount> findByUserAndProvider(User user, UserAccount.OAuthProvider provider);

    /**
     * 사용자의 모든 연동된 계정을 조회합니다.
     *
     * @param user 조회할 사용자
     * @return 해당 사용자의 모든 연동된 계정 목록
     */
    List<UserAccount> findByUser(User user);

    /**
     * 사용자 ID로 모든 연동된 계정을 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @return 해당 사용자의 모든 연동된 계정 목록
     */
    List<UserAccount> findByUserId(String userId);

    /**
     * OAuth 제공자별로 모든 계정을 조회합니다.
     *
     * @param provider OAuth 제공자
     * @return 해당 제공자의 모든 연동된 계정 목록
     */
    List<UserAccount> findByProvider(UserAccount.OAuthProvider provider);

    /**
     * 계정 연동 상태별로 계정을 조회합니다.
     *
     * @param status 계정 연동 상태
     * @return 해당 상태의 모든 연동된 계정 목록
     */
    List<UserAccount> findByStatus(UserAccount.AccountStatus status);

    /**
     * 사용자와 상태로 활성 계정을 조회합니다.
     *
     * @param user 조회할 사용자
     * @param status 계정 연동 상태
     * @return 해당 사용자의 특정 상태 계정 목록
     */
    List<UserAccount> findByUserAndStatus(User user, UserAccount.AccountStatus status);

    /**
     * 제공자 이메일로 계정을 조회합니다.
     *
     * @param providerEmail 제공자 이메일
     * @return 해당 이메일을 가진 연동된 계정 목록
     */
    List<UserAccount> findByProviderEmail(String providerEmail);

    /**
     * 사용자가 특정 OAuth 제공자와 연동했는지 확인합니다.
     *
     * @param user 확인할 사용자
     * @param provider OAuth 제공자
     * @return 연동되어 있으면 true, 그렇지 않으면 false
     */
    boolean existsByUserAndProvider(User user, UserAccount.OAuthProvider provider);

    /**
     * OAuth 제공자와 제공자 계정 ID의 존재 여부를 확인합니다.
     *
     * @param provider OAuth 제공자
     * @param providerAccountId 제공자에서의 계정 ID
     * @return 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByProviderAndProviderAccountId(
            UserAccount.OAuthProvider provider,
            String providerAccountId
    );

    /**
     * 토큰이 만료되지 않은 활성 계정을 조회합니다.
     *
     * @return 토큰이 유효한 활성 계정 목록
     */
    @Query("SELECT ua FROM UserAccount ua WHERE ua.status = 'ACTIVE' AND (ua.tokenExpiresAt IS NULL OR ua.tokenExpiresAt > CURRENT_TIMESTAMP)")
    List<UserAccount> findActiveAccountsWithValidToken();

    /**
     * 특정 사용자의 활성 계정을 조회합니다.
     *
     * @param user 조회할 사용자
     * @return 해당 사용자의 활성 계정 목록
     */
    @Query("SELECT ua FROM UserAccount ua WHERE ua.user = :user AND ua.status = 'ACTIVE'")
    List<UserAccount> findActiveAccountsByUser(@Param("user") User user);
}