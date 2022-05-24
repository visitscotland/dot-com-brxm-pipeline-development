import { shallowMount } from '@vue/test-utils';

import VsRecaptcha from '../Recaptcha';

const factoryShallowMount = () => shallowMount(VsRecaptcha, {
    propsData: {
        siteKey: '123124',
        invalid: true,
        errorMsg: 'Please complete the recaptcha',
    },
});

describe('VsRecaptcha', () => {
    it('should render a div with the class `vs-recaptcha`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.classes('vs-recaptcha')).toBe(true);
    });

    describe(':props', () => {
        it('should render an invalid class if the invalid prop is true', () => {
            const wrapper = factoryShallowMount();
            const recaptchaStub = wrapper.find('vuerecaptcha-stub');

            expect(recaptchaStub.classes('vs-recaptcha__embed--error')).toBe(true);
        });

        it('should render a validation message invalid prop is true', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.html()).toContain('Please complete the recaptcha');
        });
    });

    describe(':methods', () => {
        it('should emit a `verified` event when response method is called', () => {
            const wrapper = factoryShallowMount();
            wrapper.vm.verified('response');

            expect(wrapper.emitted('verified')).toBeTruthy();
        });
    });
});
