import {
    required,
    email,
    minLength,
    maxLength,
    helpers,
} from 'vuelidate/lib/validators';

const postcode = helpers.regex('postcode', /^([A-Z][A-HJ-Y]?\d[A-Z\d]? ?\d[A-Z]{2}|GIR ?0A{2})$/);

const validateFormElementMixin = {
    data() {
        return {
            inputVal: this.value,
        };
    },
    computed: {
        /**
         * calculate whether element is required
         */
        isRequired() {
            if (typeof required !== 'undefined' && 'required' in this.validationRules) {
                return true;
            }

            return false;
        },
        /**
         * set rules object for validation
         * needed because `required`, `email` and other values
         * can't be key value pairs
         */
        rules() {
            let rulesObj = {
            };

            // eslint-disable-next-line
            for (const [key, value] of Object.entries(this.validationRules)) {
                // rules have to be either a function defined by
                // https://vuelidate-next.netlify.app/validators.html
                if (key === 'required') {
                    rulesObj = {
                        ...rulesObj,
                        required,
                    };
                } else if (key === 'email') {
                    rulesObj = {
                        ...rulesObj,
                        email,
                    };
                } else if (key === 'minLength') {
                    rulesObj = {
                        ...rulesObj,
                        minLength: minLength(value),
                    };
                } else if (key === 'maxLength') {
                    rulesObj = {
                        ...rulesObj,
                        maxLength: maxLength(value),
                    };
                } else if (key === 'invalidVal') {
                    const noInvalid = (val) => val
                        .indexOf(this.validationRules.invalidVal) === -1;

                    rulesObj = {
                        ...rulesObj,
                        noInvalid,
                    };
                } else if (key === 'postcode') {
                    rulesObj = {
                        ...rulesObj,
                        postcode,
                    };
                }
            }

            if (typeof rulesObj !== 'undefined') {
                return {
                    inputVal: rulesObj,
                };
            }

            return {
            };
        },
        errorsList() {
            const errorsArray = [];
            const rulesKeys = Object.keys(this.rules.inputVal);

            rulesKeys.forEach((key) => {
                if (!this.$v.inputVal[key]) {
                    errorsArray.push(key);
                }
            });

            return errorsArray;
        },
    },
    methods: {
        /**
         * manually run validation and emit to parent
         */
        manualValidate() {
            if ('required' in this.rules.inputVal && !this.inputVal) {
                if (this.errorsList.indexOf('required') === -1) {
                    this.errorsList.push('required');
                }
            } else {
                this.errorsList.forEach((error, index) => {
                    if (error === 'required') {
                        this.errorsList.splice(index, 1);
                    }
                });
            }

            this.touched = true;
            this.$v.$touch();

            /**
             * Emit watchable data when a field's validation
             * status is changed
             * @type {object}
             * @property {string} field the name of the field
             * @property {string} value the current value of the field
             * @property {array} errors a list of errors that the field has
             */
            this.$emit('status-update', {
                field: this.fieldName,
                value: this.inputVal,
                errors: this.errorsList,
            });
        },
        /**
         * Emit status of input - for automatic updating
         */
        emitStatus() {
            setTimeout(() => {
                this.manualValidate();
                this.touched = true;
                this.$v.$touch();
            }, 50);
        },
    },
};

export default validateFormElementMixin;
