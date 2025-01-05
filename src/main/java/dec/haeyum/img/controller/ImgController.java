package dec.haeyum.img.controller;

import dec.haeyum.img.service.ImgService;
import io.netty.handler.codec.http.HttpConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImgController {

    private final ImgService imgService;

    @ResponseBody
    @GetMapping("/image/{fileName}")
    public ResponseEntity<Resource> getImg (@PathVariable(name = "fileName") String fileName){
        Resource img = imgService.getImg(fileName);
        String contentType = imgService.findContentType(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,contentType)
                .body(img);
    }


}
