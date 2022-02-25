import { shallowMount } from '@vue/test-utils';

import VsBreadcrumb from '../Breadcrumb';

const slotText = 'Breadcrumb text';

const factoryShallowMount = () => shallowMount(VsBreadcrumb, {
    slots: {
        default: slotText,
    },
    attrs: {
        class: 'test-class',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsBreadcrumb', () => {
    it('should render a nav with a b-breadcrumb-stub', () => {
        const breadcrumb = wrapper.find('bbreadcrumb-stub');

        expect(breadcrumb.exists()).toBe(true);
    });

    it('should bind given attributes to b-breadcrumb-stub', () => {
        const breadcrumb = wrapper.find('bbreadcrumb-stub');

        expect(breadcrumb.classes()).toContain('test-class');
    });

    describe(':slots', () => {
        it('renders content inserted into default `slot`', () => {
            expect(wrapper.text()).toContain(slotText);
        });
    });
});
