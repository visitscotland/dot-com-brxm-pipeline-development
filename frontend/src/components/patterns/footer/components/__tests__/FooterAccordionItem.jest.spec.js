import { shallowMount, mount } from '@vue/test-utils';

import VsFooterAccordionItem from '../FooterAccordionItem';

const slotContent = 'Default slot content';
const iconOpenSlotContent = 'An open icon';
const iconClosedSlotContent = 'A closed icon';

const factoryShallowMount = (propsData) => shallowMount(VsFooterAccordionItem, {
    propsData: {
        ...propsData,
        controlId: '123',
    },
    slots: {
        default: slotContent,
        'icon-open': iconOpenSlotContent,
        'icon-closed': iconClosedSlotContent,
    },
});

const factoryMount = (propsData) => mount(VsFooterAccordionItem, {
    propsData: {
        ...propsData,
        controlId: '123',
    },
    slots: {
        default: slotContent,
        'icon-open': iconOpenSlotContent,
        'icon-closed': iconClosedSlotContent,
    },
});

describe('VsFooterAccordionItem', () => {
    it('should render a component with the data-test attribute `vs-footer-accordion-item`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-footer-accordion-item');
    });

    describe(':props', () => {
        it('should accept and render a `title` property', async() => {
            const wrapper = factoryMount();

            await wrapper.setProps({
                title: 'Some title',
            });

            expect(wrapper.text()).toContain('Some title');
        });

        // All other props are passed directly to the child and tested in AccordionItem
    });

    describe(':slots', () => {
        it('renders content inserted into the default slot', () => {
            const wrapper = factoryMount();

            expect(wrapper.text()).toContain(slotContent);
        });

        it('renders content inserted into the default `icon-open` slot when open', () => {
            const wrapper = factoryMount({
                openByDefault: true,
            });

            expect(wrapper.text()).toContain(iconOpenSlotContent);
        });

        it('renders content inserted into the default `icon-closed` slot when closed', () => {
            const wrapper = factoryMount({
                openByDefault: false,
            });

            expect(wrapper.text()).toContain(iconClosedSlotContent);
        });
    });
});
