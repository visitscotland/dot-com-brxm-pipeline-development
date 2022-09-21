<#include "../modal/modal.ftl">
<#include "../../../../frontend/components/vs-video.ftl">

<#macro videoModal videoId videoTitle="">
    <@modal
        modalId="${videoId}"
        closeBtnText="${label('essentials.global', 'close')}"
        isVideoModal="true"
    >
        <vs-video
            video-id="${videoId}"
            video-title="${videoTitle}"
            language="${locale}"
            single-minute-descriptor="${label('video', 'video.minute-text')}"
            plural-minute-descriptor="${label('video', 'video.minutes-text')}"
        />
    </@modal>
</#macro>
