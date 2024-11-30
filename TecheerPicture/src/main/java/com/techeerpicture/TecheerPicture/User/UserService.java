package com.techeerpicture.TecheerPicture.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {

  private static final Logger logger = Logger.getLogger(UserService.class.getName());

  @Autowired
  private UserRepository userRepository;

  public User createUser(String nickname) {
    logger.info("createUser 호출됨. 닉네임: " + nickname);

    try {
      // 닉네임 중복 확인
      Optional<User> existingUser = userRepository.findByNickname(nickname);
      if (existingUser.isPresent()) {
        throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
      }

      // 사용자 생성 및 저장
      User user = new User();
      user.setNickname(nickname);
      return userRepository.save(user);

    } catch (IllegalArgumentException e) {
      logger.severe("IllegalArgumentException 발생: " + e.getMessage());
      throw e; // 컨트롤러에서 처리하도록 예외 전달
    } catch (Exception e) {
      logger.severe("예기치 못한 오류 발생: " + e.getMessage());
      throw new RuntimeException("서버 내부 오류가 발생했습니다.");
    }
  }

  public User getUserById(Long id) {
    logger.info("getUserById 호출됨. 사용자 ID: " + id);

    try {
      return userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    } catch (IllegalArgumentException e) {
      logger.severe("IllegalArgumentException 발생: " + e.getMessage());
      throw e; // 컨트롤러에서 처리하도록 예외 전달
    } catch (Exception e) {
      logger.severe("예기치 못한 오류 발생: " + e.getMessage());
      throw new RuntimeException("서버 내부 오류가 발생했습니다.");
    }
  }
}
