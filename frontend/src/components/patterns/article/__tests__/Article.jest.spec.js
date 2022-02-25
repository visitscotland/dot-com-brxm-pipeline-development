import { shallowMount } from '@vue/test-utils';
import VsArticle from '../Article';

const defaultSlotText = 'Article content';
const titleSlotText = 'Article title';
const introSlotText = 'Article intro';
const imgSlotText = 'Article img';

const factoryShallowMount = (propsData) => shallowMount(VsArticle, {
    propsData: {
        ...propsData,
    },
    slots: {
        default: defaultSlotText,
        vsArticleImg: imgSlotText,
        vsArticleTitle: titleSlotText,
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

    describe(':slots', () => {
        it('renders content inserted in a vsArticleImg slot', () => {
            expect(wrapper.text()).toContain(imgSlotText);
        });

        it('renders content inserted in a vsArticleTitle slot', () => {
            expect(wrapper.text()).toContain(titleSlotText);
        });

        it('renders content inserted in a vsArticleIntro slot', () => {
            expect(wrapper.text()).toContain(introSlotText);
        });

        it('renders content inserted in the default slot', () => {
            expect(wrapper.text()).toContain(defaultSlotText);
        });
    });
});
