import { shallowMount, mount } from '@vue/test-utils';

import VsAccordionToggle from '../AccordionToggle';

const iconOpenSlot = 'Icon Open';
const iconClosedSlot = 'Icon Closed';
const defaultSlot = 'Default Slot';

const factoryShallowMount = (propsData) => shallowMount(VsAccordionToggle, {
    propsData: {
        ...propsData,
    },
});

describe('VsAccordionToggle', () => {
    it('should render accordionToggle', () => {
        const wrapper = factoryShallowMount();

        const AccordionToggle = wrapper.find('vsbutton-stub.vs-accordion-toggle');

        expect(AccordionToggle.exists()).toBe(true);
    });

    describe(':props', () => {
        it(':visible - show accordion open or closed by default', () => {
            const wrapper = factoryShallowMount({
                visible: false,
            });

            const AccordionToggle = wrapper.find('vsbutton-stub.vs-accordion-toggle');

            expect(AccordionToggle.attributes('aria-expanded')).toBe('false');
        });

        it(':variant - variant for which button to show in headers', () => {
            const wrapper = factoryShallowMount({
                variant: 'secondary',
            });

            const AccordionToggle = wrapper.find('vsbutton-stub.vs-accordion-toggle');

            expect(AccordionToggle.attributes('variant')).toBe('secondary');
        });
    });

    describe('slots:', () => {
        it('should render content inserted into default slot', () => {
            const wrapper = shallowMount(VsAccordionToggle, {
                slots: {
                    default: defaultSlot,
                },
            });

            expect(wrapper.text()).toContain(defaultSlot);
        });

        it('should render content inserted into icon open slot', () => {
            const wrapper = shallowMount(VsAccordionToggle, {
                slots: {
                    'icon-open': iconOpenSlot,
                },
            });

            expect(wrapper.text()).toContain(iconOpenSlot);
        });

        it('should render content inserted into icon closed slot', () => {
            const wrapper = shallowMount(VsAccordionToggle, {
                slots: {
                    'icon-closed': iconClosedSlot,
                },
                propsData: {
                    visible: false,
                },
            });

            expect(wrapper.text()).toContain(iconClosedSlot);
        });
    });

    describe(':methods', () => {
        it(':triggerToggle should toggle accordion', async() => {
            const wrapper = mount(VsAccordionToggle);

            const accordionToggle = wrapper.find('button.vs-accordion-toggle');

            await accordionToggle.trigger('click');

            expect(wrapper.emitted('toggle-panel')).toBeTruthy();
        });
    });
});
