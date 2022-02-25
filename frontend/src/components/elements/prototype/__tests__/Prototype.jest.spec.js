import { shallowMount } from '@vue/test-utils';

import VsPrototype from '../Prototype';

const slotText = 'Prototype content';

const factoryShallowMount = () => shallowMount(VsPrototype, {
    slots: {
        default: slotText,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsPrototype', () => {
    it('should render a div with the class `vs-prototype`', () => {
        wrapper = factoryShallowMount();

        expect(wrapper.element.tagName).toBe('DIV');
        expect(wrapper.classes('vs-prototype')).toBe(true);
    });

    describe(':slots', () => {
        it('renders content inserted into default `slot`', () => {
            wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain(slotText);
        });
    });
});
