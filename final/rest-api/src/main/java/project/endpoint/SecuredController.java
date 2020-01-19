package project.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/secure")
public class SecuredController {

    @GetMapping()
    public ResponseEntity securedFunction(){
        return ResponseEntity.status(HttpStatus.OK).body("Value hidden with Basic HTTP Auth");
    }
}
