<#include "../modal/modal.ftl">
<#include "../../../../frontend/components/vs-video.ftl">
<#include "video-schema.ftl">

<#macro videoModal video >
<@videoSchema video />
    <@modal
        modalId="${video.youtubeId}"
        closeBtnText="${label('essentials.global', 'close')}"
        isVideoModal="true"
    >
        <vs-video
            video-id="${video.youtubeId}"
            video-title="${video.label}"
            language="${locale}"
            single-minute-descriptor="${label('video', 'video.minute-text')}"
            plural-minute-descriptor="${label('video', 'video.minutes-text')}"
            no-cookies-message="${label('video', 'video.no-cookies')}"
            no-js-message="${label('video', 'video.no-js')}"
            cookie-btn-text="${label('essentials.global', 'cookie.link-message')}"
            error-message="${label('essentials.global', 'third-party-error')}"
        />
    </@modal>
</#macro>
