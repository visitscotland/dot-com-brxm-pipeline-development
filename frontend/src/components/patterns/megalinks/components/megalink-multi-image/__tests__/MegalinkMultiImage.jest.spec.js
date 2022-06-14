import { shallowMount } from '@vue/test-utils';
import VsMegalinkMultiImage from '../MegalinkMultiImage';

const videoId = '12345';
const videoBtnText = 'Play Video';

const factoryShallowMount = () => shallowMount(VsMegalinkMultiImage, {
    propsData: {
        featured: true,
        lastFeatured: true,
        imgSrc: 'test',
        linkType: 'external',
        linkUrl: 'www.visitscotland.com',
        theme: 'dark',
        days: '3',
        daysLabel: 'days',
        transport: 'bus',
        transportName: 'Bus',
        videoId,
        videoBtnText,
    },
    slots: {
        vsMultiImageHeading: 'Multi-image heading',
        vsMultiImageContent: '<p>Multi-image content</p>',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsMegalinkMultiImage', () => {
    describe(':props', () => {
        it('if the featured prop is true, it should render an element with class "megalink-multi-image--featured"', () => {
            expect(wrapper.find('[data-test="megalink-multi-image-featured"]').classes()).toContain('vs-megalink-multi-image--featured');
        });
        it('renders with a `megalink-multi-image--featured-last` class if the lastFeatured prop is true', () => {
            expect(wrapper.find('[data-test="megalink-multi-image-featured"]').classes()).toContain('vs-megalink-multi-image--featured-last');
        });
        it('renders the correct theme class', () => {
            expect(wrapper.find('[data-test="megalink-multi-image-featured"]').classes()).toContain('vs-megalink-multi-image--dark');
        });
        it('renders card panels if days and transport are provided', () => {
            expect(wrapper.find('[data-test="vs-itinerary-panels"]').exists()).toBe(true);
        });

        it('should accept a `videoId` property and pass it to the stretched-link-card', () => {
            const cardStub = wrapper.find('VsStretchedLinkCard-stub');
            expect(cardStub.attributes().videoid).toBe(videoId);
        });

        it('should accept a `videoBtnText` property and pass it to the stretched-link-card', () => {
            const cardStub = wrapper.find('VsStretchedLinkCard-stub');
            expect(cardStub.attributes().videobtntext).toBe(videoBtnText);
        });
    });

    describe(':slots', () => {
        it('renders content inserted in a vsMultiImageHeading slot', () => {
            expect(wrapper.find('[data-test="megalink-multi-image__title"]').text()).toBe('Multi-image heading');
        });

        it('renders content inserted in a vsMultiImageContent slot', () => {
            expect(wrapper.find('[data-test="megalink-multi-image__content"]').html()).toContain('<p>Multi-image content</p>');
        });
    });
});
