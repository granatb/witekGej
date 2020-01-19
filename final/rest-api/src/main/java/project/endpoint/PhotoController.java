package project.endpoint;

import project.photo.boundary.ExternalService;
import project.photo.boundary.PhotoService;
import project.photo.boundary.command.UpdatePhotoCommand;
import project.photo.boundary.vo.PhotoVO;
import project.photo.boundary.command.CreateNewPhotoCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/point")
public class PhotoController {

    private final PhotoService photoService;
    private final ExternalService externalService;

    /*
     * Returns array of all entities of given resource
     * URL has to uniquely identify function in Controller
     * That function would conflict with getPointsPaginated
     */
    @GetMapping()
     public List<PhotoVO> getPoints(){
        return photoService.getAllPoints();
    }


    /*
     * Returns page of all entities of given resource
     * To avoid too long request
     *
    @GetMapping()
    public Page<PhotoVO> getPointsPaginated(FetchPhotosCommand command){
        return pointService.getPointsPaginated(command);
    }
    */

    /*
     * Creates new instance of entity
     * Please notice that Command does not get ID, as it have to be new entity.
     * It prohibits conflict with already existing instance.
     * */
    @PostMapping()
    public ResponseEntity createNewPoint(@Valid @RequestBody CreateNewPhotoCommand command){
        try{
            //photoService.createPoint(command);
            externalService.test(command);
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
     * Updates all attributes
     * Replaces missing values by null
     * */
    @PutMapping()
    public ResponseEntity updatePointAllAttributes(@Valid @RequestBody UpdatePhotoCommand command){
        try{
            photoService.updatePointAllAttributes(command);
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /*
     * Updates all attributes
     * Replaces missing values by null
     * */
    @PutMapping("/{pointKey}")
    public ResponseEntity updatePointAllAttributes(@PathVariable("pointKey") Long pointKey, @Valid @RequestBody UpdatePhotoCommand command){
        command.setPointKey(pointKey);
        return updatePointAllAttributes(command);
    }

    /*
     * Updates selected attributes
     * Do not modify attributes if are passed as null/not passed at all
     * */
    @PatchMapping()
    public ResponseEntity updatePointSelectedAttributes(@Valid @RequestBody UpdatePhotoCommand command){
        try{
            photoService.updatePointSelectedAttributes(command);
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /*
     * Updates selected attributes
     * Do not modify attributes if are passed as null/not passed at all
     * */
    @PatchMapping("/{pointKey}")
    public ResponseEntity updatePointSelectedAttributes(@PathVariable("pointKey") Long pointKey, @Valid @RequestBody UpdatePhotoCommand command){
        command.setPointKey(pointKey);
        return updatePointSelectedAttributes(command);
    }

    @DeleteMapping("/{pointKey}")
    public ResponseEntity deletePoint(@PathVariable("pointKey") Long pointKey){
        try{
            photoService.deletePoint(pointKey);
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}