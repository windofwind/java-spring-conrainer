package info.addline.acution.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import info.addline.acution.user.entity.User;
import info.addline.acution.user.entity.UserAccount;

/**
 * 사용자 데이터 접근을 위한 JPA 리포지토리 인터페이스입니다.
 *
 * <p>이 인터페이스는 Spring Data JPA의 JpaRepository를 확장하여
 * 사용자 엔티티에 대한 기본적인 CRUD 작업과 함께 사용자 관련 특화된 쿼리 메서드들을 제공합니다.</p>
 *
 * <p>주요 기능:</p>
 * <ul>
 *   <li>기본 CRUD 작업 (JpaRepository 제공)</li>
 *   <li>사용자명/이메일 기반 조회</li>
 *   <li>사용자명/이메일 중복 검사</li>
 *   <li>상태별 사용자 조회</li>
 *   <li>키워드 기반 사용자 검색</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see User
 * @see JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 주 이메일로 사용자를 조회합니다.
     *
     * @param primaryEmail 조회할 주 이메일
     * @return 해당 주 이메일의 사용자 (Optional)
     */
    Optional<User> findByPrimaryEmail(String primaryEmail);

    /**
     * 이메일 인증 상태로 사용자를 조회합니다.
     *
     * @param emailVerified 인증 상태
     * @return 해당 인증 상태의 사용자 목록
     */
    List<User> findByEmailVerified(Boolean emailVerified);

    /**
     * 주 이메일 존재 여부를 확인합니다.
     *
     * @param primaryEmail 확인할 주 이메일
     * @return 주 이메일이 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByPrimaryEmail(String primaryEmail);

    /**
     * 소셜 계정으로 사용자를 조회합니다.
     *
     * @param provider OAuth 제공자
     * @param providerAccountId 제공자에서의 계정 ID
     * @return 해당 소셜 계정과 연결된 사용자 (Optional)
     */
    @Query("SELECT u FROM User u JOIN u.accounts a WHERE a.provider = :provider AND a.providerAccountId = :providerAccountId")
    Optional<User> findByProviderAccount(@Param("provider") UserAccount.OAuthProvider provider,
                                        @Param("providerAccountId") String providerAccountId);

    /**
     * 상태별로 사용자 목록을 조회합니다.
     *
     * @param status 조회할 사용자 상태
     * @return 해당 상태의 사용자 목록
     */
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findByStatus(@Param("status") String status);

    /**
     * 이메일에서 키워드를 포함하는 사용자들을 검색합니다.
     *
     * <p>대소문자를 구분하지 않고 부분 문자열 검색을 수행합니다.</p>
     *
     * @param keyword 검색할 키워드
     * @return 키워드를 포함하는 사용자 목록
     */
    @Query("SELECT u FROM User u WHERE u.primaryEmail LIKE %:keyword%")
    List<User> searchByEmail(@Param("keyword") String keyword);

    /**
     * 활성 상태의 모든 사용자를 조회합니다.
     *
     * <p>상태가 'ACTIVE'인 사용자들만 반환합니다.</p>
     *
     * @return 활성 사용자 목록
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    List<User> findActiveUsers();

    /**
     * 인증된 이메일을 가진 활성 사용자를 조회합니다.
     *
     * @return 인증된 활성 사용자 목록
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND u.emailVerified = true")
    List<User> findActiveVerifiedUsers();
}
