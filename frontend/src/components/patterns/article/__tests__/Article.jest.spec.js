import { shallowMount } from '@vue/test-utils';
import VsArticle from '../Article';

const defaultSlotText = 'Article content';
const introSlotText = 'Article intro';
const imgSlotText = 'Article img';

const factoryShallowMount = (propsData) => shallowMount(VsArticle, {
    propsData: {
        ...propsData,
        title: 'Route to the summit',
        anchorLink: 'Routes',
    },
    slots: {
        default: defaultSlotText,
        vsArticleImg: imgSlotText,
        vsArticleIntro: introSlotText,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsArticle', () => {
    it('should render a component with the data-test attribute `vs-article`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-article');
    });

    describe(':props', () => {
        it('renders `title` passed to the article', () => {
            expect(wrapper.text()).toContain('Route to the summit');
        });
        it('renders `anchorLink` passed to the article', () => {
            const header = wrapper.find('article[data-test=vs-article]').find('.vs-article__header');
            const anchor = header.find('#Routes');
            expect(anchor.exists()).toBe(true);
        });
    });

    describe(':slots', () => {
        it('renders content inserted in a vsArticleImg slot', () => {
            expect(wrapper.text()).toContain(imgSlotText);
        });

        it('renders content inserted in a vsArticleIntro slot', () => {
            expect(wrapper.text()).toContain(introSlotText);
        });

        it('renders content inserted in the default slot', () => {
            expect(wrapper.text()).toContain(defaultSlotText);
        });
    });
});
