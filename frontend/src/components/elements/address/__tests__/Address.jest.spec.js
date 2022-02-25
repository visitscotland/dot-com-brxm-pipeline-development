import { shallowMount } from '@vue/test-utils';

import VsAddress from '../Address';

const defaultSlot = 'Default Slot';

const factoryShallowMount = (propsData) => shallowMount(VsAddress, {
    propsData: {
        ...propsData,
    },
});

describe('VsAddress', () => {
    it('should render address', () => {
        const wrapper = factoryShallowMount();

        const Address = wrapper.find('div[data-test=vs-address]');

        expect(Address.exists()).toBe(true);
    });

    describe('slots:', () => {
        it('should render content inserted into `default` slot', () => {
            const wrapper = shallowMount(VsAddress, {
                slots: {
                    default: defaultSlot,
                },
            });

            expect(wrapper.text()).toContain(defaultSlot);
        });
    });
});
