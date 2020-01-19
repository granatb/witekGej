package project.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.photo.boundary.ExternalService;
import project.photo.boundary.PhotoService;
import project.photo.boundary.StorageFileNotFoundException;
import project.photo.boundary.StorageService;
import project.photo.boundary.command.UpdatePhotoCommand;
import project.photo.boundary.vo.PhotoVO;
import project.photo.boundary.command.CreateNewPhotoCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/point")
public class PhotoController {



    private final PhotoService photoService;
    private final ExternalService externalService;
/*
    private final StorageService storageService;

    @Autowired
    public PhotoController(StorageService storageService, PhotoService photoService, ExternalService externalService) {
        this.storageService = storageService;
        this.externalService = externalService;
        this.photoService = photoService;
    }

    @GetMapping("/uploaded")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(PhotoController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/uploaded/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/uploaded")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
*/


    @GetMapping()
     public List<PhotoVO> getPoints(){
        return photoService.getAllPoints();
    }

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



    @PutMapping()
    public ResponseEntity updatePointAllAttributes(@Valid @RequestBody UpdatePhotoCommand command){
        try{
            photoService.updatePointAllAttributes(command);
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("/{pointKey}")
    public ResponseEntity updatePointAllAttributes(@PathVariable("pointKey") Long pointKey, @Valid @RequestBody UpdatePhotoCommand command){
        command.setPointKey(pointKey);
        return updatePointAllAttributes(command);
    }


    @PatchMapping()
    public ResponseEntity updatePointSelectedAttributes(@Valid @RequestBody UpdatePhotoCommand command){
        try{
            photoService.updatePointSelectedAttributes(command);
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

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