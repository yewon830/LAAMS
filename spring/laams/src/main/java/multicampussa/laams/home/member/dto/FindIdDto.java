package multicampussa.laams.home.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import multicampussa.laams.director.domain.Director;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindIdDto {
    private String email;
    private String name;
}
