<template>
    <div
        data-test="vs-input"
    >
        <p
            class="hint-text"
            :id="`hint-${fieldName}`"
            v-if="hintText"
        >
            {{ hintText }}
        </p>
        <template
            v-if="$v.inputVal.$anyError || invalid"
        >
            <span
                v-for="error in errorsList"
                :key="error"
                class="error"
                :id="`error-${fieldName}`"
                aria-live="assertive"
            >
                {{ validationMessages[error] || genericValidation[error] }}
            </span>
        </template>
        <BFormInput
            ref="input"
            :type="type"
            class="vs-input"
            v-model="inputVal"
            :class="elementClass"
            :id="fieldName"
            :name="fieldName"
            :placeholder="placeholder"
            :required="isRequired"
            :autocomplete="autocompleteValue(fieldName)"
            :v="inputVal"
            :aria-invalid="$v.inputVal.$anyError || invalid"
            :aria-describedby="$v.inputVal.$anyError || invalid ? `error-${fieldName}` : null"
            :maxlength="validationRules.maxLength ? validationRules.maxLength : null"
            :minlength="validationRules.minLength ? validationRules.minLength : null"
            @blur="manualValidate"
        />
        <VsButton
            v-if="showClearButton"
            class="vs-input__clear-button d-none d-lg-block"
            data-test="input-clear-button"
            variant="transparent"
            icon="close"
            size="md"
            icon-only
            @click.native.prevent="clearInputAndFocus()"
        >
            <span class="sr-only">
                {{ clearButtonText }}
            </span>
        </VsButton>
    </div>
</template>

<script>
import Vue from 'vue';
import Vuelidate from 'vuelidate';
import { BFormInput } from 'bootstrap-vue';
import VsButton from '../button/Button';
import validateFormElementMixin from '../../../mixins/validateFormElementMixin';

Vue.use(Vuelidate);

/**
 * An input allows a user to enter a short amount of text.
 *
 * @displayName Input
 */

export default {
    name: 'VsInput',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BFormInput,
        VsButton,
    },
    mixins: [
        validateFormElementMixin,
    ],
    props: {
        /**
         * Default value of the field
         */
        value: {
            type: String,
            default: '',
        },
        /**
         * Name of the field (for name and id attributes)
         */
        fieldName: {
            type: String,
            required: true,
        },
        /**
         * Type of input
         */
        type: {
            type: String,
            default: 'text',
        },
        autoComplete: {
            type: Boolean,
            default: true,
        },
        /**
         * Rules for Vuelidate plugin
         */
        validationRules: {
            type: Object,
            default() {
                return {
                };
            },
        },
        /**
         * Prop to define invalid from parent
         */
        invalid: {
            type: Boolean,
            default: false,
        },
        /**
         * Prop to trigger manual validation. Used by a parent
         * component to trigger validation eg. when the submit
         * button is clicked.
         */
        triggerValidate: {
            type: Boolean,
            default: false,
        },
        /**
         * Specific validation messages for different
         * types of validation
         */
        validationMessages: {
            type: Object,
            default() {
                return {
                };
            },
        },
        /**
         * Fallback translated validation - this is a set of
         * validation messages to be used when no specific
         * validation message is needed, eg. "This field is required"
         */
        genericValidation: {
            type: Object,
            default() {
                return {
                };
            },
        },
        /**
         * Content for hint text
         */
        hintText: {
            type: String,
            default: '',
        },
        /**
         * Text for the 'clear' button
         * the existence of this will defined whether the button
         * also exists
         */
        clearButtonText: {
            type: String,
            default: '',
        },
        /**
         * Element placeholder text
         */
        placeholder: {
            type: String,
            default: '',
        },
    },
    computed: {
        /**
         * element type class plus error classes
         */
        elementClass() {
            const errorClass = this.$v.inputVal.$anyError || this.invalid ? 'vs-input--error' : '';
            const nameClass = `vs-input--${this.fieldName}`;

            return `${errorClass} ${nameClass}`;
        },
        showClearButton() {
            if (this.inputVal.length && this.clearButtonText !== '') {
                return true;
            }

            return false;
        },
        errorClass() {
            return this.$v.inputVal.$anyError || this.invalid ? 'vs-input--error' : '';
        },
    },
    watch: {
        /**
         * Watch for prop changing to allow parent
         * to trigger validation
         */
        triggerValidate() {
            this.manualValidate();
        },
        inputVal(newValue) {
            /**
             * Emit watchable data when the field is changed
             * @type {object}
             * @property {string} field the name of the field
             * @property {string} value the current value of the field
             */
            this.$emit('updated', {
                field: this.name,
                value: newValue,
            });
        },
        value(newValue) {
            this.inputVal = newValue;
        },
    },
    methods: {
        /**
         * Clear any text entered in the input
         */
        clearInput() {
            this.inputVal = '';
        },
        /**
         * Focus on the input
         */
        focusOnInput() {
            this.$nextTick(() => {
                this.$refs.input.$el.focus();
            });
        },
        /**
         * Clears the input on button click
         * and adds focus back to the input
         */
        clearInputAndFocus() {
            this.clearInput();
            this.focusOnInput();
        },
        /**
         *  return autocomplete value in appropriate places
         */
        autocompleteValue(fieldName) {
            // https://html.spec.whatwg.org/multipage/forms.html#enabling-client-side-automatic-filling-of-form-controls
            let autocomplete;

            switch (fieldName) {
            case 'firstName':
                autocomplete = 'given-name';
                break;

            case 'lastName':
                autocomplete = 'family-name';
                break;

            case 'Email':
                autocomplete = 'email';
                break;

            case 'PostalCode':
                autocomplete = 'postal-code';
                break;

            default:
                autocomplete = this.autoComplete ? 'on' : 'off';
                break;
            }

            return autocomplete;
        },
    },
    validations() {
        return this.rules;
    },
};
</script>

<style lang="scss">
@include forms-common;

.vs-input {
    @include form-element-styles;
    margin-top: $spacer-2;

    &.form-control-md {
        height: 50px;
    }

    &:focus {
        @include form-focus-state;
    }

    &--error {
        @include form-error-state;
    }

    &__clear-button {
        position: absolute;
        right: $spacer-5;
        top: 50%;
        transform: translate(0, -50%);
    }
}
</style>
