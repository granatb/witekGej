package project.photo.boundary.command;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewPhotoCommand {
    @NotEmpty(message = "Please give me a name")
    private String pathString;
    private Double xsCoord;
    private Double ysCoord;
}
