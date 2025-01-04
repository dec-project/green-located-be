package dec.haeyum.img.service.impl;

import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.img.service.ImgService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImgServiceImpl implements ImgService {

    @Value("${spring.file.filePath}")
    private String filePath;

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
}
