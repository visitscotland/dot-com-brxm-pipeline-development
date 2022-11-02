<template>
    <div
        class="vs-recaptcha"
        aria-live="assertive"
    >
        <span
            v-if="invalid"
            class="error"
        >
            {{ errorMsg }}
        </span>
        <VueRecaptcha
            :sitekey="siteKey"
            @verify="verified"
            @render="render"
            class="vs-recaptcha__embed"
            :class="invalid ? 'vs-recaptcha__embed--error' :''"
            :language="language"
        />
    </div>
</template>

<script>
import { VueRecaptcha } from 'vue-recaptcha';

/**
 * The reCaptcha component allows us to tell human and
 * automated bots apart when submitting data.
 *
 * @displayName Recaptcha
 */

export default {
    name: 'VsRecaptcha',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VueRecaptcha,
    },
    props: {
        /**
         * Recaptcha site key string
         */
        siteKey: {
            type: String,
            required: true,
        },
        /**
         * Whether or not the form is invalid - this should
         * also only be true when an attempt at submission has
         * been made
         */
        invalid: {
            type: Boolean,
            default: false,
        },
        /**
         * Language to show text in
         */
        language: {
            type: String,
            default: 'en',
        },
        /**
         * Validation message if the recaptcha isn't completed
         */
        errorMsg: {
            type: String,
            default: '',
        },
        /**
         * Text for invisible recaptcha textarea - tells
         * screenreader users it's not needed
         */
        textareaLabel: {
            type: String,
            default: 'Does not need any text',
        },
    },
    methods: {
        verified(response) {
            /**
             * Emit Google response to recaptcha submission
             * @type {string}
             * @property {object} field the object returned by Google
             * in response to the recaptcha being submitted
             */
            this.$emit('verified', response);
        },
        render() {
            // when recaptcha is rendered add a label to the invisible textarea
            // to show screenreader users it doesn't need filling
            const recaptchaTextarea = document.getElementById('g-recaptcha-response');
            const recaptchaParent = recaptchaTextarea.parentNode;
            const existingRecaptchaLabel = document.querySelector('label[for="g-recaptcha-response"]');
            const recaptchaLabel = document.createElement('label');
            recaptchaLabel.setAttribute('for', 'g-recaptcha-response');
            recaptchaLabel.setAttribute('class', 'sr-only');
            recaptchaLabel.textContent = this.textareaLabel;

            if (!existingRecaptchaLabel) {
                recaptchaParent.insertBefore(recaptchaLabel, recaptchaTextarea);
            }
        },
    },
};
</script>

<style lang="scss">
    .vs-recaptcha {
        &__embed {
            &--error {
                & > div {
                    outline: 2px solid $color-theme-danger;
                    margin-top: $spacer-2;
                }
            }
        }
    }
</style>
