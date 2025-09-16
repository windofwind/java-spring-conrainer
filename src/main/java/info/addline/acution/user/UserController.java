package info.addline.acution.user;

import info.addline.acution.user.dto.UserLoginDto;
import info.addline.acution.user.dto.UserRegistrationDto;
import info.addline.acution.user.dto.UserResponseDto;
import info.addline.acution.user.dto.UserUpdateDto;
import info.addline.acution.user.entity.User;
import info.addline.acution.user.entity.UserAccount;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 통합 사용자 관리 REST API 컨트롤러입니다.
 *
 * <p>이 컨트롤러는 통합 로그인을 지원하는 사용자 관련 모든 HTTP 요청을 처리하며,
 * 다음과 같은 기능을 제공합니다:</p>
 * <ul>
 *   <li>사용자 생성 및 관리</li>
 *   <li>소셜 로그인 지원</li>
 *   <li>사용자 정보 조회 및 검색</li>
 *   <li>이메일 인증 및 상태 관리</li>
 *   <li>사용자 탈퇴 및 삭제</li>
 * </ul>
 *
 * <p>모든 API 엔드포인트는 Swagger/OpenAPI 문서화가 적용되어 있으며,
 * {@code /api/users} 경로로 접근할 수 있습니다.</p>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see UserService
 * @see UserResponseDto
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "사용자 관리 API")
public class UserController {

    /**
     * 사용자 비즈니스 로직을 처리하는 서비스 인스턴스입니다.
     */
    private final UserService userService;

