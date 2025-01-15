package net.engineeringdigest.journalApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Document(collection = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id  // Automatically generated ObjectId by MongoDB
    private ObjectId id;  // MongoDB will generate this for you

    @NotBlank(message = "Please provide a title.")
    @Size(min = 3, max = 30, message = "Title must be between 3 and 30 characters.")
    private String title;

    @NotBlank(message = "Please provide a description.")
    @Size(min = 30, max = 500, message = "Description must be between 30 and 500 characters.")
    private String description;

    @NotBlank(message = "Please provide a category.")
    private String category;

    @NotBlank(message = "Please provide a country.")
    private String country;

    @NotBlank(message = "Please provide a city.")
    private String city;

    @NotBlank(message = "Please provide a location.")
    @Size(min = 10, message = "Location must contain at least 10 characters.")
    private String location;

    @NotNull(message = "Please provide a fixed salary.")
    @Min(value = 1000, message = "Fixed salary must be at least 4 digits.")
    private Double fixedSalary;

    @NotNull(message = "Please provide a salary range.")
    @Min(value = 1000, message = "Salary 'From' must be at least 4 digits.")
    private Double salaryFrom;

    @NotNull(message = "Please provide a salary range.")
    @Min(value = 1000, message = "Salary 'To' must be at least 4 digits.")
    private Double salaryTo;

    private Boolean expired;

    private Date jobPostedOn;

    @NotNull(message = "Please provide the user who posted the job.")
    private ObjectId postedBy;

}
