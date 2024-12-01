package com.techeerpicture.TecheerPicture.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nicknames")
@Tag(name = "User API", description = "사용자 닉네임 관리 API")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  @Operation(summary = "닉네임 생성", description = "새로운 닉네임을 생성합니다.")
  public ResponseEntity<?> createNickname(@RequestBody UserRequest userRequest) {
    try {
      User createdUser = userService.createUser(userRequest.getNickname());
      return ResponseEntity.ok(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
    }
  }

  @GetMapping("/{id}")
  @Operation(summary = "닉네임 조회", description = "ID를 사용하여 닉네임을 조회합니다.")
  public ResponseEntity<?> getUserById(@PathVariable Long id) {
    try {
      User user = userService.getUserById(id);
      return ResponseEntity.ok(user);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
    }
  }

  @PutMapping("/{id}")
  @Operation(summary = "닉네임 수정", description = "ID를 사용하여 닉네임을 수정합니다.")
  public ResponseEntity<?> updateNickname(@PathVariable Long id, @RequestBody UserRequest userRequest) {
    try {
      User updatedUser = userService.updateUser(id, userRequest.getNickname());
      return ResponseEntity.ok(updatedUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "닉네임 삭제", description = "ID를 사용하여 닉네임을 삭제합니다.")
  public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    try {
      userService.deleteUser(id);
      return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
    }
  }
}
