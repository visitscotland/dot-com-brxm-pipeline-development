import { shallowMount } from '@vue/test-utils';
import VsVideo from '../Video';

const videoId = 'C0DPdy98e4c';
const singleMinuteDescriptor = '%s minute';
const pluralMinuteDescriptor = '%s minutos';
const language = 'de';

const factoryShallowMount = (propsData, compData) => shallowMount(VsVideo, {
    propsData: {
        videoId,
        showDuration: true,
        singleMinuteDescriptor,
        pluralMinuteDescriptor,
        language,
        ...propsData,
    },
    computed: {
        player() {
            return {
                getDuration() {
                    // return fake video lenth of 3 mins 30 secs
                    return Promise.resolve(210);
                },
            };
        },
        ...compData,
    },
});

describe('VsVideo', () => {
    it('should render a div with the class `vs-video`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.find('div[data-test=vs-video]').exists()).toBe(true);
    });

    describe(':props', () => {
        it('should pass a videoId prop to the youtube component', async() => {
            const wrapper = factoryShallowMount();

            wrapper.setData({
                requiredCookies: [],
            });

            await wrapper.vm.$nextTick();
            expect(wrapper.find('youtube-stub').attributes('videoid')).toBe(videoId);
        });

        it('should pass a language prop to `playerVars` data object', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.vm.playerVars.hl).toBe('de');
        });
    });

    describe(':data', () => {
        it('should show a roundedDuration that rounds up, if the duration is 0 minutes and < 30 seconds', async() => {
            const wrapper = factoryShallowMount();

            // a 25 second video, which should round to 1 minute
            wrapper.vm.formatTime(25);

            wrapper.setData({
                showDuration: true,
            });

            wrapper.setData({
                requiredCookies: [],
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.vm.duration.roundedMinutes).toContain('1');
        });

        it('should show a roundedDuration that rounds down, if the duration is x minutes and < 30 seconds', async() => {
            const wrapper = factoryShallowMount();

            // a 1 minute 20 second video, which should round down to 1 minute
            wrapper.vm.formatTime(80);

            await wrapper.setData({
                showDuration: true,
            });

            expect(wrapper.vm.duration.roundedMinutes).toContain('1');
        });

        it('should show a roundedDuration that rounds up, if the duration is x minutes and >= 30 seconds', async() => {
            // a 1 minute 30 second video, which should round up to 2 minutes
            const wrapper = factoryShallowMount();

            wrapper.vm.formatTime(90);

            await wrapper.setData({
                showDuration: true,
            });

            expect(wrapper.vm.duration.roundedMinutes).toContain('2');
        });

        it('should show render the singleMinuteDescriptor for a 1 minute video', async() => {
            // a 1 minute 20 second video, which should round down to 1 minute
            const wrapper = factoryShallowMount();

            wrapper.vm.formatTime(80);

            await wrapper.setData({
                showDuration: true,
            });

            expect(wrapper.vm.duration.roundedMinutes).toBe(singleMinuteDescriptor.replace('%s', '1'));
        });

        it('should render the pluralMinuteDiscriptor for a multi minute video', async() => {
            const wrapper = factoryShallowMount();

            // a 3 minute 40 second video, which should round up to 4 minute
            wrapper.vm.formatTime(220);

            await wrapper.setData({
                showDuration: true,
            });

            expect(wrapper.vm.duration.roundedMinutes).toBe(pluralMinuteDescriptor.replace('%s', '4'));
        });
    });

    describe(':methods', () => {
        it('should call the playVideo method when receiving emitted event', () => {
            jest.useFakeTimers();
            const wrapper = factoryShallowMount();
            const mockPlayMethod = jest.fn();
            wrapper.vm.playVideo = mockPlayMethod;

            wrapper.vm.$root.$emit('video-controls', 'play', videoId, 'modal');

            // wait for setTimeout to run
            jest.advanceTimersByTime(1500);

            expect(mockPlayMethod).toHaveBeenCalled();
        });

        it('should call the pauseVideo method when receiving emitted event', () => {
            const wrapper = factoryShallowMount();
            const mockPauseMethod = jest.fn();
            wrapper.vm.pauseVideo = mockPauseMethod;

            wrapper.vm.$root.$emit('video-controls', 'pause', videoId);

            expect(mockPauseMethod).toHaveBeenCalled();
        });

        it('should return the formatted time in minutes and seconds', async() => {
            const wrapper = factoryShallowMount();
            wrapper.vm.formatTime(210);

            expect(wrapper.vm.duration.minutes).toBe(3);
            expect(wrapper.vm.duration.seconds).toBe(30);
        });

        it('should not render the video duration if video duration is 0', () => {
            const wrapper = factoryShallowMount();

            wrapper.vm.formatTime(0);

            expect(wrapper.find('p[data-test="vs-video-duration"]').exists()).toBe(false);
        });
    });
});
