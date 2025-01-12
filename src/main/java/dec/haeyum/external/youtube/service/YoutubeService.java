package dec.haeyum.external.youtube.service;

import dec.haeyum.external.youtube.dto.YoutubeDetailDto;

public interface YoutubeService {


    YoutubeDetailDto searchVideoUrl(String searchWord);

}
