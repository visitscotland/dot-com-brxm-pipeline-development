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
            no-cookies-message="${label('video', 'video.no-cookies')}"
            no-js-message="${label('video', 'video.no-js')}"
            cookie-link-text="${label('essentials.global', 'cookie.link-message')}"
            error-message="${label('essentials.global', 'third-party-error')}"
        />
    </@modal>
</#macro>
