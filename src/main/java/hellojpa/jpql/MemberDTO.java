package hellojpa.jpql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MemberDTO {

    private final String username;

    private final int age;
}
