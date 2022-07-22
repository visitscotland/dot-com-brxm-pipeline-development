<template>
    <div class="cookies-fallback">
        <img
            class="warning-icon"
            style="margin: 0 auto; height: 60px; margin-bottom: 2rem"
            src="../../../assets/images/icons/warning.png"
            alt="warning icon"
        >
        <p
            class="fallback-text"
            style="padding: 0"
        >
            <!-- eslint-disable vue/no-v-html -->
            <span v-html="fallbackMessage" />
            <!-- eslint-enable vue/no-v-html -->
            <button
                id="ot-sdk-btn"
                class="ot-sdk-show-settings"
            >
                Button Text
            </button>
        </p>
    </div>
</template>

<script>

/**
 * Component used to show users a message whenever content
 * behind it is locked due to their cookies preferences
 *
 * @displayName Cookies Fallback
 */

export default {
    name: 'VsCookiesFallback',
    props: {
        fallbackMessage: {
            type: String,
            default: 'Cookies are required to view this content. <br>Change your preferences at ',
        },
    },
};
</script>
<style lang="scss">
.cookies-fallback {
    background-color: #e2e2e2;
    padding-top: 20px;
    margin-top: 30px;
    margin-bottom: 30px;
    text-align: center;
    padding-bottom: 5px;
    width: 100%;

    img.warning-icon {
        height: 60px;
        margin-bottom: 15px;
        padding-left: 10px;
    }
}

@media (max-width: 575px) {
    .cookies-fallback {
        width: 100%;
        padding: 15px;

        img {
            padding-left: 0 !important;
            height: 50px !important;
        }

        .fallback-text {
            padding-left: 20px;
        }
    }
}
</style>
