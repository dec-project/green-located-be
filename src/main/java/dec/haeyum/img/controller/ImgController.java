package dec.haeyum.img.controller;

import dec.haeyum.img.service.ImgService;
import io.netty.handler.codec.http.HttpConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
public class ImgController {

    private final ImgService imgService;

//    @GetMapping(value = "/image/{fileName}")
//    public ResponseEntity<Resource> getImg (@PathVariable(name = "fileName") String fileName){
//        Resource img = imgService.getImg(fileName);
//        imgService.compressImg(img);
//        String contentType = "image/jpg";
//
//        try {
//            Files.probeContentType(img.getFile().toPath());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
//                .body(img);
//    }


    @GetMapping(value = "/image/{fileName}")
    public ResponseEntity<byte[]> getImg (@PathVariable(name = "fileName") String fileName){
        return imgService.compressImg(fileName);
    }




}
