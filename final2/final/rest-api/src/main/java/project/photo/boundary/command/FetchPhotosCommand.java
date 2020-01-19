package project.photo.boundary.command;

import lombok.*;

import javax.validation.constraints.Max;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FetchPhotosCommand {
    //initial value
    protected Integer page = 0;
    @Max(500)
    protected Integer perPage = 50;
}