    /**
     * UserController 생성자입니다.
     *
     * @param userService 사용자 관련 비즈니스 로직을 처리하는 서비스
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 새로운 사용자를 생성합니다.
     *
     * <p>주 이메일로 새로운 통합 사용자를 생성합니다.</p>
     *
     * @param primaryEmail 주 이메일 주소
     * @return 생성된 사용자 정보를 담은 ResponseEntity
     * @throws RuntimeException 이메일이 이미 존재할 때
     */
    @PostMapping("/create")
    @Operation(summary = "사용자 생성", description = "주 이메일로 새로운 사용자를 생성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "사용자 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
    })
    public ResponseEntity<UserResponseDto> createUser(@RequestParam String primaryEmail) {
        try {
            UserResponseDto user = userService.createUser(primaryEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 주 이메일로 사용자를 조회합니다.
     *
     * @param primaryEmail 조회할 주 이메일
     * @return 사용자 정보를 담은 ResponseEntity
     */
    @GetMapping("/email/{primaryEmail}")
    @Operation(summary = "주 이메일로 사용자 조회", description = "주 이메일로 사용자 정보를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<UserResponseDto> getUserByPrimaryEmail(
            @Parameter(description = "주 이메일", required = true) @PathVariable String primaryEmail) {
        try {
            UserResponseDto user = userService.getUserByPrimaryEmail(primaryEmail);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ID로 사용자 정보를 조회합니다.
     *
     * @param id 조회할 사용자의 고유 식별자(UUID)
     * @return 조회된 사용자 정보를 담은 ResponseEntity
     * @throws RuntimeException 해당 ID의 사용자가 존재하지 않을 때
     */
    @GetMapping("/{id}")
    @Operation(summary = "사용자 조회", description = "ID로 사용자 정보를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<UserResponseDto> getUser(
            @Parameter(description = "사용자 CUID", required = true) @PathVariable String id) {
        try {
            UserResponseDto user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 사용자의 소셜 계정 목록을 조회합니다.
     *
     * @param id 사용자 CUID
     * @return 연결된 소셜 계정 목록
     */
    @GetMapping("/{id}/social-accounts")
    @Operation(summary = "소셜 계정 목록 조회", description = "사용자의 연결된 소셜 계정 목록을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<List<UserAccount>> getUserSocialAccounts(
            @Parameter(description = "사용자 CUID", required = true) @PathVariable String id) {
        try {
            List<UserAccount> accounts = userService.getUserSocialAccounts(id);
            return ResponseEntity.ok(accounts);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 모든 활성 사용자 목록을 조회합니다.
     *
     * <p>탈퇴하지 않은 모든 사용자의 목록을 반환합니다.</p>
     *
     * @return 활성 사용자 목록을 담은 ResponseEntity
     */
    @GetMapping
    @Operation(summary = "활성 사용자 목록 조회", description = "모든 활성 사용자 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public ResponseEntity<List<UserResponseDto>> getAllActiveUsers() {
        List<UserResponseDto> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 이메일로 사용자를 검색합니다.
     *
     * <p>이메일에서 키워드를 포함하는 사용자들을 찾아 반환합니다.</p>
     *
     * @param keyword 검색할 키워드 (이메일의 일부)
     * @return 검색된 사용자 목록을 담은 ResponseEntity
     */
    @GetMapping("/search")
    @Operation(summary = "사용자 검색", description = "이메일로 사용자를 검색합니다")
    @ApiResponse(responseCode = "200", description = "검색 성공")
    public ResponseEntity<List<UserResponseDto>> searchUsersByEmail(
            @Parameter(description = "검색 키워드", required = true) @RequestParam String keyword) {
        List<UserResponseDto> users = userService.searchUsersByEmail(keyword);
        return ResponseEntity.ok(users);
    }

    /**
     * 인증된 사용자 목록을 조회합니다.
     *
     * @return 인증된 활성 사용자 목록
     */
    @GetMapping("/verified")
    @Operation(summary = "인증된 사용자 목록 조회", description = "이메일 인증된 모든 활성 사용자 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public ResponseEntity<List<UserResponseDto>> getVerifiedUsers() {
        List<UserResponseDto> users = userService.getAllVerifiedUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자의 주 이메일을 변경합니다.
     *
     * @param id 사용자 CUID
     * @param newPrimaryEmail 새로운 주 이메일
     * @return 수정된 사용자 정보
     */
    @PutMapping("/{id}/primary-email")
    @Operation(summary = "주 이메일 변경", description = "사용자의 주 이메일을 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "이메일 중복")
    })
    public ResponseEntity<UserResponseDto> updatePrimaryEmail(
            @Parameter(description = "사용자 CUID", required = true) @PathVariable String id,
            @RequestParam String newPrimaryEmail) {
        try {
            UserResponseDto user = userService.updatePrimaryEmail(id, newPrimaryEmail);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 사용자의 이메일 인증 상태를 업데이트합니다.
     *
     * @param id 사용자 CUID
     * @param verified 인증 상태
     * @return 업데이트된 사용자 정보
     */
    @PutMapping("/{id}/email-verification")
    @Operation(summary = "이메일 인증 상태 업데이트", description = "사용자의 이메일 인증 상태를 업데이트합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<UserResponseDto> updateEmailVerification(
            @Parameter(description = "사용자 CUID", required = true) @PathVariable String id,
            @RequestParam boolean verified) {
        try {
            UserResponseDto user = userService.updateEmailVerificationStatus(id, verified);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 사용자를 탈퇴 처리합니다 (논리 삭제).
     *
     * <p>실제 데이터를 삭제하지 않고 탈퇴 플래그를 설정하여
     * 사용자를 비활성화 상태로 만듭니다.</p>
     *
     * @param id 탈퇴할 사용자의 UUID
     * @return 탈퇴 처리 결과를 담은 ResponseEntity
     * @throws RuntimeException 해당 ID의 사용자가 존재하지 않을 때
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 탈퇴", description = "사용자를 탈퇴 처리합니다 (논리 삭제)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "탈퇴 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "사용자 CUID", required = true) @PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 사용자를 데이터베이스에서 영구 삭제합니다.
     *
     * <p><strong>주의:</strong> 이 작업은 되돌릴 수 없으며,
     * 사용자의 모든 데이터가 완전히 삭제됩니다.</p>
     *
     * @param id 영구 삭제할 사용자의 UUID
     * @return 삭제 처리 결과를 담은 ResponseEntity
     * @throws RuntimeException 해당 ID의 사용자가 존재하지 않을 때
     */
    @DeleteMapping("/{id}/permanent")
    @Operation(summary = "사용자 영구 삭제", description = "사용자를 데이터베이스에서 영구 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<Void> permanentlyDeleteUser(
            @Parameter(description = "사용자 CUID", required = true) @PathVariable String id) {
        try {
            userService.permanentlyDeleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
