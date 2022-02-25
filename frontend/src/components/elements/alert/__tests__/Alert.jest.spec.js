import { shallowMount } from '@vue/test-utils';

import VsAlert from '../Alert';

const defaultSlot = 'Default Slot';

const factoryShallowMount = (propsData) => shallowMount(VsAlert, {
    propsData: {
        ...propsData,
    },
});

describe('VsAlert', () => {
    it('should render a component with the `vs-alert` data-test attribute', () => {
        const wrapper = factoryShallowMount();

        const Alert = wrapper.find('div[data-test=vs-alert]');

        expect(Alert.exists()).toBe(true);
    });

    describe('slots:', () => {
        it('should render content inserted into `default` slot', () => {
            const wrapper = shallowMount(VsAlert, {
                slots: {
                    default: defaultSlot,
                },
            });

            expect(wrapper.text()).toContain(defaultSlot);
        });
    });
});
