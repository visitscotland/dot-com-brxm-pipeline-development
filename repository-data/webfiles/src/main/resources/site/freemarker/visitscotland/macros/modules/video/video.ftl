<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-video.ftl">
<#include "video-schema.ftl">

<#macro video video>
    <@videoSchema video />
    <vs-video
            video-id="${video.youtubeId}"
            language="${locale}"
            single-minute-descriptor="${label('video', 'video.minute-text')}"
            plural-minute-descriptor="${label('video', 'video.minutes-text')}"
    />
</#macro>
