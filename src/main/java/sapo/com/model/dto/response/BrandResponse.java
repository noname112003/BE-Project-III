package sapo.com.model.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BrandResponse {
    private Long id ;
    private String name ;
    private String code ;
    private Boolean status;
    private String description  ;
    private LocalDateTime createdOn ;
    private LocalDateTime updatedOn ;
}
