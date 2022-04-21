import { shallowMount } from '@vue/test-utils';

import VsRichTextWrapper from '../RichTextWrapper';

const slotText = 'Rich text wrapper content';

const factoryShallowMount = (propsData) => shallowMount(VsRichTextWrapper, {
    slots: {
        default: slotText,
    },
    propsData: {
        ...propsData,
    },
});

describe('VsRichTextWrapper', () => {
    it('should render a div with the class `vs-rich-text-wrapper`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.element.tagName).toBe('DIV');
        expect(wrapper.classes('vs-rich-text-wrapper')).toBe(true);
    });

    describe(':props', () => {
        it(':variant - should accept and render a `variant` prop', () => {
            const testVariant = 'lead';
            const wrapper = factoryShallowMount({
                variant: testVariant,
            });

            expect(wrapper.classes('vs-rich-text-wrapper--lead')).toBe(true);
        });
    });

    describe(':slots', () => {
        it('renders content inserted into default `slot`', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain(slotText);
        });
    });
});
