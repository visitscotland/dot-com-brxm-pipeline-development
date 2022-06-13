<template>
    <div
        class="vs-warning"
        :class="{
            [`vs-warning--${variant}`]: true
        }"
        data-test="vs-warning"
    >
        <VsIcon
            class="vs-warning__icon"
            name="review"
            custom-colour="#FCCA1B"
        />
        <p
            class="vs-warning__message"
        >
            {{ warningMessage }}
        </p>
        <VsLink
            v-if="warningLink && warningLink.url"
            data-test="vs-warning__link"
            :href="warningLink.url"
            variant="dark"
        >
            {{ warningLink.label }}
        </VsLink>
    </div>
</template>
<script>

import VsIcon from '@components/elements/icon';
import VsLink from '@components/elements/link';

/**
 * A generic warning component that expands to cover whatever component
 * contains it
 *
 * @displayName Warning
 */
export default {
    name: 'VsWarning',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsIcon,
        VsLink,
    },
    props: {
        /**
        * The warning message to display to the user
        */
        warningMessage: {
            type: String,
            required: true,
        },
        /**
        * An optional link to a page that the user can go to to try and
        * correct the issue
        */
        warningLink: {
            type: Object,
            default: null,
        },
        variant: {
            type: String,
            default: 'normal',
            validator: (value) => value.match(
                /(small|normal)/,
            ),
        },
    },
};
</script>

<style lang="scss">
    .vs-warning {
        position: absolute;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        padding: $spacer-4;
        display: flex;
        flex-direction: column;
        align-items: center;
        text-align: center;
        justify-content: center;
        background-color: rgba($color-black, 0.8);
        color: $color-white;
    }

    .vs-warning__icon {
        width: 4rem !important;
        height: 4rem !important;
        margin-bottom: 2rem;
    }

    .vs-warning--small {
        .vs-warning__icon {
            width: 3rem !important;
            height: 3rem !important;
            margin-bottom: 1rem;
        }
    }

    .vs-warning__message {
        margin-bottom: $spacer-0;
    }
</style>
