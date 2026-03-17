package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author model representing an author entity.
 * Matches Fake REST API Author schema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @JsonProperty("id")
    @NotNull(message = "ID cannot be null or empty")
    private Integer id;

    @JsonProperty("idBook")
    private Integer idBook;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;
}
