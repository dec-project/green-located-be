package dec.haeyum.img.service.impl;

import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.img.service.ImgService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
            Path file = Paths.get(filePath).resolve(fileName).normalize();
            if (!Files.exists(file)){
                throw new BusinessException(ErrorCode.NOT_EXISTED_IMG);
            }
            resource = new UrlResource("file:" + filePath + fileName);
        }catch (MalformedURLException e){
            throw new BusinessException(ErrorCode.NOT_EXISTED_IMG);
        }
        return resource;

    }

    @Override
    public String findContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("."));
        if (extension.equals(".png")){
            return "image/png";
        } else if (extension.equals("jpg") || extension.equals("jpeg")) {
            return "image/jpeg";
        } else if (extension.equals("gif")) {
            return "image/gif";
        }
        return "image/jpeg";
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
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
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

}
