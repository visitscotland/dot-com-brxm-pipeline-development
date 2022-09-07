<template>
    <div
        class="vs-form"
        data-test="vs-form"
    >
        <!-- element into which the (completely empty) form is embedded invisibly -->
        <form class="d-none" />

        <template v-if="!submitted">
            <form @submit.prevent="preSubmit">
                <fieldset>
                    <legend class="vs-form__main-heading vs-heading--style-level-2">
                        {{ getTranslatedContent('heading') }}
                    </legend>
                    <BFormGroup
                        v-for="(field, index) in formData.fields"
                        :key="field.name"
                        :label="needsLabel(field) ? getTranslatedLabel(field.name, index) : ''"
                        :label-for="needsLabel(field) ? field.name : ''"
                        :class="conditionalElementClass(field.name)"
                    >
                        <legend
                            v-if="!isUndefined(field.descriptor)
                                && field.element === 'checkbox'"
                        >
                            {{ getTranslatedLegend(field.name, index) }}
                        </legend>
                        <div
                            :class="conditionalElementClass(field.name)"
                            aria-live="assertive"
                        >
                            <template v-if="field.element === 'input'">
                                <VsInput
                                    :ref="field.name"
                                    @status-update="updateFieldData"
                                    :field-name="field.name"
                                    :type="field.type"
                                    :validation-rules="field.validation || {}"
                                    :validation-messages="getTranslatedValidation(field.name, index)
                                        || {}"
                                    :generic-validation="getMessagingData('validation', language)"
                                    :invalid="errorFields.indexOf(field.name) > -1 ? true : false"
                                    :trigger-validate="triggerValidate"
                                    :hint-text="getTranslatedHint(field.name, index)"
                                    :placeholder="field.placeholder || ''"
                                />
                            </template>

                            <template v-if="field.element === 'select'">
                                <VsSelect
                                    :options="getTranslatedOptions(field.name, index)"
                                    :ref="field.name"
                                    @status-update="updateFieldData"
                                    :field-name="field.name"
                                    :validation-rules="field.validation || {}"
                                    :validation-messages="getTranslatedValidation(field.name, index)
                                        || {}"
                                    :generic-validation="getMessagingData('validation', language)"
                                    :invalid="errorFields.indexOf(field.name) > -1 ? true : false"
                                    :trigger-validate="triggerValidate"
                                    :country-list-url="countryListUrl"
                                    :countries="field.countries"
                                    :hint-text="getTranslatedHint(field.name, index)"
                                />
                            </template>

                            <template v-if="field.element === 'checkbox'">
                                <VsCheckbox
                                    :key="field.name"
                                    :ref="field.name"
                                    :name="field.name"
                                    :value="field.value"
                                    :id="field.name"
                                    :label="getTranslatedLabel(field.name, index)"
                                    @status-update="updateFieldData"
                                    :field-name="field.name"
                                    :validation-rules="field.validation || {}"
                                    :validation-messages="getTranslatedValidation(field.name, index)
                                        || {}"
                                    :generic-validation="getMessagingData('validation', language)"
                                    :invalid="errorFields.indexOf(field.name) > -1 ? true : false"
                                    :trigger-validate="triggerValidate"
                                    :optional-text="getMessagingData('optional', language)"
                                    :hint-text="getTranslatedHint(field.name, index)"
                                    :info-text="getTranslatedInfo(field.name, index)"
                                />
                            </template>
                        </div>
                    </BFormGroup>
                </fieldset>

                <VsRecaptcha
                    @verified="onRecaptchaVerify"
                    :site-key="recaptchaKey"
                    :invalid="!recaptchaVerified && showErrorMessage"
                    :language="language"
                    :error-msg="getMessagingData('recaptchaError', language)"
                    class="mt-9"
                    :textarea-label="recaptchaTextareaLabel"
                />

                <VsButton
                    variant="primary"
                    type="submit"
                    class="vs-form__submit mt-9"
                    @click.native="preSubmit"
                >
                    {{ getTranslatedContent('submit') }}
                </VsButton>
            </form>
        </template>

        <div
            class="vs-form__no-js"
            data-test="vs-form-no-js"
        >
            <VsIcon
                name="review"
                variant="primary"
                size="xl"
                class="mb-5"
            />
            <div>
                <slot name="no-js" />
            </div>
        </div>

        <div aria-live="assertive">
            <p v-if="submitting">
                <slot name="submitting" />
            </p>

            <template v-if="submitted">
                <VsHeading
                    v-if="getTranslatedContent('successHeading')"
                    level="2"
                >
                    {{ getTranslatedContent('successHeading') }}
                </VsHeading>

                <!-- eslint-disable vue/no-v-html -->
                <p
                    class="vs-form__content"
                    v-html="getTranslatedContent('successContent')"
                />
                <!-- eslint-enable vue/no-v-html -->
            </template>

            <p v-if="submitError">
                <slot name="submitError" />
            </p>
        </div>
    </div>
