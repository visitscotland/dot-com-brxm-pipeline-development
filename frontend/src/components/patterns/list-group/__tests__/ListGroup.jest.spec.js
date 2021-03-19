import { shallowMount } from '@vue/test-utils';

import VsListGroup from '../ListGroup';

const slotContent = 'List Group Content';

const factoryShallowMount = (propsData) => shallowMount(VsListGroup, {
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

describe('VsListGroup', () => {
    it('should render a component with the data-test attribute `vs-list-group`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-list-group');
    });

    describe(':slots', () => {
        it('renders content inserted into the default `slot`', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
