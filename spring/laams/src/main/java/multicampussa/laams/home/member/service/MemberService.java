package multicampussa.laams.home.member.service;

import lombok.RequiredArgsConstructor;
import multicampussa.laams.director.domain.Director;
import multicampussa.laams.home.member.dto.LoginRequestDto;
import multicampussa.laams.home.member.dto.MemberDto;
import multicampussa.laams.home.member.dto.MemberSignUpDto;
import multicampussa.laams.home.member.jwt.JwtTokenProvider;
import multicampussa.laams.home.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender javaMailSender;
    private final Random random = new SecureRandom();

    // 회원가입
    public ResponseEntity<String> signUp(MemberSignUpDto memberSignUpDto) {
        if (memberRepository.existsByEmail(memberSignUpDto.getEmail()) && !memberRepository.findByEmail(memberSignUpDto.getEmail()).get().getIsDelete()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
        } else {
            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(memberSignUpDto.getPassword());

            Director director;
            if (!memberRepository.existsByEmail(memberSignUpDto.getEmail()) || !memberRepository.findByEmail(memberSignUpDto.getEmail()).get().getIsVerified()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증을 진행해주세요.");
            } else {
                // 삭제한 이메일로 다시 한 번 회원가입 할 때
                director = memberRepository.findByEmail(memberSignUpDto.getEmail()).get();

                director.update(memberSignUpDto, encodedPassword);

                Director savedMember = memberRepository.save(director);
            }

            return ResponseEntity.status(HttpStatus.OK).body("회원 가입에 성공하였습니다.");
        }
    }

    // 이메일 인증
    public void requestEmailVerification(String email) {
        String code = generateVerificationCode();

        // 이메일이 공백인 경우 오류 발생
        if (email == "") {
            throw new IllegalArgumentException("400: 이메일을 입력해주세요.");
        }

        // 이메일이 이미 DB에 존재하는 경우
        if (memberRepository.existsByEmail(email)) {
            Director existingMember = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("404: 유저를 찾을 수 없습니다."));

            // 해당 이메일에 대한 사용자 계정이 삭제 상태가 아니라면 에러 메시지 반환
            if (!existingMember.getIsDelete()) {
                throw new RuntimeException("409: 이미 존재하는 이메일입니다.");
            } else {
                // 삭제된 사용자라면 인증 코드를 업데이트
                existingMember.updateVerificationCode(email, code);
                memberRepository.save(existingMember);
            }
        } else {
            // 이메일이 DB에 없는 경우 새로운 Director 객체 생성 및 저장
            Director newMember = new Director();
            newMember.updateVerificationCode(email, code);
            memberRepository.save(newMember);
        }

        try {
            String message = "다음 코드를 입력하여 이메일을 확인해주세요: " + code;
            sendEmail(email, "이메일 확인 코드", message);
        } catch (Exception e) {
//            throw new IllegalArgumentException("400: 이메일 형식이 잘못되었습니다.");
            throw new IllegalArgumentException("400: " + e.getMessage());
        }
    }

    // 인증코드 발급
    private String generateVerificationCode() {
        int code = 100000 + random.nextInt(900000); // 100000에서 999999 사이의 난수 생성
        return Integer.toString(code);
    }

    // 이메일 보내기 (JavaMailSender 사용)
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }

    // 이메일 인증코드 확인
    public void confirmEmailVerification(String email, String code) {
        Director director = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        if (!director.getVerificationCode().equals(code)) {
            throw new RuntimeException("인증 코드가 일치하지 않습니다.");
        }

        director.updateVerified(true); // 이메일이 인증되었음을 표시
        memberRepository.save(director);
    }

    // 관리자가 회원의 정보를 조회하는 서비스(Email)
    public MemberDto AdminInfo(String email) {
        return Director.toAdminDto(memberRepository.findByEmail(email).get());
    }

    // 로그인 및 토큰 발급
    public ResponseEntity<Map<String, Object>> signIn(LoginRequestDto loginRequestDto, String refreshToken) {
        Map<String, Object> response = new HashMap<>();
        Optional<Director> memberOptional = memberRepository.findByEmail(loginRequestDto.getEmail());
        memberOptional.get().updateRefreshToken(refreshToken);
        memberRepository.save(memberOptional.get());

        if (!memberOptional.get().getIsDelete()) {
            if (memberOptional.isPresent()) {
                Director director = memberOptional.get();
                if (passwordEncoder.matches(loginRequestDto.getPassword(), director.getPassword())) {
                    response.put("message", "로그인에 성공하였습니다.");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    response.put("message", "비밀번호가 일치하지 않습니다.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } else {
                response.put("message", "사용자를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            response.put("message", "탈퇴한 유저입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
