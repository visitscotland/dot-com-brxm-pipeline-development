import { shallowMount } from '@vue/test-utils';

import VsTooltip from '../Tooltip';

const slotText = 'Tooltip content';

const factoryShallowMount = (propsData) => shallowMount(VsTooltip, {
    slots: {
        default: slotText,
    },
    propsData: {
        title: 'Bus',
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsTooltip', () => {
    it('should render a vsbutton-stub element', () => {
        const tooltip = wrapper.find('vsbutton-stub');

        expect(tooltip.exists()).toBe(true);
    });

    describe(':props', () => {
        it(':title - should accept and render a `title` property', () => {
            expect(wrapper.attributes('title')).toBe('Bus');
        });
    });
});
