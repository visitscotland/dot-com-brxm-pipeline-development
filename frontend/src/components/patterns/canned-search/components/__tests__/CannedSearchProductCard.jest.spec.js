import { mount } from '@vue/test-utils';
import VsCannedSearchProductCard from '../CannedSearchProductCard';

const imgSrc = 'https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm';
const title = 'Arden House';
const description = 'Test product description';
const detailLink = {
    link: '/info/accommodation/arden-house-p572561',
    label: 'View Details',
    type: 'internal',
};
const websiteLink = {
    link: 'http://www.ardenhouse.org.uk',
    label: 'Visit website',
    type: 'external',
};

const slideIndex = '0';

const subHeadingContent = 'This is a sub heading';
const starSlotContent = 'This is a star rating';
const catSlotContent = 'This is a list of categories';
const logoSlotContent = 'This is a list of logos';
const summarySlotContent = 'This is a summary';
const badgeSlotContent = 'Tese are some badges';

const factoryMount = (propsData) => mount(VsCannedSearchProductCard, {
    propsData: {
        imgSrc,
        title,
        description,
        detailLink,
        websiteLink,
        slideIndex,
        ...propsData,
    },
    slots: {
        vsCannedSearchSubHeading: subHeadingContent,
        vsCannedSearchStarRating: starSlotContent,
        vsCannedSearchCategories: catSlotContent,
        vsCannedSearchLogos: logoSlotContent,
        vsCannedSearchBadges: badgeSlotContent,
        vsCannedSearchSummary: summarySlotContent,
    },
    provide: () => ({
        slideCols: {
            xs: '12',
            sm: '6',
            md: '4',
            lg: '3',
        },
        visibleSlides: [4, 5, 6],
    }),
});

let wrapper;
beforeEach(() => {
    wrapper = factoryMount();
});

describe('VsCannedSearchProductCard', () => {
    describe(':props', () => {
        it('should render a component with the data-test attribute `vs-product-card`', () => {
            expect(wrapper.find('[data-test="vs-product-card"]').exists()).toBe(true);
        });

        describe(':img', () => {
            it('should render a `vs-img` with the `src` provided in `imgSrc`', () => {
                const mainProdImg = wrapper.find('[data-test="vs-product-card__img"]');

                expect(mainProdImg.props().src).toBe(imgSrc);
            });
        });

        it('should render the content of the `title` property as a vs-heading', () => {
            const prodTitle = wrapper.find('[data-test="vs-product-card__title"]');

            expect(prodTitle.html()).toContain(title);
        });

        describe(':link', () => {
            it('should render a `vs-link` with the `href` provided in `detailLink.link`', () => {
                const prodLink = wrapper.find('[data-test="vs-product-card__link"]');

                expect(prodLink.props().href).toBe(detailLink.link);
            });

            it('should render a `vs-link` with the `default` type', () => {
                const prodLink = wrapper.find('[data-test="vs-product-card__link"]');

                expect(prodLink.props().type).toBe('default');
            });

            it('should render the content of the `detailLink.label` property', () => {
                expect(wrapper.html()).toContain(detailLink.label);
            });
        });

        it('should render the content of the `description` property', () => {
            expect(wrapper.html()).toContain(description);
        });
    });

    describe(':slots', () => {
        it('should render the content of the `vsCannedSearchSubHeading` slot', () => {
            expect(wrapper.html()).toContain(subHeadingContent);
        });

        it('should render the content of the `vsCannedSearchStarRating` slot', () => {
            expect(wrapper.html()).toContain(starSlotContent);
        });

        it('should render the content of the `vsCannedSearchCategories` slot', () => {
            expect(wrapper.html()).toContain(catSlotContent);
        });

        it('should render the content of the `vsCannedSearchLogos` slot', () => {
            expect(wrapper.html()).toContain(logoSlotContent);
        });

        it('should render the content of the `vsCannedSearchBadges` slot', () => {
            expect(wrapper.html()).toContain(badgeSlotContent);
        });

        it('should render the content of the `vsCannedSearchSummary` slot', () => {
            expect(wrapper.html()).toContain(summarySlotContent);
        });
    });
});
