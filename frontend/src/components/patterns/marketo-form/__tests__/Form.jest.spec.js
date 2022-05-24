import { shallowMount } from '@vue/test-utils';
import VsMarketoForm from '../MarketoForm';

const formData = {
    formSandboxId: '90',
    formLiveId: '91',
    content: {
        heading: 'What are your details?',
        successHeading: 'What happens next?',
        successContent: 'submitted text',
        submit: 'Subscribe',
        noJs: 'no js content',
    },
    fields: [
        {
            name: 'FirstName',
            element: 'input',
            type: 'text',
            label: 'First name',
            validation: {
                required: true,
                minLength: 3,
            },
            validationMessages: {
                required: 'This is required',
                minLength: 'You must have a minimum of 3 characters',
            },
        },
        {
            name: 'LastName',
            element: 'input',
            type: 'text',
            label: 'Last name',
            validation: {
                required: true,
            },
            validationMessages: {
                required: 'This field is required',
            },
        },
        {
            name: 'Email',
            element: 'input',
            type: 'email',
            label: 'Email address',
            validation: {
                required: true,
                email: true,
            },
            validationMessages: {
                required: 'Your email is required',
                email: 'Please ensure your email is in the correct format',
            },
        },
        {
            name: 'conditionalField',
            element: 'input',
            type: 'text',
            label: 'This is a conditional field',
            conditional: {
                selectexample: 'first',
            },
        },
    ],
    de: {
        FirstName: {
            label: 'German for "first name"',
            validationMessages: {
                minLength: 'You must have a minimum of 3 characters',
            },
        },
        LastName: {
            validationMessages: {
                required: 'This field is required in German',
            },
        },
        Email: {
            validationMessages: {
                required: 'Your email is required (de)',
                email: 'Please ensure your email is in the correct format (de)',
            },
        },
        content: {
            heading: 'What are your details? (de)',
            successHeading: 'What happens next? (de)',
            successContent: 'Since you’ve just joined us, we’ll send you a couple of extra emails to introduce ourselves properly. (de)',
            submit: 'Subscribe (de)',
        },
    },
};

const globalMessaging = {
    en: {
        submit: 'submit',
        validation: {
            required: 'This field is required',
            email: 'Please enter a valid email',
            postcode: 'Please enter a valid postcode',
        },
        required: 'required',
    },
    de: {
        submit: 'submit (de)',
        validation: {
            required: 'This field is required (de)',
            email: 'Please enter a valid email (de)',
            postcode: 'Please enter a valid postcode',
        },
        required: 'required (de)',
    },
};

const factoryShallowMount = () => shallowMount(VsMarketoForm, {
    slots: {
        submitError: 'error text',
        invalid: 'invalid text',
        submitting: 'submitting text',
        submitted: 'submitted text',
    },
    propsData: {
        dataUrl: 'testUrl',
        marketoInstance: '123',
        munchkinId: '123',
        messagingUrl: 'test',
        recaptchaKey: 'xyz',
        formId: '123',
    },
    data() {
        return {
            formData,
            messagingData: globalMessaging,
        };
    },
});

beforeEach(() => {
    window.MktoForms2 = {
        loadForm: jest.fn(),
    };
});

describe('VsMarketoForm', () => {
    it('should render a component with the data-test attribute `vs-form`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.find('[data-test="vs-form"]').exists()).toBe(true);
    });

    it('should render a header from the data supplied', () => {
        const wrapper = factoryShallowMount();
        const conditionalField = wrapper.find('vsheading-stub');

        expect(conditionalField.exists()).toBe(true);
    });

    it('should render three input fields', async() => {
        const wrapper = factoryShallowMount();
        await wrapper.vm.$nextTick();

        const allInputs = wrapper.findAll('bformgroup-stub');

        expect(allInputs.length).toBe(4);
    });

    it('should render a submit element with a value of `submit` from the data', async() => {
        const wrapper = factoryShallowMount();

        const submitBtn = wrapper.find('vsbutton-stub[type="submit"]');

        await wrapper.vm.$nextTick();
        expect(submitBtn.text()).toBe('Subscribe');
    });

    it('should not render a conditional field by default', () => {
        const wrapper = factoryShallowMount();
        const conditionalField = wrapper.find('vsinput-stub [fieldname="conditionalField"]');

        expect(conditionalField.exists()).toBe(false);
    });

    describe(':slots', () => {
        it('should render the `submitting` slot', async() => {
            const wrapper = factoryShallowMount();
            wrapper.vm.submitting = true;

            await wrapper.vm.$nextTick();

            expect(wrapper.html()).toContain('submitting text');
        });

        it('should render the `submitted` test from data when the data attribute is true', async() => {
            const wrapper = factoryShallowMount();
            wrapper.vm.submitted = true;

            await wrapper.vm.$nextTick();

            expect(wrapper.html()).toContain('submitted text');
        });

        it('should render the `submitError` slot', async() => {
            const wrapper = factoryShallowMount();
            wrapper.vm.submitError = true;

            await wrapper.vm.$nextTick();

            expect(wrapper.html()).toContain('error text');
        });
    });

    describe(':methods', () => {
        it('should mark the form as invalid if input data with an error is supplied', async() => {
            const wrapper = factoryShallowMount();

            const fieldData = {
                field: 'firstName',
                errors: true,
            };

            wrapper.vm.updateFieldData(fieldData);

            await wrapper.vm.$nextTick();

            expect(wrapper.vm.formIsInvalid).toBeTruthy();
        });

        it('should push a field`s name into the errorFields array when it has errors in its data', async() => {
            const wrapper = factoryShallowMount();

            wrapper.vm.manageErrorStatus('FirstName', ['required']);

            await wrapper.vm.$nextTick();

            expect(wrapper.vm.errorFields.indexOf('FirstName')).toBe(0);
        });

        it('should show translated labels if they exist', async() => {
            const wrapper = factoryShallowMount();

            wrapper.setProps({
                language: 'de',
            });
            await wrapper.vm.$nextTick();

            const allInputs = wrapper.findAll('bformgroup-stub');
            const firstNameLabel = allInputs.at(0);
            expect(firstNameLabel.attributes('label')).toContain('German for "first name"');
        });

        it('should show the default label if no translation exists', async() => {
            const wrapper = factoryShallowMount();

            wrapper.setProps({
                language: 'de',
            });
            await wrapper.vm.$nextTick();

            const allInputs = wrapper.findAll('bformgroup-stub');
            const lastNameLabel = allInputs.at(1);

            expect(lastNameLabel.attributes('label')).toContain('Last name');
        });

        it('should show the translated submit message', async() => {
            const wrapper = factoryShallowMount();

            wrapper.setProps({
                language: 'de',
            });
            await wrapper.vm.$nextTick();

            const submitEl = wrapper.find('vsbutton-stub[type="submit"]');
            expect(submitEl.text()).toBe('Subscribe (de)');
        });
    });
});
