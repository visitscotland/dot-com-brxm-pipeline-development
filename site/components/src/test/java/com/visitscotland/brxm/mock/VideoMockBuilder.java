package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.Image;
import com.visitscotland.brxm.hippobeans.Video;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VideoMockBuilder {

    Video video = mock(Video.class);

    public VideoMockBuilder withImage(){
        when(video.getImage()).thenReturn(mock(Image.class));
        return this;
    }

    public VideoMockBuilder label(String label) {
        when(video.getLabel()).thenReturn(label);
        return this;
    }

    public VideoMockBuilder teaser(String teaser) {
        when(video.getTeaser()).thenReturn(teaser);
        return this;
    }

    public VideoMockBuilder url(String url) {
        if (!url.contains("v=")){
            throw new IllegalArgumentException("The current validator prevents from entering URLs without the 'v' parameter");
        }
        when(video.getUrl()).thenReturn(url);
        return this;
    }

    public VideoMockBuilder title(String title) {
        when(video.getTitle()).thenReturn(title);
        return this;
    }

    public Video build(){
        return  video;
    }

}
