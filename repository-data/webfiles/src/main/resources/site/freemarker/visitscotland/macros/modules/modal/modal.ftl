<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-modal.ftl">

<#macro modal isVideoModal closeBtnText modalId>
    <vs-modal
        modal-id="${modalId}"
        close-btn-text="${closeBtnText}"
        :is-video-modal="${isVideoModal}"
    >
        <#nested>
    </vs-modal>
</#macro>