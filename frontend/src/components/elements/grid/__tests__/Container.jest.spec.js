import { shallowMount } from '@vue/test-utils';

import VsContainer from '../Container';

const slotText = 'Container text';

const factoryShallowMount = () => shallowMount(VsContainer, {
    slots: {
        default: slotText,
    },
    attrs: {
        style: 'background: #ccc',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsContainer', () => {
    it('should render a bcontainer-stub', () => {
        expect(wrapper.element.tagName).toBe('BCONTAINER-STUB');
    });

    describe(':attrs', () => {
        it('should accept and render attributes', () => {
            expect(wrapper.attributes('style')).toBe('background: #ccc');
        });
    });

    describe(':slots', () => {
        it('renders content inserted into default `slot`', () => {
            expect(wrapper.text()).toContain(slotText);
        });
    });
});
