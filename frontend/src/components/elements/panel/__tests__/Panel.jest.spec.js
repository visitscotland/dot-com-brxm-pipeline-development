import { shallowMount, mount } from '@vue/test-utils';

import VsPanel from '../Panel';

const defaultSlotText = 'Panel text';
const titleSlotText = 'Panel title';

const factoryShallowMount = (propsData) => shallowMount(VsPanel, {
    slots: {
        default: defaultSlotText,
        'vs-panel-title': titleSlotText,
    },
    propsData: {
        ...propsData,
    },
});

const factoryMount = (propsData) => mount(VsPanel, {
    slots: {
        default: defaultSlotText,
        'vs-panel-title': titleSlotText,
    },
    propsData: {
        ...propsData,
    },
});

describe('VsPanel', () => {
    it('should render a bcard-stub', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.element.tagName).toBe('BCARD-STUB');
    });

    it('should render a bcardtext-stub', () => {
        const wrapper = factoryShallowMount();
        const cardTextStub = wrapper.find('bcardtext-stub');

        expect(cardTextStub.exists()).toBe(true);
    });

    describe(':slots', () => {
        it('renders content inserted into default `slot`', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain(defaultSlotText);
        });

        it('renders content inserted into vs-panel-title `slot` inside ', () => {
            const wrapper = factoryMount();

            expect(wrapper.text()).toContain(titleSlotText);
        });
    });
});
