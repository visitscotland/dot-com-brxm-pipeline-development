import { shallowMount } from '@vue/test-utils';

import VsTabs from '../Tabs';

const defaultSlot = 'Default Slot';
const factoryShallowMount = () => shallowMount(VsTabs);

describe('VsTabs', () => {
    it('should render a tabs wrapper', () => {
        const wrapper = factoryShallowMount();
        const Tabs = wrapper.find('div[data-test=vs-tabs]');

        expect(Tabs.exists()).toBe(true);
    });

    describe('slots:', () => {
        it('should render content inserted into `default` slot', () => {
            const wrapper = shallowMount(VsTabs, {
                slots: {
                    default: defaultSlot,
                },
            });

            expect(wrapper.text()).toContain(defaultSlot);
        });
    });
});
