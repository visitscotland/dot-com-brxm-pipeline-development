import { shallowMount } from '@vue/test-utils';
import VsMegalinkLinkList from '../MegalinkLinkList';

const videoId = '12345';
const videoBtnText = 'Play Video';

const factoryShallowMount = () => shallowMount(VsMegalinkLinkList, {
    propsData: {
        featured: true,
        imgSrc: 'test',
        linkType: 'external',
        theme: 'dark',
        linkUrl: 'www.visitscotland.com',
        days: '3',
        daysLabel: 'days',
        transport: 'bus',
        transportName: 'Bus',
        videoId,
        videoBtnText,
    },
    slots: {
        vsLinkListHeading: 'Multi-image heading',
        vsLinkListContent: '<p>Multi-image content</p>',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsMegalinkLinkList', () => {
    describe(':props', () => {
        it('renders the correct theme class', () => {
            expect(wrapper.find('.vs-megalink-link-list--dark').exists()).toBe(true);
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
        it('renders content inserted in a vsLinkListHeading slot', () => {
            expect(wrapper.find('[data-test="megalink-link-list__title"]').text()).toBe('Multi-image heading');
        });

        it('renders content inserted in a vsLinkListContent slot', () => {
            expect(wrapper.find('[data-test="megalink-link-list__content"]').html()).toContain('<p>Multi-image content</p>');
        });
        it('renders card panels if days and transport are provided', () => {
            expect(wrapper.find('[data-test="vs-itinerary-panels"]').exists()).toBe(true);
        });
    });
});
