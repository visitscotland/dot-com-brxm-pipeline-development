import { shallowMount } from '@vue/test-utils';
import VsWarning from '../Warning';

const defaultSlotText = 'There is no javascript';

const factoryShallowMount = (propsData, slots) => shallowMount(VsWarning, {
    propsData: {
        ...propsData,
    },
    slots: {
        default: defaultSlotText,
        ...slots,
    },
});

describe('VsWarning', () => {
    it('should render a component with the data-test attribute `vs-warning`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-warning');
    });

    describe(':props', () => {
        it(':icon - should render an `review` icon by default', () => {
            const wrapper = factoryShallowMount();
            const iconStub = wrapper.find('vsicon-stub');

            expect(iconStub.attributes('name')).toBe('review');
        });

        it(':icon - should render an icon with the same name as the `icon` prop', () => {
            const wrapper = factoryShallowMount({
                icon: 'test',
            });
            const iconStub = wrapper.find('vsicon-stub');

            expect(iconStub.attributes('name')).toBe('test');
        });

        it(':theme - should render a class matching the `theme` prop', () => {
            const wrapper = factoryShallowMount({
                theme: 'dark',
            });

            expect(wrapper.classes()).toContain('vs-warning--dark');
        });

        it(':size - should render a class matching the `size` prop', () => {
            const wrapper = factoryShallowMount({
                size: 'small',
            });

            expect(wrapper.classes()).toContain('vs-warning--small');
        });

        it(':transparent - should render a class if the `transparent` prop is true', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.classes()).toContain('vs-warning--transparent');
        });

        it(':transparent - should not render a class if the `transparent` prop is false', () => {
            const wrapper = factoryShallowMount({
                transparent: false,
            });

            expect(wrapper.classes('vs-warning--transparent')).toBe(false);
        });

        it(':align - should render a class matching the `align` prop', () => {
            const wrapper = factoryShallowMount({
                align: 'right',
            });

            expect(wrapper.classes()).toContain('vs-warning--right');
        });

        it(':type - should show a cookie manangement button if `type` is `cookie` and the slot is populated', () => {
            const wrapper = factoryShallowMount(
                {
                    type: 'cookie',
                },
                {
                    'button-text': 'Manage cookies',
                },
            );
            const cookieBtn = wrapper.find('vsbutton-stub');

            expect(cookieBtn.classes('vs-warning__cookie-trigger')).toBe(true);
        });
    });

    describe(':slots', () => {
        it('should display the content of the default slot', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain(defaultSlotText);
        });

        it('should display the content of the `button-text` slot', () => {
            const btnText = 'Manage cookies';
            const wrapper = factoryShallowMount(
                {
                },
                {
                    'button-text': btnText,
                },
            );

            expect(wrapper.text()).toContain(btnText);
        });
    });
});
