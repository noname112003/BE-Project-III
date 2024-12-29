package sapo.com.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import sapo.com.validator.phone.TenCharacter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @NotEmpty(message = "Name cannot be empty \n ")
    private String name;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty \n")
    @Column(unique = true)
    private String email;
//    @NotEmpty(message = "Password cannot be empty\n ")
    private String password ;
    @Size(max = 11)
    @Column(unique = true)
//    @NotEmpty(message = "Phone Number cannot be empty\n ")
    @TenCharacter
    private String phoneNumber ;
    private String address ;
    private Boolean status ;
    @JsonFormat (pattern = "yyyy-MM-dd")
    private LocalDate birthDay ;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles ;

//    @OneToMany(fetch = FetchType.LAZY , mappedBy = "user" , cascade = CascadeType.ALL)
//    private List<Orders> orders ;

//    @OneToMany(fetch = FetchType.EAGER , mappedBy = "user" , cascade = CascadeType.ALL)
//    private List<Variants> variants ;


    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdOn ;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateOn ;

}
