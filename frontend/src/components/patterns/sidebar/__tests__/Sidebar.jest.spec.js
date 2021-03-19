import { shallowMount } from '@vue/test-utils';

import VsSidebar from '../Sidebar';

const slotContent = 'Sidebar Content';

const factoryShallowMount = (propsData) => shallowMount(VsSidebar, {
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

describe('VsSidebar', () => {
    it('should render a component with the data-test attribute `vs-sidebar`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-sidebar');
    });

    describe(':props', () => {
        it('should accept and render a `tag` property', async() => {
            await wrapper.setProps({
                tag: 'test-tag',
            });

            expect(wrapper.attributes('tag')).toBe('test-tag');
        });
    });

    describe(':slots', () => {
        it('renders content inserted into the default `slot`', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
