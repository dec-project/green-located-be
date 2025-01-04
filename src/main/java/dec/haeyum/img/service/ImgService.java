package dec.haeyum.img.service;

import org.springframework.core.io.Resource;

public interface ImgService {
    Resource getImg(String fileName);

    String findContentType(String fileName);
}
