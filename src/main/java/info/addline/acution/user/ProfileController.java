package info.addline.acution.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("user/profile")
@Tag(name = "Profile", description = "Profile API")
public class ProfileController {
  /**
   * 사용자 비즈니스 로직을 처리하는 서비스 인스턴스입니다.
   */
  private final UserServiceInterface userService;

  public ProfileController(UserServiceInterface userService) {
    this.userService = userService;
  }

  @GetMapping("/")
  public String getProfile(@RequestParam String param) {
    return this.userService.toString();
  }
}
