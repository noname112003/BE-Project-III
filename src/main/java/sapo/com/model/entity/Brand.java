package sapo.com.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import sapo.com.model.dto.response.BrandResponse;
import sapo.com.model.dto.response.CategoryResponse;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name ;
    @Column(unique = true)
    private String code ;
    private Boolean status;
    private String description  ;
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdOn ;
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime updatedOn ;
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Product> products;

    public BrandResponse transferToResponse(){
        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setId(this.id);
        brandResponse.setName(this.name);
        brandResponse.setCode(this.code);
        brandResponse.setDescription(this.description);
        brandResponse.setStatus(this.status);
        brandResponse.setCreatedOn(this.createdOn);
        brandResponse.setUpdatedOn(this.updatedOn);
        return brandResponse;
    }

}