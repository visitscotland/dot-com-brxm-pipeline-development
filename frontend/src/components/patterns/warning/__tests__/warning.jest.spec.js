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
    });
});
