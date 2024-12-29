package sapo.com.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryRequest {
    @Valid
    @NotBlank(message = "Tên loại sản phẩm không được trống.")
    private String name ;
    private String code ;
    private String description  ;
}