</template>

<script>
import Vue from 'vue';
import { BFormGroup } from 'bootstrap-vue';
import VsInput from '../../elements/input/Input';
import VsSelect from '../../elements/select/Select';
import VsCheckbox from '../../elements/checkbox/Checkbox';
import VsIcon from '../../elements/icon/Icon';
import VsRecaptcha from '../../elements/recaptcha/Recaptcha';
import VsButton from '../../elements/button/Button';
import VsHeading from '../../elements/heading/Heading';

const axios = require('axios');

/**
 * A form that results in a user posting data to Marketo.
 *
 * @displayName Marketo Form
 */

export default {
    name: 'VsMarketoForm',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsInput,
        VsSelect,
        VsCheckbox,
        BFormGroup,
        VsRecaptcha,
        VsButton,
        VsIcon,
        VsHeading,
    },
    props: {
        /**
         * The URL for the form data file
         */
        dataUrl: {
            type: String,
            required: true,
        },
        /**
         * Recaptcha site key string
         */
        recaptchaKey: {
            type: String,
            required: true,
        },
        /**
         * Text for invisible recaptcha textarea - tells
         * screenreader users it's not needed
         */
        recaptchaTextareaLabel: {
            type: String,
            default: 'Does not need any text',
        },
        /**
         * If the form should use sandbox or live Marketo details
         */
        isProd: {
            type: Boolean,
            default: false,
        },
        /**
         * Munchkin ID for the Marketo environment
         */
        munchkinId: {
            type: String,
            required: true,
        },
        /**
         * Marketo instance URL for the form
         */
        marketoInstance: {
            type: String,
            required: true,
        },
        /**
         * Language indicator for content
         */
        language: {
            type: String,
            default: 'en',
        },
        /**
         * URL for generic messaging config
         */
        messagingUrl: {
            type: String,
            required: true,
        },
        /**
         * URL for generic messaging config
         */
        countryListUrl: {
            type: String,
            default: '',
        },
    },
    data() {
        return {
            submitted: false,
            submitting: false,
            submitError: false,
            formData: {
            },
            messagingData: {
            },
            form: {
            },
            formIsInvalid: false,
            showErrorMessage: false,
            errorFields: [
            ],
            triggerValidate: false,
            recaptchaVerified: false,
            conditionalFields: {
            },
            inputVal: '',
        };
    },
    computed: {
        formId() {
            return this.isProd ? this.formData.formLiveId : this.formData.formSandboxId;
        },
        showFormHeading() {
            if (!this.isUndefined(this.getTranslatedContent)
                && !this.isUndefined(this.getTranslatedContent('heading'))) {
                return true;
            }

            return false;
        },
    },
    created() {
        this.getFormData();
        this.getGlobalMessaging();
    },
    methods: {
        /**
         * Axios call to retrieve form data
         */
        getFormData() {
            axios.get(this.dataUrl)
                .then((response) => {
                    this.formData = response.data;

                    if (window.MktoForms2) {
                        window.MktoForms2
                            .loadForm(this.marketoInstance, this.munchkinId, this.formId);
                    }

                    response.data.fields.forEach((field) => {
                        // create a data entry for each field
                        this.form[field.name] = '';

                        // ensure that hidden fields don't show on load
                        if (field.conditional) {
                            // Vue.set needed here to ensure reactivity of
                            // elements added to the object.
                            Vue.set(this.conditionalFields, field.name, false);
                        }
                    });
                });
        },
        /**
         * Axios call to retrieve global messaging data
         */
        getGlobalMessaging() {
            axios.get(this.messagingUrl)
                .then((response) => {
                    this.messagingData = response.data;
                });
        },
        /**
         * get appropriate language object
         */
        getLanguageObj() {
            let languageObj;

            if (!this.isUndefined(this.formData[this.language])) {
                languageObj = this.formData[this.language] || undefined;
            } else {
                languageObj = {
                };
            }

            return languageObj;
        },
        /**
         * get translated label if available
         */
        getTranslatedLabel(fieldName, index) {
            const languageObj = this.getLanguageObj();
            let labelText = '';

            if (this.language !== 'en'
                && !this.isUndefined(languageObj[fieldName])
                && !this.isUndefined(languageObj[fieldName].label)
            ) {
                labelText = languageObj[fieldName].label;
            } else {
                labelText = this.formData.fields[index].label;
            }

            if (this.showOptionalText(this.formData.fields[index])
                && !this.isUndefined(this.getMessagingData('optional', this.language))) {
                labelText = `${labelText} (${this.getMessagingData('optional', this.language)})`;
            }

            return labelText;
        },
        /**
         * get translated label if available
         */
        getTranslatedLegend(fieldName, index) {
            const languageObj = this.getLanguageObj();
            let legendText = '';

            if (this.language !== 'en'
                && !this.isUndefined(languageObj[fieldName])
                && !this.isUndefined(languageObj[fieldName].descriptor)
            ) {
                legendText = languageObj[fieldName].descriptor;
            } else {
                legendText = this.formData.fields[index].descriptor;
            }

            return legendText;
        },
        /*
         * get translated info content if available
         */
        getTranslatedInfo(fieldName, index) {
            const languageObj = this.getLanguageObj();
            let infoText = '';

            if (this.language !== 'en'
                && !this.isUndefined(languageObj[fieldName])
                && !this.isUndefined(languageObj[fieldName].info)
            ) {
                infoText = languageObj[fieldName].info;
            } else {
                infoText = this.formData.fields[index].info;
            }

            return infoText;
        },
        /**
         * get translated validation messages
         */
        getTranslatedValidation(fieldName, index) {
            const languageObj = this.getLanguageObj();

            let validationObj;

            if (this.language === 'en'
                && !this.isUndefined(this.formData.fields[index].validationMessages)) {
                validationObj = this.formData.fields[index].validationMessages;
            } else if (!this.isUndefined(languageObj[fieldName])
                && !this.isUndefined(languageObj[fieldName].validationMessages)) {
                validationObj = languageObj[fieldName].validationMessages;
            } else if (this.language === 'en') {
                validationObj = this.formData.fields[index].validationMessages;
            }

            return validationObj;
        },
        /**
         * get language appriopriate options for a select element
         */
        getTranslatedOptions(fieldName, index) {
            const languageObj = this.getLanguageObj();

            let optionsArr = [];

            if (this.language !== 'en'
                && !this.isUndefined(languageObj[fieldName])
                && !this.isUndefined([fieldName].options)) {
                optionsArr = languageObj[fieldName].options;
            } else {
                optionsArr = this.formData.fields[index].options;
            }

            if (typeof optionsArr === 'undefined') {
                optionsArr = [];
            }

            return optionsArr;
        },
        getTranslatedHint(fieldName, index) {
            const languageObj = this.getLanguageObj();
            let hintText = '';

            if (this.language !== 'en'
                && !this.isUndefined(languageObj[fieldName])
                && !this.isUndefined(languageObj[fieldName].hint)) {
                hintText = languageObj[fieldName].hint;
            } else if (!this.isUndefined(this.formData.fields[index].hint)) {
                hintText = this.formData.fields[index].hint;
            } else {
                hintText = '';
            }

            return hintText;
        },
        getTranslatedContent(type) {
            let text;
            const languageObj = this.getLanguageObj();

            if (this.language === 'en'
                && !this.isUndefined(this.formData.content)
                && !this.isUndefined(this.formData.content[type])
            ) {
                text = this.formData.content[type];
            } else if (!this.isUndefined(languageObj.content)
                && !this.isUndefined(languageObj.content[type])) {
                text = languageObj.content[type];
            } else {
                text = this.getMessagingData(type, this.language);
            }

            return text;
        },
        /**
         * check messaging data exists and then pass value back
         */
        getMessagingData(type, lang) {
            if (Object.keys(this.messagingData).length > 0) {
                const dataType = this.messagingData[lang];
                return dataType[type];
            }

            if (type === 'validation') {
                return {
                };
            }

            return '';
        },
        /**
         * check if value is undefined
         */
        isUndefined(value) {
            if (typeof value === 'undefined') {
                return true;
            }

            return false;
        },
        /**
         * update field data and error status
         */
        updateFieldData(data) {
            this.form[data.field] = data.value || '';

            if (Array.isArray(data.errors)) {
                this.formIsInvalid = data.errors.length > 0;
            } else {
                this.formIsInvalid = data.errors;
            }

            this.manageErrorStatus(data.field, data.errors);
            this.checkConditionalFields();
        },
        /**
         * update error status of fields for validation feedback
         */
        manageErrorStatus(field, errors) {
            const index = this.errorFields.indexOf(field);

            if (index !== -1) {
                if (!errors || errors.length < 1) {
                    this.errorFields.splice(index, 1);
                }
            } else if (errors && errors.length > 0) {
                this.errorFields.push(field);
            }
        },
        showOptionalText(field) {
            if (this.isUndefined(field.validation) || this.isUndefined(field.validation.required)) {
                return true;
            }

            return false;
        },
        /**
         * whether or not an element should have a label defined (for Bootstrap Vue)
         */
        needsLabel(field) {
            if (field.element === 'radio'
                || field.element === 'submit'
                || field.element === 'checkbox') {
                return false;
            }

            return true;
        },
        /**
         * before submitting validate fields and recaptcha.
         * If successful run the Marketo submit method
         */
        preSubmit(e) {
            e.preventDefault();

            function isRequired(value) {
                return value.validation && value.validation.required;
            }

            this.onRecaptchaVerify();

            this.triggerValidate = true;

            const fieldIsRequired = this.formData.fields.filter(isRequired);

            if (fieldIsRequired.length === 0) {
                this.formIsInvalid = false;
            } else {
                fieldIsRequired.forEach((field) => {
                    if (this.form[field.name] === '') {
                        this.formIsInvalid = true;
                    }
                });
            }

            this.showErrorMessage = this.formIsInvalid.length > 1;

            // check conditional fields - if they're not shown
            // then clear any value they may have
            Object.entries(this.conditionalFields).forEach(([key, value]) => {
                if (!value) {
                    this.form[key] = '';
                }
            });

            if (this.errorFields.length > 0) {
                this.formIsInvalid = true;
            }

            if (!this.formIsInvalid && this.recaptchaVerified) {
                this.marketoSubmit();
            } else {
                this.showErrorMessage = true;
            }
        },
        /**
         * submits form data to Marketo payload and sets submitted status
         */
        marketoSubmit() {
            const myForm = window.MktoForms2.allForms()[0];
            myForm.addHiddenFields(this.form);
            myForm.addHiddenFields({
                lastReCAPTCHAUserFingerprint: window.grecaptcha.getResponse(),
                lastRecaptchaEnabledFormID: this.formId,
            });

            myForm.submit(() => {
                this.submitting = true;
            });
            /* eslint-ignore-next-line */
            myForm.onSuccess(() => {
                this.submitting = false;
                this.submitted = true;
                return false;
            });
        },
        /**
         * listens to recaptcha response to check if it's verified
         */
        onRecaptchaVerify() {
            if (window.grecaptcha.getResponse() !== '') {
                this.recaptchaVerified = true;
            } else {
                this.recaptchaVerified = false;
            }
        },
        /**
         * checks whether conditional fields meet the rules to show them
         */
        checkConditionalFields() {
            Object.keys(this.conditionalFields).forEach((field) => {
                // match the field to the form data
                const fieldData = this.formData.fields.find((o) => o.name === field);
                let showField = true;

                // iterate through rules in object
                Object.keys(fieldData.conditional).forEach((rule) => {
                    const conditions = fieldData.conditional[rule];
                    if (Array.isArray(conditions)) {
                        // if the rule is an array of values
                        // set the value to false if the field value isn't in the array
                        if (conditions.indexOf(this.form[rule]) === -1) {
                            showField = false;
                        }
                    } else if (this.form[rule] !== conditions) {
                        // if the rule is a string just check the field value
                        // against the string
                        showField = false;
                    }

                    if (showField) {
                        this.conditionalFields[field] = true;
                    } else {
                        this.conditionalFields[field] = false;
                    }
                });
            });
        },
        /**
         * return the correct class to show or hide
         * conditional elements
         */
        conditionalElementClass(fieldName) {
            return this.conditionalFields[fieldName] === true
                || typeof this.conditionalFields[fieldName] === 'undefined'
                ? '' : 'd-none';
        },
    },
};
</script>

<style lang='scss'>
    .vs-form {
        &__main-heading {
            @extend %heading-default-styles;
        }

        &__content {
            font-size: $font-size-6;
        }
        label {
            font-weight: $font-weight-semi-bold;
            margin-bottom: 0;
        }

        .error {
            font-size: $font-size-body;
            color: $color-theme-danger;
        }

        .hint-text {
            font-size: $font-size-body;
            color: $color-gray-shade-1;
            margin-bottom: 0;
        }

        .form-group {
            margin-bottom: $spacer-6;
        }

        &__no-js {
            display: none;
        }
    }

    @include no-js {
        .vs-form {

            & > form {
                display: none;
            }

            &__no-js {
                display: block;
            }
        }
    }
</style>
