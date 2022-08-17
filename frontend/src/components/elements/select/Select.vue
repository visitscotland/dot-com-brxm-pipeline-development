<template>
    <div class="vs-select">
        <p
            class="hint-text"
            :id="`hint-${fieldName}`"
        >
            {{ hintText }}
        </p>
        <span
            v-for="error in errorsList"
            :key="error"
            class="error"
        >
            <template
                v-if="$v.inputVal.$anyError || invalid"
            >
                {{ validationMessages[error] || genericValidation[error] }}
            </template>
        </span>
        <div class="vs-select__container  mt-2">
            <BFormSelect
                v-model="inputVal"
                v-bind="$attrs"
                :options="fieldOptions"
                :name="fieldName"
                :id="fieldName"
                @change="emitStatus"
                @blur="emitStatus"
                data-test="vs-select"
                class="vs-select__element"
                :required="isRequired"
                :aria-invalid="$v.inputVal.$anyError || invalid"
                :aria-describedby="`hint-${fieldName}`"
                :class="errorClass"
            />
            <span class="vs-select__focus" />
        </div>
    </div>
</template>

<script>
import Vue from 'vue';
import Vuelidate from 'vuelidate';
import { BFormSelect } from 'bootstrap-vue';
import validateFormElementMixin from '../../../mixins/validateFormElementMixin';

Vue.use(Vuelidate);

const axios = require('axios');

/**
 * A select element allows a user to choose a value from a list of options.
 *
 * @displayName Select
 */

export default {
    name: 'VsSelect',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BFormSelect,
    },
    mixins: [
        validateFormElementMixin,
    ],
    props: {
        /**
         * Options for select element
         */
        options: {
            type: Array,
            required: true,
        },
        /**
         * Name of the field (for name and id attributes)
         */
        fieldName: {
            type: String,
            required: true,
        },
        /**
         * Default value of the field
         */
        value: {
            type: String,
            default: '',
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
         * Prop to trigger manual validation
         */
        triggerValidate: {
            type: Boolean,
            default: false,
        },
        /**
         * Prop to define invalid from parent
         */
        invalid: {
            type: Boolean,
            default: false,
        },
        /**
         * Validation messages
         */
        validationMessages: {
            type: Object,
            default() {
                return {
                };
            },
        },
        /**
         * URL for list of countries file - this will be used
         * if the 'country' prop is set to true
         */
        countryListUrl: {
            type: String,
            default: '',
        },
        /*
         * Content for hint text
         */
        hintText: {
            type: String,
            default: '',
        },
        /**
         * Whether the options should be a countries list
         */
        countries: {
            type: Boolean,
            default: false,
        },
        /*
         * Fallback translated validation
         */
        genericValidation: {
            type: Object,
            default() {
                return {
                };
            },
        },
    },
    data() {
        return {
            inputVal: this.value,
            touched: false,
            countryList: [],
        };
    },
    computed: {
        errorClass() {
            return this.$v.inputVal.$anyError || this.invalid ? 'vs-select__element--error' : '';
        },
        fieldOptions() {
            return this.countries ? this.countryList : this.options;
        },
    },
    watch: {
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
        /**
         * Watch for prop changing to allow parent
         * to trigger validation
         */
        triggerValidate() {
            this.manualValidate();
        },
    },
    beforeMount() {
        /**
         * import country list
         */
        if (this.countries) {
            axios.get(this.countryListUrl)
                .then((response) => {
                    this.countryList = response.data.countries;
                });
        }
    },
    validations() {
        return this.rules;
    },
};
</script>

<style lang="scss">
    @include forms-common;

    .vs-select {
        &__container {
            position: relative;
            width: 100%;
            cursor: pointer;
            height: 50px;
            border-radius: 0;

            &::after {
                content: "";
                width: 1.5rem;
                height: 1.5rem;
                border: 1px solid $color-black;
                border-radius: 1000px;
                background-image: url('~#assets/svg/icons/chevron.svg');
                display: block;
                position: absolute;
                top: calc(50% - #{$spacer-3});
                right: $spacer-4;
                background-repeat: no-repeat;
                background-size: 60% 60%;
                background-position: center center;
                transform: rotate(180deg);
                pointer-events: none;
            }
        }

        &__element {
            // A reset of styles, including removing the default dropdown arrow
            appearance: none;
            background-color: transparent;
            border: none;
            padding: 0 $spacer-4 0;
            margin: 0;
            width: 100%;
            font-family: inherit;
            font-size: inherit;
            cursor: inherit;
            line-height: inherit;
            height: 50px;
            @include form-element-styles;

            &--error {
                @include form-error-state;
            }

            &:focus {
                outline: none;
                box-shadow: none;

                & + .vs-select__focus {
                    position: absolute;
                    top: -1px;
                    left: -1px;
                    right: -1px;
                    bottom: -1px;
                    pointer-events: none;
                    @include form-focus-state;
                }
            }
        }
    }
</style>
