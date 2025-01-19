package dec.haeyum.member.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.chat.Entity.ChatMessage;
import dec.haeyum.social.entity.SocialEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String username;
    private String profileImg;
    private String password;
    private LocalDate createDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorite",
            joinColumns = @JoinColumn(name = "memberId"),
            inverseJoinColumns = @JoinColumn(name = "calendarId")
    )
    private List<CalendarEntity> favorite = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    List<String> roles = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private SocialEntity social;

    @OneToMany(mappedBy = "senderMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public Member(String nickname, String picture) {
        this.username = nickname;
        this.profileImg = picture;
        this.createDate = LocalDate.now();
        this.roles.add("ROLE_USER");
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void setSocial(SocialEntity socialEntity) {
       this.social = socialEntity;
    }

}
