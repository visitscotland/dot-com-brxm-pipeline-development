import { shallowMount } from '@vue/test-utils';
import VsArticleSidebar from '../ArticleSidebar';

const quoteSlotText = 'Article sidebar quote';
const imgSlotText = 'Article sidebar img';

const factoryShallowMount = (propsData) => shallowMount(VsArticleSidebar, {
    propsData: {
        ...propsData,
    },
    slots: {
        vsArticleSidebarQuote: quoteSlotText,
        vsArticleSidebarImg: imgSlotText,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsArticleSidebar', () => {
    it('should render a component with the data-test attribute `vs-article-sidebar`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-article-sidebar');
    });

    describe(':props', () => {
        it(':sidebarAlign - Should add the correct alignment class for `left` sidebar', () => {
            wrapper = factoryShallowMount({
                sidebarAlign: 'left',
            });

            const sidebar = wrapper.find('[data-test="vs-article-sidebar"]');
            expect(sidebar.classes()).toContain('vs-article-sidebar--left');
        });

        it(':sidebarAlign - Should add the correct alignment class for `right` sidebar', () => {
            wrapper = factoryShallowMount({
                sidebarAlign: 'right',
            });

            const sidebar = wrapper.find('[data-test="vs-article-sidebar"');
            expect(sidebar.classes()).toContain('vs-article-sidebar--right');
        });
    });

    describe(':computed', () => {
        it(':sidebarImgClasses - Image wrapper should have correct padding when no quote visible', () => {
            const modifiedWrapper = shallowMount(VsArticleSidebar, {
                slots: {
                    vsArticleSidebarQuote: '',
                    vsArticleSidebarImg: imgSlotText,
                },
            });

            const imgWrapper = modifiedWrapper.find('[data-test="vs-article-sidebar"]').find('.vs-article-sidebar__img-wrapper');
            expect(imgWrapper.classes()).toContain('pb-8');
        });

        it(':sidebarArticleClasses - Quote wrapper should have correct padding when no img visible', () => {
            const modifiedWrapper = shallowMount(VsArticleSidebar, {
                slots: {
                    vsArticleSidebarImg: '',
                    vsArticleSidebarQuote: quoteSlotText,
                },
            });

            const quoteWrapper = modifiedWrapper.find('[data-test="vs-article-sidebar"]').find('.vs-article-sidebar__quote-wrapper');
            expect(quoteWrapper.classes()).toContain('pt-0');
        });
    });

    describe(':slots', () => {
        it('renders content inserted in a `vsArticleSidebarQuote` slot', () => {
            expect(wrapper.text()).toContain(quoteSlotText);
        });

        it('renders content inserted in a `imgSlotText` slot', () => {
            expect(wrapper.text()).toContain(imgSlotText);
        });
    });
});
