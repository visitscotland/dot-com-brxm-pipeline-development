import { shallowMount } from '@vue/test-utils';
import VsStretchedLinkCard from '../StretchedLinkCard';

const imgUrl = 'https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm';

const factoryShallowMount = () => shallowMount(VsStretchedLinkCard, {
    propsData: {
        link: 'https://www.visitscotland.com/',
        type: 'external',
        imgSrc: imgUrl,
        imgAlt: 'Image alt',
    },
    slots: {
        stretchedCardCategory: 'Stretched link category',
        stretchedCardHeader: 'Stretched link header',
        stretchedCardContent: 'Stretched link content',
    },
});

describe('VsStretchedLinkCard', () => {
    it('should render an element with class stretched-link', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.find('[data-test="vs-stretched-link"]').exists()).toBe(true);
    });

    describe(':props', () => {
        it('should render the component with the link prop passed as the stretched link href', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-stretched-link"]').attributes().href).toBe('https://www.visitscotland.com/');
        });
        it('should not render an image if not src attribute is set', async() => {
            const wrapper = shallowMount(VsStretchedLinkCard, {
                propsData: {
                    link: 'https://www.visitscotland.com/',
                    type: 'external',
                    imgSrc: '',
                    imgAlt: 'Image alt',
                },
            });

            await expect(wrapper.find('[data-test="vs-stretched-link__img"]').exists()).toBe(false);
        });
    });

    describe(':slots', () => {
        it('renders content inserted in a stretchedCardCategory slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-stretched-link-card__category"]').text()).toBe('Stretched link category');
        });
        it('renders content inserted in a stretchedCardHeader slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-stretched-link"]').text()).toBe('Stretched link header');
        });
        it('renders content inserted in a stretchedCardContent slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-stretched-link-card__content"]').text()).toBe('Stretched link content');
        });
    });
});
