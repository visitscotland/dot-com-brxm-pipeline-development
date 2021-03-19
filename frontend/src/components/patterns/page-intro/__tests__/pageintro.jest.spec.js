import { shallowMount } from '@vue/test-utils';
import VsPageIntro from '../PageIntro';

const factoryShallowMount = (slotData) => shallowMount(VsPageIntro, {
    propsData: {
        background: 'dark',
    },
    ...slotData,
});

describe('VsPageIntro', () => {
    describe(':props', () => {
        const wrapper = factoryShallowMount();
        it('should render the correct class for the supplied background prop', () => {
            expect(wrapper.find('[data-test="vs-page-intro"]').classes()).toContain('vs-page-intro--dark');
        });
    });

    describe(':slots', () => {
        it('should only render the lower content if the slot is defined', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-page-intro__lower"]').exists()).toBe(false);
        });

        it('should render upper slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    upper: '<div>This is the upper slot content</div>',
                },
            });
            expect(wrapper.find('[data-test="vs-page-intro__upper"]').html()).toContain('<div>This is the upper slot content</div>');
        });

        it('should render lower slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    lower: '<div>This is the lower slot content</div>',
                },
            });
            expect(wrapper.find('[data-test="vs-page-intro__lower"]').html()).toContain('<div>This is the lower slot content</div>');
        });
    });
});
