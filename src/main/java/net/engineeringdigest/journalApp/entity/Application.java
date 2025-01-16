package net.engineeringdigest.journalApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.util.Date;

@Document(collection = "applications") // Marks this class as a MongoDB document
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates an all-argument constructor
public class Application {

    @Id
    private ObjectId id; // MongoDB document ID

    @NotBlank(message = "Please enter your Name!")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters!")
    @Field(name = "name")
    private String name;

    @NotBlank(message = "Please enter your Email!")
    @Email(message = "Please provide a valid Email!")
    @Field(name = "email")
    private String email;

    @NotBlank(message = "Please provide a cover letter!")
    @Field(name = "coverLetter")
    private String coverLetter;

    @NotNull(message = "Please enter your Phone Number!")
    @Min(value = 1000000000L, message = "Phone number must be valid!") // Assumes a 10-digit number
    @Field(name = "phone")
    private Long phone;

    @NotBlank(message = "Please enter your Address!")
    @Field(name = "address")
    private String address;

    @Field(name = "resume")
    private Resume resume;

    @DBRef // Reference to another document (User)
    @Field(name = "applicantID")
    private User applicant;

    @DBRef // Reference to another document (User)
    @Field(name = "employerID")
    private User employer;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Resume {
        @NotBlank(message = "Public ID is required!")
        @Field(name = "public_id")
        private String publicId;

        @NotBlank(message = "URL is required!")
        @Field(name = "url")
        private String url;
    }
}
