import { shallowMount } from '@vue/test-utils';

import VsHeading from '../Heading';

const defaultSlot = 'Default Slot';
const subHeadingSlot = 'Sub Heading Slot';

const factoryShallowMount = (propsData) => shallowMount(VsHeading, {
    propsData: {
        ...propsData,
    },
});

describe('VsHeading', () => {
    it('should render a h1 by default', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.element.tagName).toBe('H1');
        expect(wrapper.classes('vs-heading')).toBe(true);
    });

    describe(':props', () => {
        it(':level - should accept and render a `level` prop', () => {
            const wrapper = factoryShallowMount({
                level: 2,
            });

            expect(wrapper.element.tagName).toBe('H2');
        });

        it(':thin - should accept and render a `thin` prop', () => {
            const wrapper = factoryShallowMount({
                thin: true,
            });

            expect(wrapper.classes('vs-heading--thin')).toBe(true);
        });

        it(':thin - should *NOT* render a `vs-heading--thin` class if `thin` is set to false', () => {
            const wrapper = factoryShallowMount({
                thin: false,
            });

            expect(wrapper.classes('vs-heading--thin')).toBe(false);
        });

        it(':overrideStyleLevel - should render correct override class', () => {
            const wrapper = factoryShallowMount({
                overrideStyleLevel: 2,
            });

            expect(wrapper.classes('vs-heading--style-level-2')).toBe(true);
        });
    });

    describe('slots:', () => {
        it('should render content inserted into `default` slot', () => {
            const modifiedWrapper = shallowMount(VsHeading, {
                slots: {
                    default: defaultSlot,
                },
            });

            expect(modifiedWrapper.text()).toContain(defaultSlot);
        });

        it('should render content inserted into `sub-heading` slot', () => {
            const modifiedWrapper = shallowMount(VsHeading, {
                slots: {
                    'sub-heading': subHeadingSlot,
                },
            });

            expect(modifiedWrapper.text()).toContain(subHeadingSlot);
        });

        it('should *NOT* render a div with the class `heading__sub-heading` if sub-heading slot is not provided', () => {
            const wrapper = factoryShallowMount({
                level: 2,
            });
            const subHeading = wrapper.find('.heading__sub-heading');

            expect(subHeading.exists()).toBe(false);
        });
    });
});
