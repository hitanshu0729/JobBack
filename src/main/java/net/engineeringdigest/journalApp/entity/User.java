package net.engineeringdigest.journalApp.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id  // Automatically generated ObjectId by MongoDB
    private ObjectId id;

    @NotEmpty(message = "Please enter your Name!")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters!")
    private String name;

    @NotEmpty(message = "Please enter your Email!")
    @Email(message = "Please provide a valid Email!")
    @Indexed(unique = true)  // Ensures the email field is unique
    private String email;

    @NotNull(message = "Please enter your Phone Number!")
    private Long phone;

    @NotEmpty(message = "Please provide a Password!")
    @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters!")
    private String password;

    @NotEmpty(message = "Please select a role")
    private String role;

    @CreatedDate
    private LocalDateTime createdAt;

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
