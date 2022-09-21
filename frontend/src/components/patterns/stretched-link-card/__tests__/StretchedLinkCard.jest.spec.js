import { shallowMount } from '@vue/test-utils';
import VsStretchedLinkCard from '../StretchedLinkCard';

const imgUrl = 'https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm';

const factoryShallowMount = (slotsData) => shallowMount(VsStretchedLinkCard, {
    propsData: {
        link: 'https://www.visitscotland.com/',
        type: 'external',
        imgSrc: imgUrl,
        imgAlt: 'Image alt',
    },
    slots: {
        stretchedCardHeader: 'Stretched link header',
    },
    ...slotsData,
});

describe('VsStretchedLinkCard', () => {
    it('should render an element with class stretched-link', () => {
        const wrapper = factoryShallowMount({
            slots: {
                stretchedCardHeader: 'Header slot content',
            },
        });
        expect(wrapper.find('[data-test="vs-stretched-link"]').exists()).toBe(true);
    });

    describe(':props', () => {
        it('should render the component with the link prop passed as the stretched link href', async() => {
            const wrapper = factoryShallowMount({
                slots: {
                    stretchedCardHeader: 'Header slot content',
                },
            });

            expect(wrapper.find('[data-test="vs-stretched-link"]').attributes().href).toBe('https://www.visitscotland.com/');
        });

        it('should not render an image if no src attribute is set', async() => {
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

        it('should render a video play button if a videoId is set', async() => {
            const wrapper = shallowMount(VsStretchedLinkCard, {
                propsData: {
                    link: 'https://www.visitscotland.com/',
                    type: 'external',
                    imgSrc: imgUrl,
                    imgAlt: 'Image alt',
                    videoId: '12345',
                },
                computed: {
                    videoLoaded() {
                        return true;
                    },
                    videoDetails() {
                        return {
                            videoFullDuration: {
                                minutes: 1,
                                seconds: 13,
                            },
                        };
                    },
                },
            });

            wrapper.setData({
                requiredCookies: [],
            });
            await wrapper.vm.$nextTick();

            expect(wrapper.find('[data-test="vs-stretched-link-card__video-button"]').exists()).toBe(true);
        });

        it('should render `videoBtnText` if set and a videoId is present', async() => {
            const videoBtnText = 'PlayVideo';

            const wrapper = shallowMount(VsStretchedLinkCard, {
                propsData: {
                    link: 'https://www.visitscotland.com/',
                    type: 'external',
                    imgSrc: imgUrl,
                    imgAlt: 'Image alt',
                    videoId: '12345',
                    videoBtnText,
                },
                computed: {
                    videoLoaded() {
                        return true;
                    },
                    videoDetails() {
                        return {
                            videoFullDuration: {
                                minutes: 1,
                                seconds: 13,
                            },
                        };
                    },
                },
            });

            wrapper.setData({
                requiredCookies: [],
            });
            await wrapper.vm.$nextTick();

            expect(wrapper.html()).toContain(videoBtnText);
        });

        it('should render a full warning component if `errorType` is `full`', async() => {
            const wrapper = shallowMount(VsStretchedLinkCard, {
                propsData: {
                    link: 'https://www.visitscotland.com/',
                    type: 'external',
                    imgSrc: imgUrl,
                    imgAlt: 'Image alt',
                    videoId: '12345',
                    videoBtnText: 'Play',
                    errorType: 'full',
                },
                computed: {
                    showError() {
                        return true;
                    },
                },
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.find('[data-test="vs-stretched-link-card__full-warning"]').exists()).toBe(true);
        });

        it('should render a image warning component if `errorType` is `image`', async() => {
            const wrapper = shallowMount(VsStretchedLinkCard, {
                propsData: {
                    link: 'https://www.visitscotland.com/',
                    type: 'external',
                    imgSrc: imgUrl,
                    imgAlt: 'Image alt',
                    videoId: '12345',
                    videoBtnText: 'Play',
                    errorType: 'image',
                },
                computed: {
                    showError() {
                        return true;
                    },
                },
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.find('[data-test="vs-stretched-link-card__image-warning"]').exists()).toBe(true);
        });
    });

    describe(':slots', () => {
        it('renders content inserted in a stretchedCardCategory slot', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    stretchedCardCategory: 'Stretched link category',
                },
            });
            expect(wrapper.find('[data-test="vs-stretched-link-card__category"]').text()).toBe('Stretched link category');
        });
        it('renders content inserted in a stretchedCardHeader slot', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    stretchedCardHeader: 'Stretched link header',
                },
            });
            expect(wrapper.find('[data-test="vs-stretched-link"]').text()).toBe('Stretched link header');
        });
        it('renders content inserted in a stretchedCardContent slot', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    stretchedCardContent: 'Stretched link content',
                },
            });
            expect(wrapper.find('[data-test="vs-stretched-link-card__content"]').text()).toBe('Stretched link content');
        });
        it('should only render the link if stretchedCardLink slot content is supplied', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-stretched-link-card__link"]').exists()).toBe(false);
        });
        it('renders content inserted in a stretchedCardLink slot', async() => {
            const wrapper = factoryShallowMount({
                slots: {
                    stretchedCardLink: 'Stretched link link text',
                },
            });
            expect(wrapper.find('[data-test="vs-stretched-link-card__link"]').text()).toBe('Stretched link link text');
        });
        it('renders a link in the header if no stretchedCardLink is supplied', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    stretchedCardHeader: 'Stretched link header',
                },
            });

            expect(wrapper.find('[data-test="vs-stretched-link-card__title"]').find('[data-test="vs-stretched-link"]').exists()).toBe(true);
        });
        it('does not render a link in the header if a stretchedCardLink slot content is supplied', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    stretchedCardHeader: 'Stretched link header',
                    stretchedCardLink: 'Stretched link link text',
                },
            });
            expect(wrapper.find('[data-test="vs-stretched-link-card__title"]').exists()).toBe(false);
        });
    });
});
