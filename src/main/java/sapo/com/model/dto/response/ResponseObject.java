package sapo.com.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseObject {
    private String message ;
    private HttpStatus status ;
    private Object data ;

    public ResponseObject(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
