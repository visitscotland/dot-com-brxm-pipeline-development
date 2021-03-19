import { shallowMount } from '@vue/test-utils';

import VsCloseButton from '../CloseButton';

const slotContent = 'SR Only Content';

const factoryShallowMount = (propsData) => shallowMount(VsCloseButton, {
    propsData: {
        ...propsData,
    },
    slots: {
        default: slotContent,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VSCloseButton', () => {
    it('should render a vsbutton-stub', () => {
        expect(wrapper.element.tagName).toBe('VSBUTTON-STUB');
    });

    it('should contain a vsicon-stub', () => {
        const icon = wrapper.find('vsicon-stub');

        expect(icon.exists()).toBe(true);
    });

    describe(':slots', () => {
        it('renders content inserted into the default `slot`', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
