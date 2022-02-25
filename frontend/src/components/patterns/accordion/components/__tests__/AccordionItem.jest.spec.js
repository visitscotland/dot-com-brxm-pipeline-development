import { shallowMount, mount } from '@vue/test-utils';

import VsAccordionItem from '../AccordionItem';

const titleSlot = 'Item Title';

const factoryShallowMount = (propsData) => shallowMount(VsAccordionItem, {
    propsData: {
        ...propsData,
        controlId: '1234',
    },
});

describe('VsAccordionItem', () => {
    it('should render accordionItem', () => {
        const wrapper = factoryShallowMount();

        const accordionItem = wrapper.find('[data-test=vs-accordion__item]');
        const accordionItemHeader = wrapper.find('[data-test=vs-accordion__item-header]');
        const accordionItemTitle = wrapper.find('[data-test=vs-accordion__item-title]');
        const accordionItemBody = wrapper.find('[data-test=vs-accordion__item-body]');

        expect(accordionItem.exists()).toBe(true);
        expect(accordionItemHeader.exists()).toBe(true);
        expect(accordionItemTitle.exists()).toBe(true);
        expect(accordionItemBody.exists()).toBe(true);
    });

    describe(':props', () => {
        it(':controlId should be assigned to panel id', () => {
            const wrapper = factoryShallowMount();

            const accordionItemBody = wrapper.find('[data-test=vs-accordion__item-body]');

            expect(accordionItemBody.attributes('id')).toBe('1234');
        });

        it(':itemBreakPoint assigns accordion class name', () => {
            const wrapper = factoryShallowMount({
                itemBreakPoint: 'sm',
            });

            const accordionToggle = wrapper.find('vsaccordiontoggle-stub');

            expect(accordionToggle.classes()).toContain('d-sm-none');
        });

        it(':openByDefault - to open or close accordion by default', () => {
            const wrapper = factoryShallowMount({
                openByDefault: false,
            });

            const accordionItemBody = wrapper.find('vsaccordiontoggle-stub');
            expect(accordionItemBody.classes()).toContain('d-lg-none');
        });

        it(':variant assigns variant to VsAccordionToggle', () => {
            const wrapper = factoryShallowMount({
                variant: 'secondary',
            });

            const accordionItemBody = wrapper.find('vsaccordiontoggle-stub');
            expect(accordionItemBody.attributes('variant')).toBe('secondary');
        });
    });

    describe(':methods', () => {
        it(':onButtonClick should toggle accordion', async() => {
            const wrapper = mount(VsAccordionItem, {
                propsData: {
                    openByDefault: true,
                    controlId: '1234',
                },
            });

            const accordionToggle = wrapper.find('.vs-accordion-toggle');
            expect(accordionToggle.attributes('aria-expanded')).toBe('true');

            await accordionToggle.trigger('click');

            expect(accordionToggle.attributes('aria-expanded')).toBe('false');
        });
    });

    describe('slots:', () => {
        it('should render content inserted into title slot', () => {
            const wrapper = shallowMount(VsAccordionItem, {
                propsData: {
                    controlId: '1234',
                },
                slots: {
                    title: titleSlot,
                },
            });

            expect(wrapper.text()).toContain(titleSlot);
        });
    });
});
