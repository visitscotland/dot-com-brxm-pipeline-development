import { shallowMount } from '@vue/test-utils';

import VsContentSection from '../ContentSection';

const slotContent = 'Content Section Content';

const factoryShallowMount = (propsData) => shallowMount(VsContentSection, {
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

describe('VsContentSection', () => {
    it('should render a component with the data-test attribute `vs-content-section`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-content-section');
    });

    describe(':props', () => {
        it('should accept and render a `heading` property', async() => {
            await wrapper.setProps({
                heading: 'test-heading',
            });

            expect(wrapper.text()).toContain('test-heading');
        });

        it('should have the `lead` class if the `lead` property is true', async() => {
            await wrapper.setProps({
                lead: true,
            });

            expect(wrapper.classes('lead')).toBe(true);
        });

        it('should not have the `lead` class if the `lead` property is false', async() => {
            await wrapper.setProps({
                lead: false,
            });

            expect(wrapper.classes('lead')).toBe(false);
        });

        it('should have the class `content-section-front` if the `tag` property is set to `front`', async() => {
            await wrapper.setProps({
                tag: 'front',
            });

            expect(wrapper.classes('content-section-front')).toBe(true);
        });
    });

    describe(':slots', () => {
        it('renders content inserted into the default `slot`', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
