import { shallowMount } from '@vue/test-utils';
import VsImageWithCaption from '../ImageWithCaption';

const defaultSlotText = 'Image';
const toggleIconSlot = 'Toggle icon';
const alertSlot = 'Video alert message';
const videoTitleSlot = 'Video title';
const videoDurationSlot = 'Video duration';
const captionSlot = 'Image caption';
const imageSrcValue = 'visitscotland';
const youtubeId = 'abc213';

const factoryShallowMount = (propsData) => shallowMount(VsImageWithCaption, {
    propsData: {
        imageSrc: imageSrcValue,
        isVideo: true,
        playButtonText: 'Play video',
        videoId: youtubeId,
        ...propsData,
    },
    slots: {
        'toggle-icon': toggleIconSlot,
        'img-caption': captionSlot,
        'video-alert': alertSlot,
        'video-title': videoTitleSlot,
        'video-duration': videoDurationSlot,
        default: defaultSlotText,
    },
});

describe('VsImageWithCaption', () => {
    it('should render a component with the data-test attribute `vs-image-with-caption`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-image-with-caption');
    });

    it('hides caption by default', async() => {
        const wrapper = factoryShallowMount();
        const captionWrapper = wrapper.find('[data-test="vs-image-with-caption"]').find('.vs-image-with-caption__caption-wrapper');

        expect(captionWrapper.classes('d-block')).toBe(false);
    });

    describe(':props', () => {
        it('should set correct class if `closedDefaultCaption` is true', () => {
            const wrapper = factoryShallowMount({
                closedDefaultCaption: true,
            });
            const imageWithCaption = wrapper.find('[data-test="vs-image-with-caption"]');

            expect(imageWithCaption.classes('vs-image-with-caption--closed-default')).toBe(true);
        });

        it('should set correct classes if `isHeroImage` is true', () => {
            const wrapper = factoryShallowMount({
                isHeroImage: true,
            });
            const imageWithCaption = wrapper.find('[data-test="vs-image-with-caption"]');
            const captionWrapper = wrapper.find('[data-test="vs-image-with-caption"]').find('.vs-image-with-caption__caption-wrapper');

            expect(imageWithCaption.classes('vs-image-with-caption--hero')).toBe(true);
            expect(captionWrapper.classes('container')).toBe(true);
        });

        it('should set correct classes if `mobileOverlap` is true', () => {
            const wrapper = factoryShallowMount({
                mobileOverlap: true,
            });
            const imageWrapper = wrapper.find('[data-test="vs-image-with-caption"]').find('.vs-image-with-caption__image-wrapper');

            expect(imageWrapper.classes('vs-image-with-caption--overlapped')).toBe(true);
        });

        it('should render correct `toggleButtonText` on the toggle button', () => {
            const wrapper = factoryShallowMount({
                toggleButtonText: 'Toggle caption',
            });
            const toggleCaptionBtn = wrapper.find('vstogglebutton-stub');

            expect(toggleCaptionBtn.text()).toContain('Toggle caption');
        });

        it('should set correct ID for aria controls with `imageSrc`', () => {
            const wrapper = factoryShallowMount();
            const captionWrapper = wrapper.find('[data-test="vs-image-with-caption"]').find('.vs-image-with-caption__caption-wrapper');

            expect(captionWrapper.attributes('id')).toBe(`image_${imageSrcValue}`);
        });

        it('should add a Video Caption component if `isVideo` is true', async() => {
            const wrapper = factoryShallowMount({
                propsData: {
                    isVideo: true,
                },
            });

            expect(wrapper.find('.vs-image-with-caption__video-caption-wrapper').exists()).toBe(true);
        });

        it('should pass the correct videoID via prop to the video caption component', () => {
            const wrapper = factoryShallowMount();
            const videoCaption = wrapper.find('vsvideocaption-stub');

            expect(videoCaption.html()).toContain(youtubeId);
        });
    });

    describe(':slots', () => {
        it('renders content in the `default` slot', () => {
            const wrapper = factoryShallowMount();
            const imageWrapper = wrapper.find('[data-test="vs-image-with-caption"]').find('.vs-image-with-caption__image-wrapper');

            expect(imageWrapper.text()).toContain(defaultSlotText);
        });

        it('renders content in the `imgCaption` slot', () => {
            const wrapper = factoryShallowMount();
            const captionWrapper = wrapper.find('[data-test="vs-image-with-caption"]').find('.vs-image-with-caption__caption-wrapper');

            expect(captionWrapper.text()).toContain(captionSlot);
        });

        it('renders content in the `video-alert` slot', () => {
            const wrapper = factoryShallowMount();
            const videoCaptionStub = wrapper.find('vsvideocaption-stub');

            expect(videoCaptionStub.text()).toContain(alertSlot);
        });

        it('renders content in the `video-title` slot', () => {
            const wrapper = factoryShallowMount();
            const videoCaptionStub = wrapper.find('vsvideocaption-stub');

            expect(videoCaptionStub.text()).toContain(videoTitleSlot);
        });

        it('renders content in the `video-duration` slot', () => {
            const wrapper = factoryShallowMount();
            const videoCaptionStub = wrapper.find('vsvideocaption-stub');

            expect(videoCaptionStub.text()).toContain(videoDurationSlot);
        });
    });

    describe(':methods', () => {
        it(':toggleCaption - caption is shown when toggle method is called', async() => {
            const wrapper = factoryShallowMount();
            wrapper.setData({
                showCaption: false,
            });
            wrapper.vm.toggleCaption();
            await wrapper.vm.$nextTick();

            expect(wrapper.vm.showCaption).toBe(true);
        });

        it(':toggleCaption - caption is hidden when toggle method is called twice', async() => {
            const wrapper = factoryShallowMount();
            const captionWrapper = wrapper.find('[data-test="vs-image-with-caption"]').find('.vs-image-with-caption__caption-wrapper');
            wrapper.vm.toggleCaption();
            wrapper.vm.toggleCaption();

            await wrapper.vm.$nextTick();

            expect(captionWrapper.classes('d-block')).toBe(false);
        });
    });
});
