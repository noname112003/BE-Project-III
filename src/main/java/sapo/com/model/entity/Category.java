package sapo.com.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name ;
    @Column(unique = true)
    private String code ;
    private Boolean status;
    private String description  ;
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "dd-MM-yyyy HH:mm")
    @Column(name = "created_on")
    private LocalDateTime createdOn ;
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "dd-MM-yyyy HH:mm")
    @Column(name = "updated_on")
    private LocalDateTime updatedOn ;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Product> products;

    public CategoryResponse transferToResponse(){
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(this.id);
        categoryResponse.setName(this.name);
        categoryResponse.setCode(this.code);
        categoryResponse.setDescription(this.description);
        categoryResponse.setStatus(this.status);
        categoryResponse.setCreatedOn(this.createdOn);
        categoryResponse.setUpdatedOn(this.updatedOn);
        return categoryResponse;
    }


}