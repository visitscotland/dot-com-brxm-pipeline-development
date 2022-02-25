import { shallowMount } from '@vue/test-utils';
import VsPageIntro from '../PageIntro';

const factoryShallowMount = (slotData) => shallowMount(VsPageIntro, {
    propsData: {
        background: 'dark',
        simpleIntro: false,
    },
    ...slotData,
});

describe('VsPageIntro', () => {
    describe(':props', () => {
        const wrapper = factoryShallowMount();
        it('should render the correct class for the supplied background prop', () => {
            expect(wrapper.find('[data-test="vs-page-intro"]').classes()).toContain('vs-page-intro--dark');
        });

        it('should render the correct class for the simpleIntro prop', async() => {
            await wrapper.setProps({
                heroIntro: true,
            });
            expect(wrapper.find('[data-test="vs-page-intro"]').classes()).toContain('vs-page-intro--hero');
        });
    });

    describe(':slots', () => {
        it('should render hero slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    vsIntroHero: '<div>This is the hero slot content</div>',
                },
            });

            expect(wrapper.find('[data-test="vs-page-intro"]').html()).toContain('<div>This is the hero slot content</div>');
        });

        it('should render breadcrumb slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    vsIntroBreadcrumb: '<div>This is the breadcrumb slot content</div>',
                },
            });
            expect(wrapper.html()).toContain('<div>This is the breadcrumb slot content</div>');
        });

        it('should render share button slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    vsShareButton: '<div>This is the share button slot content</div>',
                },
            });
            expect(wrapper.html()).toContain('<div>This is the share button slot content</div>');
        });

        it('should render title slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    vsIntroHeading: '<div>This is the title slot content</div>',
                },
            });
            expect(wrapper.html()).toContain('<div>This is the title slot content</div>');
        });

        it('should render introduction slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    vsIntroContent: '<div>This is the intro content slot content</div>',
                },
            });
            expect(wrapper.html()).toContain('<div>This is the intro content slot content</div>');
        });

        it('should render summary box slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    VsIntroSummaryBox: '<div>This is the summary box slot content</div>',
                },
            });
            expect(wrapper.html()).toContain('<div>This is the summary box slot content</div>');
        });

        it('should only render the lower content if the slot is defined', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-page-intro__lower"]').exists()).toBe(false);
        });

        it('should render lower slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    VsIntroLower: '<div>This is the lower slot content</div>',
                },
            });
            expect(wrapper.find('[data-test="vs-page-intro__lower"]').html()).toContain('<div>This is the lower slot content</div>');
        });
    });
});
