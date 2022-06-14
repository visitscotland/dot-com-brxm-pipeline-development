import { shallowMount } from '@vue/test-utils';
import VsWarning from '../Warning';

const warningMessage = 'There is no javascript';

const factoryShallowMount = () => shallowMount(VsWarning, {
    propsData: {
        warningMessage,
    },
});

describe('VsWarning', () => {
    it('should render a component with the data-test attribute `vs-warning`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-warning');
    });

    describe(':props', () => {
        it(':variant - should accept and render variants as props', async() => {
            const wrapper = factoryShallowMount();
            const variant = 'small';

            await wrapper.setProps({
                variant,
            });

            expect(wrapper.classes()).toContain(`vs-warning--${variant}`);
        });

        it('should render the contents of the `warningMessage` prop', async() => {
            const wrapper = factoryShallowMount();

            await wrapper.setProps({
                warningMessage,
            });

            expect(wrapper.html()).toContain(warningMessage);
        });

        it('should render a link with the test attr `vs-warning__link` if `warningLink` provided', async() => {
            const wrapper = factoryShallowMount();

            const warningLink = {
                url: 'https://google.com',
                lable: 'Update settings here',
            };

            await wrapper.setProps({
                warningLink,
            });

            expect(wrapper.find('[data-test="vs-warning__link"]').exists()).toBe(true);
        });

        it('should render a link with the href provided in `warningLink`', async() => {
            const wrapper = factoryShallowMount();

            const warningLink = {
                url: 'https://google.com',
                label: 'Update settings here',
            };

            await wrapper.setProps({
                warningLink,
            });

            expect(wrapper.find('[data-test="vs-warning__link"]').props().href).toBe(warningLink.url);
        });

        it('should render a link with the content provided in `noCookiesLink`', async() => {
            const wrapper = factoryShallowMount();

            const warningLink = {
                url: 'https://google.com',
                label: 'Update settings here',
            };

            await wrapper.setProps({
                warningLink,
            });

            expect(wrapper.find('[data-test="vs-warning__link"]').html()).toContain(warningLink.label);
        });
    });
});
