package dec.haeyum.social.entity;

import dec.haeyum.external.kakao.dto.response.TokenAccessResponseDto;
import dec.haeyum.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity(name = "social")
@Getter
public class SocialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long socialId;
    private String socialName;
    private String socialSub;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Member member;


    public List<String> createSocialWithKakao(TokenAccessResponseDto response, String imgName) {
        this.socialName = "카카오";
        this.socialSub = response.idTokenDecode().getSub();

        Member memberEntity = new Member(response.idTokenDecode().getNickname(),imgName);
        setMember(memberEntity);
        return member.getRoles();
    }

    private void setMember(Member memberEntity) {
        this.member = memberEntity;
        this.member.setSocial(this);
    }

}
