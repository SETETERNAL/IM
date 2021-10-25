package im.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

    private Long id;
    private String username;
    private String password;

    public Member(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Member(String username) {
        this.username = username;
    }
}
