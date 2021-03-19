import { shallowMount } from '@vue/test-utils';
import VsMegaNavAccordionItem from '../MegaNavAccordionItem';

const factoryShallowMount = () => shallowMount(VsMegaNavAccordionItem, {
    propsData: {
        controlId: 'accordion-item',
        title: 'Test Title',
        level: '1',
    },
    slots: {
        default: '<div class="accordion-item"></div>',
    },
});

describe('VsMegaNavAccordionItem', () => {
    it('should render a component with the data-test attribute `vs-mega-nav-accordion-item`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-mega-nav-accordion-item');
    });

    describe(':props', () => {
        it('returns a unique ID from the title', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.attributes('data-unique-id')).toBe('vs-mega-nav-test-title');
        });
    });

    describe(':slots', () => {
        it('renders a div element in the default slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.findAll('.accordion-item').length).toBe(1);
        });
    });
});
