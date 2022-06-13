import { shallowMount } from '@vue/test-utils';
import VsLinkListItem from '../components/LinkListItem';

const slotContent = 'Link text';

const factoryShallowMount = () => shallowMount(VsLinkListItem, {
    slots: {
        default: slotContent,
    },
});

describe('VsLinkListItem', () => {
    describe(':slots', () => {
        it(':default - should render the slot content', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain(slotContent);
        });
    });

    describe(':props', () => {
        it('should render `videoDescriptor` if set and a videoId is present', async() => {
            const videoDescriptor = 'Video';

            const wrapper = shallowMount(VsLinkListItem, {
                propsData: {
                    videoId: 'g-Fhvj7vW-E',
                    videoDescriptor,
                    type: 'video',
                    href: '#',
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

            await expect(wrapper.html()).toContain(videoDescriptor);
        });

        it('should render a properly formated duration if videoId is present', async() => {
            const videoDescriptor = 'Video';

            const wrapper = shallowMount(VsLinkListItem, {
                propsData: {
                    videoId: 'g-Fhvj7vW-E',
                    videoDescriptor,
                    type: 'video',
                    href: '#',
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

            await expect(wrapper.html()).toContain('1:13');
        });
    });
});
