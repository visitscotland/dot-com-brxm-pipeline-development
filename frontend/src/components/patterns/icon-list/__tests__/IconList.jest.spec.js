import { shallowMount } from '@vue/test-utils';

import VsIconList from '../IconList';

const slotContent = 'Slot Content';

const factoryShallowMount = (propsData) => shallowMount(VsIconList, {
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

describe('VsIconList', () => {
    it('should render a component with the data-test attribute `vs-icon-list`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-icon-list');
    });

    describe(':props', () => {
        it('should accept and render a `title` property', async() => {
            await wrapper.setProps({
                title: 'Some title',
            });
            expect(wrapper.text()).toContain('Some title');
        });
    });

    describe(':slots', () => {
        it('renders content inserted into the default `slot`', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
