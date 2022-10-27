<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-video.ftl">
<#include "video-schema.ftl">

<#macro video video>
    <@videoSchema video />
    <vs-video
        video-title="${video.cta}"
        video-id="${video.youtubeId}"
        language="${locale}"
        single-minute-descriptor="${label('video', 'video.minute-text')}"
        plural-minute-descriptor="${label('video', 'video.minutes-text')}"
        no-cookies-message="${label('video', 'video.no-cookies')}"
        no-js-message="${label('video', 'video.no-js')}"
        cookie-btn-text="${label('essentials.global', 'cookie.link-message')}"
        error-message="${label('essentials.global', 'third-party-error')}"
    >
    </vs-video>
</#macro>
