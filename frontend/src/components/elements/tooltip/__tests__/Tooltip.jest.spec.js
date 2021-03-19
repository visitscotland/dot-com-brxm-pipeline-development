import { shallowMount } from '@vue/test-utils';

import VsTooltip from '../Tooltip';

const slotText = 'Tooltip content';

const factoryShallowMount = (propsData) => shallowMount(VsTooltip, {
    slots: {
        default: slotText,
    },
    propsData: {
        ...propsData,
    },
});

describe('VsPrototype', () => {
    it('should render a span with the class `vs-tooltip`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.element.tagName).toBe('SPAN');
        expect(wrapper.classes('vs-tooltip')).toBe(true);
    });

    describe(':props', () => {
        it(':title - should accept and render an `title` property', () => {
            const testTitle = 'A Test Title';
            const wrapper = factoryShallowMount({
                title: testTitle,
            });

            expect(wrapper.attributes('title')).toBe(testTitle);
        });
    });

    describe(':slots', () => {
        it('renders content inserted into default `slot`', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain(slotText);
        });
    });
});
