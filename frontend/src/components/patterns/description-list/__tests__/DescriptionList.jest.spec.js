import { shallowMount } from '@vue/test-utils';

import VsDescriptionList from '../DescriptionList';

const TestComponent = {
    template: '<div>Test Component</div>',
};

const factoryShallowMount = (propsData) => shallowMount(VsDescriptionList, {
    slots: {
        default: TestComponent,
    },
    propsData: {
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsDescriptionList', () => {
    it('should render a vsrow-stub with a `description-list` class', () => {
        expect(wrapper.element.tagName).toBe('VSROW-STUB');
        expect(wrapper.classes('vs-description-list')).toBe(true);
    });

    describe(':props', () => {
        it(':inline - should accept an `inline` property', () => {
            wrapper = factoryShallowMount({
                inline: true,
            });

            expect(wrapper.props().inline).toBe(true);
        });

        it(':inline - should be false by default', () => {
            expect(wrapper.props().inline).toBe(false);
        });

        it(':inline - should provide an `inline` property down to children components', () => {
            wrapper = factoryShallowMount({
                inline: true,
            });

            /* eslint-disable no-underscore-dangle */
            expect(wrapper.vm._provided.inline).toBe(true);
        });
    });

    describe(':slots', () => {
        it('renders content inserted into default `slot`', () => {
            const modifiedWrapper = factoryShallowMount();

            expect(modifiedWrapper.text()).toContain('Test Component');
        });
    });
});
