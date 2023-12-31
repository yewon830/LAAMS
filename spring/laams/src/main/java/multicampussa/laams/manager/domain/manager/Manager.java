package multicampussa.laams.manager.domain.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import multicampussa.laams.global.BaseTimeEntity;
import multicampussa.laams.home.member.dto.MemberDto;
import multicampussa.laams.home.member.dto.MemberUpdateDto;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Manager extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(unique = true)
    private String id;
    private String name;
    private String email;
    private String pw;
    private String phone;
    private String refreshToken;

    public static MemberDto toMemberDto(Manager manager) {
        return MemberDto.builder()
                .memberNo(manager.getNo())
                .name(manager.getName())
                .phone(manager.getPhone())
                .createdAt(manager.getCreatedAt())
                .updatedAt(manager.getUpdatedAt())
                .email(manager.getEmail())
                .build();
    }

    // 리프레시 토큰 업데이트
    // 이미 리프레시 토큰이 있어도 업데이트 됨.
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // 사용자가 자신의 정보 변경
    public void update(MemberUpdateDto memberUpdateDto) {
        this.id = memberUpdateDto.getId();
        this.name = memberUpdateDto.getName();
        this.phone = memberUpdateDto.getPhone();
        this.email = memberUpdateDto.getEmail();
    }

    // 비밀번호 변경
    public void updatePassword(String encode) {
        this.pw = encode;
    }
}
