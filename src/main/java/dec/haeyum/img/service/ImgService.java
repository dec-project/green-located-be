package dec.haeyum.img.service;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ImgService {
    Resource getImg(String fileName);

    String findContentType(String fileName);

    String downloadImg(String url) ;
}
