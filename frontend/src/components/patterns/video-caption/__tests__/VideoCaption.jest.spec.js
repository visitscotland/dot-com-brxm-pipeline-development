import { shallowMount, createWrapper } from '@vue/test-utils';
import VsVideoCaption from '../VideoCaption';

jest.mock('../../../../stores/video.store.js');

const videoTitleSlot = 'Video title';
const videoDurationSlot = '3 minute video';
const alertMsgSlot = 'This is a no-js alert';
const buttonText = 'Button text';

const factoryShallowMount = (propsData) => shallowMount(VsVideoCaption, {
    slots: {
        'video-title': videoTitleSlot,
        'video-duration': videoDurationSlot,
        'video-alert': alertMsgSlot,
    },
    propsData: {
        videoBtnText: buttonText,
        withToggleBtn: true,
        videoId: '123456',
        ...propsData,
    },
    data() {
        return {
            videoExists: true,
        };
    },
    computed: {
        videoLoaded: {
            get() {
                return this.videoExists;
            },
            set(val) {
                this.videoExists = val;
            },
        },
    },
});

describe('VsVideoCaption', () => {
    it('should render a component with the data-test attribute `video-caption`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('video-caption');
    });

    describe(':slots', () => {
        it('should render the video title slot content', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.html()).toContain(videoTitleSlot);
        });

        it('should render the video alert message slot content', async() => {
            const wrapper = factoryShallowMount();
            wrapper.vm.videoLoaded = false;
            await wrapper.vm.$nextTick();
            const nojsMsg = wrapper.find('[data-test="video-caption-nojs"]');

            expect(nojsMsg.html()).toContain(alertMsgSlot);
        });
    });

    describe(':computed', () => {
        it('should get the video details', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.vm.videoDetails.videoId).toBe('123456');
        });

        it('renders the video duration', () => {
            const wrapper = factoryShallowMount();
            const durationText = wrapper.find('.vs-video-caption__duration');

            expect(durationText.text()).toBe('1 minute video');
        });

        it('should show the content if a video has been loaded', () => {
            const videoData = {
                videoId: '123456',
                videoDurationMsg: '1 minute video',
                videoDuration: 55,
            };

            const wrapper = factoryShallowMount({
                computed: {
                    videoDetails() {
                        return videoData;
                    },
                },
            });

            expect(wrapper.find('.vs-video-caption').exists()).toBe(true);
        });

        it('should not show the content if a video has not been loaded', async() => {
            const wrapper = factoryShallowMount();

            wrapper.vm.videoLoaded = false;

            await wrapper.vm.$nextTick();

            expect(wrapper.find('.vs-video-caption__title').exists()).toBe(false);
        });
    });

    describe(':props', () => {
        it('should populate button text with the `videoBtnText` prop', () => {
            const wrapper = factoryShallowMount();
            const playButton = wrapper.find('.vs-video-caption__button');

            expect(playButton.text()).toBe(buttonText);
        });

        it('should include a toggle button if `withToggleBtn` prop is true', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.find('vstogglebutton-stub').exists()).toBe(true);
        });

        it('should not include a toggle button if `withToggleBtn` prop is false', () => {
            const wrapper = factoryShallowMount();
            wrapper.setProps({
                withToggleBtn: false,
            });

            expect(wrapper.find('vstogglebutton-stub').exists()).toBe(true);
        });
    });

    describe(':methods', () => {
        it('emits `toggleAction` when emitToggle method is called', async() => {
            const wrapper = factoryShallowMount();
            wrapper.vm.emitToggle();
            await wrapper.vm.$nextTick();

            expect(wrapper.emitted().toggleAction).toBeTruthy();
        });

        it('emits `bv::show::modal` when emitShowModal method is called', async() => {
            const wrapper = factoryShallowMount();
            const rootWrapper = createWrapper(wrapper.vm.$root);
            wrapper.vm.emitShowModal();
            await wrapper.vm.$nextTick();
            expect(rootWrapper.emitted('bv::show::modal')).toBeTruthy();
        });
    });
});
