package project.photo.boundary.command;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePhotoCommand {
    private Long pointKey;
    private String pathString;
    private Double xsCoord;
    private Double ysCoord;
}
