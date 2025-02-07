package dec.haeyum.img.service.impl;

import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.img.service.ImgService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImgServiceImpl implements ImgService {

    @Value("${spring.file.filePath}")
    private String filePath;
    private WebClient webClient;

    @PostConstruct
    public void init(){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .build();
    }


    @Override
    public Resource getImg(String fileName) {
        Resource resource = null;

        try {
            resource = new UrlResource("file:" + filePath + fileName);
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(ErrorCode.NOT_EXISTED_IMG);
        }

        return resource;

    }

    @Override
    public String downloadImg(String url)  {

        if (url == null){
            return "";
        }

        String extension = url.substring(url.lastIndexOf("."));

        String uuidName = UUID.randomUUID().toString() + extension;

        String fileName = filePath + uuidName;

        try {
            byte[] response = webClient.get()
                    .uri(url)
                    .header("Accept-Encoding","gzip")
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
            if (response != null){
                Path path = Paths.get(fileName);
                Files.write(path,response);
            }else {
                throw new BusinessException(ErrorCode.NOT_EXISTED_IMGPATH);
            }

        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return uuidName;
    }

    @Override
    public String downloadImg(MultipartFile profileImg) {
        String originalFilename = profileImg.getOriginalFilename();
        String extension = ".webp";
        String uuidFileName = UUID.randomUUID().toString();
        String fileName = uuidFileName + extension;
        String filePathName = filePath + fileName;
        File file = new File(filePathName);
        try {
            profileImg.transferTo(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileName;
    }

    @Override
    public void deleteImg(String profileImg) {
        String imgFilePath = filePath + profileImg;
        Path file = Paths.get(imgFilePath);
        try {
            Files.delete(file);
        }catch (IOException e){
            throw new BusinessException(ErrorCode.CANT_DELETE_FILE);
        }
    }

    @Override
    public String downloadImg(Path path) {
        String originalFile = path.getFileName().toString();
        String originalFileName = originalFile.substring(0,originalFile.lastIndexOf("."));
        try {
            // originalFile
            String extension = ".webp";
            String fileName = originalFileName + extension;
            // 저장 위치
            String destinationPath = filePath + fileName;
            Path destination = Paths.get(destinationPath);
            Files.copy(path,destination, StandardCopyOption.REPLACE_EXISTING);
            log.info("downloadImg success = {}",originalFileName);
            return fileName;
        }catch (Exception e){
            e.printStackTrace();
            log.error("downloadImg Fail = {}", originalFileName);
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> compressImg(String fileName) {
        try {
            File file = new File(filePath + fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Thumbnails.of(file)
                    .size(150,150)
                    .outputQuality(0.7)
                    .outputFormat("jpeg")
                    .toOutputStream(baos);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(baos.toByteArray());


        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

}
