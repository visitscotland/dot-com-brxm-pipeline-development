import { shallowMount } from '@vue/test-utils';

import VsCol from '../Col';

const slotText = 'Col text';

const factoryShallowMount = () => shallowMount(VsCol, {
    slots: {
        default: slotText,
    },
    attrs: {
        class: 'cols-6 sm-12',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsCol', () => {
    it('should render a bcol-stub', () => {
        expect(wrapper.element.tagName).toBe('BCOL-STUB');
    });

    describe(':attrs', () => {
        it('should accept and render attributes', () => {
            expect([
                wrapper.classes('cols-6'),
                wrapper.classes('sm-12'),
            ]).toEqual([
                true, true,
            ]);
        });
    });

    describe(':slots', () => {
        it('renders content inserted into default `slot`', () => {
            expect(wrapper.text()).toContain(slotText);
        });
    });
});
