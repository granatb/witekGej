package project.photo.boundary.vo;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoVO {
    private Long pointKey;
    private String pathString;
    private Double xsCoord;
    private Double ysCoord;
}
