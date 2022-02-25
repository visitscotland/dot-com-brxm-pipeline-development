import { shallowMount } from '@vue/test-utils';

import VsAccordion from '../Accordion';

const defaultSlot = 'Default Slot';

const factoryShallowMount = (propsData) => shallowMount(VsAccordion, {
    propsData: {
        ...propsData,
    },
});

describe('VsAccordion', () => {
    it('should render accordion', () => {
        const wrapper = factoryShallowMount();

        const Accordion = wrapper.find('div[data-test=vs-accordion]');

        expect(Accordion.exists()).toBe(true);
    });

    describe('slots:', () => {
        it('should render content inserted into `default` slot', () => {
            const wrapper = shallowMount(VsAccordion, {
                slots: {
                    default: defaultSlot,
                },
            });

            expect(wrapper.text()).toContain(defaultSlot);
        });
    });
});
