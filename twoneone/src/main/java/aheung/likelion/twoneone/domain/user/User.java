package aheung.likelion.twoneone.domain.user;

import aheung.likelion.twoneone.domain.enums.Role;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;    //아이디(닉네임)

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String profileImg;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}
